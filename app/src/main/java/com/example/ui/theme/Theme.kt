package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val VanguardDarkColorScheme = darkColorScheme(
    primary = GoldAccent,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = GoldAccentLight,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    tertiary = GoldAccentLight,
    background = ObsidianBlack,
    onBackground = PlatinumWhite,
    surface = DarkGunmetal,
    onSurface = PlatinumWhite,
    surfaceVariant = DarkCharcoal,
    onSurfaceVariant = PlatinumMuted,
    outline = SlateBorder,
    outlineVariant = SlateLightBorder
)

private val VanguardLightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    secondary = Color(0xFF475569),
    onSecondary = Color.White,
    background = BackgroundLight,
    onBackground = Color(0xFF0F172A),
    surface = SurfaceLight,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek luxury dark theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> VanguardDarkColorScheme
        else -> VanguardLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
