@file:Suppress("DEPRECATION")

package ht.ferit.fjjukic.foodlovers.app_common.listener

import android.content.Context
import android.location.Geocoder
import android.os.Build
import ht.ferit.fjjukic.foodlovers.app_common.utils.checkNetworkState
import java.util.Locale

interface LocationHandler {
    fun getLocation(context: Context, lat: String, long: String, action: (String) -> Unit) {
        if (!context.checkNetworkState()) action("")

        val geocoder = Geocoder(context, Locale.getDefault())
        if (Build.VERSION.SDK_INT >= 33) {
            geocoder.getFromLocation(lat.toDouble(), long.toDouble(), 1) { addresses ->
                action("${addresses[0].countryName}, ${addresses[0].locality ?: addresses[0].adminArea}")
            }
        } else {
            val addresses = geocoder.getFromLocation(lat.toDouble(), long.toDouble(), 1)
            val location = when {
                addresses.isNullOrEmpty() -> ""
                else -> "${addresses[0].countryName}, ${addresses[0].locality ?: addresses[0].adminArea}"
            }
            action(location)
        }
    }
}