package ht.ferit.fjjukic.foodlovers.app_recipe.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ht.ferit.fjjukic.foodlovers.app_common.live_data.SingleLiveData
import ht.ferit.fjjukic.foodlovers.app_common.model.ActionResultModel
import ht.ferit.fjjukic.foodlovers.app_common.model.RecipeModel
import ht.ferit.fjjukic.foodlovers.app_common.repository.recipe.RecipeRepository
import ht.ferit.fjjukic.foodlovers.app_common.listener.ResultListener
import ht.ferit.fjjukic.foodlovers.app_common.view_model.BaseViewModel

class ShowRecipeViewModel(
    private val recipeRepository: RecipeRepository
) : BaseViewModel() {
    private val _recipe: MutableLiveData<RecipeModel> = MutableLiveData()
    private val _resultModel = SingleLiveData<ActionResultModel>()

    val recipe: LiveData<RecipeModel> = _recipe

    val resultModel: LiveData<ActionResultModel> = _resultModel

    fun init(recipeID: String) {
        when {
            recipeID.isEmpty() -> {
                _resultModel.postValue(ActionResultModel(false))
            }
            else -> {
                loadRecipe(recipeID)
            }
        }
    }

    private fun loadRecipe(recipeID: String) {
        val listener = object: ResultListener {
            override fun <T> onSuccess(value: T) {
                value?.let {
                    if(value is RecipeModel){
                        _recipe.postValue(value!!)
                    }
                }
            }

            override fun onFailure(message: String) {
                _resultModel.postValue(ActionResultModel(false, message))
            }
        }

        recipeRepository.getRecipe(recipeID)
    }
}