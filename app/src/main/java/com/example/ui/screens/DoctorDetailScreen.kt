package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.LocalHospital
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Videocam
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.mock.Doctor
import com.example.ui.components.TopBarWithBackAndMore
import com.example.ui.theme.AmberBadge
import com.example.ui.theme.EmeraldLive
import com.example.ui.theme.EmeraldSoft
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.IndigoSoftContainer
import com.example.ui.theme.SlateSecondary

@Composable
fun DoctorDetailScreen(
  doctor: Doctor,
  onBack: () -> Unit,
  onBookAppointment: () -> Unit,
  onStartVideoCall: () -> Unit,
  modifier: Modifier = Modifier,
) {
  BackHandler { onBack() }

  Box(modifier = modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier.fillMaxSize().statusBarsPadding(),
      contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 110.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      item {
        TopBarWithBackAndMore(
          title = "Doctor Profile",
          onBack = onBack,
        )
      }

      item {
        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(24.dp),
          color = MaterialTheme.colorScheme.surface,
          shadowElevation = 2.dp,
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Image(
                painter = painterResource(doctor.avatarRes),
                contentDescription = doctor.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(92.dp).clip(RoundedCornerShape(20.dp)),
              )
              Spacer(modifier = Modifier.width(14.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = doctor.name,
                  style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                )
                Text(
                  text = doctor.specialtyName,
                  style = MaterialTheme.typography.bodyLarge,
                  color = IndigoPrimary,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier =
                      Modifier.size(8.dp)
                        .clip(CircleShape)
                        .background(if (doctor.isOnline) EmeraldLive else SlateSecondary),
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = if (doctor.isOnline) "Online • ${doctor.nextAvailableSlot}" else doctor.nextAvailableSlot,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (doctor.isOnline) EmeraldLive else SlateSecondary,
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
              DetailStatBox("Experience", "${doctor.experienceYears} yrs", Modifier.weight(1f))
              DetailStatBox("Rating", "${doctor.rating} (${doctor.reviewCount})", Modifier.weight(1f))
              DetailStatBox("Fee", "$${doctor.consultationFeeUsd}", Modifier.weight(1f))
            }
          }
        }
      }

      item {
        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(22.dp),
          color = MaterialTheme.colorScheme.surface,
          shadowElevation = 2.dp,
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "About Specialist",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = doctor.bio,
              style = MaterialTheme.typography.bodyLarge,
              color = SlateSecondary,
            )
            Spacer(modifier = Modifier.height(14.dp))
            Surface(
              shape = RoundedCornerShape(14.dp),
              color = IndigoSoftContainer.copy(alpha = 0.55f),
            ) {
              Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Icon(Icons.Rounded.LocalHospital, contentDescription = null, tint = IndigoPrimary)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                  text = doctor.hospitalName,
                  style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                )
              }
            }
          }
        }
      }
    }

    // Sticky Bottom CTA Bar
    Surface(
      modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().navigationBarsPadding(),
      color = MaterialTheme.colorScheme.surface,
      shadowElevation = 12.dp,
    ) {
      Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        OutlinedButton(
          onClick = onStartVideoCall,
          modifier = Modifier.weight(1f).height(50.dp).testTag("detail_video_call_button"),
          shape = RoundedCornerShape(50),
          border = BorderStroke(1.5.dp, IndigoPrimary),
        ) {
          Icon(Icons.Rounded.Videocam, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Video Call", color = IndigoPrimary)
        }
        Button(
          onClick = onBookAppointment,
          modifier = Modifier.weight(1.3f).height(50.dp).testTag("detail_book_button"),
          shape = RoundedCornerShape(50),
          colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary, contentColor = Color.White),
        ) {
          Icon(Icons.Rounded.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Book Now")
        }
      }
    }
  }
}

@Composable
private fun DetailStatBox(
  label: String,
  value: String,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier,
    shape = RoundedCornerShape(16.dp),
    color = if (label == "Rating") EmeraldSoft else IndigoSoftContainer.copy(alpha = 0.6f),
  ) {
    Column(
      modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        if (label == "Rating") {
          Icon(Icons.Rounded.Star, contentDescription = null, tint = AmberBadge, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(3.dp))
        }
        Text(
          text = value,
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
        )
      }
      Text(
        text = label,
        style = MaterialTheme.typography.bodySmall,
        color = SlateSecondary,
      )
    }
  }
}
