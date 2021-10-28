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
package com.galih.library_helper_class.permission

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.galih.library_helper_class.permission.model.PermissionRequest

/**
 * @param permissions vararg of all the permissions for request.
 * @param requestBlock block constructing [PermissionRequest] object for permission request.
 */
inline fun AppCompatActivity.requestPermissions(
    vararg permissions: String,
    requestBlock: PermissionRequest.() -> Unit
) {
    PermissionManager.requestPermissions(this, *permissions) { this.requestBlock() }
}

/**
 * @param permissions vararg of all the permissions for request.
 * @param requestBlock block constructing [PermissionRequest] object for permission request.
 */
inline fun Fragment.requestPermissions(
    vararg permissions: String,
    requestBlock: PermissionRequest.() -> Unit
) {
    PermissionManager.requestPermissions(this, *permissions) { this.requestBlock() }
}

/**
 * Example:
 *
 * requestPermissions(Manifest.permission.CAMERA)
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
