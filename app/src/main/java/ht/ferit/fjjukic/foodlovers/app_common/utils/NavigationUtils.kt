package ht.ferit.fjjukic.foodlovers.app_common.utils

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import ht.ferit.fjjukic.foodlovers.R
import ht.ferit.fjjukic.foodlovers.app_main.view.MainActivity
import ht.ferit.fjjukic.foodlovers.app_main.view.NavigationActivity

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

fun FragmentActivity.navigateToScreen(fragment: Fragment, tag: String? = null) {
    supportFragmentManager.commit {
        replace(R.id.frame_layout, fragment, tag)
        tag?.let { addToBackStack(tag) }
    }
}