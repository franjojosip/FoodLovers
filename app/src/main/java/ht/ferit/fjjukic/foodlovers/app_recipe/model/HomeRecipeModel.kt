package ht.ferit.fjjukic.foodlovers.app_recipe.model


sealed class HomeScreenRecipe

object NoRecipePlaceholder : HomeScreenRecipe()

class Category(val title: String): HomeScreenRecipe()

class TodayChoiceRecipe(
    val id: String,
    val title: String,
    val description: String = "",
    val time: String = "",
    val difficulty: String = ""
): HomeScreenRecipe()

class TopRecipe(
    val id: String,
    val title: String,
    val time: String = "",
    val difficulty: String = "",
    val user: String = ""
): HomeScreenRecipe()