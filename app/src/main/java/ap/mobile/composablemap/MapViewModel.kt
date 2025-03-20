package ap.mobile.composablemap

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ap.mobile.composablemap.abc.Colony
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class MapViewModel() : ViewModel() {

  private val parcelRepository: ParcelRepository = ParcelRepository()
  private val _uiState = MutableStateFlow(MapUiState())
  val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

  init {
    _uiState.value = MapUiState()
    this.getParcels()
  }

  fun moveToSingapore() {
    moveToLocation(LatLng(1.35, 103.87))
  }

  fun moveToLocation(location: LatLng) {
    _uiState.update { currentState ->
      currentState.copy(currentPosition = location) }
  }

  // Function to fetch the user's location and update the state
  fun fetchUserLocation(context: Context, fusedLocationClient: FusedLocationProviderClient) {
    // Check if the location permission is granted
    if (ContextCompat.checkSelfPermission(context,
        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
      try {
        // Fetch the last known location
        // fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        //   location?.let {
        //    // Update the user's location in the state
        //    val userLatLng = LatLng(it.latitude, it.longitude)
        //    _userLocation.value = userLatLng
        //   }
        // }
        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY,
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
        Timber.e("Permission for location access was revoked: ${e.localizedMessage}")
      }
    } else {
      Timber.e("Location permission is not granted.")
    }
  }

  fun setCameraPosition(cameraPosition: LatLng) {
    println("Camera position: ${cameraPosition.latitude}, ${cameraPosition.longitude}")
    _uiState.update { currentState ->
      currentState.copy(cameraPosition = cameraPosition) }
  }

  fun setZoomLevel(zoom: Float) {
    println("Zoom: ${zoom}")
    _uiState.update { currentState ->
      currentState.copy(zoom = zoom) }
  }

  fun getParcels() {
    _uiState.update { currentState ->
      currentState.copy(isLoading = true) }
    viewModelScope.launch() {
      val parcels = ParcelRepository().getAllParcels()
      _uiState.update { currentState ->
        currentState.copy(parcels = parcels, isLoading = false) }
    }
  }

  fun setTabIndex(index: Int) {
    _uiState.update { currentState ->
      currentState.copy(tabIndex = index) }
  }

  fun getDeliveryRecommendation() {
    _uiState.update { currentState ->
      currentState.copy(isLoadingRecommendation = true) }
    viewModelScope.launch {
      val result = parcelRepository.computeDelivery(::setProgress)
      when (result) {
        is Result.Success<Colony.Delivery> -> {
          val deliveryRoute = mutableListOf<LatLng>()
          result.data.parcels.forEach {
            deliveryRoute.add(it.position)
          }
          _uiState.update { currentState ->
            currentState.copy(
              deliveries = result.data.parcels,
              deliveryRoute = deliveryRoute,
              deliveryDistance = result.data.distance,
              deliveryDuration = result.data.duration,
              isLoadingRecommendation = false
            )
          }
        }
        else -> {}// Show error in UI
      }
    }
  }

  fun setProgress(progress: Float): Float {
    _uiState.update { currentState ->
      currentState.copy(loadingProgress = progress)
    }
    return progress
  }

}
