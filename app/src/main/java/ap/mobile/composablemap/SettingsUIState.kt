package ap.mobile.composablemap

data class SettingsUIState (
  val preference: PreferenceState = PreferenceState(),
  val options: Map<String, String> = mapOf<String, String>(),
  val hostFriendlyValue: String = "",
  val optMethodFriendlyValue: String = "",
  val useOnlineApiFriendlyValue: String = ""
)