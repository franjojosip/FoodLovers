package ht.ferit.fjjukic.foodlovers.app_recipe.recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ht.ferit.fjjukic.foodlovers.app_recipe.model.*
import ht.ferit.fjjukic.foodlovers.app_recipe.recycler.viewholders.CategoryViewHolder
import ht.ferit.fjjukic.foodlovers.app_recipe.recycler.viewholders.TodayRecipeHolder
import ht.ferit.fjjukic.foodlovers.app_recipe.recycler.viewholders.TopRecipeViewHolder
import ht.ferit.fjjukic.foodlovers.app_recipe.recycler.viewholders.NoRecipesViewHolder
import ht.ferit.fjjukic.foodlovers.databinding.CategoryItemBinding
import ht.ferit.fjjukic.foodlovers.databinding.NoRecipesPlaceholderBinding
import ht.ferit.fjjukic.foodlovers.databinding.RecipeListItemBinding
import ht.ferit.fjjukic.foodlovers.databinding.TopRecipeItemBinding

class RecipeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data: MutableList<HomeScreenRecipe> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> TodayRecipeHolder(
                RecipeListItemBinding.inflate(layoutInflater, parent, false)
            )
            1 -> TopRecipeViewHolder(
                TopRecipeItemBinding.inflate(layoutInflater, parent, false)
            )
            2 -> CategoryViewHolder(
                CategoryItemBinding.inflate(layoutInflater, parent, false)
            )
            3 -> NoRecipesViewHolder(
                NoRecipesPlaceholderBinding.inflate(layoutInflater, parent, false)
            )
            else -> throw Exception("View type doesn't exist")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TodayRecipeHolder -> {
                holder.setData(data[position] as TodayChoiceRecipe)
            }
            is TopRecipeViewHolder -> {
                holder.setData(data[position] as TopRecipe)
            }
            is CategoryViewHolder -> {
                holder.setData(data[position] as Category)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            data[position] is TodayChoiceRecipe -> 0
            data[position] is TopRecipe -> 1
            data[position] is Category -> 2
            data[position] is NoRecipePlaceholder -> 3
            else -> throw Exception("Class doesn't exist")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: MutableList<HomeScreenRecipe>) {
        this.data.apply {
            clear()
            addAll(data)
        }
        notifyDataSetChanged()
    }
}