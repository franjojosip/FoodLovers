package ht.ferit.fjjukic.foodlovers.app_common.utils

fun Int.convertToTime(): String {
    val hours = this / 60
    val mins = this % 60

    return when {
        hours > 0 && mins > 0 -> {
            "${hours}h ${mins}min"
        }
        hours > 0 -> {
            "${hours}h"
        }
        else -> {
            "${mins}min"
        }
    }
}

fun Int.convertToServings(): String {
    return when {
        this == 1 -> "$this serving"
        else -> "$this servings"
    }
}