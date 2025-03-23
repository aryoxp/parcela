package ap.mobile.composablemap

import com.google.android.gms.maps.model.LatLng

data class ParcelState(
  val showParcelSheet: Boolean = false,
  val parcel: Parcel = Parcel(0),
  val isComputing: Boolean = false,
  val parcels: List<Parcel> = emptyList(),
  val deliveries: List<Parcel> = emptyList(),
  val deliveryDuration: Float = 0.0f,
  val deliveryDistance: Float = 0.0f,
  val deliveryRoute: List<LatLng> = emptyList(),
)