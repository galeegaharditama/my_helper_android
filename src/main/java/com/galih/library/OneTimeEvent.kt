package com.galih.library

import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author aminography
 * https://gist.github.com/JoseAlcerreca/e0bba240d9b3cffa258777f12e5c0ae9#gistcomment-3245478
 *
 * A wrapper to make sure that the value will only be consumed once.
 */
class OneTimeEvent<T>(
  private val value: T
) {

  private val isConsumed = AtomicBoolean(false)

  internal fun getValue(): T? =
    if (isConsumed.compareAndSet(false, true)) value
    else null

  fun consume(block: (T) -> Unit): T? = getValue()?.also(block)
}

fun <T> T.toOneTimeEvent() =
  OneTimeEvent(this)
