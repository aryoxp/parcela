package ap.mobile.composablemap

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.IntOffset
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ap.mobile.composablemap.model.ParcelMapItem
import ap.mobile.composablemap.optimizer.Logger
import ap.mobile.composablemap.repository.PreferencesKeys
import ap.mobile.composablemap.view.AppBar
import ap.mobile.composablemap.view.BottomNavigationBar
import ap.mobile.composablemap.view.BottomSheet
import ap.mobile.composablemap.view.DeliveryContent
import ap.mobile.composablemap.view.DeliveryMap
import ap.mobile.composablemap.view.ParcelDestination
import ap.mobile.composablemap.view.PreferenceDialog
import ap.mobile.composablemap.view.SettingsScreenPreferenceList
import ap.mobile.composablemap.view.SettingsScreenTopAppBar
import ap.mobile.composablemap.viewmodel.DeliveryUiState
import ap.mobile.composablemap.viewmodel.MapUiState
import ap.mobile.composablemap.viewmodel.MapViewModel
import ap.mobile.composablemap.viewmodel.ParcelUIState
import ap.mobile.composablemap.viewmodel.SettingsUIState
import ap.mobile.composablemap.viewmodel.SettingsViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.maps.android.compose.MapsComposeExperimentalApi
import kotlinx.serialization.Serializable
import timber.log.Timber
import java.net.URLDecoder


class MainActivity : ComponentActivity() {

  sealed class Nav {
    @Serializable object Main : Nav()
    @Serializable object Settings : Nav()
  }

  sealed class NavMain {
    @Serializable object Map : NavMain()
    @Serializable object Parcel : NavMain()
    @Serializable object Delivery : NavMain()
  }


  @RequiresApi(Build.VERSION_CODES.Q)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    val vm = MapViewModel(this.application)
    val vmSettings = SettingsViewModel(findActivity())

    // request permission for writing data to a file
    val request = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
      uri?.let {
        // call this to persist permission across device reboots
        contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        val decoded = URLDecoder.decode(uri.toString(), "UTF-8")
        val path = decoded.substringAfterLast(":")
        vmSettings.updatePreference(PreferencesKeys.LOG_FILE, path)
        vmSettings.clearPreference()
        Logger.saveFile(this, path, "data.csv")
      }
    }
    setContent {
      AppTheme(darkTheme = false, dynamicColor = false) {
        val mapUiState by vm.mapUiState.collectAsState()
        val deliveryUiState by vm.deliveryUiState.collectAsState()
        val settingsUIState by vmSettings.settingsUiState.collectAsState()
        val parcelState by vm.parcelState.collectAsState()
        MyScaffold(
          request, mapUiState,
          fetchUserLocation = { vm.fetchUserLocation(this, it) },
          selectParcel = {
            vm.selectParcel(it)
            vm.parcelSheet(it?.let { true } ?: false)
          },
          deliveryUiState = deliveryUiState,
          settingsUiState = settingsUIState,
          parcelUiState = parcelState,
          getDeliveryRecommendation = { vm.getDeliveryRecommendation(this, it) },
          updatePreference = { key, value -> vmSettings.updatePreference(key, value) },
          setPreference = { vmSettings.setPreference(it) },
          updateSwitchPreference = { key, value -> vmSettings.updateSwitchPreference(key, value) },
          clearPreference = { vmSettings.clearPreference() }
        )
      }
    }
  }

  @Composable
  fun MyScaffold(
    request: ActivityResultLauncher<Uri?>,
    mapUiState: MapUiState,
    parcelUiState: ParcelUIState,
    deliveryUiState: DeliveryUiState,
    settingsUiState: SettingsUIState,
    fetchUserLocation: (FusedLocationProviderClient) -> Unit,
    selectParcel: (ParcelMapItem?) -> Unit,
    getDeliveryRecommendation: (ParcelMapItem?) -> Unit,
    setPreference: (String) -> Unit,
    updatePreference: (String, String) -> Unit = { _, _ -> },
    updateSwitchPreference: (String, Boolean) -> Unit = { _, _ -> },
    clearPreference: () -> Unit = {},
    ) {
    val rootNavController: NavHostController = rememberNavController()
    val animationSpec = TweenSpec<IntOffset>(300, easing = FastOutSlowInEasing)
    val fadeAnimationSpec: FiniteAnimationSpec<Float> = TweenSpec(1000)

    val window = (LocalActivity.current as Activity).window
    val view = LocalView.current

    SideEffect {
      // force light mode for status bar items
      WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
    }

    NavHost(navController = rootNavController,
      startDestination = Nav.Main,
      enterTransition = {
        fadeIn(animationSpec = fadeAnimationSpec) + slideIntoContainer(
          animationSpec = animationSpec,
          towards = AnimatedContentTransitionScope.SlideDirection.Start
        )
      },
      exitTransition = {
        fadeOut(animationSpec = fadeAnimationSpec) + slideOutOfContainer(
          animationSpec = animationSpec,
          towards = AnimatedContentTransitionScope.SlideDirection.Start
        )
      },
      popEnterTransition = {
        fadeIn(animationSpec = fadeAnimationSpec
        ) + slideIntoContainer(
          animationSpec = animationSpec,
          towards = AnimatedContentTransitionScope.SlideDirection.End
        )
      },
      popExitTransition = {
        fadeOut(animationSpec = fadeAnimationSpec) + slideOutOfContainer(
          animationSpec = animationSpec,
          towards = AnimatedContentTransitionScope.SlideDirection.End
        )
      }
    ) {
      composable<Nav.Main> {
        MapScreen(
          onNavigate = { rootNavController.navigate(it) },
          onConfirmExit = { findActivity().finish() },
          mapUiState = mapUiState,
          fetchUserLocation = { fetchUserLocation(it) },
          selectParcel = { selectParcel(it) },
          deliveryUiState = deliveryUiState,
          parcelState = parcelUiState,
          getDeliveryRecommendation = { getDeliveryRecommendation(it) }
        )
      }
      composable<Nav.Settings> {
        SettingsScreen(
          state = settingsUiState,
          onSetPreference = { key -> setPreference(key) },
          onClearPreference = { clearPreference() },
          onUpdatePreference = { key, value -> updatePreference(key, value) },
          onUpdateSwitchPreference = { key, value -> updateSwitchPreference(key, value) },
          onBackButtonClick = { rootNavController.popBackStack() },
          request = request
        )
      }
    }
  }

  @Composable
  fun MapScreen(
    onNavigate: (Nav) -> Unit,
    onConfirmExit: (Boolean) -> Unit,
    mapUiState: MapUiState,
    fetchUserLocation: (FusedLocationProviderClient) -> Unit,
    selectParcel: (ParcelMapItem?) -> Unit,
    deliveryUiState: DeliveryUiState,
    parcelState: ParcelUIState,
    getDeliveryRecommendation: (ParcelMapItem?) -> Unit
  ) {
    var tabIndex by remember { mutableIntStateOf(0) }
    var showExitDialog by remember { mutableStateOf(false) }
    val mainNavController = rememberNavController()
    val fadeAnimationSpec = TweenSpec<Float>(300, easing = FastOutSlowInEasing)
    val animationSpec = TweenSpec<IntOffset>(300, easing = FastOutSlowInEasing)

    Scaffold(modifier = Modifier.fillMaxSize(),
      containerColor = MaterialTheme.colorScheme.surface,
      topBar = { AppBar(
        onNavigateBack = {
          if (mainNavController.currentDestination?.hasRoute<NavMain.Map>() == true) {
            showExitDialog = true
          } else {
            mainNavController.popBackStack()
          }
        },
        onNavigate = { destination -> onNavigate(destination) }
      ) },
      bottomBar = { BottomNavigationBar(tabIndex, onNavigate = { index ->
        when (index) {
          0 -> {
            if (mainNavController.currentDestination?.hasRoute<NavMain.Map>() != true) {
              mainNavController.popBackStack()
              tabIndex = 0
            }
          }
          1 -> {
            if (mainNavController.currentDestination?.hasRoute<NavMain.Parcel>() != true) {
              mainNavController.navigate(NavMain.Parcel) {
                popUpTo(NavMain.Map)
              }
              tabIndex = 1
            }
          }
          2 -> {
            if (mainNavController.currentDestination?.hasRoute<NavMain.Delivery>() != true) {
              mainNavController.navigate(NavMain.Delivery) {
                popUpTo(NavMain.Map)
              }
              tabIndex = 2
            }
          }
        }
      }) }
    ) { padding ->
      NavHost(
        navController = mainNavController,
        startDestination = NavMain.Map,
        enterTransition = {
          fadeIn(animationSpec = fadeAnimationSpec) + slideIntoContainer(
            animationSpec = animationSpec,
            towards = AnimatedContentTransitionScope.SlideDirection.Start
          )
        },
        exitTransition = {
          fadeOut(animationSpec = fadeAnimationSpec) + slideOutOfContainer(
            animationSpec = animationSpec,
            towards = AnimatedContentTransitionScope.SlideDirection.Start
          )
        },
        popEnterTransition = {
          fadeIn(animationSpec = fadeAnimationSpec
          ) + slideIntoContainer(
            animationSpec = animationSpec,
            towards = AnimatedContentTransitionScope.SlideDirection.End
          )
        },
        popExitTransition = {
          fadeOut(animationSpec = fadeAnimationSpec) + slideOutOfContainer(
            animationSpec = animationSpec,
            towards = AnimatedContentTransitionScope.SlideDirection.End
          )
        }
        ) {
        composable<NavMain.Map> { // (route = MapScreen.Map.name) {
          MapDestination(
            modifier = Modifier.padding(padding),
            mapUiState = mapUiState,
            parcelState = parcelState,
            fetchUserLocation = { fetchUserLocation(it) },
            selectParcel = { selectParcel(it) },
            deselectParcel = { selectParcel(null) },
            getDeliveryRecommendation = { getDeliveryRecommendation(it) }
          )
        }
        composable<NavMain.Parcel> { // (route = MapScreen.Parcel.name) {
          ParcelDestination(
            modifier = Modifier.padding(padding),
            onBackHandler = { mainNavController.popBackStack() },
            parcels = mapUiState.parcels
          )
        }
        composable<NavMain.Delivery> { // (route = MapScreen.Delivery.name) {
          DeliveryDestination(
            modifier = Modifier.padding(padding),
            onBackHandler = { mainNavController.popBackStack() },
            uiState = deliveryUiState,
            getDeliveryRecommendation = { getDeliveryRecommendation(null) }
          )
        }
      }
    }
    if (showExitDialog) {
      AlertDialog(
        onDismissRequest = { showExitDialog = !showExitDialog },
        title = { Text("Exit App") },
        text = { Text("Do you want to exit?") },
        dismissButton = {
          TextButton(onClick = { showExitDialog = !showExitDialog }) {
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
  }

  @OptIn(MapsComposeExperimentalApi::class, ExperimentalMaterial3Api::class)
  @Composable
  fun MapDestination(modifier: Modifier = Modifier,
                     mapUiState: MapUiState,
                     parcelState: ParcelUIState,
                     fetchUserLocation: (FusedLocationProviderClient) -> Unit,
                     selectParcel: (ParcelMapItem?) -> Unit,
                     deselectParcel: () -> Unit,
                     getDeliveryRecommendation: (ParcelMapItem) -> Unit
  ) {
    val context = LocalContext.current

    val fusedLocationClient = remember {
      LocationServices.getFusedLocationProviderClient(context)
    }

    // Handle permission requests for accessing fine location
    val permissionLauncher = rememberLauncherForActivityResult(
      contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
      if (isGranted) {
        // Fetch the user's location and update the camera if permission is granted
        fetchUserLocation(fusedLocationClient)
      } else {
        // Handle the case when permission is denied
        Timber.e("Location permission was denied by the user.")
      }
    }

    DeliveryMap(modifier = modifier,
      parcels = mapUiState.parcels,
      deliveryRoute = mapUiState.deliveryRoute,
      currentPosition = mapUiState.currentPosition,
      zoom = mapUiState.zoom,
      onSelectParcel = { selectParcel(it) },
      onCheckLocationPermission = {
        when (PackageManager.PERMISSION_GRANTED) {
          // Check if the location permission is already granted
          ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) -> {
            // Fetch the user's location and update the camera
            fetchUserLocation(fusedLocationClient)
          } else -> {
            // Request the location permission if it has not been granted
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
          }
        }
      }
    )

    BottomSheet(
      isComputing = parcelState.isComputing,
      showBottomSheet = parcelState.showParcelSheet,
      deliveryDistance = parcelState.deliveryDistance,
      deliveryDuration = parcelState.deliveryDuration,
      parcel = parcelState.parcel,
      parcels = parcelState.deliveries,
      onDismiss = deselectParcel,
      onGetDeliveryRecommendation = { parcel -> getDeliveryRecommendation(parcel) }
    )
  }

  @Composable
  fun DeliveryDestination(modifier: Modifier = Modifier,
                          onBackHandler: () -> Unit,
                          uiState: DeliveryUiState,
                          getDeliveryRecommendation: () -> Unit
  ) {
    // val uiState by vm.deliveryUiState.collectAsState()
    // val context = LocalContext.current

    DeliveryContent(
      modifier,
      parcels = uiState.deliveryRoute,
      distance = uiState.deliveryDistance,
      duration = uiState.deliveryDuration,
      isLoading = uiState.isComputing,
      loadingProgress = uiState.computingProgress,
      onGetDeliveryRecommendation = {
        getDeliveryRecommendation()
      }
    )
    BackHandler(enabled = true) { onBackHandler() }
  }

  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  fun SettingsScreen(
    state: SettingsUIState,
    onSetPreference: (String) -> Unit,
    onClearPreference: () -> Unit,
    onUpdatePreference: (String, String) -> Unit,
    onUpdateSwitchPreference: (String, Boolean) -> Unit,
    onBackButtonClick: () -> Unit,
    request: ActivityResultLauncher<Uri?>,
  ) {
    Scaffold(topBar = {
        SettingsScreenTopAppBar(onBackButtonClick = { onBackButtonClick() } )
      }) { padding ->
      SettingsScreenPreferenceList(padding, categoryItems = mapOf(
        PreferencesKeys.HOST to state.hostFriendlyValue,
        PreferencesKeys.OPTIMIZER to state.optimizerFriendlyValue,
        PreferencesKeys.OPT_METHOD to state.optMethodFriendlyValue,
        PreferencesKeys.USE_API to state.useOnlineApiFriendlyValue,
        PreferencesKeys.LOG_FILE to state.logFileFriendlyValue,
        PreferencesKeys.HEURISTIC_INIT to state.useHeuristicValue
        ),
        onCategoryItemClick = { onSetPreference(it) },
        onUpdateSwitchPreference = {
          key, value -> onUpdateSwitchPreference(key, value)
        }
      )
      if (state.preference.key.isNotBlank())
        PreferenceDialog(onDismiss = { onClearPreference() },
          preference = state.preference,
          preferenceOptions = state.options,
          onUpdatePreference = {
            key, value -> onUpdatePreference(key, value)
          },
          request = request
        )
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
