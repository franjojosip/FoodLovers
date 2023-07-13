package ht.ferit.fjjukic.foodlovers.app_recipe.recycler.viewholders

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_recipe.CategoryListener
import ht.ferit.fjjukic.foodlovers.app_recipe.model.RecipeCategory
import ht.ferit.fjjukic.foodlovers.databinding.ItemCategoryBinding

class CategoryViewHolder(private val binding: ItemCategoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun setData(data: RecipeCategory, listener: CategoryListener?) {
        binding.tvTitle.text = data.title

        binding.spaceStart.isVisible = data.hasMarginStart
        val drawable = binding.root.context.getResource("background_${data.title.lowercase()}")

        Glide.with(binding.root)
            .load(drawable)
            .placeholder(R.drawable.background_placeholder)
            .into(binding.ivImage)

        binding.root.setOnClickListener {
            listener?.onCategoryClick(data.title)
        }
    }
}

@SuppressLint("DiscouragedApi")
fun Context.getResource(name: String): Drawable? {
    val resID = this.resources.getIdentifier(name, "drawable", this.packageName)
    return ActivityCompat.getDrawable(this, resID)
}