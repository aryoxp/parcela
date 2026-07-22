package ap.mobile.composablemap.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ap.mobile.composablemap.AppTheme
import ap.mobile.composablemap.aco.Path
import ap.mobile.composablemap.model.ParcelMapItem

@Composable
fun ParcelDeliveryItem(parcel: ParcelMapItem, index: Int) {
  Row {
    Column(Modifier.width(42.dp).padding(end = 0.dp), horizontalAlignment = Alignment.CenterHorizontally) {
      VerticalDivider(
        modifier = Modifier.height(14.dp),
        thickness = 5.dp,
        color = if (index == 0) Color.Transparent
        else MaterialTheme.colorScheme.inversePrimary
      )
      Box(
        modifier = Modifier
          .size(18.dp)
          .border(
            width = 4.dp, // This determines the thickness of the ring
            color = MaterialTheme.colorScheme.primary,
            shape = CircleShape
          )
      )
      VerticalDivider(
        modifier = Modifier.height(64.dp),
        thickness = 5.dp,
        color = MaterialTheme.colorScheme.inversePrimary
      )
    }
    Row(
      Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
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
        Text(
          text = parcel.address,
          overflow = TextOverflow.Ellipsis,
          maxLines = 1,
          modifier = Modifier.padding(end = 8.dp)
        )
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(top = 2.dp)
        ) {
          Icon(
            Icons.Filled.LocationOn,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = "Localized description"
          )
          Text(
            text = " ${parcel.lat}, ${parcel.lng}",
            color = MaterialTheme.colorScheme.primary
          )
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
}

@Composable
fun DeliveryMetaInformation(
  parcels: List<ParcelMapItem>,
  distance: Float,
  duration: Float
) {
  Row(
    modifier = Modifier
      .background(MaterialTheme.colorScheme.secondaryContainer)
      .padding(top = 16.dp, bottom = 16.dp)
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
    // Text("${"%.2f".format(duration)} hrs")
    Text(Path.convertDecimalToHoursMinutes(duration))
  }
}

@Composable
fun DeliveryContent(
  modifier: Modifier = Modifier,
  isLoading: Boolean,
  parcels: List<ParcelMapItem>,
  loadingProgress: Float,
  distance: Float = 0f,
  duration: Float = 0f,
  onGetDeliveryRecommendation: () -> Unit = {},
  onCancelGetDeliveryRecommendation: () -> Unit = {}
) {
  Row {
    Column(
      modifier = modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(
        Modifier
          .padding(16.dp)
          .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
      ) {
        Button(onClick = {
          if (!isLoading) onGetDeliveryRecommendation()
          else onCancelGetDeliveryRecommendation()
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
            itemsIndexed(parcels) { index, parcel ->
              ParcelDeliveryItem(parcel, index)
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

@Preview
@Composable
fun PreviewParcelDeliveryItem() {
  AppTheme(darkTheme = false, dynamicColor = false) {
    ParcelDeliveryItem(
      ParcelMapItem(
        1, recipientName = "Djoko Sudemo",
        address = "Jl Agung Timur 4 Blok O No. 2 Kav. 18-19, Sunter Podomoro, North Jakarta"
      ), 1
    )
  }
}