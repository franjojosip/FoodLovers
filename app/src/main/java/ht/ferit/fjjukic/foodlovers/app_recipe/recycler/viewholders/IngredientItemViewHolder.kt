package ht.ferit.fjjukic.foodlovers.app_recipe.recycler.viewholders

import androidx.recyclerview.widget.RecyclerView
import ht.ferit.fjjukic.foodlovers.app_recipe.model.Ingredient
import ht.ferit.fjjukic.foodlovers.databinding.ItemIngredientListItemBinding

class IngredientItemViewHolder(private val binding: ItemIngredientListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun setData(ingredient: Ingredient) {
        binding.tvIngredientName.text = "${ingredient.amount} ${ingredient.name}"
    }
}