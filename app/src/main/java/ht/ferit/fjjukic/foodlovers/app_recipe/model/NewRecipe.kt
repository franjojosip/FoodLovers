package ht.ferit.fjjukic.foodlovers.app_recipe.model

data class NewRecipe(
    var name: String? = null,
    var servings: Int = 1,
    var time: Int = 15,
    var imageURI: String = "",
    var ingredients: MutableList<IngredientUI> = mutableListOf()
)
