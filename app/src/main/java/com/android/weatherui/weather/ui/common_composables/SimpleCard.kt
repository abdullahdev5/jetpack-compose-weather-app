package com.android.weatherui.weather.ui.common_composables

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.android.weatherui.ui.theme.SimpleCardBgColor
import com.android.weatherui.ui.theme.White

@Composable
fun SimpleCard(
    icon: Painter,
    modifier: Modifier,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = SimpleCardBgColor,
            contentColor = White
        ),
        shape = RoundedCornerShape(5.dp),
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = modifier
        )
    }
}