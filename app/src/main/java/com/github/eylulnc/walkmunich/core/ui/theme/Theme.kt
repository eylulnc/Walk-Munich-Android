package com.github.eylulnc.walkmunich.core.ui.theme

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

private val LightColorScheme = lightColorScheme(
    primary = OrangeMain,
    onPrimary = Color.White,
    primaryContainer = Yellow.copy(alpha = 0.2f),
    onPrimaryContainer = BlueNavy,

    secondary = BlueTeal,
    onSecondary = Color.White,
    secondaryContainer = BlueTeal.copy(alpha = 0.15f),
    onSecondaryContainer = BlueNavy,

    background = NeutralLight,
    onBackground = TextLight,
    surface = NeutralSurface,
    onSurface = TextLight,
    surfaceVariant = Color(0xFFDDE1E6),
    onSurfaceVariant = TextMutedLight,

    outline = Color(0xFFCED4DA),
    error = RedError,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = OrangeMain,
    onPrimary = Color.White,
    primaryContainer = OrangeDark.copy(alpha = 0.7f),
    onPrimaryContainer = Color.Black,

    secondary = BlueTeal,
    onSecondary = Color.White,
    secondaryContainer = BlueNavy.copy(alpha = 0.6f),
    onSecondaryContainer = Color.White,

    background = NeutralDark,
    onBackground = TextDark,
    surface = NeutralCardDark,
    onSurface = TextDark,
    surfaceVariant = Color(0xFF2C3444),
    onSurfaceVariant = TextMutedDark,

    outline = Color(0xFF3A3F4B),
    error = RedError,
    onError = Color.White
)

@Composable
fun WalkMunichTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
