package com.galeegaharditama.helper.extension

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * Extension method to provide hide keyboard for [Activity].
 */
fun Activity.hideSoftKeyboard() {
  if (currentFocus != null) {
    val inputMethodManager = getSystemService(
      Context
        .INPUT_METHOD_SERVICE
    ) as InputMethodManager
    currentFocus?.let { inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0) }
  } else {
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
  }
}

fun Activity.hideKeyboard() {
  val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
}

/**
 * Return whether Keyboard is currently visible on screen or not.
 *
 * @return true if keyboard is visible.
 */
fun Activity.isKeyboardVisible(): Boolean {
  val r = Rect()

  // r will be populated with the coordinates of your view that area still visible.
  window.decorView.getWindowVisibleDisplayFrame(r)

  // get screen height and calculate the difference with the usable area from the r
  val height = getDisplaySize().y
  val diff = height - r.bottom

  // If the difference is not 0 we assume that the keyboard is currently visible.
  return diff != 0
}

/**
 * Initiate a fragment in an [FragmentActivity]
 */
fun FragmentActivity.initScreen(containerId: Int, fragment: Fragment, tag: String? = null) {
  supportFragmentManager
    .beginTransaction()
    .add(containerId, fragment, tag)
    .commit()
}
