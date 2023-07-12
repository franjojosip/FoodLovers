package ht.ferit.fjjukic.foodlovers.app_recipe.recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ht.ferit.fjjukic.foodlovers.app_recipe.home.HomeListener
import ht.ferit.fjjukic.foodlovers.app_recipe.model.*
import ht.ferit.fjjukic.foodlovers.app_recipe.recycler.viewholders.*
import ht.ferit.fjjukic.foodlovers.databinding.*

class RecipeAdapter(val listener: HomeListener? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data: MutableList<HomeScreenRecipe> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> TodayRecipeHolder(
                ItemTodayChoiceBinding.inflate(layoutInflater, parent, false)
            )

            1 -> TopRecipeViewHolder(
                ItemTopRecipeBinding.inflate(layoutInflater, parent, false)
            )

            2 -> NoRecipesViewHolder(
                LayoutNoRecipesPlaceholderBinding.inflate(layoutInflater, parent, false)
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
                holder.setData(data[position] as TodayChoiceRecipe, listener)
            }

            is TopRecipeViewHolder -> {
                holder.setData(data[position] as TopRecipe, listener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            data[position] is TodayChoiceRecipe -> 0
            data[position] is TopRecipe -> 1
            data[position] is NoRecipePlaceholder -> 2
            else -> throw Exception("Class doesn't exist")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<HomeScreenRecipe>) {
        this.data.apply {
            clear()
            addAll(data)
        }
        notifyDataSetChanged()
    }
}