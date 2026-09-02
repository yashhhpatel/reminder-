package com.remindly.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.remindly.app.domain.model.AppThemeMode

data class ExtendedColors(
    val textPrimary: Color,
    val textSecondary: Color,
    val cardBorder: Color,
    val toggleOffTrack: Color,
    val toggleOnTrack: Color,
    val toggleKnobOn: Color,
    val toggleKnobOff: Color,
    val premiumGold: Color,
    val premiumGoldBorder: Color,
    val goProGradientStart: Color,
    val goProGradientEnd: Color,
    val destructive: Color,
    val surfaceElevated: Color,
)

private val LightExtendedColors = ExtendedColors(
    textPrimary = LightTextPrimary,
    textSecondary = LightTextSecondary,
    cardBorder = LightBorder,
    toggleOffTrack = LightToggleOffTrack,
    toggleOnTrack = BrandPurple,
    toggleKnobOn = ToggleKnobOn,
    toggleKnobOff = Color(0xFFFFFFFF),
    premiumGold = PremiumGold,
    premiumGoldBorder = PremiumGoldBorder,
    goProGradientStart = GoProGreenStart,
    goProGradientEnd = GoProGreenEnd,
    destructive = DestructiveRed,
    surfaceElevated = LightSurfaceElevated,
)

private val DarkExtendedColors = ExtendedColors(
    textPrimary = DarkTextPrimary,
    textSecondary = DarkTextSecondary,
    cardBorder = DarkBorder,
    toggleOffTrack = DarkToggleOffTrack,
    toggleOnTrack = BrandPurpleLight,
    toggleKnobOn = ToggleKnobOn,
    toggleKnobOff = ToggleKnobOff,
    premiumGold = PremiumGold,
    premiumGoldBorder = PremiumGoldBorder,
    goProGradientStart = GoProGreenStart,
    goProGradientEnd = GoProGreenEnd,
    destructive = DestructiveRed,
    surfaceElevated = DarkSurfaceElevated,
)

val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }

private val LightColors = lightColorScheme(
    primary = BrandPurple,
    onPrimary = Color.White,
    secondary = BrandPurpleMuted,
    onSecondary = Color.White,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceElevated,
    onSurfaceVariant = LightTextSecondary,
    error = DestructiveRed,
    outline = LightBorder,
)

private val DarkColors = darkColorScheme(
    primary = BrandPurpleLight,
    onPrimary = Color.White,
    secondary = BrandPurpleMuted,
    onSecondary = Color.White,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = DarkTextSecondary,
    error = DestructiveRed,
    outline = DarkBorder,
)

@Composable
fun RemindlyTheme(
    themeMode: AppThemeMode = AppThemeMode.AUTO,
    content: @Composable () -> Unit,
) {
    val systemDark = isSystemInDarkTheme()
    val useDark = when (themeMode) {
        AppThemeMode.AUTO -> systemDark
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
    }

    val colorScheme = if (useDark) DarkColors else LightColors
    val extendedColors = if (useDark) DarkExtendedColors else LightExtendedColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        val activity = view.context as? Activity
        androidx.compose.runtime.SideEffect {
            activity?.window?.let { window ->
                window.statusBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !useDark
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !useDark
            }
        }
    }

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            shapes = AppShapes,
            content = content,
        )
    }
}

object AppTheme {
    val extendedColors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
}
