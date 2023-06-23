package ht.ferit.fjjukic.foodlovers.app_recipe.model

class Step(
    var position: Int,
    description: String
) : HomeScreenRecipe(description = description) {
    constructor() : this(0, "")
}
