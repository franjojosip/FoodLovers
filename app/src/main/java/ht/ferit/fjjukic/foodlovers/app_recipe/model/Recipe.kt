package ht.ferit.fjjukic.foodlovers.app_recipe.model

import java.util.UUID

data class Recipe(
    var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var servings: Int = 1,
    var author: String = "",
    var time: Int = 15,
    var imagePath: String = "",
    var ingredients: MutableList<Ingredient> = mutableListOf(),
    var steps: MutableList<Step> = mutableListOf()
)