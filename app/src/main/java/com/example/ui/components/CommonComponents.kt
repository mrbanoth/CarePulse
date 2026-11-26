package com.example.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.Apartment
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.Medication
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.mock.Doctor
import com.example.data.mock.SpecialtyCategory
import com.example.data.mock.SpecialtyIconType
import com.example.ui.theme.EmeraldLive
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.IndigoSoftContainer
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.SlateSecondary
import com.example.ui.theme.SubtleOutline

enum class BottomNavTab(
  val route: String,
  val labelRes: Int,
  val activeIcon: ImageVector,
  val inactiveIcon: ImageVector,
  val testTag: String,
) {
  HOME("home", R.string.nav_home, Icons.Rounded.Home, Icons.Outlined.Home, "nav_home_tab"),
  TREAT("treatment", R.string.nav_treat, Icons.Rounded.MedicalServices, Icons.Outlined.MedicalServices, "nav_treat_tab"),
  MEDS("meds", R.string.nav_meds, Icons.Rounded.Medication, Icons.Outlined.Medication, "nav_meds_tab"),
  CLINIC("clinics", R.string.nav_clinic, Icons.Rounded.Apartment, Icons.Outlined.Apartment, "nav_clinic_tab"),
  PROFILE("profile", R.string.nav_profile, Icons.Rounded.Person, Icons.Outlined.Person, "nav_profile_tab"),
}

@Composable
fun FloatingBottomNavBar(
  selectedTab: BottomNavTab,
  onSelectTab: (BottomNavTab) -> Unit,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier.fillMaxWidth().navigationBarsPadding(),
    color = MaterialTheme.colorScheme.surface,
    shadowElevation = 14.dp,
    tonalElevation = 2.dp,
    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      BottomNavTab.entries.forEach { tab ->
        val isSelected = tab == selectedTab
        Box(
          modifier =
            Modifier.testTag(tab.testTag)
              .minimumInteractiveComponentSize()
              .clip(RoundedCornerShape(50))
              .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
              .clickable { onSelectTab(tab) }
              .animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessMediumLow))
              .padding(horizontal = if (isSelected) 16.dp else 12.dp, vertical = 10.dp),
          contentAlignment = Alignment.Center,
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
          ) {
            Icon(
              imageVector = if (isSelected) tab.activeIcon else tab.inactiveIcon,
              contentDescription = stringResource(tab.labelRes),
              tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(22.dp),
            )
            if (isSelected) {
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = stringResource(tab.labelRes),
                color = Color.White,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 1,
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun CircularWhiteIconButton(
  onClick: () -> Unit,
  icon: ImageVector,
  contentDescription: String,
  testTag: String,
  modifier: Modifier = Modifier,
  size: Dp = 44.dp,
) {
  Surface(
    onClick = onClick,
    modifier = modifier.size(size).minimumInteractiveComponentSize().testTag(testTag),
    shape = CircleShape,
    color = MaterialTheme.colorScheme.surface,
    shadowElevation = 2.dp,
  ) {
    Box(contentAlignment = Alignment.Center) {
      Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        tint = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.size(22.dp),
      )
    }
  }
}

@Composable
fun TopBarWithBackAndMore(
  title: String,
  onBack: () -> Unit,
  onMoreClick: (() -> Unit)? = null,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    CircularWhiteIconButton(
      onClick = onBack,
      icon = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
      contentDescription = stringResource(R.string.cd_back),
      testTag = "top_back_button",
    )
    Text(
      text = title,
      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
      color = MaterialTheme.colorScheme.onBackground,
    )
    if (onMoreClick != null) {
      CircularWhiteIconButton(
        onClick = onMoreClick,
        icon = Icons.Rounded.MoreVert,
        contentDescription = stringResource(R.string.cd_more_options),
        testTag = "top_more_button",
      )
    } else {
      Spacer(modifier = Modifier.size(44.dp))
    }
  }
}

@Composable
fun SectionHeaderRow(
  title: String,
  actionLabel: String? = stringResource(R.string.action_see_all),
  onActionClick: (() -> Unit)? = null,
  testTag: String = "see_all_button",
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = title,
      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
      color = MaterialTheme.colorScheme.onBackground,
    )
    if (actionLabel != null && onActionClick != null) {
      Text(
        text = actionLabel,
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
        color = SlateSecondary,
        modifier =
          Modifier.testTag(testTag)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onActionClick)
            .padding(horizontal = 6.dp, vertical = 4.dp),
      )
    }
  }
}

@Composable
fun SpecialtyTileCard(
  specialty: SpecialtyCategory,
  isSelected: Boolean,
  onClick: () -> Unit,
) {
  val accentColor = Color(specialty.accentColorHex)
  Surface(
    onClick = onClick,
    modifier = Modifier.width(96.dp).testTag("specialty_${specialty.id}"),
    shape = RoundedCornerShape(18.dp),
    color = if (isSelected) IndigoSoftContainer else MaterialTheme.colorScheme.surface,
    border = if (isSelected) BorderStroke(1.5.dp, IndigoPrimary) else null,
    shadowElevation = if (isSelected) 3.dp else 1.dp,
  ) {
    Column(
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 14.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      SpecialtyVectorArt(
        iconType = specialty.iconType,
        accentColor = accentColor,
        modifier = Modifier.size(34.dp),
      )
      Text(
        text = specialty.name,
        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 11.sp),
        color = if (isSelected) IndigoPrimary else InkPrimary,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    }
  }
}

@Composable
fun SpecialtyVectorArt(
  iconType: SpecialtyIconType,
  accentColor: Color,
  modifier: Modifier = Modifier,
) {
  Canvas(modifier = modifier) {
    val w = size.width
    val h = size.height
    val stroke = Stroke(width = w * 0.08f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    when (iconType) {
      SpecialtyIconType.NEUROLOGY -> {
        // Stylized dual-hemisphere brain outline
        drawOval(
          color = accentColor.copy(alpha = 0.22f),
          topLeft = Offset(w * 0.12f, h * 0.16f),
          size = androidx.compose.ui.geometry.Size(w * 0.38f, h * 0.66f),
        )
        drawOval(
          color = accentColor.copy(alpha = 0.22f),
          topLeft = Offset(w * 0.50f, h * 0.16f),
          size = androidx.compose.ui.geometry.Size(w * 0.38f, h * 0.66f),
        )
        drawOval(
          color = accentColor,
          topLeft = Offset(w * 0.12f, h * 0.16f),
          size = androidx.compose.ui.geometry.Size(w * 0.38f, h * 0.66f),
          style = stroke,
        )
        drawOval(
          color = accentColor,
          topLeft = Offset(w * 0.50f, h * 0.16f),
          size = androidx.compose.ui.geometry.Size(w * 0.38f, h * 0.66f),
          style = stroke,
        )
        drawLine(accentColor, Offset(w * 0.24f, h * 0.42f), Offset(w * 0.38f, h * 0.36f), strokeWidth = w * 0.06f, cap = StrokeCap.Round)
        drawLine(accentColor, Offset(w * 0.62f, h * 0.52f), Offset(w * 0.76f, h * 0.46f), strokeWidth = w * 0.06f, cap = StrokeCap.Round)
      }
      SpecialtyIconType.CARDIOLOGY -> {
        val heartPath = Path().apply {
          moveTo(w * 0.5f, h * 0.84f)
          cubicTo(w * 0.14f, h * 0.60f, w * 0.12f, h * 0.24f, w * 0.34f, h * 0.22f)
          cubicTo(w * 0.44f, h * 0.21f, w * 0.5f, h * 0.30f, w * 0.5f, h * 0.34f)
          cubicTo(w * 0.5f, h * 0.30f, w * 0.56f, h * 0.21f, w * 0.66f, h * 0.22f)
          cubicTo(w * 0.88f, h * 0.24f, w * 0.86f, h * 0.60f, w * 0.5f, h * 0.84f)
          close()
        }
        drawPath(heartPath, color = accentColor.copy(alpha = 0.24f))
        drawPath(heartPath, color = accentColor, style = stroke)
        // Pulse wave
        val pulse = Path().apply {
          moveTo(w * 0.26f, h * 0.50f)
          lineTo(w * 0.40f, h * 0.50f)
          lineTo(w * 0.46f, h * 0.36f)
          lineTo(w * 0.54f, h * 0.62f)
          lineTo(w * 0.60f, h * 0.50f)
          lineTo(w * 0.74f, h * 0.50f)
        }
        drawPath(pulse, color = accentColor, style = Stroke(width = w * 0.065f, cap = StrokeCap.Round, join = StrokeJoin.Round))
      }
      SpecialtyIconType.ORTHOPEDICS -> {
        // Stylized joint / bone icon
        drawLine(accentColor, Offset(w * 0.32f, h * 0.16f), Offset(w * 0.52f, h * 0.44f), strokeWidth = w * 0.13f, cap = StrokeCap.Round)
        drawLine(accentColor, Offset(w * 0.52f, h * 0.56f), Offset(w * 0.66f, h * 0.84f), strokeWidth = w * 0.13f, cap = StrokeCap.Round)
        drawCircle(accentColor.copy(alpha = 0.28f), radius = w * 0.18f, center = Offset(w * 0.52f, h * 0.50f))
        drawCircle(accentColor, radius = w * 0.13f, center = Offset(w * 0.52f, h * 0.50f), style = stroke)
      }
      SpecialtyIconType.PULMONOLOGY -> {
        // Trachea + lungs
        drawLine(accentColor, Offset(w * 0.5f, h * 0.14f), Offset(w * 0.5f, h * 0.44f), strokeWidth = w * 0.08f, cap = StrokeCap.Round)
        drawOval(accentColor.copy(alpha = 0.24f), Offset(w * 0.14f, h * 0.32f), androidx.compose.ui.geometry.Size(w * 0.30f, h * 0.52f))
        drawOval(accentColor.copy(alpha = 0.24f), Offset(w * 0.56f, h * 0.32f), androidx.compose.ui.geometry.Size(w * 0.30f, h * 0.52f))
        drawOval(accentColor, Offset(w * 0.14f, h * 0.32f), androidx.compose.ui.geometry.Size(w * 0.30f, h * 0.52f), style = stroke)
        drawOval(accentColor, Offset(w * 0.56f, h * 0.32f), androidx.compose.ui.geometry.Size(w * 0.30f, h * 0.52f), style = stroke)
      }
      SpecialtyIconType.PODIATRY -> {
        // Stylized medical cross + arch
        drawCircle(accentColor.copy(alpha = 0.20f), radius = w * 0.38f, center = Offset(w * 0.5f, h * 0.5f))
        drawLine(accentColor, Offset(w * 0.5f, h * 0.28f), Offset(w * 0.5f, h * 0.72f), strokeWidth = w * 0.12f, cap = StrokeCap.Round)
        drawLine(accentColor, Offset(w * 0.28f, h * 0.5f), Offset(w * 0.72f, h * 0.5f), strokeWidth = w * 0.12f, cap = StrokeCap.Round)
      }
    }
  }
}

@Composable
fun RecentDoctorHorizontalCard(
  doctor: Doctor,
  onCardClick: () -> Unit,
  onBookNowClick: () -> Unit,
  onCallClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Surface(
    onClick = onCardClick,
    modifier = modifier.width(292.dp).height(164.dp).testTag("recent_doctor_card_${doctor.id}"),
    shape = RoundedCornerShape(22.dp),
    color = MaterialTheme.colorScheme.surface,
    shadowElevation = 2.dp,
  ) {
    Row(
      modifier = Modifier.fillMaxSize().padding(14.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Box(
        modifier =
          Modifier.width(102.dp)
            .fillMaxHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF3F1F7)),
      ) {
        Image(
          painter = painterResource(doctor.avatarRes),
          contentDescription = doctor.name,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize(),
        )
        if (doctor.isOnline) {
          Box(
            modifier =
              Modifier.align(Alignment.TopEnd)
                .padding(6.dp)
                .size(10.dp)
                .clip(CircleShape)
                .background(EmeraldLive)
                .border(1.5.dp, Color.White, CircleShape),
          )
        }
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(
        modifier = Modifier.fillMaxHeight().weight(1f),
        verticalArrangement = Arrangement.SpaceBetween,
      ) {
        Column(modifier = Modifier.padding(top = 4.dp)) {
          Text(
            text = doctor.name,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = doctor.specialtyName,
            style = MaterialTheme.typography.bodyMedium,
            color = SlateSecondary,
            maxLines = 1,
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "${doctor.experienceYears} years experience",
            style = MaterialTheme.typography.bodySmall,
            color = SlateSecondary,
          )
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Surface(
            onClick = onBookNowClick,
            modifier = Modifier.height(38.dp).testTag("book_now_${doctor.id}"),
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, SubtleOutline),
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Icon(
                imageVector = Icons.Rounded.CalendarMonth,
                contentDescription = null,
                tint = IndigoPrimary,
                modifier = Modifier.size(15.dp),
              )
              Spacer(modifier = Modifier.width(5.dp))
              Text(
                text = stringResource(R.string.action_book_now),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = IndigoPrimary,
              )
            }
          }

          Surface(
            onClick = onCallClick,
            modifier = Modifier.size(38.dp).minimumInteractiveComponentSize().testTag("call_doctor_${doctor.id}"),
            shape = CircleShape,
            color = IndigoPrimary,
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.Rounded.Call,
                contentDescription = stringResource(R.string.cd_call_doctor),
                tint = Color.White,
                modifier = Modifier.size(18.dp),
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun RecommendDoctorCard(
  doctor: Doctor,
  onCardClick: () -> Unit,
  onVisitNowClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Surface(
    onClick = onCardClick,
    modifier = modifier.fillMaxWidth().testTag("recommend_doctor_card_${doctor.id}"),
    shape = RoundedCornerShape(22.dp),
    color = MaterialTheme.colorScheme.surface,
    shadowElevation = 2.dp,
  ) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(14.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Box(
        modifier =
          Modifier.size(width = 104.dp, height = 108.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF3F1F7)),
      ) {
        Image(
          painter = painterResource(doctor.avatarRes),
          contentDescription = doctor.name,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize(),
        )
      }

      Spacer(modifier = Modifier.width(14.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth(),
        ) {
          Text(
            text = doctor.name,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
          )
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Rounded.Star,
              contentDescription = null,
              tint = Color(0xFFF5A623),
              modifier = Modifier.size(14.dp),
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
              text = "${doctor.rating}",
              style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
              color = MaterialTheme.colorScheme.onSurface,
            )
          }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = doctor.specialtyName,
          style = MaterialTheme.typography.bodyMedium,
          color = SlateSecondary,
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "${doctor.experienceYears} years experience",
          style = MaterialTheme.typography.bodySmall,
          color = SlateSecondary,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(
          onClick = onVisitNowClick,
          modifier = Modifier.height(38.dp).testTag("visit_now_${doctor.id}"),
          shape = RoundedCornerShape(50),
          colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary, contentColor = Color.White),
          contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 22.dp, vertical = 0.dp),
        ) {
          Text(
            text = stringResource(R.string.action_visit_now),
            style = MaterialTheme.typography.labelLarge,
          )
        }
      }
    }
  }
}

@Composable
fun AvatarCircle(
  @DrawableRes avatarRes: Int,
  contentDescription: String,
  size: Dp,
  modifier: Modifier = Modifier,
) {
  Image(
    painter = painterResource(avatarRes),
    contentDescription = contentDescription,
    contentScale = ContentScale.Crop,
    modifier = modifier.size(size).clip(CircleShape),
  )
}
