package com.github.eylulnc.walkmunich.ui.theme

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
    // core roles
    primary = OrangeMain,
    onPrimary = Color.Black,
    secondary = BlueTeal,
    onSecondary = Color.White,
    tertiary = Green,
    onTertiary = Color.White,

    // containers
    primaryContainer = Yellow,
    onPrimaryContainer = Color.Black,
    secondaryContainer = BlueLight,
    onSecondaryContainer = BlueNavy,
    tertiaryContainer = GreenLight,
    onTertiaryContainer = Color.Black,

    // surfaces & backgrounds
    background = BgLight,
    onBackground = BlueNavy,
    surface = BgLight,
    onSurface = Color.Black,
    surfaceVariant = BlueLight.copy(alpha = 0.35f),
    onSurfaceVariant = BlueNavy,

    // outline / error
    outline = BlueNavy.copy(alpha = 0.5f),
    error = RedOrange,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = OrangeMain,
    onPrimary = Color.Black,
    secondary = BlueTeal,
    onSecondary = Color.White,
    tertiary = Green,
    onTertiary = Color.White,

    primaryContainer = OrangeMain.copy(alpha = 0.85f),
    onPrimaryContainer = Color.Black,
    secondaryContainer = BlueNavy,
    onSecondaryContainer = Color.White,
    tertiaryContainer = Green.copy(alpha = 0.85f),
    onTertiaryContainer = Color.Black,

    background = BgDark,
    onBackground = Color(0xFFECECEC),
    surface = SurfaceDark,
    onSurface = Color(0xFFECECEC),
    surfaceVariant = SurfaceDark.copy(alpha = 0.7f),
    onSurfaceVariant = Color(0xFFCFD8DC),

    outline = Color(0xFF909090),
    error = RedOrange,
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
