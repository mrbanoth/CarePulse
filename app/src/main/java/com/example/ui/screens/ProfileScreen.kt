package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.components.AvatarCircle
import com.example.ui.components.TopBarWithBackAndMore
import com.example.ui.state.DoctorAppUiState
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.IndigoSoftContainer
import com.example.ui.theme.SlateSecondary
import com.example.ui.theme.SubtleOutline

@Composable
fun ProfileScreen(
  uiState: DoctorAppUiState,
  onBack: () -> Unit,
  onStartQuickVideoCall: (String) -> Unit,
  onOpenTreatments: () -> Unit,
  onOpenNotifications: () -> Unit,
  onSwitchTreatmentPatient: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val profile = uiState.homeProfile
  var appointmentRemindersEnabled by remember { mutableStateOf(true) }
  val navBottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

  LazyColumn(
    modifier = modifier.fillMaxSize().statusBarsPadding(),
    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 104.dp + navBottomInset),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    item {
      TopBarWithBackAndMore(
        title = stringResource(R.string.title_profile),
        onBack = onBack,
      )
    }

    // Main Member Card
    item {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
      ) {
        Column(modifier = Modifier.fillMaxWidth().padding(18.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            AvatarCircle(
              imageUrl = profile?.avatarUrl,
              avatarRes = profile?.avatarRes ?: R.drawable.img_patient_avatar,
              contentDescription = profile?.displayName ?: "Siyam Ahamed",
              size = 64.dp,
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = profile?.displayName ?: "Siyam Ahamed",
                  style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                  shape = RoundedCornerShape(50),
                  color = IndigoSoftContainer,
                ) {
                  Text(
                    text = "PRO",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = IndigoPrimary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                  )
                }
              }
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "${profile?.genderAgeLabel ?: "Male, 27y.o"} • ${profile?.heightLabel ?: "Height: 5.9\""}",
                style = MaterialTheme.typography.bodyMedium,
                color = SlateSecondary,
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
          ) {
            ProfileMetricPill("Blood", profile?.bloodType ?: "O+", Modifier.weight(1f))
            ProfileMetricPill("Appointments", "${uiState.appointments.size}", Modifier.weight(1f))
            ProfileMetricPill("Meds Active", "${uiState.prescriptions.size}", Modifier.weight(1f))
          }
        }
      }
    }

    // Instant Telehealth Call Banner (launches Dr. Nick Tylor video consultation)
    item {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = IndigoPrimary,
      ) {
        Row(
          modifier = Modifier.fillMaxWidth().padding(16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Instant Podiatry Telehealth",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = Color.White,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "Dr. Nick Tylor is available for live call",
              style = MaterialTheme.typography.bodySmall,
              color = Color.White.copy(alpha = 0.85f),
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Button(
            onClick = { onStartQuickVideoCall("doc_nick") },
            modifier = Modifier.testTag("profile_video_call_button"),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = IndigoPrimary),
          ) {
            Icon(Icons.Rounded.Videocam, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Join Call")
          }
        }
      }
    }

    // Actions & Settings Card
    item {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
      ) {
        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
          ProfileMenuItem(
            icon = Icons.Rounded.MedicalServices,
            title = "Active Treatment Patient",
            subtitle = uiState.treatmentPatient?.displayName ?: "Esther Howard",
            onClick = onOpenTreatments,
          )
          HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = SubtleOutline)
          ProfileMenuItem(
            icon = Icons.Rounded.SwapHoriz,
            title = "Switch Family Treatment Profile",
            subtitle = "Toggle between Esther Howard & Siyam Ahamed",
            onClick = onSwitchTreatmentPatient,
          )
          HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = SubtleOutline)
          ProfileMenuItem(
            icon = Icons.Rounded.Notifications,
            title = "Notification Inbox",
            subtitle = "${uiState.unreadNotificationCount} unread alerts",
            onClick = onOpenNotifications,
          )
          HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = SubtleOutline)
          Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Box(
              modifier = Modifier.size(40.dp),
              contentAlignment = Alignment.Center,
            ) {
              Icon(Icons.Rounded.CalendarMonth, contentDescription = null, tint = IndigoPrimary)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Appointment Reminders",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
              )
              Text(
                text = "Alert 30 mins before video call",
                style = MaterialTheme.typography.bodySmall,
                color = SlateSecondary,
              )
            }
            Switch(
              checked = appointmentRemindersEnabled,
              onCheckedChange = { appointmentRemindersEnabled = it },
              colors = SwitchDefaults.colors(checkedTrackColor = IndigoPrimary),
            )
          }
        }
      }
    }
  }
}

@Composable
private fun ProfileMetricPill(
  label: String,
  value: String,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier,
    shape = RoundedCornerShape(16.dp),
    color = IndigoSoftContainer.copy(alpha = 0.65f),
  ) {
    Column(
      modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
        text = value,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = IndigoPrimary,
      )
      Text(
        text = label,
        style = MaterialTheme.typography.bodySmall,
        color = SlateSecondary,
      )
    }
  }
}

@Composable
private fun ProfileMenuItem(
  icon: ImageVector,
  title: String,
  subtitle: String,
  onClick: () -> Unit,
) {
  Surface(
    onClick = onClick,
    color = Color.Transparent,
  ) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
        Icon(imageVector = icon, contentDescription = null, tint = IndigoPrimary)
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
        )
        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodySmall,
          color = SlateSecondary,
        )
      }
      Icon(
        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
        contentDescription = null,
        tint = SlateSecondary,
      )
    }
  }
}
