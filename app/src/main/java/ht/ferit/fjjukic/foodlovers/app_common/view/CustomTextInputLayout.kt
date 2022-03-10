package ht.ferit.fjjukic.foodlovers.app_common.view

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.textfield.TextInputLayout
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.databinding.EditTextViewBinding

class CustomTextInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr) {

    private var binding: EditTextViewBinding =
        EditTextViewBinding.inflate(LayoutInflater.from(context))
    private var errorMessage: String? = null

    init {
        setupAttributes(attrs, defStyleAttr)
        addView(binding.root)
        toggleError(true)
    }

    private fun setupAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.CustomTextInput, defStyleAttr, 0).apply {
            errorMessage = this.getString(R.styleable.CustomTextInput_errorMessage)
            binding.etField.inputType =
                this.getInt(
                    R.styleable.CustomTextInput_android_inputType,
                    InputType.TYPE_CLASS_TEXT
                )
        }.recycle()
    }

    fun toggleError(isEnabled: Boolean) {
        this.isErrorEnabled = isEnabled
        this.error = if (isEnabled) errorMessage else null
    }

}