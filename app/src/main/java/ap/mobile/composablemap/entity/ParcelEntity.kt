package ap.mobile.composablemap.entity

data class ParcelEntity(
  val id: Int = 0,
  val lat: Double = 0.0,
  val lng: Double = 0.0,
  val type: String = "",
  val recipientName: String = "",
  val address: String = ""
)
