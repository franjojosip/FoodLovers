package ht.ferit.fjjukic.foodlovers.app_recipe.recycler.viewholders

import androidx.recyclerview.widget.RecyclerView
import ht.ferit.fjjukic.foodlovers.app_recipe.model.TodayChoiceRecipe
import ht.ferit.fjjukic.foodlovers.databinding.RecipeListItemBinding

class TodayRecipeHolder(private val binding: RecipeListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun setData(recipe: TodayChoiceRecipe) {
        binding.tvTitle.text = recipe.title
        binding.tvDescription.text = recipe.description
        binding.tvTime.text = recipe.time
        binding.tvDifficulty.text = recipe.difficulty
    }
}