package ht.ferit.fjjukic.foodlovers.app_recipe.showrecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ht.ferit.fjjukic.foodlovers.app_common.model.LoadingBar
import ht.ferit.fjjukic.foodlovers.app_common.repository.MockRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepository
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.BasicRecipe

class ShowRecipeViewModel(
    private val recipeRepository: RecipeRepository
) : BaseViewModel() {

    private val _recipe: MutableLiveData<BasicRecipe> = MutableLiveData()
    val recipe: LiveData<BasicRecipe> = _recipe

    fun loadRecipe(id: String) {
        _screenEvent.postValue(LoadingBar(true))
        _recipe.value = MockRepository.getRecipeByID(id)
        _screenEvent.postValue(LoadingBar(false))
    }
}