package com.galeegaharditama.helper.extension

import java.text.SimpleDateFormat
import java.util.*

fun Long.toDateTime(pattern: String): String {
  val newFormat = SimpleDateFormat(pattern, Locale.US)
  return newFormat.format(Date(this))
}

fun Long.datePlus(days: Int = 0, weeks: Int = 0): Long {
  val calendar = Calendar.getInstance()
  calendar.timeInMillis = this
  calendar.add(Calendar.WEEK_OF_YEAR, weeks)
  calendar.add(Calendar.DAY_OF_YEAR, days)
  return calendar.timeInMillis
}
