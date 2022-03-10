package ht.ferit.fjjukic.foodlovers.app_recipe.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import ht.ferit.fjjukic.foodlovers.R

class CustomStepperLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var position = 0
    private var numOfSteps = 4

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_stepper_layout, this, true)
        resetSteps()
    }

    fun setNumOfSteps(value: Int) {
        numOfSteps = value
    }

    private fun resetSteps() {
        children.filter { it is AppCompatImageView }.forEach {
            it.isEnabled = false
        }
        children.filter { it.tag == "line" }.forEach { view ->
            view.isEnabled = false
        }
    }

    fun setStep(positionIndex: Int) {
        if (positionIndex + 1 > numOfSteps) return

        position = positionIndex

        children.filter { it.tag == "step" }.forEachIndexed { index, view ->
            view.isEnabled = index <= position
        }
        children.filter { it.tag == "line" }.forEachIndexed { index, view ->
            view.isEnabled = index <= position
        }
    }
}