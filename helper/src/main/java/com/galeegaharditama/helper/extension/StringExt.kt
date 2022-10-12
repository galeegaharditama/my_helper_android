package com.galeegaharditama.helper.extension

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

/**
 * Adds variable placeholders () to the given string. Each placeholder is separated
 * by a comma.
 *
 * @param symbol => symbol to append this string
 * @param count => Number of PlaceHolder
 * to APPEND symbol (?) to String
 **/
fun StringBuilder.appendPlaceHolder(
  delimiter: String = ",",
  symbol: String,
  count: Int
): StringBuilder {
  for (i in 0 until count) {
    this.append(symbol)
    if (i < count - 1) {
      this.append(delimiter)
    }
  }
  return this
}

fun String.ucWords(): String {
  return this.lowercase(Locale.getDefault()).split(' ').joinToString(" ") {
    it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
  }
}

fun String.toTimeMillis(pattern: String = "yyyy-MM-dd HH:mm:ss"): Long {
  return SimpleDateFormat(pattern, Locale.US).parse(this)?.time
    ?: throw NullPointerException("Parse Return Null")
}

fun StringBuilder.getErrorFromNetwork(errorInputStream: InputStream): StringBuilder? {
  try {
    val reader = BufferedReader(InputStreamReader(errorInputStream))
    var line: String?
    while (reader.readLine().also { line = it } != null) {
      this.append(line)
    }
  } catch (e: IOException) {
    e.printStackTrace()
    return null
  }
  return this
}
