package com.eleni.speechflow.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

private val LightColors = lightColorScheme(
    primary            = NavyDeep,
    onPrimary          = PureWhite,
    primaryContainer   = NavyMedium,
    onPrimaryContainer = PureWhite,
    secondary          = SkySoft,
    onSecondary        = NavyDeep,
    secondaryContainer = SkyPale,
    onSecondaryContainer = NavyDeep,
    tertiary           = CoralWarm,
    onTertiary         = PureWhite,
    tertiaryContainer  = CoralLight,
    onTertiaryContainer = NavyDeep,
    background         = OffWhite,
    onBackground       = SlateDeep,
    surface            = PureWhite,
    onSurface          = SlateDeep,
    surfaceVariant     = SlateLight,
    onSurfaceVariant   = SlateMedium,
    error              = ErrorRed,
    onError            = PureWhite,
    outline            = SlateLight,
)

private val DarkColors = darkColorScheme(
    primary            = SkySoft,
    onPrimary          = NavyDeep,
    primaryContainer   = NavyMedium,
    onPrimaryContainer = SkyPale,
    secondary          = SkyPale,
    onSecondary        = NavyDeep,
    tertiary           = CoralWarm,
    onTertiary         = NavyDeep,
    background         = NavyDeep,
    onBackground       = SkyPale,
    surface            = NavyDeep,
    onSurface          = OffWhite,
    error              = ErrorRed,
    onError            = PureWhite,
)

@Composable
fun SpeechFlowTheme(
    theme: String = "system",
    fontScale: Float = 1.0f,
    content: @Composable () -> Unit,
) {
    val darkTheme = when (theme) {
        "light" -> false
        "dark"  -> true
        else    -> isSystemInDarkTheme()
    }
    val colors = if (darkTheme) DarkColors else LightColors

    val typography = Typography(
        displayLarge = SpeechFlowTypography.displayLarge.copy(
            fontSize = SpeechFlowTypography.displayLarge.fontSize * fontScale,
            lineHeight = SpeechFlowTypography.displayLarge.lineHeight * fontScale
        ),
        displayMedium = SpeechFlowTypography.displayMedium.copy(
            fontSize = SpeechFlowTypography.displayMedium.fontSize * fontScale,
            lineHeight = SpeechFlowTypography.displayMedium.lineHeight * fontScale
        ),
        displaySmall = SpeechFlowTypography.displaySmall.copy(
            fontSize = SpeechFlowTypography.displaySmall.fontSize * fontScale,
            lineHeight = SpeechFlowTypography.displaySmall.lineHeight * fontScale
        ),
        headlineLarge = SpeechFlowTypography.headlineLarge.copy(
            fontSize = SpeechFlowTypography.headlineLarge.fontSize * fontScale,
            lineHeight = SpeechFlowTypography.headlineLarge.lineHeight * fontScale
        ),
        headlineMedium = SpeechFlowTypography.headlineMedium.copy(
            fontSize = SpeechFlowTypography.headlineMedium.fontSize * fontScale,
            lineHeight = SpeechFlowTypography.headlineMedium.lineHeight * fontScale
        ),
        headlineSmall = SpeechFlowTypography.headlineSmall.copy(
            fontSize = SpeechFlowTypography.headlineSmall.fontSize * fontScale,
            lineHeight = SpeechFlowTypography.headlineSmall.lineHeight * fontScale
        ),
        titleLarge = SpeechFlowTypography.titleLarge.copy(
            fontSize = SpeechFlowTypography.titleLarge.fontSize * fontScale,
            lineHeight = SpeechFlowTypography.titleLarge.lineHeight * fontScale
        ),
        titleMedium = SpeechFlowTypography.titleMedium.copy(
            fontSize = SpeechFlowTypography.titleMedium.fontSize * fontScale,
            lineHeight = SpeechFlowTypography.titleMedium.lineHeight * fontScale
        ),
        titleSmall = SpeechFlowTypography.titleSmall.copy(
            fontSize = SpeechFlowTypography.titleSmall.fontSize * fontScale,
            lineHeight = SpeechFlowTypography.titleSmall.lineHeight * fontScale
        ),
        bodyLarge = SpeechFlowTypography.bodyLarge.copy(
            fontSize = SpeechFlowTypography.bodyLarge.fontSize * fontScale,
            lineHeight = SpeechFlowTypography.bodyLarge.lineHeight * fontScale
        ),
        bodyMedium = SpeechFlowTypography.bodyMedium.copy(
            fontSize = SpeechFlowTypography.bodyMedium.fontSize * fontScale,
            lineHeight = SpeechFlowTypography.bodyMedium.lineHeight * fontScale
        ),
        bodySmall = SpeechFlowTypography.bodySmall.copy(
            fontSize = SpeechFlowTypography.bodySmall.fontSize * fontScale,
            lineHeight = SpeechFlowTypography.bodySmall.lineHeight * fontScale
        ),
        labelLarge = SpeechFlowTypography.labelLarge.copy(
            fontSize = SpeechFlowTypography.labelLarge.fontSize * fontScale,
            lineHeight = SpeechFlowTypography.labelLarge.lineHeight * fontScale
        ),
        labelMedium = SpeechFlowTypography.labelMedium.copy(
            fontSize = SpeechFlowTypography.labelMedium.fontSize * fontScale,
            lineHeight = SpeechFlowTypography.labelMedium.lineHeight * fontScale
        ),
        labelSmall = SpeechFlowTypography.labelSmall.copy(
            fontSize = SpeechFlowTypography.labelSmall.fontSize * fontScale,
            lineHeight = SpeechFlowTypography.labelSmall.lineHeight * fontScale
        )
    )

    MaterialTheme(
        colorScheme = colors,
        typography  = typography,
        shapes      = SpeechFlowShapes,
        content     = content,
    )
}
