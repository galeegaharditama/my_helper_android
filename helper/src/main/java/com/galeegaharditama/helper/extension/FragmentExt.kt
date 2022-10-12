package com.galeegaharditama.helper.extension

import androidx.fragment.app.Fragment

fun Fragment.hideSoftKeyboard() {
  activity?.hideSoftKeyboard()
}
