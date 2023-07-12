package ht.ferit.fjjukic.foodlovers.app_common.utils

import android.content.res.ColorStateList
import android.content.res.Resources
import androidx.core.content.res.ResourcesCompat
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.database.model.Category
import ht.ferit.fjjukic.foodlovers.app_common.database.model.Difficulty
import ht.ferit.fjjukic.foodlovers.app_common.database.model.Recipe
import ht.ferit.fjjukic.foodlovers.app_common.model.CategoryModel
import ht.ferit.fjjukic.foodlovers.app_common.model.DifficultyModel
import ht.ferit.fjjukic.foodlovers.app_common.model.IngredientModel
import ht.ferit.fjjukic.foodlovers.app_common.model.RecipeModel
import ht.ferit.fjjukic.foodlovers.app_common.model.StepModel
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.BasicRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.HomeScreenRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.Ingredient
import ht.ferit.fjjukic.foodlovers.app_recipe.model.RecipeCategory
import ht.ferit.fjjukic.foodlovers.app_recipe.model.Step
import ht.ferit.fjjukic.foodlovers.app_recipe.model.TodayChoiceRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.TopRecipe


fun List<CategoryModel>.mapToRecipeCategory(): List<RecipeCategory> {
    return map { category ->
        RecipeCategory(
            category.id,
            category.name
        )
    }
}

fun List<RecipeModel>.mapToBasicRecipes(): List<HomeScreenRecipe> {
    return map {
        BasicRecipe(
            it.id,
            it.name,
            it.description,
            it.time.toString(),
            it.servings,
            it.difficulty?.name ?: "",
            it.category?.name ?: "",
            it.imagePath,
            getUser(it.user),
            mutableListOf(),
            mutableListOf(),
            it.isFavorite,
            it.user?.userId ?: ""
        )
    }.sortedBy { it.title }
}

fun RecipeModel.mapToBasicRecipe(): BasicRecipe {
    return BasicRecipe(
        id,
        name,
        description,
        time.toString(),
        servings,
        difficulty?.name ?: "",
        category?.name ?: "",
        imagePath,
        getUser(user),
        ingredients,
        steps,
        isFavorite = isFavorite,
        userId = user?.userId ?: ""
    )
}

fun List<RecipeModel>.mapToTopRecipe(): List<TopRecipe> {
    return map { recipe ->
        TopRecipe(
            recipe.id,
            recipe.name,
            recipe.description,
            recipe.time.toString(),
            recipe.servings,
            recipe.difficulty?.name ?: "",
            recipe.category?.name ?: "",
            recipe.imagePath,
            getUser(recipe.user),
            recipe.isFavorite
        )
    }.sortedBy { it.title }
}

fun List<RecipeModel>.mapToTodayChoiceRecipe(): List<TodayChoiceRecipe> {
    return map { recipe ->
        TodayChoiceRecipe(
            recipe.id,
            recipe.name,
            recipe.description,
            recipe.time.toString(),
            recipe.servings,
            recipe.difficulty?.name ?: "",
            recipe.category?.name ?: "",
            recipe.imagePath,
            getUser(recipe.user),
            recipe.isFavorite
        )
    }.sortedBy { it.title }
}

fun RecipeModel.mapToRecipe(): Recipe {
    return Recipe(
        this.id,
        this.name,
        this.description,
        this.time,
        this.servings,
        this.steps.map { Step(it.position, it.description) }.sortedBy { it.position },
        this.ingredients.map { Ingredient(it.name, it.amount) },
        this.difficulty!!.id,
        this.category!!.id,
        this.imagePath,
        this.user!!.userId,
    )
}

fun Recipe.mapToRecipeModel(
    category: CategoryModel,
    difficulty: DifficultyModel,
    user: UserModel,
    isFavorite: Boolean
): RecipeModel {
    return RecipeModel(
        this.id,
        this.name,
        this.description,
        this.time,
        this.servings,
        this.steps.map { StepModel(it.position, it.description) }.toMutableList(),
        this.ingredients.map { IngredientModel(it.name, it.amount) }.toMutableList(),
        difficulty,
        category,
        user,
        this.imagePath,
        isFavorite
    )
}

fun Category.mapToCategoryModel(): CategoryModel {
    return CategoryModel(id, name.replaceFirstChar(Char::titlecase))
}

fun CategoryModel.mapToCategory(): Category {
    return Category(id, name.replaceFirstChar(Char::titlecase))
}

fun mapToDifficultyModels(data: List<Difficulty>): List<DifficultyModel> {
    val difficulties = mutableListOf<DifficultyModel>()

    data.forEach {
        difficulties.add(it.mapToDifficultyModel())
    }
    return difficulties.sortedBy { it.name }
}

fun Difficulty.mapToDifficultyModel(): DifficultyModel {
    return DifficultyModel(
        id,
        name.replaceFirstChar(Char::titlecase)
    )
}

fun DifficultyModel.mapToDifficulty(): Difficulty {
    return Difficulty(
        id,
        name.replaceFirstChar(Char::titlecase)
    )
}

fun getUser(user: UserModel?): String {
    return if (user != null) "By ${user.name}" else "By Anonymous"
}

fun getColorStateList(difficulty: String, resources: Resources): ColorStateList {
    return when (difficulty.lowercase()) {
        "easy" -> {
            ColorStateList.valueOf(
                ResourcesCompat.getColor(
                    resources,
                    R.color.color_difficulty_easy,
                    null
                )
            )
        }

        "medium" -> {
            ColorStateList.valueOf(
                ResourcesCompat.getColor(
                    resources,
                    R.color.color_difficulty_medium,
                    null
                )
            )
        }

        else -> {
            ColorStateList.valueOf(
                ResourcesCompat.getColor(
                    resources,
                    R.color.color_difficulty_hard,
                    null
                )
            )
        }
    }
}