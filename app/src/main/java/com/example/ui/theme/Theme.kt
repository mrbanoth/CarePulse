package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val CarePulseLightColorScheme =
  lightColorScheme(
    primary = IndigoPrimary,
    onPrimary = PureWhiteSurface,
    primaryContainer = IndigoSoftContainer,
    onPrimaryContainer = IndigoDark,
    secondary = PodiatryTeal,
    onSecondary = PureWhiteSurface,
    secondaryContainer = SoftLavenderSurface,
    onSecondaryContainer = InkPrimary,
    tertiary = OrthoAmber,
    onTertiary = PureWhiteSurface,
    background = AlabasterCanvas,
    onBackground = InkPrimary,
    surface = PureWhiteSurface,
    onSurface = InkPrimary,
    surfaceVariant = SoftLavenderSurface,
    onSurfaceVariant = SlateSecondary,
    outline = SubtleOutline,
    outlineVariant = SubtleOutline,
    error = CoralRed,
    onError = Color.White,
  )

private val CarePulseDarkColorScheme =
  darkColorScheme(
    primary = DarkIndigoPrimary,
    onPrimary = Color(0xFF141229),
    primaryContainer = IndigoDark,
    onPrimaryContainer = PureWhiteSurface,
    secondary = PodiatryTeal,
    onSecondary = Color(0xFF0A2422),
    background = DarkBackground,
    onBackground = PureWhiteSurface,
    surface = DarkSurface,
    onSurface = PureWhiteSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFB7B4C7),
    outline = Color(0xFF39374A),
    error = CoralRed,
    onError = Color.White,
  )

val CarePulseShapes =
  Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(30.dp),
  )

@Composable
fun CarePulseTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) CarePulseDarkColorScheme else CarePulseLightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = CarePulseTypography,
    shapes = CarePulseShapes,
    content = content,
  )
}
