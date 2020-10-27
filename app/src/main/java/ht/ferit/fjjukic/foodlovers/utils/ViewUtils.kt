package ht.ferit.fjjukic.foodlovers.utils

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import ht.ferit.fjjukic.foodlovers.ui.main.view.MainActivity
import ht.ferit.fjjukic.foodlovers.ui.main.view.NavigationActivity

fun Context.startNavigationActivity() =
    Intent(this, NavigationActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun Context.startMainActivity() =
    Intent(this, MainActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun Context.hideKeyboard(view: View) {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.apply {
        hideSoftInputFromWindow(view.windowToken, 0)
    }
}