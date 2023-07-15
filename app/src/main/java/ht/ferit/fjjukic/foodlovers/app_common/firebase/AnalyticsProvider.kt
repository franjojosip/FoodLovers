package ht.ferit.fjjukic.foodlovers.app_common.firebase

interface AnalyticsProvider {
    fun logScreenEvent(screenName: String, additionalInfo: Pair<String, String>? = null)
}