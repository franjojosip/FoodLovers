package ht.ferit.fjjukic.foodlovers.app_common.view

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import ht.ferit.fjjukic.foodlovers.R


class CustomToolbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.layout_toolbar, this)

        val btnAction = findViewById<ImageView>(R.id.btn_action)
        val btnEndAction = findViewById<ImageView>(R.id.btn_end_action)
        val tvTitle = findViewById<TextView>(R.id.tv_title)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.CustomToolbarView)
        try {
            val text = ta.getString(R.styleable.CustomToolbarView_text)
            //val actionColor = ta.getResourceId(R.styleable.CustomToolbarView_actionColor, ContextCompat.getColor(context, R.color.color_search_text))
            val drawableId = ta.getResourceId(R.styleable.CustomToolbarView_image, 0)
            if (drawableId != 0) {
                val drawable = AppCompatResources.getDrawable(context, drawableId)
                btnAction.setImageDrawable(drawable)
            }
            btnAction.setColorFilter(ContextCompat.getColor(context, R.color.color_search_text))
            tvTitle.text = text
        } finally {
            ta.recycle()
        }

        btnEndAction.setOnClickListener {
            /*Create an ACTION_SEND Intent*/
            val intent = Intent(Intent.ACTION_SEND)
            /*This will be the actual content you wish you share.*/
            val shareBody = "Recipe deeplink example"
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_SUBJECT,
                "Share a recipe"
            )
            intent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(context, Intent.createChooser(intent, "Share a recipe"), null)
        }
    }

    fun setTitle(title: String) {
        findViewById<TextView>(R.id.tv_title).text = title
    }

    fun setupAction(action: () -> Unit) {
        findViewById<ImageView>(R.id.btn_action).setOnClickListener {
            action()
        }
    }

    fun enableEndAction() {
        findViewById<ImageView>(R.id.btn_end_action).isVisible = true
    }

}