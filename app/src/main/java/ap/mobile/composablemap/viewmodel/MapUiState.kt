package ap.mobile.composablemap.viewmodel

import ap.mobile.composablemap.model.ParcelMapItem
import com.google.android.gms.maps.model.LatLng

data class MapUiState(
  val currentPosition: LatLng = LatLng(-7.9666, 112.6326),
  val zoom: Float = 15.0f,
  val cameraPosition: LatLng = LatLng(-7.9666, 112.6326),
  val parcels: List<ParcelMapItem> = emptyList(),
  val deliveryRoute: List<LatLng> = emptyList(),
  val recompose : Boolean = false
)