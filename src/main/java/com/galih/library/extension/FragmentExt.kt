package com.galih.library.extension

import androidx.fragment.app.Fragment

fun Fragment.hideSoftKeyboard() {
  activity?.hideSoftKeyboard()
}
