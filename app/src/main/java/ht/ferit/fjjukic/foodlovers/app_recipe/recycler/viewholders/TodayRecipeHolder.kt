package ht.ferit.fjjukic.foodlovers.app_recipe.recycler.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ht.ferit.fjjukic.foodlovers.app_common.utils.convertToTime
import ht.ferit.fjjukic.foodlovers.app_recipe.HomeListener
import ht.ferit.fjjukic.foodlovers.app_recipe.model.TodayChoiceRecipe
import ht.ferit.fjjukic.foodlovers.databinding.RecipeListItemBinding

class TodayRecipeHolder(private val binding: RecipeListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun setData(recipe: TodayChoiceRecipe, listener: HomeListener?) {
        binding.tvTitle.text = recipe.title
        binding.tvDescription.text = recipe.description
        binding.tvTime.text = recipe.time.toInt().convertToTime()
        binding.tvDifficulty.text = recipe.difficulty

        Glide.with(binding.root)
            .load(recipe.imagePath)
            .into(binding.ivRecipe)

        binding.root.setOnClickListener {
            listener?.onRecipeClick(recipe.id)
        }
    }
}