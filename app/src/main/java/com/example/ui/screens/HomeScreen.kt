package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.mock.Appointment
import com.example.data.mock.Doctor
import com.example.ui.components.AvatarCircle
import com.example.ui.components.RecentDoctorHorizontalCard
import com.example.ui.components.RecommendDoctorCard
import com.example.ui.components.SectionHeaderRow
import com.example.ui.components.SpecialtyTileCard
import com.example.ui.state.DoctorAppUiState
import com.example.ui.theme.AmberBadge
import com.example.ui.theme.IndigoAppointmentOverlay
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.SlateSecondary

@Composable
fun HomeScreen(
  uiState: DoctorAppUiState,
  onSearchQueryChanged: (String) -> Unit,
  onSpecialtyToggled: (String) -> Unit,
  onClearFilters: () -> Unit,
  onNotificationsClick: () -> Unit,
  onProfileClick: () -> Unit,
  onAppointmentClick: (Appointment) -> Unit,
  onSeeAllDoctorsClick: () -> Unit,
  onDoctorClick: (Doctor) -> Unit,
  onBookNowClick: (Doctor) -> Unit,
  onStartVideoCall: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val homeProfile = uiState.homeProfile
  val isFiltering = uiState.searchQuery.isNotBlank() || uiState.selectedSpecialtyId != null

  LazyColumn(
    modifier = modifier.fillMaxSize().statusBarsPadding(),
    contentPadding = PaddingValues(top = 14.dp, bottom = 108.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp),
  ) {
    // 1. Greeting Header + Notification/Profile Pill
    item {
      Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = stringResource(R.string.greeting_hello),
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Medium, fontSize = 25.sp),
            color = MaterialTheme.colorScheme.onBackground,
          )
          Text(
            text = homeProfile?.firstName ?: "Siyam Ahamed!",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold, fontSize = 26.sp),
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        }

        // Top-right pill: Notification bell + Avatar with PRO badge
        Surface(
          shape = RoundedCornerShape(50),
          color = MaterialTheme.colorScheme.surface,
          shadowElevation = 2.dp,
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            Box(
              modifier =
                Modifier.size(38.dp)
                  .clip(CircleShape)
                  .clickable(onClick = onNotificationsClick)
                  .testTag("home_notification_button"),
              contentAlignment = Alignment.Center,
            ) {
              Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = stringResource(R.string.cd_notifications),
                tint = InkPrimary,
                modifier = Modifier.size(22.dp),
              )
              if (uiState.unreadNotificationCount > 0) {
                Box(
                  modifier =
                    Modifier.align(Alignment.TopEnd)
                      .padding(top = 7.dp, end = 8.dp)
                      .size(8.dp)
                      .clip(CircleShape)
                      .background(AmberBadge)
                      .border(1.5.dp, Color.White, CircleShape),
                )
              }
            }

            Box(
              modifier =
                Modifier.clickable(onClick = onProfileClick)
                  .testTag("home_profile_avatar"),
              contentAlignment = Alignment.BottomCenter,
            ) {
              AvatarCircle(
                avatarRes = homeProfile?.avatarRes ?: R.drawable.img_doc_male_neuro,
                contentDescription = homeProfile?.displayName ?: "Profile",
                size = 42.dp,
              )
              if (homeProfile?.isPro == true) {
                Surface(
                  modifier = Modifier.offset(y = 4.dp),
                  shape = RoundedCornerShape(50),
                  color = Color(0xFFDCD9E8),
                  border = androidx.compose.foundation.BorderStroke(1.dp, Color.White),
                ) {
                  Text(
                    text = "PRO",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp, fontWeight = FontWeight.Bold),
                    color = InkPrimary,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp),
                  )
                }
              }
            }
          }
        }
      }
    }

    // 2. Search Bar
    item {
      Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(54.dp),
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
      ) {
        Row(
          modifier = Modifier.fillMaxSize().padding(horizontal = 18.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Icon(
            imageVector = Icons.Rounded.Search,
            contentDescription = null,
            tint = SlateSecondary,
            modifier = Modifier.size(22.dp),
          )
          Spacer(modifier = Modifier.width(12.dp))
          Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
            if (uiState.searchQuery.isEmpty()) {
              Text(
                text = stringResource(R.string.search_doctor_hint),
                style = MaterialTheme.typography.bodyLarge,
                color = SlateSecondary,
              )
            }
            BasicTextField(
              value = uiState.searchQuery,
              onValueChange = onSearchQueryChanged,
              singleLine = true,
              textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
              cursorBrush = SolidColor(IndigoPrimary),
              modifier = Modifier.fillMaxWidth().testTag("home_search_input"),
            )
          }
          if (uiState.searchQuery.isNotEmpty()) {
            IconButton(onClick = { onSearchQueryChanged("") }, modifier = Modifier.size(36.dp)) {
              Icon(Icons.Rounded.Close, contentDescription = "Clear search", tint = SlateSecondary)
            }
          }
        }
      }
    }

    // 3. Specialty Categories Row
    item {
      LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        items(uiState.specialties, key = { it.id }) { specialty ->
          SpecialtyTileCard(
            specialty = specialty,
            isSelected = uiState.selectedSpecialtyId == specialty.id,
            onClick = { onSpecialtyToggled(specialty.id) },
          )
        }
      }
    }

    // Optional filtered results block when user searches or selects a specialty
    item {
      AnimatedVisibility(visible = isFiltering) {
        Column(
          modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = "Matching Doctors (${uiState.filteredDoctors.size})",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            )
            TextButton(onClick = onClearFilters) {
              Text("Clear Filter")
            }
          }
          if (uiState.filteredDoctors.isEmpty()) {
            Surface(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(20.dp),
              color = MaterialTheme.colorScheme.surface,
            ) {
              Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
              ) {
                Text(
                  text = "No doctors matched your filter",
                  style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "Try searching another name or specialty.",
                  style = MaterialTheme.typography.bodyMedium,
                  color = SlateSecondary,
                )
              }
            }
          } else {
            uiState.filteredDoctors.forEach { doctor ->
              RecommendDoctorCard(
                doctor = doctor,
                onCardClick = { onDoctorClick(doctor) },
                onVisitNowClick = { onBookNowClick(doctor) },
              )
            }
          }
        }
      }
    }

    // 4. Upcoming Appointment Card
    item {
      Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
        SectionHeaderRow(
          title = stringResource(R.string.section_upcoming_appointment),
          actionLabel = null,
        )
        Spacer(modifier = Modifier.height(12.dp))
        val upcoming = uiState.appointments.firstOrNull()
        if (upcoming != null) {
          UpcomingAppointmentIndigoCard(
            appointment = upcoming,
            onClick = { onAppointmentClick(upcoming) },
          )
        } else {
          Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = IndigoPrimary,
          ) {
            Row(
              modifier = Modifier.padding(18.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "No upcoming appointments",
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                  color = Color.White,
                )
                Text(
                  text = "Book a specialist below to get started",
                  style = MaterialTheme.typography.bodyMedium,
                  color = Color.White.copy(alpha = 0.85f),
                )
              }
              Button(
                onClick = onSeeAllDoctorsClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = IndigoPrimary),
                shape = RoundedCornerShape(50),
              ) {
                Text("Find Doctor")
              }
            }
          }
        }
      }
    }

    // 5. My Recent Visit Section
    item {
      Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.padding(horizontal = 20.dp)) {
          SectionHeaderRow(
            title = stringResource(R.string.section_my_recent_visit),
            onActionClick = onSeeAllDoctorsClick,
            testTag = "home_see_all_recent",
          )
        }
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
          contentPadding = PaddingValues(horizontal = 20.dp),
          horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
          items(uiState.recentVisitDoctors, key = { it.id }) { doctor ->
            RecentDoctorHorizontalCard(
              doctor = doctor,
              onCardClick = { onDoctorClick(doctor) },
              onBookNowClick = { onBookNowClick(doctor) },
              onCallClick = { onStartVideoCall(doctor.id) },
            )
          }
        }
      }
    }
  }
}

@Composable
private fun UpcomingAppointmentIndigoCard(
  appointment: Appointment,
  onClick: () -> Unit,
) {
  Surface(
    onClick = onClick,
    modifier = Modifier.fillMaxWidth().testTag("upcoming_appointment_card"),
    shape = RoundedCornerShape(24.dp),
    color = IndigoPrimary,
    shadowElevation = 4.dp,
  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(18.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        AvatarCircle(
          avatarRes = appointment.doctorAvatarRes,
          contentDescription = appointment.doctorName,
          size = 52.dp,
          modifier = Modifier.border(1.5.dp, Color.White.copy(alpha = 0.35f), CircleShape),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = appointment.doctorName,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = appointment.subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.88f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Inner translucent pill matching screenshot
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = IndigoAppointmentOverlay,
      ) {
        Row(
          modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier.size(26.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.16f)),
              contentAlignment = Alignment.Center,
            ) {
              Icon(
                imageVector = Icons.Rounded.CalendarMonth,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp),
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = appointment.dateLabel,
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
              color = Color.White,
            )
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier.size(26.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.16f)),
              contentAlignment = Alignment.Center,
            ) {
              Icon(
                imageVector = Icons.Rounded.AccessTime,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp),
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = appointment.timeRange,
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
              color = Color.White,
            )
          }
        }
      }
    }
  }
}
