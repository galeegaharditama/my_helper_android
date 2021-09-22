package com.galih.library_helper_class

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.View
import com.galih.library_helper_class.PermissionHelper.onRunPermission
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener

object PermissionHelper {
    fun Context.isHasPermission(permissions: MutableList<String>): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            permissions.all { singlePermission ->
                this.checkSelfPermission(singlePermission) == PackageManager.PERMISSION_GRANTED
            }
        else true
    }

    fun Activity.onRunPermission(
        permissions: List<String>,
        listenerGranted: (() -> Unit)? = null,
        listenerDeny: (() -> Unit)? = null
    ) {
        Log.i(javaClass.simpleName, "onRunPermission() called")
        val view: View = this.findViewById(android.R.id.content)
        Dexter.withContext(this).withPermissions(permissions).withListener(
            CompositeMultiplePermissionsListener(
                SnackbarOnAnyDeniedMultiplePermissionsListener.Builder
                    .with(view, R.string.base_permission_title)
                    .withOpenSettingsButton(R.string.base_setting)
                    .withDuration(Snackbar.LENGTH_INDEFINITE)
                    .build(),
                DialogOnAnyDeniedMultiplePermissionsListener.Builder
                    .withContext(this)
                    .withTitle(R.string.base_permission_title)
                    .withMessage(R.string.base_permission_message)
                    .withButtonText(R.string.base_ok)
//                    .withIcon(R.mipmap.ic_logo)
                    .build(),
                object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if (it.areAllPermissionsGranted()) {
                                Log.i(javaClass.simpleName,"AllPermissionGranted called")
                                listenerGranted?.invoke()
                            } else {
                                Log.i(javaClass.simpleName,"DenyPermission called")
                                listenerDeny?.invoke()
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }
                }
            )
        ).check()
    }

}
