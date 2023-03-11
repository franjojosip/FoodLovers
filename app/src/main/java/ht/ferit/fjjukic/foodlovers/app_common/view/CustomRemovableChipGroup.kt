package ht.ferit.fjjukic.foodlovers.app_common.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.core.view.children
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_recipe.model.FilterItem

class CustomRemovableChipGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ChipGroup(context, attrs, defStyleAttr) {

    interface RemovableClickListener {
        fun onItemClicked(item: FilterItem)
    }

    private var listener: RemovableClickListener? = null

    fun setData(items: List<FilterItem>) {
        removeAllViews()
        items.forEach { item ->
            addChip(item)
        }
    }

    @SuppressLint("InflateParams")
    private fun addChip(item: FilterItem) {
        val chip = LayoutInflater.from(context).inflate(R.layout.search_filter_chip, null) as Chip
        chip.id = View.generateViewId()
        chip.text = item.value

        chip.setOnClickListener {
            val anim = AlphaAnimation(1f,0f)
            anim.duration = 250
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    removeView(it)
                    listener?.onItemClicked(item)
                }

                override fun onAnimationStart(animation: Animation?) {}
            })

            it.startAnimation(anim)
        }
        addView(chip)
    }

    fun setListener(listener: RemovableClickListener) {
        this.listener = listener
    }
}