package com.galih.library.remote

import java.io.IOException

sealed class NetworkResponse<out T : Any, out U : Any> {
  /**
   * Success response with body
   */
  data class Success<T : Any>(val body: T) : NetworkResponse<T, Nothing>()

  /**
   * Failure response with body
   */
  data class ApiError<U : Any>(val body: U, val code: Int) : NetworkResponse<Nothing, U>()

  /**
   * Network error
   */
  data class NetworkError(val error: IOException) : NetworkResponse<Nothing, Nothing>()

  /**
   * For example, json parsing error
   */
  data class UnknownError(val error: Throwable?) : NetworkResponse<Nothing, Nothing>()
}

class ErrorResponse(
    val type: TypeError,
    val exception: Throwable?,
    val errMessage: String? = null,
    val status: Int? = null
) : Throwable(exception?.message) {
  enum class TypeError {
    API_ERROR, NETWORK_ERROR, UNKNOWN_ERROR
  }
}

interface BaseError {
  val errMessage: String
}
