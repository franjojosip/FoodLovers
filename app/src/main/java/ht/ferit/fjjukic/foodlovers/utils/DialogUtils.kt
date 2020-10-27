package ht.ferit.fjjukic.foodlovers.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AlertDialog

fun Context.showLocationIsDisabledAlert() {
    val alertDialog: AlertDialog =
        AlertDialog.Builder(this).create()
    alertDialog.setTitle("Please turn on location in settings!")
    alertDialog.setButton(
        AlertDialog.BUTTON_NEUTRAL, "Cancel"
    ) { dialog, _ ->
        dialog.dismiss()
    }
    alertDialog.setButton(
        AlertDialog.BUTTON_NEGATIVE, "Settings"
    ) { dialog, _ ->
        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        dialog.dismiss()
    }
    alertDialog.show()
}