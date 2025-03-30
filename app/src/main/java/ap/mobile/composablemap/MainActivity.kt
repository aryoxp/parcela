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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ap.mobile.composablemap.ui.AppBar
import ap.mobile.composablemap.ui.BottomNavigationBar
import ap.mobile.composablemap.ui.BottomSheet
import ap.mobile.composablemap.ui.DeliveryContent
import ap.mobile.composablemap.ui.DeliveryMap
import ap.mobile.composablemap.ui.ExitDialog
import ap.mobile.composablemap.ui.ParcelDestination
import ap.mobile.composablemap.ui.PreferenceDialog
import ap.mobile.composablemap.ui.SettingsScreenPreferenceList
import ap.mobile.composablemap.ui.SettingsScreenTopAppBar
import com.google.android.gms.location.LocationServices
import com.google.maps.android.compose.MapsComposeExperimentalApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val vm: MapViewModel by viewModels()

  sealed class Nav {
    @Serializable object Main : Nav()
    @Serializable object Settings : Nav()
  }

  sealed class NavMain {
    @Serializable object Map : NavMain()
    @Serializable object Parcel : NavMain()
    @Serializable object Delivery : NavMain()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    val vmSettings = SettingsViewModel(this)
    setContent {
      AppTheme(darkTheme = false, dynamicColor = false) {
        MyScaffold(vmSettings)
      }
    }
  }

  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  fun MyScaffold(vmSettings: SettingsViewModel) {
    val rootNavController: NavHostController = rememberNavController()
    NavHost(navController = rootNavController,
      startDestination = Nav.Main) {
      composable<Nav.Main> {
        MainScreen(rootNavController)
      }
      composable<Nav.Settings> {
        SettingsScreen(rootNavController, vmSettings)
      }
    }
  }

  @Composable
  fun MainScreen(rootNavController: NavHostController) {
    MainScaffold(rootNavController = rootNavController, onConfirmExit = {
      findActivity().finish()
    })
  }

  @Composable
  fun MainScaffold(
    rootNavController: NavController,
    onConfirmExit: (Boolean) -> Unit
  ) {
    var tabIndex by remember { mutableIntStateOf(0) }
    var showExitDialog by remember { mutableStateOf(false) }
    val mapUiState by vm.mapUiState.collectAsState()

    val mainNavController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize(),
      containerColor = MaterialTheme.colorScheme.surface,
      topBar = { AppBar(
        onNavigateBack = {
          if (mainNavController.currentDestination?.hasRoute<NavMain.Map>() == true) {
            showExitDialog = true
          } else {
            mainNavController.popBackStack()
            tabIndex = 0
          }
        },
        onNavigate = { destination ->
          rootNavController.navigate(destination)
        }
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
        composable<NavMain.Map> { // (route = MapScreen.Map.name) {
          MapDestination(modifier = Modifier.padding(padding))
        }
        composable<NavMain.Parcel> { // (route = MapScreen.Parcel.name) {
          ParcelDestination(
            modifier = Modifier.padding(padding),
            onBackHandler = {
              mainNavController.popBackStack()
              tabIndex = 0
            },
            parcels = mapUiState.parcels
          )
        }
        composable<NavMain.Delivery> { // (route = MapScreen.Delivery.name) {
          DeliveryDestination(
            modifier = Modifier.padding(padding), onBackHandler = {
              mainNavController.popBackStack()
              tabIndex = 0
            })
        }
      }
    }
    if (showExitDialog) {
      ExitDialog(onDismiss = { showExitDialog = false },
        onConfirmExit = onConfirmExit)
    }
  }

  @OptIn(MapsComposeExperimentalApi::class, ExperimentalMaterial3Api::class)
  @Composable
  fun MapDestination(modifier: Modifier = Modifier
  ) {
    val mapUiState by vm.mapUiState.collectAsState()
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
        vm.fetchUserLocation(context, fusedLocationClient)
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
      onSelectParcel = { parcel: Parcel ->
        vm.selectParcel(parcel)
        vm.parcelSheet(true)
      },
      onCheckLocationPermission = {
        when (PackageManager.PERMISSION_GRANTED) {
          // Check if the location permission is already granted

          ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) -> {
            // Fetch the user's location and update the camera
            vm.fetchUserLocation(context, fusedLocationClient)
          }

          else -> {
            // Request the location permission if it has not been granted
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
          }
        }
      }
    )


    val parcelState by vm.parcelState.collectAsState()
    BottomSheet(
      isComputing = parcelState.isComputing,
      showBottomSheet = parcelState.showParcelSheet,
      deliveryDistance = parcelState.deliveryDistance,
      deliveryDuration = parcelState.deliveryDuration,
      parcel = parcelState.parcel,
      parcels = parcelState.deliveries,
      onDismiss = { vm.parcelSheet(shouldShow = false) },
      onGetDeliveryRecommendation = { parcel -> vm.getDeliveryRecommendation(parcel) }
    )
  }

  @Composable
  fun DeliveryDestination(modifier: Modifier = Modifier,
                          onBackHandler: () -> Unit) {
    val uiState by vm.deliveryUiState.collectAsState()
    DeliveryContent(
      modifier,
      parcels = uiState.deliveryRoute,
      distance = uiState.deliveryDistance,
      duration = uiState.deliveryDuration,
      isLoading = uiState.isComputing,
      loadingProgress = uiState.computingProgress,
      onGetDeliveryRecommendation = {
        vm.getDeliveryRecommendation()
      }
    )
    BackHandler(enabled = true) { onBackHandler() }
  }

  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  fun SettingsScreen(navController: NavHostController, vmSettings: SettingsViewModel) {
    val host by vmSettings.host.collectAsState()
    val optMethod by vmSettings.optMethod.collectAsState()
    val useOnlineApi by vmSettings.useOnlineApi.collectAsState()
    val state by vmSettings.settingsUiState.collectAsState()
    Scaffold(
      topBar = {
        SettingsScreenTopAppBar {
          navController.popBackStack()
        }
      }
    ) { padding ->
      SettingsScreenPreferenceList(padding, categoryItems = mapOf(
        PreferencesKeys.HOST to host,
        PreferencesKeys.OPT_METHOD to optMethod,
        PreferencesKeys.USE_API to useOnlineApi
        ),
        onCategoryItemClick = {
          vmSettings.setPreference(it)
        },
        onUpdateSwitchPreference = {
          key, value -> vmSettings.updateSwitchPreference(key, value)
        }
      )
      if (state.preference.key.isNotBlank())
        PreferenceDialog(onDismiss = { vmSettings.clearPreference() },
          preference = state.preference,
          preferenceOptions = state.options,
          onUpdatePreference = {
            key, value -> vmSettings.updatePreference(key, value)
          }
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
