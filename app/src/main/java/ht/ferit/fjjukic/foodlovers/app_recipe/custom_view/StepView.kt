package ht.ferit.fjjukic.foodlovers.app_recipe.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import ht.ferit.fjjukic.foodlovers.app_common.model.StepModel
import ht.ferit.fjjukic.foodlovers.databinding.ItemStepCardBinding

class StepView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val stepBinding = ItemStepCardBinding.inflate(LayoutInflater.from(context), this, true)
    private var step = 1

    fun toggleCloseIcon(isVisible: Boolean) {
        stepBinding.ivClose.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

    fun setStepNumber(value: Int) {
        stepBinding.tvStep.text = "Step $value."
        step = value
    }

    fun setListener(listener: OnClickListener) {
        stepBinding.ivClose.setOnClickListener {
            listener.onClick(this)
        }
    }

    fun getData(): StepModel? {
        val step = step
        val description = stepBinding.etStepDescription.text.toString()

        return when {
            description.isNotEmpty() -> return StepModel(step, description)
            else -> null
        }
    }
}