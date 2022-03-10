package ht.ferit.fjjukic.foodlovers.app_common.listener

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

interface PermissionHandler {
    fun openSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    fun checkShouldShowPermissionRationale(activity: Activity, permissions: Array<String>): Boolean {
        permissions.forEach {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, it)) {
                return true
            }
        }
        return false
    }

    fun checkPermissions(context: Context, permissions: Array<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}