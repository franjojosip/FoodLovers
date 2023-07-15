package ht.ferit.fjjukic.foodlovers.app_common.firebase

import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsProviderImpl(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsProvider {
    override fun logScreenEvent(screenName: String, additionalInfo: Pair<String, String>?) {
        val bundle = if (additionalInfo != null) {
            bundleOf(
                Pair(FirebaseAnalytics.Param.SCREEN_NAME, screenName),
                additionalInfo
            )
        } else {
            bundleOf(Pair(FirebaseAnalytics.Param.SCREEN_NAME, screenName))
        }

        firebaseAnalytics.logEvent(
            FirebaseAnalytics.Event.SCREEN_VIEW,
            bundle
        )
    }
}