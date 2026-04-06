package ap.mobile.composablemap.viewmodel

import ap.mobile.composablemap.repository.PreferenceState

data class SettingsUIState (
  val preference: PreferenceState = PreferenceState(),
  val options: Map<String, String> = mapOf(),
  val hostFriendlyValue: String = "",
  val optimizerFriendlyValue: String = "",
  val optMethodFriendlyValue: String = "",
  val useOnlineApiFriendlyValue: String = "",
  val logFileFriendlyValue: String = "",
  val useHeuristicValue: String = ""
)