package ht.ferit.fjjukic.foodlovers.app_common.view

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.databinding.CustomActionBarBinding
import ht.ferit.fjjukic.foodlovers.databinding.SearchViewBinding

class CustomToolbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.toolbar_layout, this)

        val btnAction = findViewById<ImageView>(R.id.btn_action)
        val tvTitle = findViewById<TextView>(R.id.tv_title)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.CustomToolbarView)
        try {
            val text = ta.getString(R.styleable.CustomToolbarView_text)
            val drawableId = ta.getResourceId(R.styleable.CustomToolbarView_image, 0)
            if (drawableId != 0) {
                val drawable = AppCompatResources.getDrawable(context, drawableId)
                btnAction.setImageDrawable(drawable)
                btnAction.backgroundTintList = ColorStateList.valueOf(R.color.color_search_text)
            }
            tvTitle.text = text
        } finally {
            ta.recycle()
        }
    }

    fun setupAction(action: () -> Unit){
        findViewById<ImageView>(R.id.btn_action).setOnClickListener {
            action()
        }
    }

}