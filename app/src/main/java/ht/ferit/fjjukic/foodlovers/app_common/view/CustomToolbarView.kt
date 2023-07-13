package ht.ferit.fjjukic.foodlovers.app_common.view

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.databinding.LayoutToolbarBinding

class CustomToolbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var binding: LayoutToolbarBinding

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        binding = LayoutToolbarBinding.inflate(LayoutInflater.from(context), this, true)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.CustomToolbarView)
        try {
            val text = ta.getString(R.styleable.CustomToolbarView_text)
            val drawableId = ta.getResourceId(R.styleable.CustomToolbarView_image, 0)
            if (drawableId != 0) {
                val drawable = AppCompatResources.getDrawable(context, drawableId)
                binding.btnAction.setImageDrawable(drawable)
            }
            binding.btnAction.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.color_search_text
                )
            )
            binding.tvTitle.text = text
        } finally {
            ta.recycle()
        }

        binding.btnEndAction.setOnClickListener {
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
        binding.tvTitle.text = title
    }

    fun setupAction(action: () -> Unit) {
        binding.btnAction.setOnClickListener {
            action()
        }
    }

    fun enableEndAction() {
        binding.btnEndAction.isVisible = true
    }

}