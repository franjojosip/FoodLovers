package ht.ferit.fjjukic.foodlovers.app_recipe.showrecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionBack
import ht.ferit.fjjukic.foodlovers.app_common.model.LoadingBar
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepository
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.NewRecipe

class ShowRecipeViewModel(
    private val recipeRepository: RecipeRepository
) : BaseViewModel() {

    private val _recipe: MutableLiveData<NewRecipe> = MutableLiveData()
    val recipe: LiveData<NewRecipe> = _recipe

    fun loadRecipe(recipeID: String) {
        // Load recipe from repository
        _screenEvent.postValue(LoadingBar(false))
        //_screenEvent.postValue(ActionBack)
    }
}