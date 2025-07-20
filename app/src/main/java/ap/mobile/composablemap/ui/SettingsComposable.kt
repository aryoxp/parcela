package ap.mobile.composablemap.ui

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import ap.mobile.composablemap.PreferenceRepository.Preference.Type
import ap.mobile.composablemap.PreferenceState
import ap.mobile.composablemap.PreferencesKeys
import ap.mobile.composablemap.R
import ap.mobile.composablemap.ui.icons.ParcelaIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenTopAppBar(onBackButtonClick: () -> Unit) {
  CenterAlignedTopAppBar(
    title = { Text("Settings") },
    navigationIcon = {
      IconButton(onClick = {
        onBackButtonClick()
      } ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Localized description"
        )
      }
    },
  )
}

@Composable
fun CategoryItem(
  title: String,
  icon: ImageVector,
  value: String = "",
  onClick: () -> Unit
) {
  Surface(
    onClick = onClick,
    shape = MaterialTheme.shapes.medium,
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(30.dp)
    ) {
      Icon(icon, contentDescription = null, modifier = Modifier.size(28.dp),
        tint = MaterialTheme.colorScheme.primary)
      Column {
        Text(title, style = MaterialTheme.typography.bodyLarge)
        if (value.isNotEmpty())
          Text(value, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary)
      }
    }

  }
}

@Composable
fun SwitchCategoryItem(title: String, icon: ImageVector, value: Boolean = false, onCheckedChange: (Boolean) -> Unit) {
  Surface(
    onClick = {},
    shape = MaterialTheme.shapes.medium,
  ) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 16.dp), horizontalArrangement = Arrangement.spacedBy(30.dp)) {
      Icon(icon, contentDescription = null, modifier = Modifier.size(28.dp),
        tint = MaterialTheme.colorScheme.primary)
      Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text(title, style = MaterialTheme.typography.bodyLarge)
        var checked by remember { mutableStateOf(value) }
        Switch(checked = checked, onCheckedChange = {
          checked = it
          onCheckedChange(it)
        })
      }
    }

  }
}

@Composable
fun AppVersion(versionText: String, copyrights: String, onClick: () -> Unit) {
  Surface(onClick = onClick) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(30.dp)) {
      Box(
        modifier = Modifier.size(30.dp),
      )

      Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(versionText, style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.primary)
        Text(
          copyrights,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
        )
      }
    }
  }
}

@Composable
fun SettingsScreenPreferenceList(
  padding: PaddingValues,
  categoryItems : Map<String, String>,
  onCategoryItemClick: (preferencesKey: String) -> Unit,
  onUpdateSwitchPreference: (String, Boolean) -> Unit
) {
  Column(
    modifier = Modifier
      .padding(paddingValues = padding)
      .padding(horizontal = 16.dp)
  ) {
    CategoryItem(
      "Host",
      ParcelaIcons.Server,
      value = categoryItems[PreferencesKeys.HOST].toString(),
      onClick = { onCategoryItemClick(PreferencesKeys.HOST) }
    )
    CategoryItem(
      "Optimizer",
      ParcelaIcons.ConversionPath,
      value = categoryItems[PreferencesKeys.OPTIMIZER].toString(),
      onClick = { onCategoryItemClick(PreferencesKeys.OPTIMIZER) }
    )
    CategoryItem(
      "Optimization Method",
      ParcelaIcons.Automation,
      value = categoryItems[PreferencesKeys.OPT_METHOD].toString(),
      onClick = { onCategoryItemClick(PreferencesKeys.OPT_METHOD) }
    )
    SwitchCategoryItem(
      "Online API Utilization",
      ParcelaIcons.CloudSync,
      value = categoryItems[PreferencesKeys.USE_API].toBoolean(), // useOnlineApi?.value.toBoolean(),
      onCheckedChange = {
        onUpdateSwitchPreference(PreferencesKeys.USE_API, it)
      }
    )
    CategoryItem(
      "Log Data Location",
      ParcelaIcons.LogFile,
      value = categoryItems[PreferencesKeys.LOG_FILE].toString(),
      onClick = { onCategoryItemClick(PreferencesKeys.LOG_FILE) }
    )
    HorizontalDivider(Modifier.size(1.dp))
    AppVersion(
      stringResource(R.string.app_title),
      stringResource(R.string.app_copyright)
    ) { }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferenceDialog(
  preference: PreferenceState,
  preferenceOptions: Map<String, String> = mapOf<String, String>(),
  onUpdatePreference: (key: String, value: String) -> Unit,
  onDismiss: () -> Unit,
  request: ActivityResultLauncher<Uri?>
) {

  BasicAlertDialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .wrapContentWidth()
        .wrapContentHeight(),
      shape = MaterialTheme.shapes.large,
      tonalElevation = AlertDialogDefaults.TonalElevation
    ) {
      Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        val otfState = rememberTextFieldState(preference.value)
        val (selectedOption, onOptionSelected) =
          remember { mutableStateOf(preference.value) }
        Text(
          text = preference.title,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.primary
        )
        when (preference.type) {
          Type.STRING, Type.INT -> {
            OutlinedTextField(
              state = otfState,
              lineLimits = TextFieldLineLimits.SingleLine,
              label = { Text(preference.title) },
            )
          }
          Type.LIST -> {
            Column(Modifier.selectableGroup()) {
              for (option in preferenceOptions.keys) {
                Row(
                  Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                      selected = (option == selectedOption),
                      onClick = { onOptionSelected(option) },
                      role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  RadioButton(
                    selected = (option == selectedOption),
                    onClick = null // null recommended for accessibility with screenreaders
                  )
                  Text(
                    text = preferenceOptions[option].toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                  )
                }
              }
            }
          }
          Type.SWITCH -> {}
          Type.FILE -> {
            Button(onClick = {
              request.launch(null)
            }) {
              Text(text = "Select File")
            }
            Text(text = preference.friendlyValue)
          }
        }
        if (preference.description.isNotBlank()) {
          Text(
            text = preference.description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
          )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
          Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          TextButton( onClick = onDismiss ) {
            Text("Cancel", color = MaterialTheme.colorScheme.secondary)
          }
          TextButton(
            onClick = {
              when (preference.type) {
                Type.STRING, Type.INT ->
                  onUpdatePreference(preference.key, otfState.text.toString())
                Type.LIST ->
                  onUpdatePreference(preference.key, selectedOption)
                else -> {}
              }
              onDismiss()
            }
          ) {
            Text("Confirm")
          }
        }
      }
    }
  }
}
