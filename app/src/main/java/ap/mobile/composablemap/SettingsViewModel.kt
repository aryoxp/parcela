package ap.mobile.composablemap

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ap.mobile.composablemap.PreferenceRepository.ListPreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel (context: Context, val preferenceRepository: PreferenceRepository = PreferenceRepository(context)) : ViewModel() {

  private val _settingsUiState = MutableStateFlow<SettingsUIState>(SettingsUIState())
  val settingsUiState: StateFlow<SettingsUIState> = _settingsUiState.asStateFlow()

  init {
    viewModelScope.launch {
      preferenceRepository.initializeDataStore()
      loadPreferencesValueState()
    }
  }

  private suspend fun loadPreferencesValueState() {
    _settingsUiState.update { currentState ->
      currentState.copy(
        hostFriendlyValue = preferenceRepository.getPreference(PreferencesKeys.HOST)?.toDataState()?.friendlyValue ?: "",
        optMethodFriendlyValue = preferenceRepository.getPreference(PreferencesKeys.OPT_METHOD)?.toDataState()?.friendlyValue ?: "",
        useOnlineApiFriendlyValue = preferenceRepository.getPreference(PreferencesKeys.USE_API)?.toDataState()?.friendlyValue ?: "",
      )
    }
  }

  fun setPreference(key: String) {
    viewModelScope.launch {
      val pref = preferenceRepository.getPreference(key)
      if (pref is ListPreference) {
        _settingsUiState.update { currentState ->
          val currentPref = currentState.preference
          currentState.copy(
            preference = preferenceRepository.getPreference(key)?.toDataState() ?: currentPref,
            options = pref.options,
          )
        }
        return@launch
      }
      _settingsUiState.update { currentState ->
        val currentPref = currentState.preference
        currentState.copy(
          preference = preferenceRepository.getPreference(key)?.toDataState() ?: currentPref,
        )
      }
    }
  }

  fun clearPreference() {
    _settingsUiState.update { currentState ->
      currentState.copy(preference = PreferenceState() )
    }
  }

  fun updatePreference(key: String, value: String) {
    viewModelScope.launch {
      preferenceRepository.putString(key, value)
      loadPreferencesValueState()
    }
  }

  fun updateSwitchPreference(key: String, value: Boolean) {
    viewModelScope.launch {
      preferenceRepository.putBoolean(key, value)
      loadPreferencesValueState()
    }
  }

}