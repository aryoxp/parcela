package ap.mobile.composablemap.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ap.mobile.composablemap.AppTheme
import ap.mobile.composablemap.Parcel


@Composable
fun ParcelItem(parcel: Parcel) {
  Row(Modifier
    .fillMaxWidth()
    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically) {
    Column(Modifier.weight(1f)) {
      Row {
        Text(
          text = parcel.recipientName, fontSize = 20.sp,
          color = MaterialTheme.colorScheme.primary,
          modifier = Modifier.padding(end = 4.dp)
        )
        if (parcel.type == "Priority") {
          Icon(
            Icons.Filled.Bolt,
            tint = MaterialTheme.colorScheme.error,
            contentDescription = "Localized description"
          )
        }
      }
      Text(text = parcel.address,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        modifier = Modifier.padding(end = 8.dp))
      Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 2.dp)) {
        Icon(
          Icons.Filled.LocationOn,
          tint = MaterialTheme.colorScheme.primary,
          contentDescription = "Localized description"
        )
        Text(text = " ${parcel.lat}, ${parcel.lng}",
          color = MaterialTheme.colorScheme.primary)
      }
    }
    FilledTonalIconButton(onClick = {}) {
      Icon(
        Icons.Filled.ChevronRight,
        contentDescription = "Localized description"
      )
    }
  }
}

@Preview
@Composable
fun PreviewParcelItem() {
  AppTheme(darkTheme = false, dynamicColor = false) {
    ParcelItem(
      Parcel(
        1, recipientName = "Djoko Sudemo",
        address = "Jl Agung Timur 4 Blok O No. 2 Kav. 18-19, Sunter Podomoro, North Jakarta"
      )
    )
  }
}

@Composable
fun ParcelDestination(modifier: Modifier = Modifier,
                      onBackHandler: () -> Unit,
                      parcels: List<Parcel>) {
  LazyColumn(modifier = modifier) {
    items(parcels) { parcel ->
      ParcelItem(parcel)
    }
  }
  BackHandler(enabled = true) {
    onBackHandler()
  }
}