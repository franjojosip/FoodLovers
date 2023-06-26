package ht.ferit.fjjukic.foodlovers.app_recipe.recycler.viewholders

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ht.ferit.fjjukic.foodlovers.app_recipe.HomeListener
import ht.ferit.fjjukic.foodlovers.app_recipe.model.Category
import ht.ferit.fjjukic.foodlovers.databinding.CategoryItemBinding

class CategoryViewHolder(private val binding: CategoryItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun setData(data: Category, listener: HomeListener?) {
        binding.tvTitle.text = data.title

        binding.spaceStart.isVisible = data.hasMarginStart

        Glide.with(binding.root)
            .load(data.drawableId)
            .into(binding.ivImage)

        binding.root.setOnClickListener {
            listener?.onCategoryClick(data.title)
        }
    }
}