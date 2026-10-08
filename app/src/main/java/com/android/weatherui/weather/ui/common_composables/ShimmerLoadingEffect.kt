package com.android.weatherui.weather.ui.common_composables

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.android.weatherui.ui.theme.Blue

@Composable
fun ShimmerLoadingEffect(
    modifier: Modifier = Modifier,
    widthOsShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
    shimmeringColor: Color = Blue
) {

    val shimmerColors = listOf(
        shimmeringColor.copy(0.3f),
        shimmeringColor.copy(0.5f),
        shimmeringColor.copy(1.0f),
        shimmeringColor.copy(0.5f),
        shimmeringColor.copy(0.3f),
    )

    val transition = rememberInfiniteTransition(label = "")

    val transitionAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = (durationMillis + widthOsShadowBrush).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Shimmer Loading Animation",
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = transitionAnimation.value - widthOsShadowBrush, y = 0f),
        end = Offset(x = transitionAnimation.value, y = angleOfAxisY),
    )

    Box(
        modifier = modifier,
    ) {
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(brush = brush),
        )
    }


}