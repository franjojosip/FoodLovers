package ht.ferit.fjjukic.foodlovers.app_recipe.recycler.viewholders

import android.content.res.ColorStateList
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.utils.convertToTime
import ht.ferit.fjjukic.foodlovers.app_recipe.HomeListener
import ht.ferit.fjjukic.foodlovers.app_recipe.model.TopRecipe
import ht.ferit.fjjukic.foodlovers.databinding.TopRecipeItemBinding

class TopRecipeViewHolder(private val binding: TopRecipeItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun setData(recipe: TopRecipe, listener: HomeListener?) {
        binding.tvTitle.text = recipe.title
        binding.tvDescription.text = recipe.description
        binding.tvTime.text = recipe.time.toInt().convertToTime()
        binding.tvDifficulty.text = recipe.difficulty

        binding.ivFavorite.isVisible = recipe.isFavorite

        when (recipe.difficulty.lowercase()) {
            "easy" -> {
                binding.ivDifficulty.backgroundTintList = ColorStateList.valueOf(
                    ResourcesCompat.getColor(
                        binding.root.resources,
                        R.color.easy_difficulty_color,
                        null
                    )
                )
            }

            "medium" -> {
                binding.ivDifficulty.backgroundTintList = ColorStateList.valueOf(
                    ResourcesCompat.getColor(
                        binding.root.resources,
                        R.color.medium_difficulty_color,
                        null
                    )
                )
            }

            else -> {
                binding.ivDifficulty.backgroundTintList = ColorStateList.valueOf(
                    ResourcesCompat.getColor(
                        binding.root.resources,
                        R.color.hard_difficulty_color,
                        null
                    )
                )
            }
        }

        Glide.with(binding.root)
            .load(recipe.imagePath)
            .placeholder(R.drawable.image_placeholder)
            .into(binding.ivImage)

        binding.root.setOnClickListener {
            listener?.onRecipeClick(recipe.id)
        }
    }
}