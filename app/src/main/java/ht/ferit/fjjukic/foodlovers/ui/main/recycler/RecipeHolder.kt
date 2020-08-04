package ht.ferit.fjjukic.foodlovers.ui.main.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.data.model.Recipe
import ht.ferit.fjjukic.foodlovers.ui.common.ActionListener
import kotlinx.android.synthetic.main.recipe_list_item.view.*

class RecipeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(recipe: Recipe, onClickListener: ActionListener) {
        itemView.tvRecipeTitle.text = recipe.title
        Picasso.get()
            .load(recipe.imagePath)
            .fit()
            .placeholder(R.drawable.app_icon)
            .error(android.R.drawable.stat_notify_error)
            .into(itemView.ivRecipe)

        itemView.setOnLongClickListener {
            onClickListener.show(recipe.id)
            false
        }
        itemView.setOnClickListener {
            onClickListener.checkRecipe(recipe.id)
        }
    }
}