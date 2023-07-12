package ht.ferit.fjjukic.foodlovers.app_recipe.recycler.viewholders

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.utils.convertToTime
import ht.ferit.fjjukic.foodlovers.app_common.utils.getColorStateList
import ht.ferit.fjjukic.foodlovers.app_recipe.home.HomeListener
import ht.ferit.fjjukic.foodlovers.app_recipe.model.TopRecipe
import ht.ferit.fjjukic.foodlovers.databinding.ItemTopRecipeBinding

class TopRecipeViewHolder(private val binding: ItemTopRecipeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun setData(recipe: TopRecipe, listener: HomeListener?) {
        binding.tvTitle.text = recipe.title
        binding.tvDescription.text = recipe.description
        binding.tvTime.text = recipe.time.toInt().convertToTime()
        binding.tvDifficulty.text = recipe.difficulty
        binding.ivDifficulty.backgroundTintList =
            getColorStateList(recipe.difficulty, binding.root.resources)

        binding.ivFavorite.isVisible = recipe.isFavorite

        Glide.with(binding.root)
            .load(recipe.imagePath)
            .placeholder(R.drawable.background_placeholder)
            .into(binding.ivImage)

        binding.root.setOnClickListener {
            listener?.onRecipeClick(recipe.id)
        }
    }
}