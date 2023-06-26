package ht.ferit.fjjukic.foodlovers.app_recipe.recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ht.ferit.fjjukic.foodlovers.app_recipe.RecipeListener
import ht.ferit.fjjukic.foodlovers.app_recipe.model.BasicRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.model.HomeScreenRecipe
import ht.ferit.fjjukic.foodlovers.app_recipe.recycler.viewholders.BasicRecipeViewHolder
import ht.ferit.fjjukic.foodlovers.databinding.SearchRecipeItemBinding

class BasicRecipesAdapter(val listener: RecipeListener? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data: MutableList<HomeScreenRecipe> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> BasicRecipeViewHolder(
                SearchRecipeItemBinding.inflate(layoutInflater, parent, false)
            )

            else -> throw Exception("View type doesn't exist")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BasicRecipeViewHolder -> {
                holder.setData(data[position] as BasicRecipe, listener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            data[position] is BasicRecipe -> 0
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