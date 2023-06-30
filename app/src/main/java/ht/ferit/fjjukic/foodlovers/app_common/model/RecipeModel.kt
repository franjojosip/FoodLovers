package ht.ferit.fjjukic.foodlovers.app_common.model

class RecipeModel(
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var time: Int = 15,
    var servings: Int = 1,
    var steps: MutableList<StepModel> = mutableListOf(),
    var ingredients: MutableList<IngredientModel> = mutableListOf(),
    var difficulty: DifficultyModel? = null,
    var category: CategoryModel? = null,
    var user: UserModel? = null,
    var imagePath: String = "",
)