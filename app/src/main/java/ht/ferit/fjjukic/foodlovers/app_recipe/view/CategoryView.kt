package ht.ferit.fjjukic.foodlovers.app_recipe.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import ht.ferit.fjjukic.foodlovers.databinding.SearchViewBinding

class  CategoryView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: SearchViewBinding =
        SearchViewBinding.inflate(LayoutInflater.from(context), this, true)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }
}
