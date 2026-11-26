package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.VolumeOff
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.CallEnd
import androidx.compose.material.icons.rounded.IosShare
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.MicOff
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.VideocamOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.components.AvatarCircle
import com.example.ui.components.CircularWhiteIconButton
import com.example.ui.state.VideoCallUiState
import com.example.ui.theme.CoralRed
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SlateSecondary
import java.util.Locale

@Composable
fun VideoCallScreen(
  callState: VideoCallUiState,
  onToggleMute: () -> Unit,
  onToggleSpeaker: () -> Unit,
  onToggleCamera: () -> Unit,
  onToggleScreenShare: () -> Unit,
  onEndCall: () -> Unit,
  modifier: Modifier = Modifier,
) {
  BackHandler { onEndCall() }

  val doctor = callState.doctor
  val doctorPhoto = doctor?.avatarRes ?: R.drawable.img_doc_female_video

  val infiniteTransition = rememberInfiniteTransition(label = "live_dot")
  val liveDotAlpha by
    infiniteTransition.animateFloat(
      initialValue = 1f,
      targetValue = 0.25f,
      animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
      label = "live_dot_alpha",
    )

  Box(modifier = modifier.fillMaxSize().background(Color(0xFF1A1927))) {
    // Full-bleed doctor video feed
    Image(
      painter = painterResource(doctorPhoto),
      contentDescription = doctor?.name ?: "Doctor video consultation",
      contentScale = ContentScale.Crop,
      modifier = Modifier.fillMaxSize(),
    )

    // Subtle top & bottom gradient scrims for crisp UI visibility
    Box(
      modifier =
        Modifier.fillMaxWidth()
          .height(140.dp)
          .align(Alignment.TopCenter)
          .background(
            Brush.verticalGradient(
              colors = listOf(Color.Black.copy(alpha = 0.35f), Color.Transparent)
            )
          ),
    )

    Box(
      modifier =
        Modifier.fillMaxWidth()
          .height(230.dp)
          .align(Alignment.BottomCenter)
          .background(
            Brush.verticalGradient(
              colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.48f))
            )
          ),
    )

    // Top bar: Back button + Picture-in-Picture self-view
    Row(
      modifier =
        Modifier.fillMaxWidth()
          .statusBarsPadding()
          .padding(horizontal = 20.dp, vertical = 14.dp)
          .align(Alignment.TopCenter),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.Top,
    ) {
      CircularWhiteIconButton(
        onClick = onEndCall,
        icon = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
        contentDescription = stringResource(R.string.cd_back),
        testTag = "video_back_button",
      )

      // Self-view PiP card
      Box(
        modifier =
          Modifier.size(width = 88.dp, height = 114.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2B293B))
            .border(2.dp, Color.White, RoundedCornerShape(16.dp))
            .testTag("video_pip_self_view"),
        contentAlignment = Alignment.Center,
      ) {
        if (callState.isCameraOn) {
          Image(
            painter = painterResource(R.drawable.img_patient_avatar),
            contentDescription = "Your camera preview",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
          )
        } else {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Rounded.VideocamOff,
              contentDescription = "Camera off",
              tint = Color.White,
              modifier = Modifier.size(26.dp),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Cam Off",
              style = MaterialTheme.typography.labelSmall,
              color = Color.White.copy(alpha = 0.85f),
            )
          }
        }
      }
    }

    // Bottom Doctor Info Pill + 5 Circular Call Controls
    Column(
      modifier =
        Modifier.fillMaxWidth()
          .align(Alignment.BottomCenter)
          .navigationBarsPadding()
          .padding(horizontal = 20.dp, vertical = 20.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      if (callState.isScreenShared || callState.isMuted) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          if (callState.isScreenShared) {
            Surface(
              shape = RoundedCornerShape(50),
              color = IndigoPrimary.copy(alpha = 0.92f),
              modifier = Modifier.padding(horizontal = 4.dp),
            ) {
              Text(
                text = "Sharing Health Summary",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
              )
            }
          }
          if (callState.isMuted) {
            Surface(
              shape = RoundedCornerShape(50),
              color = Color.Black.copy(alpha = 0.65f),
              modifier = Modifier.padding(horizontal = 4.dp),
            ) {
              Text(
                text = "Mic Muted",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
              )
            }
          }
        }
      }

      // Floating Doctor Info Card
      Surface(
        modifier = Modifier.fillMaxWidth().testTag("video_doctor_pill"),
        shape = RoundedCornerShape(22.dp),
        color = Color.White.copy(alpha = 0.96f),
        shadowElevation = 6.dp,
      ) {
        Row(
          modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          AvatarCircle(
            avatarRes = doctorPhoto,
            contentDescription = doctor?.name ?: "Doctor",
            size = 46.dp,
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = doctor?.name ?: "Dr. Nick Tylor",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = Color(0xFF1A1927),
            )
            Text(
              text = doctor?.specialtyName ?: "Podiatrist",
              style = MaterialTheme.typography.bodyMedium,
              color = SlateSecondary,
            )
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier =
                Modifier.size(8.dp)
                  .alpha(liveDotAlpha)
                  .clip(CircleShape)
                  .background(CoralRed),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = formatCallDuration(callState.elapsedSeconds),
              style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
              color = Color(0xFF1A1927),
              modifier = Modifier.testTag("video_call_timer"),
            )
          }
        }
      }

      // 5 Control Buttons Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        CallControlCircleButton(
          onClick = onToggleScreenShare,
          icon = Icons.Rounded.IosShare,
          contentDescription = stringResource(R.string.cd_share_screen),
          isActive = callState.isScreenShared,
          testTag = "video_share_button",
        )
        CallControlCircleButton(
          onClick = onToggleCamera,
          icon = if (callState.isCameraOn) Icons.Rounded.PhotoCamera else Icons.Rounded.VideocamOff,
          contentDescription = stringResource(R.string.cd_toggle_camera),
          isActive = !callState.isCameraOn,
          testTag = "video_camera_button",
        )
        // Red End Call Button
        Surface(
          onClick = onEndCall,
          modifier = Modifier.size(58.dp).minimumInteractiveComponentSize().testTag("video_end_call_button"),
          shape = CircleShape,
          color = CoralRed,
          shadowElevation = 6.dp,
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              imageVector = Icons.Rounded.CallEnd,
              contentDescription = stringResource(R.string.cd_end_call),
              tint = Color.White,
              modifier = Modifier.size(26.dp),
            )
          }
        }
        CallControlCircleButton(
          onClick = onToggleSpeaker,
          icon = if (callState.isSpeakerOn) Icons.AutoMirrored.Rounded.VolumeUp else Icons.AutoMirrored.Rounded.VolumeOff,
          contentDescription = stringResource(R.string.cd_toggle_speaker),
          isActive = !callState.isSpeakerOn,
          testTag = "video_speaker_button",
        )
        CallControlCircleButton(
          onClick = onToggleMute,
          icon = if (callState.isMuted) Icons.Rounded.MicOff else Icons.Rounded.Mic,
          contentDescription = stringResource(R.string.cd_toggle_mic),
          isActive = callState.isMuted,
          testTag = "video_mic_button",
        )
      }
    }
  }
}

@Composable
private fun CallControlCircleButton(
  onClick: () -> Unit,
  icon: ImageVector,
  contentDescription: String,
  isActive: Boolean,
  testTag: String,
) {
  Surface(
    onClick = onClick,
    modifier = Modifier.size(54.dp).minimumInteractiveComponentSize().testTag(testTag),
    shape = CircleShape,
    color = if (isActive) IndigoPrimary else Color.White.copy(alpha = 0.96f),
    shadowElevation = 4.dp,
  ) {
    Box(contentAlignment = Alignment.Center) {
      Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        tint = if (isActive) Color.White else IndigoPrimary,
        modifier = Modifier.size(23.dp),
      )
    }
  }
}

private fun formatCallDuration(totalSeconds: Long): String {
  val hours = totalSeconds / 3600
  val minutes = (totalSeconds % 3600) / 60
  val seconds = totalSeconds % 60
  return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
}
