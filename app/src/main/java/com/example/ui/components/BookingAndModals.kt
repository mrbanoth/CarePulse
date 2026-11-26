package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.LocalHospital
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.mock.AppNotification
import com.example.data.mock.Appointment
import com.example.data.mock.CheckupSchedule
import com.example.data.mock.ConsultationType
import com.example.data.mock.Doctor
import com.example.data.mock.TreatmentPlan
import com.example.ui.theme.CoralRed
import com.example.ui.theme.EmeraldLive
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.IndigoSoftContainer
import com.example.ui.theme.SlateSecondary
import com.example.ui.theme.SubtleOutline

private data class DateChoice(
  val dateLabel: String,
  val dayNumber: String,
  val monthShort: String,
  val weekday: String,
)

private val availableDateChoices =
  listOf(
    DateChoice("Thu, 8 Sep 2024", "08", "Sep", "Thu"),
    DateChoice("Fri, 9 Sep 2024", "09", "Sep", "Fri"),
    DateChoice("Mon, 12 Sep 2024", "12", "Sep", "Mon"),
    DateChoice("Wed, 14 Sep 2024", "14", "Sep", "Wed"),
  )

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BookingBottomSheet(
  doctor: Doctor,
  onDismiss: () -> Unit,
  onConfirmBooking: (dateLabel: String, dayNumber: String, monthShort: String, timeSlot: String, type: ConsultationType) -> Unit,
) {
  var selectedDate by remember(doctor.id) { mutableStateOf(availableDateChoices.first()) }
  var selectedTime by remember(doctor.id) { mutableStateOf(doctor.availableTimeSlots.firstOrNull() ?: "10:30 AM") }
  var selectedType by remember(doctor.id) { mutableStateOf(ConsultationType.VIDEO) }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    containerColor = MaterialTheme.colorScheme.surface,
    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
  ) {
    Column(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        AvatarCircle(avatarRes = doctor.avatarRes, contentDescription = doctor.name, size = 54.dp)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = doctor.name,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          )
          Text(
            text = "${doctor.specialtyName} • $${doctor.consultationFeeUsd} / session",
            style = MaterialTheme.typography.bodyMedium,
            color = SlateSecondary,
          )
        }
        IconButton(onClick = onDismiss) {
          Icon(Icons.Rounded.Close, contentDescription = "Close booking")
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      Text(
        text = "Consultation Mode",
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
      )
      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        ConsultationType.entries.forEach { mode ->
          val active = mode == selectedType
          Surface(
            onClick = { selectedType = mode },
            modifier = Modifier.weight(1f).testTag("booking_mode_${mode.name}"),
            shape = RoundedCornerShape(16.dp),
            color = if (active) IndigoSoftContainer else MaterialTheme.colorScheme.surfaceVariant,
            border = if (active) BorderStroke(1.5.dp, IndigoPrimary) else null,
          ) {
            Row(
              modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center,
            ) {
              Icon(
                imageVector = if (mode == ConsultationType.VIDEO) Icons.Rounded.Videocam else Icons.Rounded.LocalHospital,
                contentDescription = null,
                tint = if (active) IndigoPrimary else SlateSecondary,
                modifier = Modifier.size(18.dp),
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = mode.label,
                style = MaterialTheme.typography.labelLarge,
                color = if (active) IndigoPrimary else MaterialTheme.colorScheme.onSurface,
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      Text(
        text = "Select Date",
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
      )
      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        availableDateChoices.forEach { choice ->
          val active = choice == selectedDate
          Surface(
            onClick = { selectedDate = choice },
            modifier = Modifier.weight(1f).testTag("booking_date_${choice.dayNumber}"),
            shape = RoundedCornerShape(16.dp),
            color = if (active) IndigoPrimary else MaterialTheme.colorScheme.surfaceVariant,
          ) {
            Column(
              modifier = Modifier.padding(vertical = 10.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
            ) {
              Text(
                text = choice.weekday,
                style = MaterialTheme.typography.labelSmall,
                color = if (active) Color.White.copy(alpha = 0.85f) else SlateSecondary,
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "${choice.monthShort} ${choice.dayNumber}",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = if (active) Color.White else MaterialTheme.colorScheme.onSurface,
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      Text(
        text = "Available Time Slots",
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
      )
      Spacer(modifier = Modifier.height(8.dp))
      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        doctor.availableTimeSlots.forEach { slot ->
          val active = slot == selectedTime
          Surface(
            onClick = { selectedTime = slot },
            shape = RoundedCornerShape(50),
            color = if (active) IndigoPrimary else MaterialTheme.colorScheme.surface,
            border = if (active) null else BorderStroke(1.dp, SubtleOutline),
          ) {
            Text(
              text = slot,
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
              color = if (active) Color.White else MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(22.dp))

      Button(
        onClick = {
          onConfirmBooking(
            selectedDate.dateLabel,
            selectedDate.dayNumber,
            selectedDate.monthShort,
            selectedTime,
            selectedType,
          )
        },
        modifier = Modifier.fillMaxWidth().height(52.dp).testTag("confirm_booking_button"),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary, contentColor = Color.White),
      ) {
        Icon(Icons.Rounded.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Confirm Appointment • ${selectedDate.monthShort} ${selectedDate.dayNumber}, $selectedTime",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
        )
      }
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreatmentPlansSheet(
  patientName: String,
  plans: List<TreatmentPlan>,
  onDismiss: () -> Unit,
) {
  ModalBottomSheet(
    onDismissRequest = onDismiss,
    containerColor = MaterialTheme.colorScheme.surface,
    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
      Text(
        text = "$patientName's Treatment Plans",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "${plans.size} active recovery protocols synced in real time",
        style = MaterialTheme.typography.bodyMedium,
        color = SlateSecondary,
      )
      Spacer(modifier = Modifier.height(16.dp))

      plans.forEach { plan ->
        Surface(
          modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
          shape = RoundedCornerShape(18.dp),
          color = MaterialTheme.colorScheme.surfaceVariant,
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Text(
                text = plan.title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f),
              )
              Text(
                text = "${(plan.progressPercent * 100).toInt()}%",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = IndigoPrimary,
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "${plan.doctorName} • ${plan.durationText}",
              style = MaterialTheme.typography.bodySmall,
              color = SlateSecondary,
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
              progress = { plan.progressPercent },
              modifier = Modifier.fillMaxWidth().height(7.dp).clip(RoundedCornerShape(50)),
              color = IndigoPrimary,
              trackColor = Color.White,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Next: ${plan.nextMilestone}",
              style = MaterialTheme.typography.bodySmall,
              color = IndigoPrimary,
            )
          }
        }
      }
      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsSheet(
  notifications: List<AppNotification>,
  onMarkAllRead: () -> Unit,
  onDismiss: () -> Unit,
) {
  ModalBottomSheet(
    onDismissRequest = onDismiss,
    containerColor = MaterialTheme.colorScheme.surface,
    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = "Notifications",
          style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        )
        TextButton(onClick = onMarkAllRead) {
          Icon(Icons.Rounded.DoneAll, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Mark all read")
        }
      }
      Spacer(modifier = Modifier.height(8.dp))

      notifications.forEachIndexed { idx, item ->
        Row(
          modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
          verticalAlignment = Alignment.Top,
        ) {
          Box(
            modifier =
              Modifier.size(38.dp)
                .clip(CircleShape)
                .background(if (item.isUnread) IndigoSoftContainer else MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
          ) {
            Icon(
              imageVector = Icons.Rounded.Notifications,
              contentDescription = null,
              tint = if (item.isUnread) IndigoPrimary else SlateSecondary,
              modifier = Modifier.size(18.dp),
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
            ) {
              Text(
                text = item.title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
              )
              Text(
                text = item.timeAgo,
                style = MaterialTheme.typography.bodySmall,
                color = SlateSecondary,
              )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = item.body,
              style = MaterialTheme.typography.bodyMedium,
              color = SlateSecondary,
            )
          }
        }
        if (idx < notifications.lastIndex) {
          HorizontalDivider(color = SubtleOutline)
        }
      }
      Spacer(modifier = Modifier.height(22.dp))
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentDetailSheet(
  appointment: Appointment,
  onStartVideoCall: () -> Unit,
  onCancelAppointment: () -> Unit,
  onDismiss: () -> Unit,
) {
  ModalBottomSheet(
    onDismissRequest = onDismiss,
    containerColor = MaterialTheme.colorScheme.surface,
    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        AvatarCircle(appointment.doctorAvatarRes, appointment.doctorName, 54.dp)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = appointment.doctorName,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          )
          Text(
            text = appointment.subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = SlateSecondary,
          )
        }
      }
      Spacer(modifier = Modifier.height(16.dp))
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = IndigoSoftContainer,
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
        ) {
          Text(
            text = appointment.dateLabel,
            style = MaterialTheme.typography.labelLarge,
            color = IndigoPrimary,
          )
          Text(
            text = appointment.timeRange,
            style = MaterialTheme.typography.labelLarge,
            color = IndigoPrimary,
          )
        }
      }
      Spacer(modifier = Modifier.height(18.dp))
      Button(
        onClick = onStartVideoCall,
        modifier = Modifier.fillMaxWidth().height(50.dp).testTag("join_video_call_button"),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary, contentColor = Color.White),
      ) {
        Icon(Icons.Rounded.Videocam, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Join Video Consultation")
      }
      Spacer(modifier = Modifier.height(10.dp))
      OutlinedButton(
        onClick = onCancelAppointment,
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, CoralRed.copy(alpha = 0.5f)),
      ) {
        Text("Cancel Appointment", color = CoralRed)
      }
      Spacer(modifier = Modifier.height(22.dp))
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckupDetailSheet(
  checkup: CheckupSchedule,
  onStartVideoCall: () -> Unit,
  onToggleCompleted: () -> Unit,
  onDismiss: () -> Unit,
) {
  ModalBottomSheet(
    onDismissRequest = onDismiss,
    containerColor = MaterialTheme.colorScheme.surface,
    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp)) {
      Text(
        text = checkup.title.replace("\n", " "),
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "${checkup.monthLabel} ${checkup.dayLabel} • ${checkup.doctorShortName} • ${checkup.location}",
        style = MaterialTheme.typography.bodyMedium,
        color = SlateSecondary,
      )
      Spacer(modifier = Modifier.height(14.dp))
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
      ) {
        Text(
          text = checkup.notes,
          style = MaterialTheme.typography.bodyMedium,
          modifier = Modifier.padding(14.dp),
        )
      }
      Spacer(modifier = Modifier.height(18.dp))
      Button(
        onClick = onStartVideoCall,
        modifier = Modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary, contentColor = Color.White),
      ) {
        Icon(Icons.Rounded.Videocam, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Call ${checkup.doctorShortName}")
      }
      Spacer(modifier = Modifier.height(10.dp))
      OutlinedButton(
        onClick = onToggleCompleted,
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(50),
      ) {
        Icon(
          Icons.Rounded.CheckCircle,
          contentDescription = null,
          tint = if (checkup.isCompleted) SlateSecondary else EmeraldLive,
          modifier = Modifier.size(18.dp),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(if (checkup.isCompleted) "Mark as Pending" else "Mark Checkup Complete")
      }
      Spacer(modifier = Modifier.height(22.dp))
    }
  }
}
