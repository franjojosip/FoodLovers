package ht.ferit.fjjukic.foodlovers.app_common.utils

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_main.view.MainActivity

fun Context.startMainActivity() =
    Intent(this, MainActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun FragmentActivity.navigateToScreen(fragment: Fragment, tag: String? = null) {
    supportFragmentManager.commit {
        replace(R.id.nav_host_fragment, fragment, tag)
        tag?.let { addToBackStack(tag) }
    }
}