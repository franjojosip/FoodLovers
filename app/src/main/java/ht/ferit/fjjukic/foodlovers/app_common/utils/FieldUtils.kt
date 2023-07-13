package ht.ferit.fjjukic.foodlovers.app_common.utils

import android.content.res.Resources
import ht.ferit.fjjukic.foodlovers.R

fun Int.convertToTime(): String {
    val hours = this / 60
    val mins = this % 60

    return when {
        hours > 0 && mins > 0 -> {
            "${hours}h ${mins}min"
        }

        hours > 0 -> {
            "$hours h"
        }
        else -> {
            "$mins min"
        }
    }
}

fun Int.convertToServings(resources: Resources): String {
    return resources.getQuantityString(R.plurals.label_num_of_servings, this, this)
}