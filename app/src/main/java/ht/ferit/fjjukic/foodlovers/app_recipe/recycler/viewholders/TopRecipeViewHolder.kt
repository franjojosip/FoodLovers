package ht.ferit.fjjukic.foodlovers.app_recipe.recycler.viewholders

import androidx.recyclerview.widget.RecyclerView
import ht.ferit.fjjukic.foodlovers.app_recipe.model.TopRecipe
import ht.ferit.fjjukic.foodlovers.databinding.TopRecipeItemBinding

class TopRecipeViewHolder(private val binding: TopRecipeItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun setData(recipe: TopRecipe) {
        binding.tvTitle.text = recipe.title
        binding.tvUser.text = recipe.user
        binding.tvTime.text = recipe.time
        binding.tvDifficulty.text = recipe.difficulty
    }
}