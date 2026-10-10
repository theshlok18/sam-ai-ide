package com.shlok.sam.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val SAMDarkColorScheme = darkColorScheme(
    primary = CobaltPrimary,
    onPrimary = DarkSurfaceLowest,
    primaryContainer = CobaltPrimary.copy(alpha = 0.2f),
    onPrimaryContainer = PrimaryFixed,
    secondary = CyberViolet,
    onSecondary = DarkSurfaceLowest,
    secondaryContainer = CyberViolet.copy(alpha = 0.2f),
    onSecondaryContainer = SecondaryFixed,
    tertiary = MintEmerald,
    onTertiary = DarkSurfaceLowest,
    background = DarkBackground,
    onBackground = TextOnSurface,
    surface = DarkSurface,
    onSurface = TextOnSurface,
    surfaceVariant = DarkSurfaceContainer,
    onSurfaceVariant = TextOnSurfaceVariant,
    outline = HairlineBorder,
    outlineVariant = OutlineVariant,
    error = StatusError,
    errorContainer = StatusErrorContainer
)

@Composable
fun SAMTheme(
    darkTheme: Boolean = true, // S.A.M. is built as a Cybernetic Studio Dark UI
    content: @Composable () -> Unit
) {
    val colorScheme = SAMDarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DarkBackground.toArgb()
            window.navigationBarColor = DarkBackground.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
