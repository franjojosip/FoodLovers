package ht.ferit.fjjukic.foodlovers.app_common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_common.model.DialogModel

fun Context.showAlertDialog(data: DialogModel) {
    AlertDialog.Builder(this).create().apply {
        setTitle(context.getString(data.title))

        setMessage("")
        data.message?.let {
            setMessage(context.getString(it))
        }

        setButton(
            AlertDialog.BUTTON_NEUTRAL, context.getString(data.neutralTitleId ?: R.string.action_cancel)
        ) { dialog, _ ->
            data.neutralAction?.let { action -> action() }
            dialog.dismiss()
        }
        data.negativeTitleId?.let {
            setButton(
                AlertDialog.BUTTON_NEGATIVE, context.getString(it)
            ) { dialog, _ ->
                data.negativeAction?.let { action -> action() }
                dialog.dismiss()
            }
        }
        setButton(
            AlertDialog.BUTTON_POSITIVE, context.getString(data.positiveTitleId)
        ) { dialog, _ ->
            data.positiveAction()
            dialog.dismiss()
        }
    }.show()
}

fun View.clearFocusAndHideKeyboard() {
    val imm = ContextCompat.getSystemService(
        context,
        InputMethodManager::class.java
    )
    imm?.hideSoftInputFromWindow(windowToken, 0)
    clearFocus()
}

fun EditText.hideKeyboardOnActionDone() {
    this.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            clearFocusAndHideKeyboard()
            return@setOnEditorActionListener true
        }
        false
    }
}

fun EditText.hideKeyboardOnLostFocus() {
    this.setOnFocusChangeListener { _, hasFocus ->
        if(!hasFocus) {
            clearFocusAndHideKeyboard()
        }
    }
}