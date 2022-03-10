package ht.ferit.fjjukic.foodlovers.app_recipe.recycler.viewholders

import androidx.recyclerview.widget.RecyclerView
import ht.ferit.fjjukic.foodlovers.app_recipe.model.Category
import ht.ferit.fjjukic.foodlovers.databinding.CategoryItemBinding

class CategoryViewHolder(private val binding: CategoryItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun setData(recipe: Category) {
        binding.tvTitle.text = recipe.title
    }
}