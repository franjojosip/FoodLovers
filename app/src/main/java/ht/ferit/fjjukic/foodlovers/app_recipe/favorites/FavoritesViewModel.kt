package ht.ferit.fjjukic.foodlovers.app_recipe.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionNavigate
import ht.ferit.fjjukic.foodlovers.app_common.repository.MockRepository
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel
import ht.ferit.fjjukic.foodlovers.app_recipe.model.HomeScreenRecipe

class FavoritesViewModel : BaseViewModel() {
    private val _recipes: MutableLiveData<List<HomeScreenRecipe>> = MutableLiveData()
    val recipes: LiveData<List<HomeScreenRecipe>> = _recipes

    init {
        _recipes.value = MockRepository.getFavoriteRecipes()
    }

    fun onRecipeClick(id: String) {
        _actionNavigate.postValue(
            ActionNavigate.NavigationWithDirections(
                FavoritesFragmentDirections.actionNavGraphFavoritesToNavGraphShowRecipe(id)
            )
        )
    }
}