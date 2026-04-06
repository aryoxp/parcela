package ap.mobile.composablemap.viewmodel

import ap.mobile.composablemap.model.ParcelMapItem
import com.google.android.gms.maps.model.LatLng

data class ParcelUIState(
  val showParcelSheet: Boolean = false,
  val parcel: ParcelMapItem = ParcelMapItem(0),
  val isComputing: Boolean = false,
  val parcels: List<ParcelMapItem> = emptyList(),
  val deliveries: List<ParcelMapItem> = emptyList(),
  val deliveryDuration: Float = 0.0f,
  val deliveryDistance: Float = 0.0f,
  val deliveryRoute: List<LatLng> = emptyList(),
)