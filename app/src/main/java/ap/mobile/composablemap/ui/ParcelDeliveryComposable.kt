package ap.mobile.composablemap.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoveToInbox
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ap.mobile.composablemap.AppTheme
import ap.mobile.composablemap.Parcel
import ap.mobile.composablemap.backgroundLight

@Composable
fun ParcelDeliveryItem(parcel: Parcel) {
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

@Composable
fun DeliveryMetaInformation(
  parcels: List<Parcel>,
  distance: Float,
  duration: Float
) {
  Row(
    modifier = Modifier
      .padding(top = 8.dp, bottom = 16.dp)
      .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center,
  ) {
    Icon(
      imageVector = Icons.Default.MoveToInbox,
      tint = MaterialTheme.colorScheme.primary,
      contentDescription = "Localized description",
      modifier = Modifier.padding(end = 4.dp)
    )
    Text("${parcels.size}", Modifier.padding(end = 16.dp))
    Icon(
      imageVector = Icons.Default.PinDrop,
      tint = MaterialTheme.colorScheme.primary,
      contentDescription = "Localized description",
      modifier = Modifier.padding(end = 4.dp)
    )
    Text("${"%.2f".format(distance)} km", Modifier.padding(end = 16.dp))
    Icon(
      imageVector = Icons.Default.AccessTime,
      tint = MaterialTheme.colorScheme.primary,
      contentDescription = "Localized description",
      modifier = Modifier.padding(end = 4.dp)
    )
    Text("${"%.2f".format(duration)} hrs")
  }
}

@Composable
fun DeliveryContent(
  modifier: Modifier = Modifier,
  isLoading: Boolean,
  parcels: List<Parcel>,
  loadingProgress: Float,
  distance: Float = 0f,
  duration: Float = 0f,
  onGetDeliveryRecommendation: () -> Unit = {}
) {
  Column(modifier = modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally) {
    Row(
      Modifier
        .padding(16.dp)
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.Center
    ) {
      Button(onClick = {
        if (!isLoading) onGetDeliveryRecommendation()
      }) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Route,
            contentDescription = "Localized description",
            tint = MaterialTheme.colorScheme.inversePrimary
          )
          AnimatedContent(targetState = isLoading) { targetIsLoading ->
            Text(text = ("Stop".takeIf { targetIsLoading } ?: "Delivery Route"))
          }
        }
      }
    }
    Row(modifier = Modifier.weight(1f)) {
      AnimatedVisibility(
        visible = !isLoading,
        enter = slideInHorizontally().plus(fadeIn()),
        exit = slideOutHorizontally().plus(fadeOut())
      ) {
        // if (!isLoading) {
        LazyColumn {
          items(parcels) { parcel ->
            ParcelDeliveryItem(parcel)
          }
        }
      }
      AnimatedVisibility(
        visible = isLoading,
        enter = slideInVertically(),
        exit = slideOutVertically()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          horizontalAlignment = Alignment.Start,
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          val percent = (loadingProgress * 100).toInt().toString() + "%"
          Text("Computing delivery route... $percent")
          LinearProgressIndicator(
            progress = { loadingProgress },
            modifier = Modifier.fillMaxWidth()
          )
        }
      }
    }

    // HorizontalDivider(color = MaterialTheme.colorScheme.secondary,
    //   modifier = Modifier.fillMaxWidth().width(1.dp).padding(horizontal = 16.dp))
    DeliveryMetaInformation(parcels, distance, duration)
  }
}

@Preview(heightDp = 480)
@Composable
fun PreviewDeliveryContent() {
  AppTheme(darkTheme = false, dynamicColor = false) {
    DeliveryContent(isLoading = true,
      modifier = Modifier.background(color = Color.White),
      parcels = emptyList(),
      loadingProgress = 0.6f)
  }
}