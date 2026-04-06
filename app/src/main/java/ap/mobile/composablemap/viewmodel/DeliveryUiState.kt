package ap.mobile.composablemap.viewmodel

import ap.mobile.composablemap.model.ParcelMapItem

data class DeliveryUiState(
  val isComputing: Boolean = false,
  val computingProgress: Float = 0f,
  val deliveryRoute: List<ParcelMapItem> = emptyList(),
  val deliveryDuration: Float = 0f,
  val deliveryDistance: Float = 0f
)
