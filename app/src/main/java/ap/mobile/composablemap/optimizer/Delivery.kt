package ap.mobile.composablemap.optimizer

import ap.mobile.composablemap.Parcel

data class Delivery(
  val parcels: List<Parcel>,
  val distance: Float = 0.0f,
  val duration: Float = 0.0f
)