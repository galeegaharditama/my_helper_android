/*
 * Copyright 2019 Sagar Viradiya
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.galih.library.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.galih.library.permission.model.PermissionRequest

/**
 * @param permissions vararg of all the permissions for request.
 * @param requestBlock block constructing [PermissionRequest] object for permission request.
 */
inline fun AppCompatActivity.reqPermissions(
  vararg permissions: String,
  requestBlock: PermissionRequest.() -> Unit
) {
  PermissionManager.requestPermissions(this, *permissions) { this.requestBlock() }
}

/**
 * @param permissions vararg of all the permissions for request.
 * @param requestBlock block constructing [PermissionRequest] object for permission request.
 */
inline fun Fragment.reqPermissions(
  vararg permissions: String,
  requestBlock: PermissionRequest.() -> Unit
) {
  PermissionManager.requestPermissions(this, *permissions) { this.requestBlock() }
}

fun Context.hasPermission(permission: String): Boolean {
  return ActivityCompat.checkSelfPermission(this, permission) ==
    PackageManager.PERMISSION_GRANTED
}

/**
 * Example:
 *
 * reqPermissions(Manifest.permission.CAMERA)
 * {
 *  requestCode = 123
 *  resultCallback = {
 *      when(this){
 *          is PermissionResult.PermissionGranted -> {}
 *          is PermissionResult.PermissionDenied -> {}
 *          is PermissionResult.ShowRational -> {}
 *          is PermissionResult.PermissionDeniedPermanently -> {}
 *      }
 *   }
 * }
 *
 **/
