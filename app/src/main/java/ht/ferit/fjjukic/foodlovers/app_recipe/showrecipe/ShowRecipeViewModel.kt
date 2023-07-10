package ht.ferit.fjjukic.foodlovers.app_recipe.showrecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.model.SnackbarModel
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepository
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_common.utils.mapToBasicRecipe
import ht.ferit.fjjukic.foodlovers.app_common.viewmodel.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.BasicRecipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ShowRecipeViewModel(
    private val preferenceManager: PreferenceManager,
    private val recipeRepository: RecipeRepository
) : BaseViewModel() {

    private val _recipe: MutableLiveData<BasicRecipe> = MutableLiveData()
    val recipe: LiveData<BasicRecipe> = _recipe

    private var recipeId = ""

    fun loadRecipe(id: String) {
        recipeId = id

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

    fun onFavoriteClick() {
        val recipe = _recipe.value ?: return

        recipe.isFavorite = !recipe.isFavorite

        handleResult({
            recipeRepository.updateRecipeFavorite(recipe.id, recipe.isFavorite)
        }, {
            withContext(Dispatchers.Main) {
                _recipe.value = recipe
            }
        }, {}, showLoading = false)
    }

    fun onEditClick() {
        actionNavigate.postValue(
            ActionNavigate.NavigationWithDirections(
                ShowRecipeFragmentDirections.actionNavShowRecipeToNavEditRecipe(recipeId)
            )
        )
    }

    fun getUserId(): String? {
        return preferenceManager.user?.userId
    }

    fun isAdmin(): Boolean {
        return preferenceManager.user?.admin ?: false
    }
}