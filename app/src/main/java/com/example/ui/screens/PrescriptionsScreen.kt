package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Medication
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.mock.Prescription
import com.example.ui.components.TopBarWithBackAndMore
import com.example.ui.state.DoctorAppUiState
import com.example.ui.theme.EmeraldLive
import com.example.ui.theme.EmeraldSoft
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.IndigoSoftContainer
import com.example.ui.theme.SlateSecondary
import com.example.ui.theme.SubtleOutline

@Composable
fun PrescriptionsScreen(
  uiState: DoctorAppUiState,
  onBack: () -> Unit,
  onToggleTaken: (String) -> Unit,
  onRequestRefill: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val takenCount = uiState.prescriptions.count { it.isTakenToday }
  val totalCount = uiState.prescriptions.size

  LazyColumn(
    modifier = modifier.fillMaxSize().statusBarsPadding(),
    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 108.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    item {
      TopBarWithBackAndMore(
        title = stringResource(R.string.title_prescriptions),
        onBack = onBack,
      )
    }

    // Daily Adherence Summary Card
    item {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = IndigoPrimary,
        shadowElevation = 3.dp,
      ) {
        Row(
          modifier = Modifier.fillMaxWidth().padding(18.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Daily Medication Tracker",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = Color.White,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "$takenCount of $totalCount doses taken today",
              style = MaterialTheme.typography.bodyMedium,
              color = Color.White.copy(alpha = 0.88f),
            )
          }
          Surface(
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.18f),
            modifier = Modifier.size(52.dp),
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text(
                text = "$takenCount/$totalCount",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
              )
            }
          }
        }
      }
    }

    items(uiState.prescriptions, key = { it.id }) { rx ->
      PrescriptionCard(
        prescription = rx,
        onToggleTaken = { onToggleTaken(rx.id) },
        onRequestRefill = { onRequestRefill(rx.id) },
      )
    }
  }
}

@Composable
private fun PrescriptionCard(
  prescription: Prescription,
  onToggleTaken: () -> Unit,
  onRequestRefill: () -> Unit,
) {
  Surface(
    modifier = Modifier.fillMaxWidth().testTag("prescription_card_${prescription.id}"),
    shape = RoundedCornerShape(22.dp),
    color = MaterialTheme.colorScheme.surface,
    shadowElevation = 2.dp,
  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier =
            Modifier.size(44.dp)
              .clip(RoundedCornerShape(14.dp))
              .background(if (prescription.isTakenToday) EmeraldSoft else IndigoSoftContainer),
          contentAlignment = Alignment.Center,
        ) {
          Icon(
            imageVector = Icons.Rounded.Medication,
            contentDescription = null,
            tint = if (prescription.isTakenToday) EmeraldLive else IndigoPrimary,
            modifier = Modifier.size(22.dp),
          )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = prescription.name,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          )
          Text(
            text = "${prescription.dosage} • ${prescription.scheduleText}",
            style = MaterialTheme.typography.bodyMedium,
            color = SlateSecondary,
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = "Prescribed by ${prescription.prescribedBy}",
          style = MaterialTheme.typography.bodySmall,
          color = SlateSecondary,
        )
        Text(
          text = "${prescription.remainingRefills} refills left",
          style = MaterialTheme.typography.labelMedium,
          color = IndigoPrimary,
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        Button(
          onClick = onToggleTaken,
          modifier = Modifier.weight(1f).height(42.dp).testTag("rx_take_${prescription.id}"),
          shape = RoundedCornerShape(50),
          colors =
            ButtonDefaults.buttonColors(
              containerColor = if (prescription.isTakenToday) EmeraldSoft else IndigoPrimary,
              contentColor = if (prescription.isTakenToday) EmeraldLive else Color.White,
            ),
        ) {
          Icon(
            imageVector =
              if (prescription.isTakenToday) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (prescription.isTakenToday) "Taken Today" else "Mark Taken",
            style = MaterialTheme.typography.labelLarge,
          )
        }

        OutlinedButton(
          onClick = onRequestRefill,
          enabled = prescription.remainingRefills > 0,
          modifier = Modifier.height(42.dp).testTag("rx_refill_${prescription.id}"),
          shape = RoundedCornerShape(50),
          border = BorderStroke(1.dp, SubtleOutline),
        ) {
          Icon(Icons.Rounded.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Refill", style = MaterialTheme.typography.labelLarge)
        }
      }
    }
  }
}
