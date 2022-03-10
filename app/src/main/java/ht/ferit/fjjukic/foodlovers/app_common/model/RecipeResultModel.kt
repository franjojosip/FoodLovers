package ht.ferit.fjjukic.foodlovers.app_common.model

data class RecipeResultModel(
    val phrase: String? = "",
    val data: MutableList<RecipeModel>,
    val difficultyLevelID: String? = "",
    val foodTypeID: String? = ""
)
