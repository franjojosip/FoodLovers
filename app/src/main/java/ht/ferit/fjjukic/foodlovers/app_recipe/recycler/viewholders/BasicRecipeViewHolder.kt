package ht.ferit.fjjukic.foodlovers.app_recipe.recycler.viewholders

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.utils.convertToServings
import ht.ferit.fjjukic.foodlovers.app_common.utils.convertToTime
import ht.ferit.fjjukic.foodlovers.app_recipe.RecipeListener
import ht.ferit.fjjukic.foodlovers.app_recipe.model.BasicRecipe
import ht.ferit.fjjukic.foodlovers.databinding.SearchRecipeItemBinding

class BasicRecipeViewHolder(private val binding: SearchRecipeItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun setData(recipe: BasicRecipe, listener: RecipeListener?) {
        binding.tvTitle.text = recipe.title
        binding.tvTime.text = recipe.time.toInt().convertToTime()
        binding.tvRecipeAuthor.text = recipe.user

        binding.tvServings.text = recipe.servings.convertToServings()

        binding.ivFavorite.isVisible = recipe.isFavorite

        Glide.with(binding.root)
            .load(recipe.imagePath)
            .placeholder(R.drawable.image_placeholder)
            .into(binding.ivRecipe)

        binding.root.setOnClickListener {
            listener?.onRecipeClick(recipe.id)
        }
    }
}