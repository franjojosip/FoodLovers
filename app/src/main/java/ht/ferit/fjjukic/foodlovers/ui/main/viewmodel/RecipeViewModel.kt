package ht.ferit.fjjukic.foodlovers.ui.main.viewmodel

import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ht.ferit.fjjukic.foodlovers.data.model.DifficultyLevel
import ht.ferit.fjjukic.foodlovers.data.model.FoodType
import ht.ferit.fjjukic.foodlovers.data.model.Recipe
import ht.ferit.fjjukic.foodlovers.data.repository.DifficultyLevelRepository
import ht.ferit.fjjukic.foodlovers.data.repository.FoodTypeRepository
import ht.ferit.fjjukic.foodlovers.data.repository.RecipeRepository
import ht.ferit.fjjukic.foodlovers.utils.startMenuActivity

class RecipeViewModel(
    private val recipeRepository: RecipeRepository,
    private val foodTypeRepository: FoodTypeRepository,
    private val difficultyLevelRepository: DifficultyLevelRepository
) : ViewModel() {
    var recipe: MutableLiveData<Recipe> = MutableLiveData()
    var foodType: MutableLiveData<FoodType> = MutableLiveData()
    var foodTypes: MutableLiveData<List<FoodType>> = MutableLiveData()
    var difficultyLevel: MutableLiveData<DifficultyLevel> = MutableLiveData()
    var difficultyLevels: MutableLiveData<List<DifficultyLevel>> = MutableLiveData()
    var actionType: String = "Add"
    var recipeID: Int = -1

    fun getRecipe(recipeID: Int): LiveData<Recipe> {
        return recipeRepository.get(recipeID)
    }

    fun getRecipes(foodTypeID: Int): LiveData<List<Recipe>> {
        return recipeRepository.getAll(foodTypeID)
    }

    fun deleteRecipe(id: Int){
        recipeRepository.delete(id)
    }

    fun deleteAll(){
        recipeRepository.deleteAll()
        foodTypeRepository.deleteAll()
        difficultyLevelRepository.deleteAll()
    }

    fun getFoodType(id: Int): LiveData<FoodType> {
        return foodTypeRepository.get(id)
    }

    fun getFoodTypes(): LiveData<List<FoodType>> {
        return foodTypeRepository.getAll()
    }

    fun getDifficultyLevel(id: Int): LiveData<DifficultyLevel> {
        return difficultyLevelRepository.get(id)
    }

    fun getDifficultyLevels(): LiveData<List<DifficultyLevel>> {
        return difficultyLevelRepository.getAll()
    }

    fun modifyRecipe(view: View) {
        val currentRecipe = recipe.value
        when {
            currentRecipe!!.title.isNotEmpty() && currentRecipe.description.isNotEmpty() && currentRecipe.imagePath.isNotEmpty() && currentRecipe.typeID != -1 && currentRecipe.difficultyLevelID != -1 -> {
                when {
                    recipeID != -1 -> {
                        recipeRepository.update(currentRecipe)
                    }
                    else -> {
                        recipeRepository.insert(currentRecipe)
                    }
                }
                Toast.makeText(view.context, "Task successfull!", Toast.LENGTH_SHORT).show()
                view.context.startMenuActivity()
            }
            else -> Toast.makeText(view.context, "Please check all fields!", Toast.LENGTH_SHORT).show()
        }
    }

    fun setRecipe(recipe: Recipe) {
        this.recipe.value = recipe
        recipeID = recipe.id
    }

    fun setFoodType(foodType: FoodType){
        recipe.value!!.typeID = foodType.id
        this.foodType.value = foodType
    }

    fun setDifficultyLevel(difficultyLevel: DifficultyLevel){
        recipe.value!!.difficultyLevelID = difficultyLevel.id
        this.difficultyLevel.value = difficultyLevel
    }

}