package ht.ferit.fjjukic.foodlovers.app_recipe.model

import ht.ferit.fjjukic.foodlovers.R


sealed class HomeScreenRecipe

object NoRecipePlaceholder : HomeScreenRecipe()

class Category(
    val title: String,
    val drawableId: Int = R.drawable.image_salty_food,
    val hasMarginStart: Boolean = true
) : HomeScreenRecipe()

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