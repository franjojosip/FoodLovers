package ht.ferit.fjjukic.foodlovers.app_common.view

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.load.engine.Resource
import ht.ferit.fjjukic.foodlovers.R

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
                btnAction.backgroundTintList = ColorStateList.valueOf(ResourcesCompat.getColor(resources, R.color.color_search_text, null))
            }
            tvTitle.text = text
        } finally {
            ta.recycle()
        }
    }

    fun setTitle(title: String) {
        findViewById<TextView>(R.id.tv_title).text = title
    }

    fun setupAction(action: () -> Unit){
        findViewById<ImageView>(R.id.btn_action).setOnClickListener {
            action()
        }
    }

}