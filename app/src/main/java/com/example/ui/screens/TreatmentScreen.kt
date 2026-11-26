package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.mock.CheckupSchedule
import com.example.data.mock.Doctor
import com.example.ui.components.AvatarCircle
import com.example.ui.components.RealTimePortraitImage
import com.example.ui.components.RecommendDoctorCard
import com.example.ui.components.SectionHeaderRow
import com.example.ui.components.TopBarWithBackAndMore
import com.example.ui.state.DoctorAppUiState
import com.example.ui.theme.AlabasterCanvas
import com.example.ui.theme.EmeraldLive
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.IndigoSoftContainer
import com.example.ui.theme.SlateSecondary

@Composable
fun TreatmentScreen(
  uiState: DoctorAppUiState,
  onBack: () -> Unit,
  onSwitchPatient: () -> Unit,
  onOpenTreatmentPlans: () -> Unit,
  onCheckupClick: (CheckupSchedule) -> Unit,
  onStartVideoCall: (String) -> Unit,
  onSeeAllDoctorsClick: () -> Unit,
  onDoctorClick: (Doctor) -> Unit,
  onVisitNowClick: (Doctor) -> Unit,
  modifier: Modifier = Modifier,
) {
  val patient = uiState.treatmentPatient
  var showMoreMenu by remember { mutableStateOf(false) }
  val navBottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

  LazyColumn(
    modifier = modifier.fillMaxSize().statusBarsPadding(),
    contentPadding = PaddingValues(top = 12.dp, bottom = 104.dp + navBottomInset),
    verticalArrangement = Arrangement.spacedBy(20.dp),
  ) {
    // 1. Top App Bar
    item {
      Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
        TopBarWithBackAndMore(
          title = stringResource(R.string.title_my_treatment),
          onBack = onBack,
          onMoreClick = { showMoreMenu = true },
        )
        Box(modifier = Modifier.align(Alignment.TopEnd)) {
          DropdownMenu(
            expanded = showMoreMenu,
            onDismissRequest = { showMoreMenu = false },
          ) {
            DropdownMenuItem(
              text = { Text("Switch Patient Profile") },
              onClick = {
                showMoreMenu = false
                onSwitchPatient()
              },
            )
            DropdownMenuItem(
              text = { Text("View Treatment Plans") },
              onClick = {
                showMoreMenu = false
                onOpenTreatmentPlans()
              },
            )
            DropdownMenuItem(
              text = { Text("Find All Specialists") },
              onClick = {
                showMoreMenu = false
                onSeeAllDoctorsClick()
              },
            )
          }
        }
      }
    }

    // 2. Patient Profile Summary Card
    item {
      Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).testTag("treatment_patient_card"),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
      ) {
        Row(
          modifier = Modifier.fillMaxWidth().padding(start = 18.dp, top = 16.dp, bottom = 16.dp, end = 14.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = patient?.displayName ?: "Esther Howard",
              style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 19.sp),
              color = MaterialTheme.colorScheme.onSurface,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = patient?.genderAgeLabel ?: "Female, 23y.o",
              style = MaterialTheme.typography.bodyMedium,
              color = SlateSecondary,
            )
            Text(
              text = patient?.heightLabel ?: "Height: 5.4\"",
              style = MaterialTheme.typography.bodySmall,
              color = SlateSecondary,
            )
            Spacer(modifier = Modifier.height(14.dp))
            Button(
              onClick = onOpenTreatmentPlans,
              modifier = Modifier.height(40.dp).testTag("treatment_plans_button"),
              shape = RoundedCornerShape(50),
              colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary, contentColor = Color.White),
              contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
            ) {
              Text(
                text = "${patient?.treatmentPlans?.size ?: 3} Treatment Plans",
                style = MaterialTheme.typography.labelLarge,
              )
            }
          }

          Spacer(modifier = Modifier.width(12.dp))

          Box(
            modifier =
              Modifier.size(width = 112.dp, height = 122.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFF3F1F7)),
          ) {
            RealTimePortraitImage(
              imageUrl = patient?.avatarUrl,
              fallbackRes = patient?.avatarRes ?: R.drawable.img_patient_avatar,
              contentDescription = patient?.displayName ?: "Patient",
              modifier = Modifier.fillMaxSize(),
            )
          }
        }
      }
    }

    // 3. My Checkup Schedule Horizontal Row
    item {
      Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.padding(horizontal = 20.dp)) {
          SectionHeaderRow(
            title = stringResource(R.string.section_my_checkup_schedule),
            onActionClick = onOpenTreatmentPlans,
            testTag = "treatment_see_all_checkups",
          )
        }
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
          contentPadding = PaddingValues(horizontal = 20.dp),
          horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
          items(uiState.checkupSchedules, key = { it.id }) { checkup ->
            CheckupScheduleCard(
              checkup = checkup,
              onCardClick = { onCheckupClick(checkup) },
              onCallClick = { onStartVideoCall(checkup.doctorId) },
            )
          }
        }
      }
    }

    // 4. Recommend Doctor Vertical Section
    item {
      Box(modifier = Modifier.padding(horizontal = 20.dp)) {
        SectionHeaderRow(
          title = stringResource(R.string.section_recommend_doctor),
          onActionClick = onSeeAllDoctorsClick,
          testTag = "treatment_see_all_doctors",
        )
      }
    }

    items(uiState.recommendedDoctors, key = { it.id }) { doctor ->
      Box(modifier = Modifier.padding(horizontal = 20.dp)) {
        RecommendDoctorCard(
          doctor = doctor,
          onCardClick = { onDoctorClick(doctor) },
          onVisitNowClick = { onVisitNowClick(doctor) },
        )
      }
    }
  }
}

@Composable
private fun CheckupScheduleCard(
  checkup: CheckupSchedule,
  onCardClick: () -> Unit,
  onCallClick: () -> Unit,
) {
  Surface(
    onClick = onCardClick,
    modifier = Modifier.width(196.dp).testTag("checkup_card_${checkup.id}"),
    shape = RoundedCornerShape(22.dp),
    color = MaterialTheme.colorScheme.surface,
    shadowElevation = 2.dp,
  ) {
    Column(
      modifier = Modifier.fillMaxWidth().padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
      ) {
        Box(
          modifier = Modifier.size(38.dp).clip(CircleShape).background(AlabasterCanvas),
          contentAlignment = Alignment.Center,
        ) {
          Icon(
            imageVector = if (checkup.isCompleted) Icons.Rounded.CheckCircle else Icons.Rounded.CalendarMonth,
            contentDescription = null,
            tint = if (checkup.isCompleted) EmeraldLive else IndigoPrimary,
            modifier = Modifier.size(18.dp),
          )
        }

        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = checkup.monthLabel,
            style = MaterialTheme.typography.bodySmall,
            color = SlateSecondary,
          )
          Text(
            text = checkup.dayLabel,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, fontSize = 24.sp),
            color = MaterialTheme.colorScheme.onSurface,
          )
        }
      }

      Text(
        text = checkup.title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold, lineHeight = 22.sp),
        color = MaterialTheme.colorScheme.onSurface,
        minLines = 2,
        maxLines = 2,
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Surface(
          shape = RoundedCornerShape(50),
          color = IndigoSoftContainer.copy(alpha = 0.55f),
          modifier = Modifier.weight(1f),
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            AvatarCircle(
              imageUrl = checkup.doctorAvatarUrl,
              avatarRes = checkup.doctorAvatarRes,
              contentDescription = checkup.doctorShortName,
              size = 24.dp,
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = checkup.doctorShortName,
              style = MaterialTheme.typography.labelMedium,
              color = MaterialTheme.colorScheme.onSurface,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
          }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Surface(
          onClick = onCallClick,
          modifier = Modifier.size(36.dp).minimumInteractiveComponentSize().testTag("checkup_call_${checkup.id}"),
          shape = CircleShape,
          color = IndigoPrimary,
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              imageVector = Icons.Rounded.Call,
              contentDescription = stringResource(R.string.cd_call_doctor),
              tint = Color.White,
              modifier = Modifier.size(16.dp),
            )
          }
        }
      }
    }
  }
}
