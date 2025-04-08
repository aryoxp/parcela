package ap.mobile.composablemap.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ap.mobile.composablemap.MainActivity.Nav
import ap.mobile.composablemap.MyClusterRenderer
import ap.mobile.composablemap.Parcel
import ap.mobile.composablemap.ParcelItem
import ap.mobile.composablemap.ui.icons.ParcelaIcons
import ap.mobile.composablemap.ui.theme.doublePulseEffect
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.StrokeStyle
import com.google.android.gms.maps.model.StyleSpan
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm
import com.google.maps.android.clustering.view.ClusterRenderer
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.clustering.rememberClusterManager
import com.google.maps.android.compose.clustering.rememberClusterRenderer
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.launch
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(onNavigateBack: () -> Unit, onNavigate: (Nav) -> Unit) {
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
  var menuExpanded by remember { mutableStateOf(false) }
  var showAboutDialog by remember { mutableStateOf(false) }
  CenterAlignedTopAppBar(
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer,
      titleContentColor = MaterialTheme.colorScheme.primary,
    ),
    title = {
      Text(
        "Parcela",
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    },
    navigationIcon = {
      IconButton(onClick = onNavigateBack) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Localized description"
        )
      }
    },
    actions = {
      IconButton(onClick = {
        menuExpanded = !menuExpanded
      }) {
        Icon(
          imageVector = Icons.Filled.Menu,
          contentDescription = "Localized description"
        )
      }
      DropdownMenu(
        expanded = menuExpanded,
        onDismissRequest = { menuExpanded = false }
      ) {
        DropdownMenuItem(
          text = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Icon(
                imageVector = Icons.Filled.AddTask,
                contentDescription = "Localized description",
                tint = MaterialTheme.colorScheme.primary
              )
              Text("Scan Parcel", color = MaterialTheme.colorScheme.primary)
            }
          },
          onClick = { menuExpanded = !menuExpanded }
        )
        DropdownMenuItem(
          text = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "Localized description",
                tint = MaterialTheme.colorScheme.primary
              )
              Text("Settings", color = MaterialTheme.colorScheme.primary)
            }
          },
          onClick = {
            menuExpanded = !menuExpanded
            onNavigate(Nav.Settings)
          }
        )
        DropdownMenuItem(
          text = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "Localized description",
                tint = MaterialTheme.colorScheme.primary
              )
              Text("About App", color = MaterialTheme.colorScheme.primary)
            }
          },
          onClick = {
            showAboutDialog = true
            menuExpanded = !menuExpanded}
        )
      }
      if (showAboutDialog) {
        AlertDialog(
          onDismissRequest = {
            showAboutDialog = false // vm.confirmExit(false)
          },
          icon = { Icon(ParcelaIcons.BoxSeam, contentDescription = null) },
          title = { Text("Parcela") },
          text = {
            Column(
              verticalArrangement = Arrangement.spacedBy(8.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.fillMaxWidth()) {
              Text(fontSize = 16.sp,
                text = "Copyright 2025\nAryo Pinandito\nMedia, Game, and Mobile Laboratory\nAll Rights Reserved.",
                lineHeight = 24.sp,
                textAlign = TextAlign.Center)
            }
          },
          modifier = Modifier,
          confirmButton = {
            TextButton(
              onClick = {
                showAboutDialog = false
              }
            ) {
              Text("OK")
            }
          }
        )
      }
    },
    scrollBehavior = scrollBehavior,
  )
}

@Composable
fun BottomNavigationBar(tabIndex: Int, onNavigate: (Int) -> Unit) {
  BottomAppBar(actions = {
      SecondaryTabRow(selectedTabIndex = tabIndex) {
        Tab(
          onClick = { onNavigate(0) },
          selected = (tabIndex == 0),
          text = {
            Text(
              text = "Map",
              textAlign = TextAlign.Center,
              color = MaterialTheme.colorScheme.primary
            )
          },
          icon = {
            Icon(
              ParcelaIcons.Explore,
              tint = MaterialTheme.colorScheme.primary,
              contentDescription = "Localized description"
            )
          }
        )
        Tab(
          onClick = { onNavigate(1) },
          selected = (tabIndex == 1),
          text = { Text(text = "Parcel", textAlign = TextAlign.Center) },
          icon = {
            Icon(
              ParcelaIcons.BoxSeam,
              tint = MaterialTheme.colorScheme.primary,
              contentDescription = "Localized description"
            )
          }
        )
        Tab(
          onClick = { onNavigate(2) },
          selected = (tabIndex == 2),
          text = { Text(text = "Delivery", textAlign = TextAlign.Center) },
          icon = {
            Icon(
              ParcelaIcons.Truck,
              tint = MaterialTheme.colorScheme.primary,
              contentDescription = "Localized description"
            )
          }
        )
      }
    }
  )
}

@Composable
fun ExitDialog(onDismiss: () -> Unit, onConfirmExit: (Boolean) -> Unit) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Exit App") },
    text = { Text("Do you want to exit?") },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    },
    confirmButton = {
      TextButton(onClick = { onConfirmExit(true) }) {
        Text("Exit")
      }
    }
  )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BottomSheet(
  showBottomSheet: Boolean,
  isComputing: Boolean,
  parcel: Parcel,
  parcels: List<Parcel> = listOf<Parcel>(),
  deliveryDistance: Float = 0f,
  deliveryDuration: Float = 0f,
  onDismiss: () -> Unit = {},
  onGetDeliveryRecommendation: (Parcel) -> Unit = {}
) {
  val sheetState = rememberModalBottomSheetState()
  val scope = rememberCoroutineScope()
  Box {
    if (showBottomSheet) {
      ModalBottomSheet(
        onDismissRequest = {
          onDismiss()
          scope.launch { sheetState.hide() }
        },
        sheetState = sheetState,
      ) {
        Column(Modifier
          .fillMaxWidth()
          .animateContentSize()) {
          Row(Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Column(Modifier.weight(1f)) {
              // Sheet content
              Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.padding(bottom = 16.dp)) {
                Text("Parcel", fontSize = 24.sp)
                Spacer(Modifier.weight(1f))
                if (parcel.type == "Priority") {
                  Icon(
                    imageVector = Icons.Default.FlashOn,
                    contentDescription = "Localized description",
                    tint = Color.hsl(0f, 1f, .33f),
                    modifier = Modifier.size(24.dp)
                  )
                  Text(
                    "Priority",
                    fontSize = 16.sp,
                    color = Color.hsl(0f, 1f, .33f),
                    modifier = Modifier.padding(end = 16.dp)
                  )
                }
                Button(onClick = {
                  onGetDeliveryRecommendation(parcel)
                  // vm.getDeliveryRecommendation(parcel)
                },
                  shape = CircleShape,
                  contentPadding = PaddingValues(0.dp),
                  modifier = Modifier.requiredSize(56.dp),
                ) {
                  if (isComputing) {
                    CircularProgressIndicator(
                      color = MaterialTheme.colorScheme.surface,
                      modifier = Modifier.size(28.dp)
                    )
                  } else {
                    Icon(
                      imageVector = Icons.Default.Directions,
                      contentDescription = "Localized description",
                      modifier = Modifier.size(28.dp)
                    )
                  }
                }
              }
              Text(
                parcel.recipientName,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
              )
              Text(parcel.address, overflow = TextOverflow.Ellipsis)
              Row(
                modifier = Modifier.padding(bottom = 0.dp, top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.PinDrop,
                  tint = MaterialTheme.colorScheme.primary,
                  contentDescription = "Localized description"
                )
                Text("${parcel.lat}, ${parcel.lng}")
              }
            }
          }
          if (deliveryDistance > 0) {
            DeliveryMetaInformation(
              parcels = parcels,
              distance = deliveryDistance,
              duration = deliveryDuration
            )
          }
          Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
          ) {
            Button(onClick = {
              scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) onDismiss()
              }
            }) {
              Row(modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Close,
                  contentDescription = "Close",
                  tint = MaterialTheme.colorScheme.surface)
                Text("Close", Modifier.padding(start = 8.dp))
              }
            }
          }
        }
      }
    }
  }
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun DeliveryMap(modifier: Modifier = Modifier,
                parcels: List<Parcel> = listOf<Parcel>(),
                deliveryRoute: List<LatLng> = listOf<LatLng>(),
                currentPosition: LatLng = LatLng(0.0, 0.0),
                zoom: Float = 13f,
                onSelectParcel: (Parcel) -> Unit = {},
                onCheckLocationPermission: () -> Unit = {}
                ) {

  val cameraPositionState = rememberCameraPositionState()
  val coroutineScope = rememberCoroutineScope()
  var parcelItems by remember { mutableStateOf(listOf<ParcelItem>()) }
  val boundsBuilder = LatLngBounds.builder()

  Column(modifier = modifier) {
    GoogleMap(
      modifier = Modifier.fillMaxSize(),
      cameraPositionState = cameraPositionState,
      onMapLoaded = {
        if (parcels.isNotEmpty()) {
          val items = mutableListOf<ParcelItem>()
          for (parcel in parcels) {
            boundsBuilder.include(parcel.position)
            items.add(ParcelItem(parcel))
          }
          parcelItems += items
        }
      }
    ) {


      MyCustomRendererClustering(parcelItems,
        onClusterClick = {
          coroutineScope.launch {
            cameraPositionState.animate(
              update = CameraUpdateFactory.newLatLng(it.position),
              durationMs = 300
            )
          }
          cameraPositionState.move(
            update = CameraUpdateFactory.zoomIn()
          )
        },
        onClusterItemClick = {
          for (item in parcelItems) {
            when (item.isSelected) {
              true -> {
                if (item.getParcel().id != it.getParcel().id) {
                  parcelItems -= item
                  parcelItems += ParcelItem(item.getParcel()).select(false)
                }
              }
              else -> {}
            }
          }
          if (!it.isSelected) {
            it.isSelected = true
            parcelItems -= it
            parcelItems += ParcelItem(it.getParcel()).select()
            coroutineScope.launch {
              cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLng(it.position),
                durationMs = 1000
              )
            }
          }
          onSelectParcel(it.getParcel())
        }
      )






      // Clustering(
      //   items = parcelItems,
      //   onClusterClick = {
      //     coroutineScope.launch {
      //       cameraPositionState.animate(
      //         update = CameraUpdateFactory.newLatLng(it.position),
      //         durationMs = 300
      //       )
      //     }
      //     cameraPositionState.move(
      //       update = CameraUpdateFactory.zoomIn()
      //     )
      //     true
      //   },
      //   onClusterItemClick = {
      //     // onSelectParcel(it.getParcel())
      //
      //
      //     // parcelItems.add(it)
      //     // parcelItems.clear()
      //     for(items in parcelItems) {
      //       items.isSelected = it.getParcel().id == items.getParcel().id
      //     }
      //     val parcelItem = parcelItems.removeAt(0)
      //     parcelItems.add(0, parcelItem)
      //     true
      //   },
      //   clusterContent = { ClusterContent(cluster = it) },
      //   clusterItemContent = { ClusterItemContent(parcelItem = it) }
      // )
      //
      if (deliveryRoute.isNotEmpty()) {
        val polylineColorPairs = listOf(
          0xFFFF0000.toInt() to 0xFF1C8ABD.toInt(),
        )
        Polyline(
          points = deliveryRoute,
          spans = polylineColorPairs.map {
            StyleSpan(
              StrokeStyle.gradientBuilder(
                it.first,
                it.second
              ).build()
            )
          },
          width = 10f
        )
      }

      // MapEffect(true) { map ->
      //   map.isMyLocationEnabled = true
      //   map.uiSettings.isMyLocationButtonEnabled = true
      //   map.uiSettings.isZoomControlsEnabled = true
      // }
    }

    LaunchedEffect(deliveryRoute) {
      if (deliveryRoute.isNotEmpty() && deliveryRoute.size > 1) {
        val builder = LatLngBounds.builder()
        for (i in 0..ceil((deliveryRoute.size / 2.0)).toInt()) {
          builder.include(deliveryRoute[i])
        }
        cameraPositionState.animate(
          update = CameraUpdateFactory.newLatLngBounds(
            builder.build(),
            128
          ),
          durationMs = 1000
        )
      }
    }
    LaunchedEffect(Unit) {
      println("Launched Effect!")
      if (parcels.isEmpty()) {
        cameraPositionState.position =
          CameraPosition.fromLatLngZoom(currentPosition, zoom)
      } else {
        // parcelItems.clear()
        for (parcel in parcels) {
          boundsBuilder.include(parcel.position)
          // parcelItems.add(ParcelItem(parcel))
        }
        cameraPositionState.move(
          update = CameraUpdateFactory.newLatLngBounds(
            boundsBuilder.build(),
            64
          )
        )
      }

      println("LaunchedEffect")
      // Request the location permission when the composable is launched
      onCheckLocationPermission()
    }
  }
}

@OptIn(MapsComposeExperimentalApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun MyCustomRendererClustering(
  items : List<ParcelItem>,
  onClusterItemClick: (parcelItem: ParcelItem) -> Unit,
  onClusterClick: (cluster: Cluster<ParcelItem>) -> Unit
) {
  // val configuration = LocalConfiguration.current
  // val screenHeight = configuration.screenHeightDp.dp
  // val screenWidth = configuration.screenWidthDp.dp
  val clusterManager = rememberClusterManager<ParcelItem>()

  clusterManager?.markerManager?.Collection()?.markers?.map { marker ->
    marker.setAnchor(0.5f, 0.5f)
  }

  // Here the clusterManager is being customized with a NonHierarchicalViewBasedAlgorithm.
  // This speeds up by a factor the rendering of items on the screen.
  // NonHierarchicalViewBasedAlgorithm(
  //   screenWidth.value.toInt(),
  //   screenHeight.value.toInt()
  // )
  clusterManager?.algorithm = NonHierarchicalDistanceBasedAlgorithm<ParcelItem>().apply {
    maxDistanceBetweenClusteredItems = 100
  }

  val renderer = rememberClusterRenderer(
    clusterContent = { cluster ->
      CircleContent(
        modifier = Modifier.size(48.dp),
        text = "10+".takeIf { cluster.size > 10 } ?: "%,d".format(cluster.size),
        color = Color.hsl(167f, .92f, .29f),
      )
    },
    clusterItemContent = {
      val selected = it.isSelected
      Column(
        modifier = Modifier.size(width = 56.dp, height = 56.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        val modifier =
          ((Modifier.doublePulseEffect(targetScale = 2f).takeIf { selected } ?: Modifier)
            .size(28.dp).takeIf { selected } ?: Modifier.size(20.dp))
        CircleContent(
          modifier = modifier,
          text = "",
          color = Color.hsl(345f, .92f, .40f).takeIf { selected } ?: Color.hsl(209f, .92f, .40f),
        )
        // FilledIconButton(
        //   onClick = {},
        //   modifier = Modifier.doublePulseEffect(
        //     targetScale = 2f
        //   ).size(42.dp)
        // ) {
        //   Icon(
        //     imageVector = Icons.Default.Adjust, contentDescription = "",
        //   )
        // }
      }
    },
    clusterManager = clusterManager,
  )

  // // public fun <T : ClusterItem> rememberClusterRenderer(
  // //   clusterContent: @Composable ((Cluster<T>) -> Unit)?,
  // //   clusterItemContent: @Composable ((T) -> Unit)?,
  // //   clusterManager: ClusterManager<T>?,
  // // ): ClusterRenderer<T>? {
  // //   val clusterContentState = rememberUpdatedState(clusterContent)
  // //   val clusterItemContentState = rememberUpdatedState(clusterItemContent)
  // val context = LocalContext.current
  // val clusterRendererState: MutableState<ClusterRenderer<ParcelItem>?> = remember { mutableStateOf(null) }
  // var renderer: ClusterRenderer<ParcelItem>? = null
  // if (clusterManager != null) {
  //   MapEffect(context) { map ->
  //     val customRenderer = MyClusterRenderer(
  //       context,
  //       map,
  //       clusterManager
  //     )
  //     // val renderer = ComposeUiClusterRenderer(
  //     //   context,
  //     //   scope = this,
  //     //   map,
  //     //   clusterManager,
  //     //   viewRendererState,
  //     //   clusterContentState,
  //     //   clusterItemContentState,
  //     // )
  //     clusterRendererState.value = customRenderer
  //     renderer = clusterRendererState.value
  //     awaitCancellation()
  //   }
  // }


  SideEffect {
    clusterManager ?: return@SideEffect
    clusterManager.setOnClusterClickListener {
      // Log.d(TAG, "Cluster clicked! $it")
      println("Cluster clicked! $it")
      onClusterClick(it)
      false
    }
    clusterManager.setOnClusterItemClickListener {
      // Log.d(TAG, "Cluster item clicked! $it")
      onClusterItemClick(it)
      println("Cluster item clicked! $it")
      true
    }
    clusterManager.setOnClusterItemInfoWindowClickListener {
      // Log.d(TAG, "Cluster item info window clicked! $it")
      println("Cluster item info window clicked! $it")
    }
  }
  SideEffect {
    if (clusterManager?.renderer != renderer) {
      clusterManager?.renderer = renderer ?: return@SideEffect
    }
  }

  if (clusterManager != null) {
    Clustering(
      items = items,
      clusterManager = clusterManager,
    )
  }
}

@Composable
private fun CircleContent(
  color: Color,
  text: String,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier,
    shape = CircleShape,
    color = color,
    contentColor = Color.White,
    border = BorderStroke(4.dp, Color.White.copy(.5f))
  ) {
    Box(contentAlignment = Alignment.Center) {
      Text(
        text,
        fontSize = 16.sp,
        color = Color.White,
        fontWeight = FontWeight.Black,
        textAlign = TextAlign.Center
      )
    }
  }
}

