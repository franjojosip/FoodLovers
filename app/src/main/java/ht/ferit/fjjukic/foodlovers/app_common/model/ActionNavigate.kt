package ht.ferit.fjjukic.foodlovers.app_common.model

import androidx.navigation.NavDirections

sealed class ActionNavigate {
    object MainScreen : ActionNavigate()
    object Prelogin : ActionNavigate()
    object Back : ActionNavigate()
    class NavigationWithDirections(val navDirections: NavDirections) : ActionNavigate()
    object Login : ActionNavigate()
    object Account : ActionNavigate()
    object ChangeUsername : ActionNavigate()
    object ChangeEmail : ActionNavigate()
    object ChangeLocation : ActionNavigate()
}