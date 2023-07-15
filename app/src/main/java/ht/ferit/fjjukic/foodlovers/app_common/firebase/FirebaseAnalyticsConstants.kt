package ht.ferit.fjjukic.foodlovers.app_common.firebase

object FirebaseAnalyticsConstants {
    object Event {
        object Screen {
            const val WELCOME = "welcome"

            const val ACCOUNT = "account"
            const val CHANGE_EMAIL = "change_email"
            const val CHANGE_LOCATION = "change_location"
            const val CHANGE_USERNAME = "change_username"

            const val LOGIN = "login"
            const val REGISTER = "register"
            const val RESET_PASSWORD = "reset_password"

            const val HOME = "home"

            const val CREATE_RECIPE = "create_recipe"
            const val EDIT_RECIPE = "edit_recipe"
            const val SHOW_RECIPE = "show_recipe"

            const val MAIN_STEP = "main_step"
            const val INGREDIENTS_STEP = "ingredient_step"
            const val STEPS_STEP = "steps_step"
            const val REVIEW_STEP = "review_step"

            const val FAVORITES = "favorites"
            const val SEARCH = "search"
            const val FILTER = "filter"

            const val RECIPES = "recipes"
            const val CATEGORY_RECIPES = "category_recipes"
        }
    }
}