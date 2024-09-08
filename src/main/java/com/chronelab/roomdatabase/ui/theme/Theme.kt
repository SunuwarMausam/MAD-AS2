package com.chronelab.roomdatabase.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Define dark color scheme
private val DarkColorScheme = darkColorScheme(
    primary = Turquiseblue,   // Primary color for dark theme
    secondary = DarkTeal40,   // Secondary color for dark theme
    tertiary = DarkOrange40   // Tertiary color for dark theme
)

// Define light color scheme
private val LightColorScheme = lightColorScheme(
    primary = LightBlue80,    // Primary color for light theme
    secondary = LightTeal80,  // Secondary color for light theme
    tertiary = LightYellow80  // Tertiary color for light theme
)

// Composable function to apply theme
@Composable
fun RoomAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // Determine the color scheme to use
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            // Use dynamic color schemes if supported and requested
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme  // Use dark color scheme if system is in dark theme
        else -> LightColorScheme  // Use light color scheme otherwise
    }

    // Apply the MaterialTheme with the chosen color scheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,  // Presumably defined elsewhere
        content = content
    )
}
