package ht.ferit.fjjukic.foodlovers.app_common.listener

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

interface PermissionHandler {

    val locationPermissions: Array<String>
        get() = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)

    val imagePermissions: Array<String>
        get() = arrayOf(storagePermissions.first(), Manifest.permission.CAMERA)

    val storagePermissions: Array<String>
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

    fun openSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    fun checkShouldShowPermissionRationale(
        activity: Activity,
        permissions: Array<String>
    ): Boolean {
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