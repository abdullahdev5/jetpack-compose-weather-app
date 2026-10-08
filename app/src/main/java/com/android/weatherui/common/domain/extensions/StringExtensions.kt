package com.android.weatherui.common.domain.extensions

fun String.takeOnlyTimeFromApi(): String {
    return substringAfter(" ")
}

fun String.takeFirstThreeWithFirstUpper(): String {

    val valueToReturn = toString().toLowerCase()
        .substring(0, 3).replaceFirstChar { it.toUpperCase() }

    return valueToReturn
}

fun String.getFullUrlOfTempConditionIconFromApi(): String {
    return "https:${toString()}"
}


fun String.takeHoursFromFormattedTimeString(): String {
    return substring(0, 2)
}

fun String.takAmPmStringFromFormattedTimeString(): String {
    return takeLast(2)
}
