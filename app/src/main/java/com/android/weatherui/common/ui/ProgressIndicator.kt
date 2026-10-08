package com.android.weatherui.common.ui

import android.R.attr.strokeWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.android.weatherui.ui.theme.DarkBlue
import com.android.weatherui.ui.theme.Gray
import com.android.weatherui.ui.theme.SimpleCardBgColor
import com.android.weatherui.ui.theme.White

@Composable
fun ProgressIndicator(
    modifier: Modifier = Modifier,
    progress: (() -> Float)? = null,
    color: Color = White,
    strokeWidth: Dp = 2.dp,
    trackColor: Color = Color.Transparent,
    strokeCap: StrokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
) {
    if (progress != null) {
        CircularProgressIndicator(
            progress = progress,
            color = color,
            trackColor = trackColor,
            strokeWidth = strokeWidth,
            strokeCap = strokeCap,
            modifier = modifier
        )
    } else {
        CircularProgressIndicator(
            color = color,
            trackColor = trackColor,
            strokeWidth = strokeWidth,
            strokeCap = strokeCap,
            modifier = modifier
        )
    }
}