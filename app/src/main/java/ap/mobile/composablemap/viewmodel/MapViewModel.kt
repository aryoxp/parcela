package ap.mobile.composablemap.viewmodel

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ap.mobile.composablemap.model.ParcelMapItem
import ap.mobile.composablemap.repository.ParcelRepository
import ap.mobile.composablemap.repository.PreferenceRepository
import ap.mobile.composablemap.repository.PreferencesKeys
import ap.mobile.composablemap.repository.Result
import ap.mobile.composablemap.optimizer.Delivery
import ap.mobile.composablemap.optimizer.Optimizer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class MapViewModel(app: Application) : AndroidViewModel(app) {

  private val context = getApplication<Application>().applicationContext
  private val parcelRepository: ParcelRepository = ParcelRepository(context = context)

  private val _mapUiState = MutableStateFlow(MapUiState())
  val mapUiState: StateFlow<MapUiState> = _mapUiState.asStateFlow()

  private val _deliveryUiState = MutableStateFlow(DeliveryUiState())
  val deliveryUiState: StateFlow<DeliveryUiState> = _deliveryUiState.asStateFlow()

  private val _parcelState = MutableStateFlow(ParcelUIState())
  val parcelState: StateFlow<ParcelUIState> = _parcelState.asStateFlow()

  init {
    getParcels()
  }

  fun moveToSingapore() {
    moveToLocation(LatLng(1.35, 103.87))
  }

  fun moveToLocation(location: LatLng) {
    _mapUiState.update { currentState ->
      currentState.copy(currentPosition = location) }
  }

  // Function to fetch the user's location and update the state
  fun fetchUserLocation(context: Context, fusedLocationClient: FusedLocationProviderClient) {
    // Check if the location permission is granted
    if (ContextCompat.checkSelfPermission(context,
        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
      try {
        // Fetch the last known location
        // fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        //   location?.let {
        //    // Update the user's location in the state
        //    val userLatLng = LatLng(it.latitude, it.longitude)
        //    _userLocation.value = userLatLng
        //   }
        // }
        fusedLocationClient.getCurrentLocation(
          Priority.PRIORITY_HIGH_ACCURACY,
          object : CancellationToken() {
          override fun onCanceledRequested(p0: OnTokenCanceledListener) =
            CancellationTokenSource().token
          override fun isCancellationRequested() = false
        })
          .addOnSuccessListener { location: Location? ->
            if (location != null)
              moveToLocation(LatLng(location.latitude, location.longitude))
          }
      } catch (e: SecurityException) {
        Timber.Forest.e("Permission for location access was revoked: ${e.localizedMessage}")
      }
    } else {
      Timber.Forest.e("Location permission is not granted.")
    }
  }

  fun setCameraPosition(cameraPosition: LatLng) {
    println("Camera position: ${cameraPosition.latitude}, ${cameraPosition.longitude}")
    _mapUiState.update { currentState ->
      currentState.copy(cameraPosition = cameraPosition) }
  }

  fun setZoomLevel(zoom: Float) {
    println("Zoom: $zoom")
    _mapUiState.update { currentState ->
      currentState.copy(zoom = zoom) }
  }

  fun getParcels() {
    // val context = getApplication<Application>().applicationContext
    viewModelScope.launch {
      val parcels = ParcelRepository(context = context).getAllParcels()
      _parcelState.update { currentState ->
        currentState.copy(parcels = parcels)
      }
      _deliveryUiState.update { currentState ->
        currentState.copy(deliveryRoute = parcels)
      }
      _mapUiState.update { currentState ->
        currentState.copy(parcels = parcels)
      }
    }
  }

  @RequiresApi(Build.VERSION_CODES.Q)
  fun getDeliveryRecommendation(context: Context, parcel: ParcelMapItem? = null) {
    _deliveryUiState.update { currentState ->
      currentState.copy(isComputing = true) }
    _parcelState.update { currentState ->
      currentState.copy(isComputing = true)
    }

    val preferenceRepository : PreferenceRepository = PreferenceRepository(context)

    viewModelScope.launch(Dispatchers.IO) {
      val result = parcelRepository.computeDelivery(
        ::setProgress,
        parcel,
        optimizer = Optimizer.valueOf(preferenceRepository.getString(PreferencesKeys.OPTIMIZER).toString()),
        useHeuristicInit = preferenceRepository.getBoolean(PreferencesKeys.HEURISTIC_INIT)
      )
      when (result) {
        is Result.Success<Delivery> -> {
          _deliveryUiState.update { currentState ->
            currentState.copy(
              deliveryRoute = result.data.parcels,
              deliveryDistance = result.data.distance,
              deliveryDuration = result.data.duration,
              isComputing = false
            )
          }
          _parcelState.update { currentState ->
            currentState.copy(
              isComputing = false,
              deliveries = result.data.parcels,
              deliveryDistance = result.data.distance,
              deliveryDuration = result.data.duration,
            )
          }
          val deliveryRoute = mutableListOf<LatLng>()
          result.data.parcels.forEach {
            deliveryRoute.add(it.position)
          }
          _mapUiState.update {currentState ->
            currentState.copy(deliveryRoute = deliveryRoute)
          }
        }
        else -> {}// Show error in UI
      }
    }
  }

  fun setProgress(progress: Float): Float {
    _deliveryUiState.update { currentState ->
      currentState.copy(computingProgress = progress)
    }
    return progress
  }

  fun selectParcel(parcel: ParcelMapItem?) {
    // _mapUiState.update { currentState ->
    //   val parcels = currentState.parcels.toMutableList<Parcel>()
    //   for(p in parcels) {
    //     p.selected = parcel.id == p.id
    //   }
    //   currentState.copy(parcels = parcels,
    //     recompose = !currentState.recompose)
    // }
    _parcelState.update { currentState ->
      currentState.copy(
        parcel = parcel?: ParcelMapItem(0)
      )
    }
  }
  fun parcelSheet(shouldShow: Boolean = true) {
    _parcelState.update { currentState ->
      currentState.copy(
        showParcelSheet = shouldShow
      )
    }
  }

}