package ht.ferit.fjjukic.foodlovers.ui.main.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ht.ferit.fjjukic.foodlovers.data.PreferenceManager
import ht.ferit.fjjukic.foodlovers.data.model.DifficultyLevel
import ht.ferit.fjjukic.foodlovers.data.model.FoodType
import ht.ferit.fjjukic.foodlovers.data.repository.DifficultyLevelRepository
import ht.ferit.fjjukic.foodlovers.data.repository.FoodTypeRepository
import ht.ferit.fjjukic.foodlovers.utils.startNavigationActivity

class MenuViewModel(
    private val repository: FoodTypeRepository
) : ViewModel() {
    var foodTypes: MutableLiveData<List<FoodType>> = MutableLiveData<List<FoodType>>()

    fun getFoodTypes(): LiveData<List<FoodType>> {
        return repository.getAll()
    }

    fun goToRecipes(view: View, id: Int) {
        when (id) {
            0 -> {
                PreferenceManager().setFoodTypeID(foodTypes.value!![0].id)
            }
            else -> {
                PreferenceManager().setFoodTypeID(foodTypes.value!![1].id)
            }
        }
        view.context.startNavigationActivity()
    }
}