package com.galih.library.extension

fun <T : Any> T.requireArgumentsOrThrow(vararg values: Boolean) {
  values.forEach {
    require(it) {
      "An argument required to instantiate " + javaClass.simpleName + "is missing"
    }
  }
}
