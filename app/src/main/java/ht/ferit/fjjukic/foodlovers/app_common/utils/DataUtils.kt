package ht.ferit.fjjukic.foodlovers.app_common.utils

import android.content.res.Resources
import ht.ferit.fjjukic.foodlovers.app_common.model.CategoryModel
import ht.ferit.fjjukic.foodlovers.app_common.model.RecipeModel
import ht.ferit.fjjukic.foodlovers.app_common.model.UserModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.BasicRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.HomeScreenRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.RecipeCategory
import ht.ferit.fjjukic.foodlovers.app_recipe.model.TodayChoiceRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.TopRecipe

inline fun <reified T> mergeLists(vararg arrays: List<T>): List<T> {
    val list: MutableList<T> = ArrayList()
    for (array in arrays) {
        list.addAll(array.map { i -> i })
    }
    return list.toMutableList()
}

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()


fun List<CategoryModel>.mapToRecipeCategory(): List<RecipeCategory> {
    return map { category ->
        RecipeCategory(
            category.id,
            category.name,
            category.drawableId
        )
    }.sortedBy { it.title }
}

fun MutableList<RecipeModel>.mapToBasicRecipes(): List<HomeScreenRecipe> {
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
            true
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
        true
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
            recipe.imagePath,
            getUser(recipe.user)
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
            recipe.imagePath,
            getUser(recipe.user)
        )
    }.sortedBy { it.title }
}

fun getUser(user: UserModel?): String {
    return if (user != null) "By ${user.name}" else "By Anonymous"
}