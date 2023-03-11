package ht.ferit.fjjukic.foodlovers.app_recipe.model

data class NewRecipe(
    var id: String = "-1",
    var name: String = "",
    var servings: Int = 1,
    var author: String = "",
    var time: Int = 15,
    var imageURI: String = "",
    var ingredients: MutableList<IngredientUI> = mutableListOf(),
    var steps: MutableList<StepUI> = mutableListOf()
)
