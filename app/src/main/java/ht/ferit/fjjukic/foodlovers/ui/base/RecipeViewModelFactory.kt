package ht.ferit.fjjukic.foodlovers.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ht.ferit.fjjukic.foodlovers.data.repository.RecipeRepository
import ht.ferit.fjjukic.foodlovers.ui.main.viewmodel.RecipeViewModel

@Suppress("UNCHECKED_CAST")
class RecipeViewModelFactory(
    private val recipeRepository: RecipeRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RecipeViewModel(recipeRepository) as T
    }

}