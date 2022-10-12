package com.galih.library

@JvmInline
value class Output<out T> internal constructor(
  val value: Any?
) {

  val isSuccess: Boolean get() = value !is Failure
  val isFailure: Boolean get() = value is Failure

  data class Failure(
    val type: IOutputError = OutputError.UNKNOWN,
    val status: Int? = null,
    val title: String? = null,
    val message: String? = null,
  )

  fun failOrNull(): Failure? =
    when (value) {
      is Failure -> value.copy()
      else -> null
    }

  companion object {
    fun <T> success(value: T): Output<T> = Output(value)
    fun <T> failure(fail: Failure): Output<T> =
      Output(createError(fail.type, fail.status, fail.title, fail.message))

    fun <T> failure(
      type: IOutputError? = null,
      status: Int? = null,
      title: String? = null,
      message: String,
    ): Output<T> = Output(createError(type, status, title, message))

    fun <T> failure(
      type: IOutputError? = null,
      status: Int? = null,
      title: String? = null,
      exception: Throwable
    ): Output<T> =
      Output(createError(type, status, title, exception.localizedMessage))
  }
}

internal fun createError(
  type: IOutputError?,
  status: Int?,
  title: String?,
  message: String?,
): Any =
  Output.Failure(type ?: OutputError.UNKNOWN, status, title, message)

fun <R, T> Output<T>.onSuccessWithTransform(transform: (value: T) -> R): Output<R> {
  return when {
    isSuccess -> Output.success(transform(value as T))
    else -> Output(value)
  }
}

inline fun <T> Output<T>.onSuccess(action: (value: T) -> Unit): Output<T> {
  if (isSuccess) action(value as T)
  return this
}

inline fun <T> Output<T>.onFailure(action: (exception: Output.Failure) -> Unit): Output<T> {
  failOrNull()?.let { action(it) }
  return this
}

interface IOutputError

enum class OutputError : IOutputError {
  /**
   * A request that resulted in a response but with empty data.
   * Ex: List of data that returned as empty will use this error type.
   */
  EMPTY,

  /**
   * A request that didn't result in a response.
   */
  NETWORK,

  /**
   * A request that resulted in a response with a non-2xx status code.
   */
  SERVER,

  /**
   * A request that resulted in an error different from an IO or Server error.
   *
   * An example of such an error is JSON parsing exception thrown by a serialization library.
   */
  UNKNOWN
}
