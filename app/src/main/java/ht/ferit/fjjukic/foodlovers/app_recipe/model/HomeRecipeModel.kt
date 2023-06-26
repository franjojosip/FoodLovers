package ht.ferit.fjjukic.foodlovers.app_recipe.model

import ht.ferit.fjjukic.foodlovers.R


sealed class HomeScreenRecipe(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val time: String = "",
    val servings: Int = 4,
    val difficulty: String = "",
    val imagePath: String = "",
    val user: String = ""
)

object NoRecipePlaceholder : HomeScreenRecipe()

class Category(
    title: String,
    val drawableId: Int = R.drawable.image_salty_food,
    val hasMarginStart: Boolean = true
) : HomeScreenRecipe(title = title)

class TodayChoiceRecipe(
    id: String,
    title: String,
    description: String,
    time: String,
    servings: Int,
    difficulty: String,
    imagePath: String,
    user: String
) : HomeScreenRecipe(id, title, description, time, servings, difficulty, imagePath, user)

class TopRecipe(
    id: String,
    title: String,
    description: String,
    time: String,
    servings: Int,
    difficulty: String,
    imagePath: String,
    user: String
) : HomeScreenRecipe(id, title, description, time, servings, difficulty, imagePath, user)

class BasicRecipe(
    id: String,
    title: String,
    description: String,
    time: String,
    servings: Int,
    difficulty: String,
    imagePath: String,
    user: String,
    val ingredients: List<Ingredient>,
    val steps: List<Step>,
    val isFavorite: Boolean
) : HomeScreenRecipe(id, title, description, time, servings, difficulty, imagePath, user)