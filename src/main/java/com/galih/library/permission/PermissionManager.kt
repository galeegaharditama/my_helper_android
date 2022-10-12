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
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.galih.library.permission.model.PermissionRequest
import com.galih.library.permission.model.PermissionResult

/**
 * A headless fragment which wraps the boilerplate code for checking and requesting permission.
 * A simple [Fragment] subclass.
 */
class PermissionManager : BasePermissionManager() {

  private var callbackMap = mutableMapOf<Int, PermissionResult.() -> Unit>()

  override fun onPermissionResult(permissionResult: PermissionResult) {
    callbackMap[permissionResult.requestCode]?.let {
      permissionResult.it()
    }
    callbackMap.remove(permissionResult.requestCode)
  }

  override fun onAttach(context: Context) {
    super.onAttach(context)
    callbackMap.clear()
  }

  companion object {

    private const val TAG = "PermissionManager"

    /**
     * A static inline method to request permission for activity.
     *
     * @param activity an instance of [AppCompatActivity]
     * @param permissions vararg of all permissions for request
     * @param requestBlock [PermissionRequest] block for permission request
     *
     */
    @JvmStatic
    @MainThread
    inline fun requestPermissions(
      activity: AppCompatActivity,
      vararg permissions: String,
      requestBlock: PermissionRequest.() -> Unit
    ) {
      val permissionRequest = PermissionRequest().apply(requestBlock)
      requireNotNull(permissionRequest.requestCode) {
        "No request code specified."
      }
      requireNotNull(permissionRequest.resultCallback) {
        "No result callback found."
      }
      requestPermissions(
        activity,
        permissionRequest.requestCode!!,
        permissionRequest.resultCallback!!,
        *permissions
      )
    }

    /**
     * A static inline method to request permission for fragment.
     *
     * @param fragment an instance of [Fragment]
     * @param permissions vararg of all permissions for request
     * @param requestBlock [PermissionRequest] block for permission request
     *
     */
    @JvmStatic
    @MainThread
    inline fun requestPermissions(
      fragment: Fragment,
      vararg permissions: String,
      requestBlock: PermissionRequest.() -> Unit
    ) {
      val permissionRequest = PermissionRequest().apply(requestBlock)
      requireNotNull(permissionRequest.requestCode) {
        "No request code specified."
      }
      requireNotNull(permissionRequest.resultCallback) {
        "No result callback found."
      }
      requestPermissions(
        fragment,
        permissionRequest.requestCode!!,
        permissionRequest.resultCallback!!,
        *permissions
      )
    }

    fun requestPermissions(
      activityOrFragment: Any,
      requestId: Int,
      callback: PermissionResult.() -> Unit,
      vararg permissions: String
    ) {
      val fragmentManager = if (activityOrFragment is AppCompatActivity) {
        activityOrFragment.supportFragmentManager
      } else {
        (activityOrFragment as Fragment).childFragmentManager
      }
      if (fragmentManager.findFragmentByTag(TAG) != null) {
        (fragmentManager.findFragmentByTag(TAG) as PermissionManager)
          .also { it.callbackMap[requestId] = callback }
          .requestPermissions(requestId, *permissions)
      } else {
        val permissionManager = PermissionManager()
        fragmentManager.beginTransaction().add(permissionManager, TAG).commitNow()
        permissionManager.callbackMap[requestId] = callback
        permissionManager.requestPermissions(requestId, *permissions)
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    callbackMap.clear()
  }
}
