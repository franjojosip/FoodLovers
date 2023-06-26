package ht.ferit.fjjukic.foodlovers.app_recipe.showrecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ht.ferit.fjjukic.foodlovers.app_common.model.LoadingBar
import ht.ferit.fjjukic.foodlovers.app_common.repository.MockRepository
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepository
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.BasicRecipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShowRecipeViewModel(
    private val recipeRepository: RecipeRepository
) : BaseViewModel() {

    private val _recipe: MutableLiveData<BasicRecipe> = MutableLiveData()
    val recipe: LiveData<BasicRecipe> = _recipe

    fun loadRecipe(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _screenEvent.postValue(LoadingBar(true))
            //_recipe.value = recipeRepository.getRecipe(id)
            _recipe.postValue(MockRepository.getRecipeByID(id))
            _screenEvent.postValue(LoadingBar(false))
        }
    }
}