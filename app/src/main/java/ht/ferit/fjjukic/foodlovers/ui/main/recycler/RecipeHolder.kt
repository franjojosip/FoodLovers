package ht.ferit.fjjukic.foodlovers.ui.main.recycler

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.data.model.RecipeModel
import ht.ferit.fjjukic.foodlovers.ui.common.ActionListener
import kotlinx.android.synthetic.main.recipe_list_item.view.*

class RecipeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(recipe: RecipeModel, onClickListener: ActionListener, context: Context) {
        itemView.tvRecipeTitle.text = recipe.title

        Glide.with(context).load(recipe.imagePath)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .placeholder(R.drawable.app_icon)
            .error(android.R.drawable.stat_notify_error)
            .into(itemView.ivRecipe)

        itemView.setOnLongClickListener {
            onClickListener.show(recipe.id, recipe.userId, recipe.imageId)
            false
        }
        itemView.setOnClickListener {
            onClickListener.checkRecipe(recipe.id)
        }
    }
}