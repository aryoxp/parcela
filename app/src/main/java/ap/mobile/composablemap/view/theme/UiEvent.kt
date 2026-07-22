package ap.mobile.composablemap.view.theme

sealed class UiEvent {
  data class ShowToast(val message: String) : UiEvent()
}