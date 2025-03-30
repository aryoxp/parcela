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

  private val _optMethod = MutableStateFlow<PreferenceState>(PreferenceState())
  val optMethod: StateFlow<PreferenceState> = _optMethod.asStateFlow()

  private val _host = MutableStateFlow<PreferenceState>(PreferenceState())
  val host: StateFlow<PreferenceState> = _host.asStateFlow()

  private val _useOnlineApi = MutableStateFlow<PreferenceState>(PreferenceState())
  val useOnlineApi: StateFlow<PreferenceState> = _useOnlineApi.asStateFlow()

  private val _settingsUiState = MutableStateFlow<SettingsUIState>(SettingsUIState())
  val settingsUiState: StateFlow<SettingsUIState> = _settingsUiState.asStateFlow()

  init {
    viewModelScope.launch {
      preferenceRepository.initializeDataStore()
      _optMethod.update { currentState ->
        preferenceRepository.getPreference(PreferencesKeys.OPT_METHOD)?.toDataState() ?: currentState
      }
      _host.update { currentState ->
        preferenceRepository.getPreference(PreferencesKeys.HOST)?.toDataState() ?: currentState
      }
      _useOnlineApi.update { currentState ->
        preferenceRepository.getPreference(PreferencesKeys.USE_API)?.toDataState() ?: currentState
      }
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
            friendlyValues = pref.friendlyValues
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
      if (key == PreferencesKeys.OPT_METHOD) {
        _optMethod.update { currentState ->
          preferenceRepository.getPreference(key)?.toDataState() ?: currentState
        }
      }
      if (key == PreferencesKeys.HOST) {
        _host.update { currentState ->
          preferenceRepository.getPreference(key)?.toDataState() ?: currentState
        }
      }
    }
  }

  fun updateSwitchPreference(key: String, value: Boolean) {
    viewModelScope.launch {
      preferenceRepository.putBoolean(key, value)
      if (key == PreferencesKeys.USE_API) {
        _useOnlineApi.update { currentState ->
          preferenceRepository.getPreference(key)?.toDataState() ?: currentState
        }
      }
    }
  }

}