package ht.ferit.fjjukic.foodlovers.app_common.model

sealed class ActionNavigate {
    object Login : ActionNavigate()
    object Registration : ActionNavigate()
    object ForgotPassword : ActionNavigate()
    object Logout : ActionNavigate()

    object Home : ActionNavigate()
    object Account : ActionNavigate()

    object SearchRecipes : ActionNavigate()

    class ShowRecipe(val id: String) : ActionNavigate()
    class CategoryRecipes(val category: String) : ActionNavigate()

    object Location : ActionNavigate()
    object ChangeLocation : ActionNavigate()
    object ChangeUsername : ActionNavigate()
    object ChangeEmail : ActionNavigate()
}