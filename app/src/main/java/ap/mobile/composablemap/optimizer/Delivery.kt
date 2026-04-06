package ap.mobile.composablemap.optimizer

import ap.mobile.composablemap.model.ParcelMapItem

data class Delivery(
  val parcels: List<ParcelMapItem>,
  val distance: Float = 0.0f,
  val duration: Float = 0.0f
)