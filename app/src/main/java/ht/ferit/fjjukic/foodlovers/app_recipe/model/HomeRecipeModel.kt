package ht.ferit.fjjukic.foodlovers.app_recipe.model

import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.model.IngredientModel
import ht.ferit.fjjukic.foodlovers.app_common.model.StepModel


sealed class HomeScreenRecipe(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val time: String = "",
    val servings: Int = 4,
    val difficulty: String = "",
    val category: String = "",
    val imagePath: String = "",
    val user: String = ""
)

object NoRecipePlaceholder : HomeScreenRecipe()

data class RecipeCategory(
    val id: String,
    val title: String,
    val drawableId: Int = R.drawable.image_salty_food,
    val hasMarginStart: Boolean = true
)

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
    category: String,
    imagePath: String,
    user: String,
    val ingredients: MutableList<IngredientModel>,
    val steps: MutableList<StepModel>,
    val isFavorite: Boolean
) : HomeScreenRecipe(id, title, description, time, servings, difficulty, category, imagePath, user)