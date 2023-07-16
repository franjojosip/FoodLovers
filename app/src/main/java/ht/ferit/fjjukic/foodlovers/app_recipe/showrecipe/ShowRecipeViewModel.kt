package ht.ferit.fjjukic.foodlovers.app_recipe.showrecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ht.ferit.fjjukic.foodlovers.app_common.base.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_common.firebase.AnalyticsProvider
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepository
import ht.ferit.fjjukic.foodlovers.app_common.shared_preferences.PreferenceManager
import ht.ferit.fjjukic.foodlovers.app_common.utils.mapToBasicRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.BasicRecipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ShowRecipeViewModel(
    private val preferenceManager: PreferenceManager,
    private val recipeRepository: RecipeRepository,
    private val analyticsProvider: AnalyticsProvider,
    private val recipeId: String
) : BaseViewModel(analyticsProvider) {

    private val _recipe: MutableLiveData<BasicRecipe> = MutableLiveData()
    val recipe: LiveData<BasicRecipe> = _recipe

    fun init() {
        handleResult({
            recipeRepository.getRecipe(recipeId)
        }, {
            if (it != null) {
                _recipe.postValue(it.mapToBasicRecipe())
            } else {
                actionNavigate.postValue(ActionNavigate.Back)
            }
        }, {
            showSnackbar(it?.message)
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
        }, {
            showSnackbar(it?.message)
        }, showLoading = false)
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

    fun logShowRecipeScreenEvent() {
        analyticsProvider.logShowRecipeScreenEvent(recipeId)
    }
}