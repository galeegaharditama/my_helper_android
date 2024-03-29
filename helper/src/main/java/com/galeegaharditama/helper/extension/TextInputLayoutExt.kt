package com.galeegaharditama.helper.extension

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.validate(message: String, validator: () -> Boolean) {
  this.error = if (validator()) null else message
}
