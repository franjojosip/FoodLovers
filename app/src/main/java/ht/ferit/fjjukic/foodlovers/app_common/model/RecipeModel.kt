package ht.ferit.fjjukic.foodlovers.app_common.model

import ht.ferit.fjjukic.foodlovers.app_recipe.model.Ingredient
import ht.ferit.fjjukic.foodlovers.app_recipe.model.Step

class RecipeModel(
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var time: Int = 15,
    var servings: Int = 1,
    var steps: MutableList<Step> = mutableListOf(),
    var ingredients: MutableList<Ingredient> = mutableListOf(),
    var difficulty: DifficultyModel,
    var category: CategoryModel,
    var user: UserModel,
    var imagePath: String = "",
)