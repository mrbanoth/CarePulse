package com.example.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.components.AppointmentDetailSheet
import com.example.ui.components.BookingBottomSheet
import com.example.ui.components.BottomNavTab
import com.example.ui.components.CheckupDetailSheet
import com.example.ui.components.FloatingBottomNavBar
import com.example.ui.components.NotificationsSheet
import com.example.ui.components.TreatmentPlansSheet
import com.example.ui.screens.ClinicsScreen
import com.example.ui.screens.DoctorDetailScreen
import com.example.ui.screens.DoctorDirectoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PrescriptionsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.TreatmentScreen
import com.example.ui.screens.VideoCallScreen
import com.example.ui.state.DoctorAppViewModel
import com.example.ui.theme.AlabasterCanvas

const val ROUTE_DIRECTORY = "directory"
const val ROUTE_DOCTOR_DETAIL = "doctor_detail/{doctorId}"
const val ROUTE_VIDEO_CALL = "video_call"

@Composable
fun CarePulseApp(
  viewModel: DoctorAppViewModel = viewModel(),
) {
  val navController = rememberNavController()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val videoCallState by viewModel.videoCallState.collectAsStateWithLifecycle()
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route

  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(uiState.statusBannerMessage) {
    val msg = uiState.statusBannerMessage
    if (msg != null) {
      snackbarHostState.showSnackbar(msg)
      viewModel.dismissStatusBanner()
    }
  }

  val activeTopTab = BottomNavTab.entries.firstOrNull { it.route == currentRoute }
  val isTopLevelRoute = activeTopTab != null

  val navigateToTab: (BottomNavTab) -> Unit = { tab ->
    navController.navigate(tab.route) {
      popUpTo(navController.graph.findStartDestination().id) { saveState = true }
      launchSingleTop = true
      restoreState = true
    }
  }

  val launchVideoCall: (String?) -> Unit = { doctorId ->
    viewModel.startVideoCall(doctorId)
    navController.navigate(ROUTE_VIDEO_CALL)
  }

  BoxWithConstraints(
    modifier = Modifier.fillMaxSize().background(AlabasterCanvas),
  ) {
    val isExpandedScreen = maxWidth >= 600.dp

    Row(modifier = Modifier.fillMaxSize()) {
      if (isExpandedScreen && isTopLevelRoute) {
        NavigationRail(
          containerColor = MaterialTheme.colorScheme.surface,
          modifier = Modifier.fillMaxHeight(),
        ) {
          BottomNavTab.entries.forEach { tab ->
            val selected = tab == activeTopTab
            NavigationRailItem(
              selected = selected,
              onClick = { navigateToTab(tab) },
              icon = {
                Icon(
                  imageVector = if (selected) tab.activeIcon else tab.inactiveIcon,
                  contentDescription = stringResource(tab.labelRes),
                )
              },
              label = { Text(stringResource(tab.labelRes)) },
            )
          }
        }
      }

      Box(
        modifier = Modifier.weight(1f).fillMaxHeight(),
        contentAlignment = Alignment.TopCenter,
      ) {
        val contentModifier =
          if (currentRoute == ROUTE_VIDEO_CALL) {
            Modifier.fillMaxSize()
          } else {
            Modifier.fillMaxSize().widthIn(max = 600.dp)
          }

        NavHost(
          navController = navController,
          startDestination = BottomNavTab.HOME.route,
          modifier = contentModifier,
        ) {
          composable(BottomNavTab.HOME.route) {
            HomeScreen(
              uiState = uiState,
              onSearchQueryChanged = viewModel::onSearchQueryChanged,
              onSpecialtyToggled = viewModel::onSpecialtyToggled,
              onClearFilters = viewModel::clearFilters,
              onNotificationsClick = viewModel::openNotificationsSheet,
              onProfileClick = { navigateToTab(BottomNavTab.PROFILE) },
              onAppointmentClick = viewModel::openAppointmentDetail,
              onSeeAllDoctorsClick = { navController.navigate(ROUTE_DIRECTORY) },
              onDoctorClick = { doc -> navController.navigate("doctor_detail/${doc.id}") },
              onBookNowClick = viewModel::openBookingSheet,
              onStartVideoCall = launchVideoCall,
            )
          }

          composable(BottomNavTab.TREAT.route) {
            TreatmentScreen(
              uiState = uiState,
              onBack = { navigateToTab(BottomNavTab.HOME) },
              onSwitchPatient = viewModel::switchTreatmentPatient,
              onOpenTreatmentPlans = viewModel::openTreatmentPlansSheet,
              onCheckupClick = viewModel::openCheckupDetail,
              onStartVideoCall = launchVideoCall,
              onSeeAllDoctorsClick = { navController.navigate(ROUTE_DIRECTORY) },
              onDoctorClick = { doc -> navController.navigate("doctor_detail/${doc.id}") },
              onVisitNowClick = viewModel::openBookingSheet,
            )
          }

          composable(BottomNavTab.MEDS.route) {
            PrescriptionsScreen(
              uiState = uiState,
              onBack = { navigateToTab(BottomNavTab.HOME) },
              onToggleTaken = viewModel::togglePrescriptionTaken,
              onRequestRefill = viewModel::requestPrescriptionRefill,
            )
          }

          composable(BottomNavTab.CLINIC.route) {
            ClinicsScreen(
              uiState = uiState,
              onBack = { navigateToTab(BottomNavTab.HOME) },
              onBookAtClinic = { clinic ->
                val matchingDoc =
                  uiState.allDoctors.firstOrNull { doc ->
                    clinic.featuredSpecialties.any {
                      doc.specialtyName.contains(it, ignoreCase = true) ||
                        it.contains(doc.specialtyName, ignoreCase = true)
                    }
                  } ?: uiState.allDoctors.firstOrNull()
                if (matchingDoc != null) {
                  viewModel.openBookingSheet(matchingDoc)
                }
              },
            )
          }

          composable(BottomNavTab.PROFILE.route) {
            ProfileScreen(
              uiState = uiState,
              onBack = { navigateToTab(BottomNavTab.HOME) },
              onStartQuickVideoCall = launchVideoCall,
              onOpenTreatments = { navigateToTab(BottomNavTab.TREAT) },
              onOpenNotifications = viewModel::openNotificationsSheet,
              onSwitchTreatmentPatient = viewModel::switchTreatmentPatient,
            )
          }

          composable(ROUTE_DIRECTORY) {
            DoctorDirectoryScreen(
              uiState = uiState,
              onSearchQueryChanged = viewModel::onSearchQueryChanged,
              onSpecialtyToggled = viewModel::onSpecialtyToggled,
              onClearFilters = viewModel::clearFilters,
              onDoctorClick = { doc -> navController.navigate("doctor_detail/${doc.id}") },
              onBookNowClick = viewModel::openBookingSheet,
              onBack = { navController.popBackStack() },
            )
          }

          composable(
            route = ROUTE_DOCTOR_DETAIL,
            arguments = listOf(navArgument("doctorId") { type = NavType.StringType }),
          ) { entry ->
            val doctorId = entry.arguments?.getString("doctorId")
            val doctor = doctorId?.let { viewModel.findDoctorById(it) } ?: uiState.allDoctors.firstOrNull()
            if (doctor != null) {
              DoctorDetailScreen(
                doctor = doctor,
                onBack = { navController.popBackStack() },
                onBookAppointment = { viewModel.openBookingSheet(doctor) },
                onStartVideoCall = { launchVideoCall(doctor.id) },
              )
            }
          }

          composable(ROUTE_VIDEO_CALL) {
            VideoCallScreen(
              callState = videoCallState,
              onToggleMute = viewModel::toggleMute,
              onToggleSpeaker = viewModel::toggleSpeaker,
              onToggleCamera = viewModel::toggleCamera,
              onToggleScreenShare = viewModel::toggleScreenShare,
              onEndCall = {
                viewModel.endVideoCall()
                navController.popBackStack()
              },
            )
          }
        }

        // Bottom Nav Bar for Compact / Phone Layouts
        if (!isExpandedScreen && isTopLevelRoute) {
          FloatingBottomNavBar(
            selectedTab = activeTopTab!!,
            onSelectTab = navigateToTab,
            modifier = Modifier.align(Alignment.BottomCenter).widthIn(max = 600.dp),
          )
        }

        // Floating Snackbar Host
        SnackbarHost(
          hostState = snackbarHostState,
          modifier =
            Modifier.align(Alignment.BottomCenter)
              .navigationBarsPadding()
              .padding(bottom = if (isTopLevelRoute && !isExpandedScreen) 86.dp else 18.dp, start = 16.dp, end = 16.dp),
        )
      }
    }
  }

  // Modal Sheets
  uiState.bookingTargetDoctor?.let { doctor ->
    BookingBottomSheet(
      doctor = doctor,
      onDismiss = viewModel::dismissBookingSheet,
      onConfirmBooking = { dateLabel, dayNumber, monthShort, timeSlot, type ->
        viewModel.confirmBooking(doctor, dateLabel, dayNumber, monthShort, timeSlot, type)
      },
    )
  }

  if (uiState.showTreatmentPlansSheet) {
    val patient = uiState.treatmentPatient
    TreatmentPlansSheet(
      patientName = patient?.displayName ?: "Esther Howard",
      plans = patient?.treatmentPlans ?: emptyList(),
      onDismiss = viewModel::dismissTreatmentPlansSheet,
    )
  }

  if (uiState.showNotificationsSheet) {
    NotificationsSheet(
      notifications = uiState.notifications,
      onMarkAllRead = viewModel::markAllNotificationsRead,
      onDismiss = viewModel::dismissNotificationsSheet,
    )
  }

  uiState.selectedAppointment?.let { appointment ->
    AppointmentDetailSheet(
      appointment = appointment,
      onStartVideoCall = {
        viewModel.dismissAppointmentDetail()
        launchVideoCall(appointment.doctorId)
      },
      onCancelAppointment = { viewModel.cancelAppointment(appointment.id) },
      onDismiss = viewModel::dismissAppointmentDetail,
    )
  }

  uiState.selectedCheckup?.let { checkup ->
    CheckupDetailSheet(
      checkup = checkup,
      onStartVideoCall = {
        viewModel.dismissCheckupDetail()
        launchVideoCall(checkup.doctorId)
      },
      onToggleCompleted = { viewModel.toggleCheckupCompleted(checkup.id) },
      onDismiss = viewModel::dismissCheckupDetail,
    )
  }
}
