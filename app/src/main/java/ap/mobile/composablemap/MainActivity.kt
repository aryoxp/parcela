package ap.mobile.composablemap

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoveToInbox
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.outlined.AllInbox
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.StrokeStyle
import com.google.android.gms.maps.model.StyleSpan
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.ceil


class MainActivity : ComponentActivity() {

  private val vm: MapViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      AppTheme(darkTheme = false, dynamicColor = false) {
        MyScaffold()
      }
    }
  }

  enum class MapScreen() {
    Map,
    Parcel,
    Delivery
  }

  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  fun MyScaffold() {
    val navUiState by vm.navUiState.collectAsState()
    val navController: NavHostController = rememberNavController()
    val tabIndex = navUiState.tabIndex
    Scaffold(modifier = Modifier.fillMaxSize(),
      containerColor = MaterialTheme.colorScheme.surface,
      topBar = { AppBar(navController) },
      bottomBar = { BottomNavigationBar(navController, tabIndex) }
    ) { padding ->
      NavHost(
        navController = navController,
        startDestination = MapScreen.Map.name,
        enterTransition = {
          fadeIn(
            animationSpec = tween(
              300, easing = LinearEasing
            )
          ) + slideIntoContainer(
            animationSpec = tween(300, easing = LinearEasing),
            towards = AnimatedContentTransitionScope.SlideDirection.Start
          )
        },
        exitTransition = {
          fadeOut(
            animationSpec = tween(
              300, easing = LinearEasing
            )
          ) + slideOutOfContainer(
            animationSpec = tween(300, easing = LinearEasing),
            towards = AnimatedContentTransitionScope.SlideDirection.Start
          )
        },
        popEnterTransition = {
          fadeIn(
            animationSpec = tween(
              300, easing = LinearEasing
            )
          ) + slideIntoContainer(
            animationSpec = tween(300, easing = LinearEasing),
            towards = AnimatedContentTransitionScope.SlideDirection.End
          )
        },
        popExitTransition = {
          fadeOut(
            animationSpec = tween(
              300, easing = LinearEasing
            )
          ) + slideOutOfContainer(
            animationSpec = tween(300, easing = LinearEasing),
            towards = AnimatedContentTransitionScope.SlideDirection.End
          )
        },

      ) {
        composable(route = MapScreen.Map.name) {
          MapDestination(modifier = Modifier.padding(padding))
        }
        composable(route = MapScreen.Parcel.name) {
          ParcelDestination(
            modifier = Modifier.padding(padding), navController = navController)
        }
        composable(route = MapScreen.Delivery.name) {
          DeliveryDestination(
            modifier = Modifier.padding(padding), navController = navController)
        }
      }
    }
    BottomSheet()
  }

  @Composable
  @OptIn(ExperimentalMaterial3Api::class)
  private fun BottomSheet() {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val parcelState by vm.parcelState.collectAsState()
    val parcels = parcelState.deliveries
    val parcel = parcelState.parcel
    var showBottomSheet = parcelState.showParcelSheet
    Box {
      if (showBottomSheet) {
        ModalBottomSheet(
          onDismissRequest = {
            vm.parcelSheet(shouldShow = false)
            scope.launch { sheetState.hide() }
          },
          sheetState = sheetState,
          ) {
          Column(Modifier.fillMaxWidth().animateContentSize()) {
            Row(Modifier.fillMaxWidth().padding(16.dp)) {
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
                    vm.getDeliveryRecommendation(parcel)
                  },
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.requiredSize(56.dp),
                  ) {
                    if (parcelState.isComputing) {
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
            if (parcelState.deliveryDistance > 0) {
              DeliveryMetaInformation(
                parcels = parcels,
                distance = parcelState.deliveryDistance,
                duration = parcelState.deliveryDuration
              )
            }
            Row(
              horizontalArrangement = Arrangement.Center,
              modifier = Modifier.fillMaxWidth()
            ) {
              Button(onClick = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                  if (!sheetState.isVisible) {
                    vm.parcelSheet(shouldShow = false)
                  }
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

  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  fun AppBar(navHostController: NavHostController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var showExitDialog by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    CenterAlignedTopAppBar(
      colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
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
        IconButton(onClick = {
          if (navHostController.currentDestination?.route != MapScreen.Map.name) {
            navHostController.popBackStack()
            vm.setTabIndex(0)
          } else showExitDialog = true
        }) {
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
            title = { Text("Parcela") },
            text = {
              Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()) {
                Text(fontSize = 16.sp,
                  text = "Copyright 2025 - Aryo Pinandito.\nAll Rights Reserved.",
                  lineHeight = 24.sp)
                // Text(fontSize = 16.sp, text = "Aryo Pinandito")
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
    when {
      showExitDialog -> {
        val activity: Activity = findActivity()
        AlertDialog(
          onDismissRequest = {
            showExitDialog = false
          },
          title = { Text("Exit App") },
          text = { Text("Do you want to exit?") },
          dismissButton = {
            TextButton(onClick = {
              showExitDialog = false
            }) {
              Text("Cancel")
            }
          },
          confirmButton = {
            TextButton(
              onClick = {
                activity.finish()
              }
            ) {
              Text("Exit")
            }
          }
        )
      }
    }
  }

  @Composable
  fun BottomNavigationBar(navController: NavHostController, tabIndex: Int) {

    BottomAppBar(
      actions = {
        TabRow(selectedTabIndex = tabIndex) {
          Tab(onClick = {
            if (navController.currentDestination?.route != MapScreen.Map.name) {
              // navController.navigate(MapScreen.Map.name) {
              //   popUpTo(MapScreen.Map.name)
              // }
              navController.popBackStack()
              vm.setTabIndex(0)
            }
          },
            selected = (tabIndex == 0),
            text = { Text(text = "Map",
              textAlign = TextAlign.Center,
              color = MaterialTheme.colorScheme.primary) },
            icon = { Icon(
              Icons.Filled.Map,
              tint = MaterialTheme.colorScheme.primary,
              contentDescription = "Localized description"
            )}
          )
          Tab(onClick = {
            if (navController.currentDestination?.route != MapScreen.Parcel.name) {
              navController.navigate(MapScreen.Parcel.name) {
                popUpTo(MapScreen.Map.name)
              }
              vm.setTabIndex(1)
            }},
            selected = (tabIndex == 1),
            text = { Text(text = "Parcel", textAlign = TextAlign.Center) },
            icon = { Icon(
              Icons.Outlined.AllInbox,
              tint = MaterialTheme.colorScheme.primary,
              contentDescription = "Localized description"
            )}
          )
          Tab(onClick = {
            if (navController.currentDestination?.route != MapScreen.Delivery.name) {
              navController.navigate(MapScreen.Delivery.name) {
                popUpTo(MapScreen.Map.name)
              }
              vm.setTabIndex(2)
            }},
            selected = (tabIndex == 2),
            text = { Text(text = "Delivery", textAlign = TextAlign.Center) },
            icon = { Icon(
              Icons.Filled.Archive,
              tint = MaterialTheme.colorScheme.primary,
              contentDescription = "Localized description"
            )}
          )
        }
        // IconButton(onClick = { /* do something */ }) {
        //   Icon(
        //     Icons.Filled.Edit,
        //     tint = MaterialTheme.colorScheme.primary,
        //     contentDescription = "Localized description",
        //   )
        // }
        // IconButton(onClick = { /* do something */ }) {
        //   Icon(
        //     Icons.Filled.Image,
        //     tint = MaterialTheme.colorScheme.primary,
        //     contentDescription = "Localized description",
        //   )
        // }
        // IconButton(onClick = { /* do something */ }) {
        //   Icon(Icons.Filled.Mic,
        //     tint = MaterialTheme.colorScheme.primary,
        //     contentDescription = "Localized description",
        //   )
        // }
      },
      // floatingActionButton = {
      //   FloatingActionButton(
      //     onClick = { /* do something */ },
      //     containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
      //     elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
      //   ) {
      //     Icon(Icons.Filled.Add,
      //       tint = MaterialTheme.colorScheme.primary,
      //       contentDescription = "Localized description")
      //   }
      // }
    )


  }

  @OptIn(MapsComposeExperimentalApi::class, ExperimentalMaterial3Api::class)
  @Composable
  fun MapDestination(modifier: Modifier = Modifier
  ) {
    Column(modifier = modifier) {
      val mapUiState by vm.mapUiState.collectAsState()
      val cameraPositionState = rememberCameraPositionState()
      val coroutineScope = rememberCoroutineScope()
      val parcelItems = remember { mutableStateListOf<ParcelItem>() }
      val boundsBuilder = LatLngBounds.builder()
      val parcels = mapUiState.parcels
      val deliveryRoute = mapUiState.deliveryRoute

      val context = LocalContext.current
      val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
      }

      GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapLoaded = {
          if (parcels.isNotEmpty()) {
            parcelItems.clear()
            for (parcel in parcels) {
              boundsBuilder.include(parcel.position)
              parcelItems.add(ParcelItem(parcel.lat, parcel.lng, parcel.recipientName, parcel.address, parcel))
            }
          }
        }
      ) {
      // ClusterItemContent(parcelItems[0])
      Clustering(
        items = parcelItems,
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
          true
        },
        onClusterItemClick = {
          vm.setParcel(it.getParcel())
          vm.parcelSheet(shouldShow = true)
          true
        },
        clusterContent = { ClusterContent(cluster = it) },
        clusterItemContent = { ClusterItemContent(parcelItem = it) })

        if (deliveryRoute.isNotEmpty()) {
          val polylineColorPairs = listOf(
            0xFFFF0000.toInt() to 0xFF1C8ABD.toInt(),
          )
          Polyline(
            points = deliveryRoute,
            // color = Color.Red,
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

        MapEffect(true) { map ->
          map.isMyLocationEnabled = true
          map.uiSettings.isMyLocationButtonEnabled = true
          map.uiSettings.isZoomControlsEnabled = true
        }
      }
      // Handle permission requests for accessing fine location
      val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
      ) { isGranted ->
        if (isGranted) {
          // Fetch the user's location and update the camera if permission is granted
          vm.fetchUserLocation(context, fusedLocationClient)
        } else {
          // Handle the case when permission is denied
          Timber.e("Location permission was denied by the user.")
        }
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

      // Request the location permission when the composable is launched
      LaunchedEffect(Unit) {
        if (parcels.isEmpty()) {
          cameraPositionState.position = CameraPosition.fromLatLngZoom(mapUiState.currentPosition,
            mapUiState.zoom)
        } else {
          parcelItems.clear()
          for (parcel in parcels) {
            boundsBuilder.include(parcel.position)
            parcelItems.add(ParcelItem(parcel.lat, parcel.lng, parcel.recipientName, parcel.address, parcel))
          }
          cameraPositionState.move(
            update = CameraUpdateFactory.newLatLngBounds(
              boundsBuilder.build(),
              64
            )
          )
        }

        println("LaunchedEffect")
        when (PackageManager.PERMISSION_GRANTED) {
          // Check if the location permission is already granted
          ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
          ) -> {
            // Fetch the user's location and update the camera
            vm.fetchUserLocation(context, fusedLocationClient)
          }
          else -> {
            // Request the location permission if it has not been granted
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
          }
        }
      }
    }
  }

  @Composable
  fun ClusterContent(cluster: Cluster<ParcelItem>) {
    val size: String = "10+".takeIf { cluster.size > 10 } ?: cluster.size.toString()
    Text(size,
      color = Color(0xFF196B52),
      fontSize = 18.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier
        .padding(24.dp)
        .drawBehind {
          drawCircle(
            color = Color(0x77FFFFFF),// Color.hsl(155f, .92f, .75f),
            radius = 76f
          )
          drawCircle(
            color = Color(0xFF8AD6B8),// Color.hsl(155f, .92f, .75f),
            radius = 64f
          )
        },
    )
  }

  @Composable
  fun ClusterItemContent(parcelItem: ParcelItem) {

    val color by remember { mutableStateOf(Color.hsl(155f, 1f, .3f)) }
    val selectedColor = Color.hsl(0f, 1f, .33f)
    var isSelected: Boolean = parcelItem.isSelected

    var toggled by remember { mutableStateOf(false)}
    val interactionSource = remember {
      MutableInteractionSource()
    }

    Column(
      modifier = Modifier
        .clickable(indication = null,
          interactionSource = interactionSource) {
          toggled = !toggled
        },
    ) {
      val offsetTarget = if (toggled) {
        IntOffset(150, 150)
      } else {
        IntOffset.Zero
      }
      val offset = animateIntOffsetAsState(
        targetValue = offsetTarget, label = "offset"
      )
      Icon(
        Icons.Filled.LocationOn,
        tint = color.takeIf { !isSelected } ?: selectedColor,
        contentDescription = parcelItem.title,
        modifier = Modifier.size(48.dp).layout { measurable, constraints ->
          val offsetValue = if (isLookingAhead) offsetTarget else offset.value
          val placeable = measurable.measure(constraints)
          layout(placeable.width + offsetValue.x, placeable.height + offsetValue.y) {
            placeable.placeRelative(offsetValue)
          }
        }
      )
    }

  }

  @Composable
  fun ParcelDestination(modifier: Modifier = Modifier, navController: NavHostController?) {
    val uiState by vm.mapUiState.collectAsState()
    val parcels: List<Parcel>  = uiState.parcels
    LazyColumn(modifier = modifier) {
      items(parcels) { parcel ->
        ParcelItem(parcel)
      }
    }
    BackHandler(enabled = true) {
      navController?.popBackStack()
      vm.setTabIndex(0)
    }
  }

  @Composable
  fun DeliveryDestination(modifier: Modifier = Modifier,
                          navController: NavHostController?) {
    val uiState by vm.deliveryUiState.collectAsState()
    val parcels: List<Parcel>  = uiState.deliveryRoute
    val isComputing: Boolean = uiState.isComputing
    val progress = uiState.computingProgress
    val distance = uiState.deliveryDistance
    val duration = uiState.deliveryDuration
    DeliveryContent(modifier, isComputing, parcels, progress, distance, duration)
    BackHandler(enabled = true) {
      navController?.popBackStack()
      vm.setTabIndex(0)
    }
  }

  @Composable
  private fun DeliveryContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    parcels: List<Parcel>,
    loadingProgress: Float,
    distance: Float = 0f,
    duration: Float = 0f
  ) {
    Column(modifier = modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally) {
      Row(
        Modifier.padding(16.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
      ) {
        Button(onClick = {
          if (!isLoading)
            vm.getDeliveryRecommendation()
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
              ParcelItem(parcel)
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

  @Composable
  private fun DeliveryMetaInformation(
    parcels: List<Parcel>,
    distance: Float,
    duration: Float
  ) {
    Row(
      modifier = Modifier.padding(top = 8.dp, bottom = 16.dp).fillMaxWidth(),
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

  // @Preview
  // @Composable
  // fun PreviewParcelDestination() {
  //   AppTheme(darkTheme = false, dynamicColor = false) {
  //     ParcelDestination(navController = null)
  //   }
  // }

  @Preview(heightDp = 480)
  @Composable
  fun PreviewDeliveryDestination() {
    AppTheme(darkTheme = false, dynamicColor = false) {
      DeliveryContent(isLoading = false, parcels = emptyList(), loadingProgress = 0.6f)
    }
  }

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
      ParcelItem(Parcel(1, recipientName = "Djoko Sudemo",
        address = "Jl Agung Timur 4 Blok O No. 2 Kav. 18-19, Sunter Podomoro, North Jakarta"))
    }
  }



  fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
      if (context is Activity) return context
      context = context.baseContext
    }
    throw IllegalStateException("No Activity found.")
  }

}
