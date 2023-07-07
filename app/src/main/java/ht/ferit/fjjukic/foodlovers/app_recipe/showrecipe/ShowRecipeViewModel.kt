package ht.ferit.fjjukic.foodlovers.app_recipe.showrecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.model.SnackbarModel
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepository
import ht.ferit.fjjukic.foodlovers.app_common.utils.mapToBasicRecipe
import ht.ferit.fjjukic.foodlovers.app_common.viewmodel.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.BasicRecipe

class ShowRecipeViewModel(
    private val recipeRepository: RecipeRepository
) : BaseViewModel() {

    private val _recipe: MutableLiveData<BasicRecipe> = MutableLiveData()
    val recipe: LiveData<BasicRecipe> = _recipe

    fun loadRecipe(id: String) {
        handleResult({
            recipeRepository.getRecipe(id)
        }, {
            if (it != null) {
                _recipe.postValue(it.mapToBasicRecipe())
            } else {
                actionNavigate.postValue(ActionNavigate.Back)
            }
        }, {
            screenEvent.postValue(
                SnackbarModel(message = "Error while loading recipe")
            )
            actionNavigate.postValue(ActionNavigate.Back)
        })
    }
}