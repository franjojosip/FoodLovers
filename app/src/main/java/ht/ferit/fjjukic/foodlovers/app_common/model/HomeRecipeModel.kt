package ht.ferit.fjjukic.foodlovers.app_common.model

data class HomeRecipeModel(
    val recipeResultModel: RecipeResultModel,
    val foodTypes: List<BaseModel>,
    val difficultyLevels: List<BaseModel>
)
