package com.android.weatherui.weather.ui.common_composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntSizeAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.weatherui.ui.theme.BrownDay
import com.android.weatherui.ui.theme.DarkBlue
import com.android.weatherui.ui.theme.DarkBrownDay
import com.android.weatherui.ui.theme.FancyCardBackgroundBrushColorDay
import com.android.weatherui.ui.theme.FancyCardBackgroundBrushColorsNight
import com.android.weatherui.weather.domain.constants.InlineTextContentIds
import com.android.weatherui.ui.theme.Yellow

@Composable
fun FancyCard(
    text: String,
    modifier: Modifier = Modifier,
    isDay: () -> Boolean,
) {

//    val brushGradient = Brush.horizontalGradient(
//        colors = listOf(
//            Color(0xff16B5BB),
//            Color(0xff1661BB),
//        )
//    )

    val brushGradient = remember(isDay()) {
        derivedStateOf {
            Brush.horizontalGradient(
                colors = if (isDay()) {
                    FancyCardBackgroundBrushColorDay
                } else {
                    FancyCardBackgroundBrushColorsNight
                }
            )
        }
    }


    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = if (isDay()) BrownDay else Yellow
        ),
        shape = RoundedCornerShape(
            topStart = 0.dp,
            bottomStart = 20.dp,
            topEnd = 0.dp,
            bottomEnd = 0.dp
        ),
        modifier = modifier
            .wrapContentSize()
            .background(
                brush = brushGradient.value,
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    bottomStart = 20.dp,
                    topEnd = 0.dp,
                    bottomEnd = 0.dp
                )
            )
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 12.sp
                    )
                ) {
                    append(text)
                    appendInlineContent(
                        id = InlineTextContentIds.SIMPLE_ARROW_RIGHT,
                        alternateText = InlineTextContentIds.SIMPLE_ARROW_RIGHT,
                    )
                }
            },
            inlineContent = InlineTextContentIcon(
                id = InlineTextContentIds.SIMPLE_ARROW_RIGHT,
                iconColor = if (isDay()) BrownDay else Yellow
            ),
            modifier = modifier
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 0.dp,
                    bottom = 0.dp
                )
        )
    }
}