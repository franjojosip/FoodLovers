package ht.ferit.fjjukic.foodlovers.ui.main.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.data.model.Recipe
import ht.ferit.fjjukic.foodlovers.ui.common.ActionListener

class RecipeAdapter(private val actionListener: ActionListener) :
    RecyclerView.Adapter<RecipeHolder>() {
    private val recipes: MutableList<Recipe> = mutableListOf()
    private var filteredRecipes: MutableList<Recipe> = mutableListOf()
    private val selectedLevel: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeHolder {
        val recipeView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_list_item, parent, false)
        return RecipeHolder(
            recipeView
        )
    }

    override fun getItemCount(): Int = filteredRecipes.size

    override fun onBindViewHolder(holder: RecipeHolder, position: Int) {
        val recipe = filteredRecipes[position]
        holder.bind(recipe, actionListener)
    }

    fun refreshData(recipes: MutableList<Recipe>) {
        this.recipes.clear()
        this.recipes.addAll(recipes)
        this.filteredRecipes.clear()
        this.filteredRecipes.addAll(recipes)
        this.notifyDataSetChanged()
    }

    fun filterData(id: Int){
        when(id){
            0 -> {
                this.filteredRecipes.clear()
                this.filteredRecipes.addAll(recipes)
            }
            else -> {
                this.filteredRecipes.clear()
                this.filteredRecipes.addAll(recipes.filter {
                    it.difficultyLevelID == id
                }.toMutableList())
            }
        }
        this.notifyDataSetChanged()
    }

}