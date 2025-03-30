package ap.mobile.composablemap

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import ap.mobile.composablemap.PreferenceRepository.Preference.Type
import kotlinx.coroutines.flow.first

private const val PREFERENCES_NAME = "app_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

object PreferencesKeys {
  const val OPT_METHOD = "OPT_METHOD"
  const val HOST = "HOST"
  const val USE_API = "USE_API"
}

data class PreferenceState(
  val key: String = "",
  val value: String = "",
  val friendlyValue: String = "",
  val title: String = "",
  val description: String = "",
  val type: Type = Type.STRING
)

class PreferenceRepository(
  private val context: Context,
) {

  val prefs = mutableMapOf<String, Preference>()

  abstract class Preference(
    open val key: String,
    open val title: String,
    open val description: String,
    open val type: Type
  ) {
    enum class Type {
      STRING, INT, LIST, SWITCH
    }
    abstract fun toDataState() : PreferenceState
  }
  class StringPreference(
    key: String,
    title: String = "",
    description: String = "",
    var value: String = ""
  ) : Preference(key, title, description, Type.STRING) {
    override fun toDataState(): PreferenceState {
      return PreferenceState(key, value, value, title, description, type)
    }
  }
  class IntPreference(
    key: String,
    title: String = "",
    description: String = "",
    var value: Int = 0
  ): Preference(key, title, description, Type.INT) {
    override fun toDataState(): PreferenceState {
      return PreferenceState(key, value.toString(), value.toString(), title, description, type)
    }
  }
  class ListPreference(
    key: String,
    title: String = "",
    description: String = "",
    var value: String = "",
    val options: Map<String, String> = mapOf<String, String>(),
    val friendlyValues: Map<String, String> = mapOf<String, String>()
  ): Preference(key, title, description, Type.LIST) {
    fun getOptions() { options }
    override fun toDataState(): PreferenceState {
      return PreferenceState(key, value, friendlyValues[value] ?: value, title, description, type)
    }
  }
  class SwitchPreference(
    key: String,
    title: String = "",
    description: String = "",
    var value: Boolean = false
  ): Preference(key, title, description, Type.SWITCH) {
    override fun toDataState(): PreferenceState {
      return PreferenceState(key, value.toString(), value.toString(), title, description, type)
    }
  }

  suspend fun initializeDataStore() {
    prefs.put(PreferencesKeys.OPT_METHOD,
      ListPreference(PreferencesKeys.OPT_METHOD,
        "Optimization Method",
        "Optimization method applied to the Artificial Bee Colony gathering and dancing phase.",
        "RANDOM",
        mapOf("RANDOM" to "Random", "SWITCH" to "Path Switch", "DEFAULT" to "Default"),
        mapOf("RANDOM" to "Random Optimization", "SWITCH" to "Path Switch Optimization", "DEFAULT" to "Default"),
      ))
    prefs.put(PreferencesKeys.HOST,
      StringPreference(PreferencesKeys.HOST,
        "Hostname",
        "Hostname of the API server",
        "http://localhost"))
    prefs.put(PreferencesKeys.USE_API,
      SwitchPreference(PreferencesKeys.USE_API,
        "Utilize Online API",
        "Online server API utilization for processing and computation.",
        false
      ))
    for (pref in prefs.values) {
      when (pref) {
        is ListPreference -> {
          if (getString(pref.key) == null) putString(pref.key, pref.value)
        }
        is StringPreference -> {
          if (getString(pref.key) == null) putString(pref.key, pref.value)
        }
        is IntPreference -> {
          if (getInt(pref.key) == null) putInt(pref.key, pref.value)
        }
        is SwitchPreference -> {
          if (getBoolean(pref.key) == null) putBoolean(pref.key, pref.value)
        }
      }
    }
  }

  suspend fun getPreference(key: String): Preference? {
    val pref = prefs[key]
    when (pref) {
      is ListPreference -> pref.value = getString(key) ?: pref.value
      is StringPreference -> pref.value = getString(key) ?: pref.value
      is IntPreference -> pref.value = getInt(key) ?: pref.value
      is SwitchPreference -> pref.value = getBoolean(key) ?: pref.value
    }
    return pref
  }

  suspend fun getString(key: String): String? {
    val preferencesKey = stringPreferencesKey(key)
    val preferences = context.dataStore.data.first()
    return preferences[preferencesKey]
  }

  suspend fun putString(key: String, value: String) {
    val preferencesKey = stringPreferencesKey(key)
    context.dataStore.edit { preferences ->
      preferences[preferencesKey] = value
    }
  }

  suspend fun getInt(key: String): Int? {
    val preferencesKey = intPreferencesKey(key)
    val preferences = context.dataStore.data.first()
    return preferences[preferencesKey]
  }

  suspend fun putInt(key: String, value: Int) {
    val preferencesKey = intPreferencesKey(key)
    context.dataStore.edit { preferences ->
      preferences[preferencesKey] = value
    }
  }

  suspend fun getBoolean(key: String): Boolean? {
    val preferencesKey = booleanPreferencesKey(key)
    val preferences = context.dataStore.data.first()
    return preferences[preferencesKey]
  }

  suspend fun putBoolean(key: String, value: Boolean) {
    val preferencesKey = booleanPreferencesKey(key)
    context.dataStore.edit { preferences ->
      preferences[preferencesKey] = value
    }
  }

  // fun getOptMethod(): Flow<String> = dataStore.data
  //   .map { preferences ->
  //     preferences[PreferencesKeys.OPT_METHOD]?: "RANDOM"
  //   }

  // suspend fun setOptMethod(optMethod: String) {
  //   dataStore.edit { settings ->
  //     val currentCounterValue = settings[PreferencesKeys.OPT_METHOD] ?: "RANDOM"
  //     settings[PreferencesKeys.OPT_METHOD] = optMethod
  //   }
  // }



}