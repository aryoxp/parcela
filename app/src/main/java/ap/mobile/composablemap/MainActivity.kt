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
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AllInbox
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import timber.log.Timber


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

  @Composable
  fun MyScaffold() {
    val mapUiState by vm.uiState.collectAsState()
    val navController: NavHostController = rememberNavController()
    var tabIndex = mapUiState.tabIndex
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
          MapDestination(
            modifier = Modifier.padding(padding),
            currentMapPosition = mapUiState.currentPosition,
            zoom = mapUiState.zoom,
            cameraPosition = mapUiState.cameraPosition,
            parcels = mapUiState.parcels)
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
  }

  @Preview
  @Composable
  fun MyScaffoldPreview() {
    AppTheme(darkTheme = false, dynamicColor = false) {
      MyScaffold()
    }
  }

  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  fun AppBar(navHostController: NavHostController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val openExitDialog = remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
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
          } else openExitDialog.value = true
        }) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Localized description"
          )
        }
      },
      actions = {
        IconButton(onClick = {
          expanded = !expanded
          // if (navHostController.currentDestination?.route != MapScreen.Parcel.name)
          //   navHostController.navigate(MapScreen.Parcel.name)
          // else navHostController.popBackStack()
        }) {
          Icon(
            imageVector = Icons.Filled.Menu,
            contentDescription = "Localized description"
          )
        }
        DropdownMenu(
          expanded = expanded,
          onDismissRequest = { expanded = false }
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
            onClick = { /* Do something... */ }
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
            onClick = { /* Do something... */ }
          )
        }
      },
      scrollBehavior = scrollBehavior,
    )
    when {
      openExitDialog.value -> {
        ExitDialog(isExiting = openExitDialog)
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

  @Composable
  fun MapDestination(
    modifier: Modifier = Modifier,
    currentMapPosition: LatLng,
    zoom: Float,
    cameraPosition: LatLng,
    parcels: List<Parcel>
  ) {

    Column(modifier = modifier) {
      val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(cameraPosition, zoom)
      }
      val coroutineScope = rememberCoroutineScope()
      val parcelItems = remember { mutableStateListOf<ParcelItem>() }
      val boundsBuilder = LatLngBounds.builder()

      if (parcels.isNotEmpty()) {
        parcelItems.clear()
        for (parcel in parcels) {
          boundsBuilder.include(parcel.position)
          parcelItems.add(ParcelItem(parcel.lat, parcel.lng, parcel.name, parcel.address))
        }
      } else boundsBuilder.include(currentMapPosition)


      LaunchedEffect(currentMapPosition) {
        // Move the camera to the user's location with a zoom level of 10f
        // cameraPositionState.move(CameraUpdateFactory
        //   .newLatLngZoom(LatLng(currentMapPosition.latitude, currentMapPosition.longitude), 10f))
        coroutineScope.launch {
          cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 128),
            durationMs = 1000
          )
          println("Coroutine")
          println(cameraPositionState.position.zoom)
          vm.setCameraPosition(cameraPositionState.position.target)
        }
      }

      LaunchedEffect(cameraPositionState.isMoving) {
        val position = cameraPositionState.position
        val isMoving = cameraPositionState.isMoving

        if (!isMoving) {
          val cameraLocation = LatLng(position.target.latitude, position.target.longitude)
          vm.setCameraPosition(cameraLocation)
          vm.setZoomLevel(cameraPositionState.position.zoom)
        }
      }

      val context = LocalContext.current
      val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
      }

      GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
      ) {
        // If the user's location is available, place a marker on the map
        if (parcels.isEmpty())
          currentMapPosition.let {
            Marker(
              state = MarkerState(position = it), // Place the marker at the user's location
              title = "Your Location", // Set the title for the marker
              snippet = "This is where you are currently located." // Set the snippet for the marker
            )
          }

        Clustering(items = parcelItems,
          onClusterClick = {
            cameraPositionState.move(
              update = CameraUpdateFactory.zoomIn()
            )
            false
          },
          clusterContent = { ClusterContent(cluster = it) },
          clusterItemContent = { ClusterItemContent(parcel = it) })

        // parcels.forEach { parcel ->
        //   Marker(
        //     state = MarkerState(position = parcel.position),
        //     title = parcel.name,
        //     snippet = parcel.address
        //   )
        // }
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

      // Request the location permission when the composable is launched
      LaunchedEffect(Unit) {
        cameraPositionState.animate(
          update = CameraUpdateFactory.newLatLngZoom(currentMapPosition, zoom),
          durationMs = 1000
        )
        println("LaunchedEffect")
        when (PackageManager.PERMISSION_GRANTED) {
          // Check if the location permission is already granted
          ContextCompat.checkSelfPermission(context,
            Manifest.permission.ACCESS_FINE_LOCATION) -> {
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
  fun ParcelDestination(modifier: Modifier = Modifier, navController: NavHostController?) {
    val uiState by vm.uiState.collectAsState()
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
    val uiState by vm.uiState.collectAsState()
    val parcels: List<Parcel>  = uiState.deliveries
    val isLoading: Boolean = uiState.isLoadingRecommendation
    Column(modifier = modifier.fillMaxWidth()) {
      Row(Modifier
        .padding(16.dp)
        .align(Alignment.CenterHorizontally)) {
        Button(onClick = {
          if (!isLoading)
            vm.getDeliveryRecommendation()
        }) {
          Text(text = ("Stop".takeIf { isLoading } ?: "Delivery Recommendation"))
        }
      }
      if (!isLoading) {
        LazyColumn {
          items(parcels) { parcel ->
            ParcelItem(parcel)
          }
        }
      } else {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
          horizontalAlignment = Alignment.Start,
          verticalArrangement = Arrangement.spacedBy(8.dp)) {
          val percent = (uiState.loadingProgress * 100).toInt().toString() + "%"
          Text("Computing delivery route... ${percent}")
          LinearProgressIndicator(
            progress = { uiState.loadingProgress },
            modifier = Modifier.fillMaxWidth()
          )
        }
      }
    }
    BackHandler(enabled = true) {
      navController?.popBackStack()
      vm.setTabIndex(0)
    }
  }

  @Preview
  @Composable
  fun PreviewParcelDestination() {
    AppTheme(darkTheme = false, dynamicColor = false) {
      ParcelDestination(navController = null)
    }
  }

  @Preview
  @Composable
  fun PreviewDeliveryDestination() {
    AppTheme(darkTheme = false, dynamicColor = false) {
      DeliveryDestination(navController = null)
    }
  }

  @Composable
  fun ParcelItem(parcel: Parcel, modifier: Modifier = Modifier) {
    Row(Modifier
      .fillMaxWidth()
      .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically) {
      Column(Modifier.weight(1f)) {
        Row {
          Text(
            text = parcel.name, fontSize = 20.sp,
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
      FilledTonalIconButton(onClick = {

      }) {
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
      ParcelItem(Parcel(1, name = "Djoko Sudemo",
        address = "Jl Agung Timur 4 Blok O No. 2 Kav. 18-19, Sunter Podomoro, North Jakarta"))
    }
  }

  @Composable
  fun ClusterContent(cluster: Cluster<ParcelItem>) {
    val size: String = "10+".takeIf { cluster.size > 10 } ?: cluster.size.toString()
    Text(size,
      color = Color(0xFF196B52),
      fontSize = 18.sp,
      modifier = Modifier.padding(24.dp)
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
  fun ClusterItemContent(parcel: ParcelItem) {
    Icon (
      Icons.Filled.LocationOn,
      tint = Color.hsl(155f, 1f, .3f),
      contentDescription = "Localized description",
      modifier = Modifier.size(32.dp)
    )
  }

  @Composable
  fun ExitDialog(modifier: Modifier = Modifier,
                 isExiting: MutableState<Boolean> = mutableStateOf(true)) {
    val activity: Activity = findActivity()
    AlertDialog(
      onDismissRequest = {
        isExiting.value = false // vm.confirmExit(false)
      },
      title = { Text("Exit App") },
      text = { Text("Do you want to exit?") },
      modifier = modifier,
      dismissButton = {
        TextButton(onClick = {
          isExiting.value = false // vm.confirmExit(false)
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

  fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
      if (context is Activity) return context
      context = context.baseContext
    }
    throw IllegalStateException("no activity")
  }

}
