package ht.ferit.fjjukic.foodlovers.app_recipe.model

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
    val user: String = "",
    var isFavorite: Boolean = false
)

object NoRecipePlaceholder : HomeScreenRecipe()

data class RecipeCategory(
    val id: String,
    val title: String,
    val hasMarginStart: Boolean = true
)

class TodayChoiceRecipe(
    id: String,
    title: String,
    description: String,
    time: String,
    servings: Int,
    difficulty: String,
    category: String,
    imagePath: String,
    user: String,
    isFavorite: Boolean
) : HomeScreenRecipe(
    id,
    title,
    description,
    time,
    servings,
    difficulty,
    category,
    imagePath,
    user,
    isFavorite
)

class TopRecipe(
    id: String,
    title: String,
    description: String,
    time: String,
    servings: Int,
    difficulty: String,
    category: String,
    imagePath: String,
    user: String,
    isFavorite: Boolean
) : HomeScreenRecipe(
    id,
    title,
    description,
    time,
    servings,
    difficulty,
    category,
    imagePath,
    user,
    isFavorite
)

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
    isFavorite: Boolean,
    val userId: String,
) : HomeScreenRecipe(
    id,
    title,
    description,
    time,
    servings,
    difficulty,
    category,
    imagePath,
    user,
    isFavorite
)