package com.android.weatherui.weather.ui.common_composables

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import com.android.weatherui.ui.theme.Yellow

inline fun InlineTextContentIcon(
    id: String,
    icon: ImageVector = Icons.Default.KeyboardArrowRight,
    iconWidthEm: TextUnit = 1.em,
    iconHeightEm: TextUnit = 1.em,
    iconColor: Color = Yellow,
) : Map<String, InlineTextContent> {
    return mapOf(
        Pair(
            id,
            InlineTextContent(
                Placeholder(
                    width = iconWidthEm,
                    height = iconWidthEm,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                )
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor
                )
            }
        )
    )
}