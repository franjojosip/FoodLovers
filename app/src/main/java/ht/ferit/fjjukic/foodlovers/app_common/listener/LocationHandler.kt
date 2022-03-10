package ht.ferit.fjjukic.foodlovers.app_common.listener

import android.content.Context
import android.location.Geocoder
import ht.ferit.fjjukic.foodlovers.app_common.utils.checkNetworkState
import java.util.Locale

interface LocationHandler {
    fun getLocation(context: Context, lat: String, long: String): String {
        if (!context.checkNetworkState()) return ""

        val geoCoder = Geocoder(context.applicationContext, Locale.getDefault())
        val addressList = geoCoder.getFromLocation(lat.toDouble(), long.toDouble(), 1)

        return when {
            addressList.isNullOrEmpty() -> ""
            else -> "${addressList[0].countryName}, ${addressList[0].locality ?: addressList[0].adminArea}"
        }
    }
}