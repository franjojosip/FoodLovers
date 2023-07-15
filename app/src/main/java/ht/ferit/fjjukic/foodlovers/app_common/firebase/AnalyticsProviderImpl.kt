package ht.ferit.fjjukic.foodlovers.app_common.firebase

import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsProviderImpl(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsProvider {
    override fun logScreenEvent(screenName: String) {
        firebaseAnalytics.logEvent(
            FirebaseAnalytics.Event.SCREEN_VIEW,
            bundleOf(Pair(FirebaseAnalytics.Param.SCREEN_NAME, screenName))
        )
    }

    override fun logShowRecipeScreenEvent(recipeId: String) {
        firebaseAnalytics.logEvent(
            FirebaseAnalytics.Event.SCREEN_VIEW,
            bundleOf(
                Pair(
                    FirebaseAnalytics.Param.SCREEN_NAME,
                    FirebaseAnalyticsConstants.Event.Screen.SHOW_RECIPE
                ),
                Pair(FirebaseAnalytics.Param.ITEM_ID, "recipeID - $recipeId")
            )
        )
    }

    override fun logCategoryRecipesScreenEvent(category: String) {
        firebaseAnalytics.logEvent(
            FirebaseAnalytics.Event.SCREEN_VIEW,
            bundleOf(
                Pair(
                    FirebaseAnalytics.Param.SCREEN_NAME,
                    FirebaseAnalyticsConstants.Event.Screen.CATEGORY_RECIPES
                ),
                Pair(FirebaseAnalytics.Param.ITEM_ID, "category - $category")
            )
        )
    }
}