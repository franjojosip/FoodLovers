package ht.ferit.fjjukic.foodlovers.ui.main.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.data.PreferenceManager
import ht.ferit.fjjukic.foodlovers.data.model.RecipeModel
import ht.ferit.fjjukic.foodlovers.ui.common.ActionListener

class RecipeAdapter(private val actionListener: ActionListener) :
    RecyclerView.Adapter<RecipeHolder>() {
    private val recipes: MutableList<RecipeModel> = mutableListOf()
    private var filteredRecipes: MutableList<RecipeModel> = mutableListOf()
    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeHolder {
        val recipeView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_list_item, parent, false)
        context = parent.context
        return RecipeHolder(
            recipeView
        )
    }

    override fun getItemCount(): Int = filteredRecipes.size

    override fun onBindViewHolder(holder: RecipeHolder, position: Int) {
        val recipe = filteredRecipes[position]
        holder.bind(recipe, actionListener, context!!)
    }

    fun refreshData(recipes: MutableList<RecipeModel>) {
        this.recipes.clear()
        this.recipes.addAll(recipes)
        filterData()
    }

    fun filterData(phrase: String = "") {
        val difficultyLevel = PreferenceManager().getDifficultyLevelID()
        val foodType = PreferenceManager().getFoodTypeID()

        this.filteredRecipes.clear()
        this.filteredRecipes.addAll(recipes)

        if (difficultyLevel != "0") {
            this.filteredRecipes.removeAll {
                it.difficultyLevelID != difficultyLevel
            }
        }
        if (foodType != "0") {
            this.filteredRecipes.removeAll {
                it.foodTypeID != foodType
            }

        }
        if (phrase.isNotEmpty()) {
            this.filteredRecipes.removeAll {
                !it.title.contains(phrase, true)
            }
        }
        this.notifyDataSetChanged()
    }

}