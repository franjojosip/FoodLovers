package ht.ferit.fjjukic.foodlovers.app_recipe.recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ht.ferit.fjjukic.foodlovers.app_recipe.CategoryListener
import ht.ferit.fjjukic.foodlovers.app_recipe.model.*
import ht.ferit.fjjukic.foodlovers.app_recipe.recycler.viewholders.*
import ht.ferit.fjjukic.foodlovers.databinding.*

class CategoryAdapter : RecyclerView.Adapter<CategoryViewHolder>() {
    private val data: MutableList<RecipeCategory> = mutableListOf()
    private var listener: CategoryListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.setData(data[position], listener)
    }

    fun setListener(listener: CategoryListener) {
        this.listener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<RecipeCategory>) {
        this.data.apply {
            clear()
            addAll(data)
        }
        notifyDataSetChanged()
    }
}