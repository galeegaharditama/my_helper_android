package com.galeegaharditama.helper.extension

import android.content.res.Resources

val Int.dp: Int
  get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.px: Int
  get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.isServerError() = this == INTERNAL_SERVER_ERROR
fun Int.isNotFound() = this == NOT_FOUND

private const val INTERNAL_SERVER_ERROR = 500
private const val NOT_FOUND = 404
