package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.LocalHospital
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.data.mock.PartnerClinic
import com.example.ui.components.TopBarWithBackAndMore
import com.example.ui.state.DoctorAppUiState
import com.example.ui.theme.AmberBadge
import com.example.ui.theme.EmeraldLive
import com.example.ui.theme.EmeraldSoft
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.IndigoSoftContainer
import com.example.ui.theme.SlateSecondary

@Composable
fun ClinicsScreen(
  uiState: DoctorAppUiState,
  onBack: () -> Unit,
  onBookAtClinic: (PartnerClinic) -> Unit,
  modifier: Modifier = Modifier,
) {
  LazyColumn(
    modifier = modifier.fillMaxSize().statusBarsPadding(),
    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 108.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    item {
      TopBarWithBackAndMore(
        title = stringResource(R.string.title_clinics),
        onBack = onBack,
      )
    }

    // Real-time clinic sync banner
    item {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = EmeraldSoft,
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Box(
            modifier = Modifier.size(9.dp).clip(CircleShape).background(EmeraldLive),
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "Live clinic wait times synced • Updated continuously",
            style = MaterialTheme.typography.labelLarge,
            color = EmeraldLive,
          )
        }
      }
    }

    items(uiState.clinics, key = { it.id }) { clinic ->
      ClinicCard(
        clinic = clinic,
        onBookClick = { onBookAtClinic(clinic) },
      )
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ClinicCard(
  clinic: PartnerClinic,
  onBookClick: () -> Unit,
) {
  Surface(
    modifier = Modifier.fillMaxWidth().testTag("clinic_card_${clinic.id}"),
    shape = RoundedCornerShape(22.dp),
    color = MaterialTheme.colorScheme.surface,
    shadowElevation = 2.dp,
  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
      ) {
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier.size(44.dp).clip(RoundedCornerShape(14.dp)).background(IndigoSoftContainer),
            contentAlignment = Alignment.Center,
          ) {
            Icon(
              imageVector = Icons.Rounded.LocalHospital,
              contentDescription = null,
              tint = IndigoPrimary,
              modifier = Modifier.size(22.dp),
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = clinic.name,
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Rounded.LocationOn,
                contentDescription = null,
                tint = SlateSecondary,
                modifier = Modifier.size(14.dp),
              )
              Spacer(modifier = Modifier.width(3.dp))
              Text(
                text = "${clinic.address} • ${clinic.distanceKm} km",
                style = MaterialTheme.typography.bodySmall,
                color = SlateSecondary,
              )
            }
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Rounded.Star,
            contentDescription = null,
            tint = AmberBadge,
            modifier = Modifier.size(15.dp),
          )
          Spacer(modifier = Modifier.width(2.dp))
          Text(
            text = "${clinic.rating}",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        clinic.featuredSpecialties.forEach { spec ->
          Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.surfaceVariant,
          ) {
            Text(
              text = spec,
              style = MaterialTheme.typography.labelMedium,
              color = SlateSecondary,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Surface(
          shape = RoundedCornerShape(50),
          color = EmeraldSoft,
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Icon(
              imageVector = Icons.Rounded.AccessTime,
              contentDescription = null,
              tint = EmeraldLive,
              modifier = Modifier.size(15.dp),
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
              text = "${clinic.liveWaitMinutes} min wait",
              style = MaterialTheme.typography.labelLarge,
              color = EmeraldLive,
            )
          }
        }

        Button(
          onClick = onBookClick,
          modifier = Modifier.height(40.dp).testTag("clinic_book_${clinic.id}"),
          shape = RoundedCornerShape(50),
          colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary, contentColor = Color.White),
        ) {
          Text("Book Clinic Visit", style = MaterialTheme.typography.labelLarge)
        }
      }
    }
  }
}
