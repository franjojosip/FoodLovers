package ht.ferit.fjjukic.foodlovers.app_recipe.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import ht.ferit.fjjukic.foodlovers.app_common.model.StepModel
import ht.ferit.fjjukic.foodlovers.databinding.CardStepBinding

class StepView @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet? = null,
    val defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = CardStepBinding.inflate(LayoutInflater.from(context), this, true)
    private var step = 1

    fun toggleCloseIcon(isVisible: Boolean) {
        binding.ivClose.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

    fun setStepNumber(value: Int) {
        binding.tvStep.text = "Step $value."
        step = value
    }

    fun setListener(listener: OnClickListener) {
        binding.ivClose.setOnClickListener {
            listener.onClick(this)
        }
    }

    fun getData(): StepModel? {
        val step = step
        val description = binding.etStepDescription.text.toString()

        return when {
            description.isNotEmpty() -> return StepModel(step, description)
            else -> null
        }
    }
}