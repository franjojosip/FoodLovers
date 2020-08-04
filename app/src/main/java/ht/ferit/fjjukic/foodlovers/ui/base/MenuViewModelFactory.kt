package ht.ferit.fjjukic.foodlovers.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ht.ferit.fjjukic.foodlovers.data.repository.DifficultyLevelRepository
import ht.ferit.fjjukic.foodlovers.data.repository.FoodTypeRepository
import ht.ferit.fjjukic.foodlovers.ui.main.viewmodel.MenuViewModel

@Suppress("UNCHECKED_CAST")
class MenuViewModelFactory(
    private val repository: FoodTypeRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MenuViewModel(repository) as T
    }

}