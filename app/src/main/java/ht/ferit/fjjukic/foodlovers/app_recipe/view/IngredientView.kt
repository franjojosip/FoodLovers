package ht.ferit.fjjukic.foodlovers.app_recipe.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_recipe.model.IngredientUI
import ht.ferit.fjjukic.foodlovers.databinding.IngredientItemBinding

class IngredientView @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet? = null,
    val defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = IngredientItemBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        setupAttributes()
    }

    private fun setupAttributes() {
        context.obtainStyledAttributes(attrs, R.styleable.IngredientView, defStyleAttr, 0).apply {
            toggleCloseIcon(
                this.getBoolean(R.styleable.IngredientView_has_close_action, false)
            )
        }.recycle()
    }

    fun toggleCloseIcon(isVisible: Boolean) {
        binding.ivClose.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

    fun setListener(listener: OnClickListener) {
        binding.ivClose.setOnClickListener {
            listener.onClick(this)
        }
    }

    fun getData(): IngredientUI? {
        val name = binding.etIngredient.text.toString()
        val amount = binding.etAmount.text.toString()

        return when {
            name.isNotEmpty() && amount.isNotEmpty() -> return IngredientUI(name, amount)
            else -> null
        }
    }
}