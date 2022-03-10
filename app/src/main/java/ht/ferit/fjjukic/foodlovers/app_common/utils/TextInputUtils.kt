package ht.ferit.fjjukic.foodlovers.app_common.utils

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.validateField(fieldValidator: (String) -> Int?, toggleAction: () -> Unit) {
    this.toggleError(fieldValidator(this.getValue()))
    toggleAction()
}

fun TextInputLayout.toggleError(errorId: Int?) {
    this.isErrorEnabled = errorId != null
    errorId?.let {
        this.error = context.getString(it)
    }
}

fun TextInputLayout.getValue(): String {
    return this.editText?.text.toString()
}