package ap.mobile.composablemap

data class SettingsUIState (
  val preference: PreferenceState = PreferenceState(),
  val options: Map<String, String> = mapOf<String, String>(),
  val friendlyValues: Map<String, String> = mapOf<String, String>()
)