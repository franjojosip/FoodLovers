package ht.ferit.fjjukic.foodlovers.app_common.model

import androidx.navigation.NavDirections

sealed class ActionNavigate {
    class NavigationWithDirections(val navDirections: NavDirections) : ActionNavigate()

    object MainScreen : ActionNavigate()
    object Prelogin : ActionNavigate()

    object Back : ActionNavigate()

    object ChangeUsername : ActionNavigate()
    object ChangeEmail : ActionNavigate()
    object ChangeLocation : ActionNavigate()
}