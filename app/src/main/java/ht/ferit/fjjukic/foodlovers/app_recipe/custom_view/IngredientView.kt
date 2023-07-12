package ht.ferit.fjjukic.foodlovers.app_recipe.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.model.IngredientModel
import ht.ferit.fjjukic.foodlovers.databinding.ItemIngredientBinding

class IngredientView @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    private val defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val ingredientBinding = ItemIngredientBinding.inflate(LayoutInflater.from(context), this, true)

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
        ingredientBinding.ivClose.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

    fun setListener(listener: OnClickListener) {
        ingredientBinding.ivClose.setOnClickListener {
            listener.onClick(this)
        }
    }

    fun getData(): IngredientModel? {
        val name = ingredientBinding.etIngredient.text.toString()
        val amount = ingredientBinding.etAmount.text.toString()

        return when {
            name.isNotEmpty() && amount.isNotEmpty() -> return IngredientModel(name, amount)
            else -> null
        }
    }
}