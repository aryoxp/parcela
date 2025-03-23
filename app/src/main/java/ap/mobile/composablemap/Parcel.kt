package ap.mobile.composablemap

import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng

data class Parcel (
  val id: Int,
  val lat: Double = 0.0,
  val lng: Double = 0.0,
  val trackingNumber: String = "",
  val recipientName: String = "",
  val address: String = "",
  val type: String = "",
  val description: String = "",
  val position: LatLng = LatLng(lat, lng),
  var markerIconHue: Float? = null,
  val isDelivered: Boolean = false,
  var selected: Boolean = false
) {
  init {
    markerIconHue =
      BitmapDescriptorFactory.HUE_AZURE.takeIf { type == "Priority" }
        ?: BitmapDescriptorFactory.HUE_RED
  }
}