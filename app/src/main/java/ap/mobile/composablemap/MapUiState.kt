package ap.mobile.composablemap

import com.google.android.gms.maps.model.LatLng

data class MapUiState(
  val currentPosition: LatLng = LatLng(-7.9666, 112.6326),
  val zoom: Float = 15.0f,
  val cameraPosition: LatLng = LatLng(-7.9666, 112.6326),
  val parcels: List<Parcel> = emptyList(),
  val deliveryRoute: List<LatLng> = emptyList(),
  val recompose : Boolean = false
  // val parcelCount: Int = 0,
  // val deliveries: List<Parcel> = emptyList(),
  // val deliveryDuration: Float = 0.0f,
  // val deliveryDistance: Float = 0.0f,
  // val isLoading: Boolean = false,
  // val isLoadingRecommendation: Boolean = false,
  // val loadingProgress: Float = 0.0f,
  // val loadingError: String? = null,
  // val selectedParcel: Parcel? = null,
  // val error: String? = null,
)