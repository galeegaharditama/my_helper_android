package com.galih.library.extension

import com.galih.library.ApiResponseException
import com.galih.library.remote.BaseError
import com.galih.library.remote.ErrorResponse
import com.galih.library.remote.NetworkResponse
import retrofit2.Response

suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Result<T> {
  return try {
    val response = call.invoke()
    if (response.isSuccessful) Result.success(response.body()!!)
    else Result.failure(
      ApiResponseException(
        response.errorBody()?.string() ?: "Something goes wrong"
      )
    )
  } catch (e: Exception) {
    Result.failure(e)
  }
}

fun <R : Any, E : BaseError> safeApiCall2(response: NetworkResponse<R, E>): Result<R> {
  return when (response) {
    is NetworkResponse.Success -> Result.success(response.body)
    is NetworkResponse.NetworkError ->
      Result.failure(
        ErrorResponse(
          ErrorResponse.TypeError.NETWORK_ERROR,
          exception = response.error,
          errMessage = response.error.localizedMessage
        )
      )
    is NetworkResponse.UnknownError ->
      Result.failure(
        ErrorResponse(
          ErrorResponse.TypeError.UNKNOWN_ERROR,
          exception = response.error,
          errMessage = response.error?.localizedMessage
        )
      )
    is NetworkResponse.ApiError ->
      Result.failure(
        ErrorResponse(
          ErrorResponse.TypeError.API_ERROR,
          exception = null,
          errMessage = response.body.errMessage,
          status = response.code
        )
      )
  }
}

suspend fun <T : Any> safeApiCall(
    onSuccess: (T) -> Result<T>,
    onFailure: (Response<T>?, Throwable) -> Result<T>,
    call: suspend () -> Response<T>,
): Result<T> {
  return try {
    val response = call.invoke()
    if (response.isSuccessful) onSuccess.invoke(response.body()!!)
    else onFailure.invoke(response, ApiResponseException("Something goes wrong"))
  } catch (e: Throwable) {
    onFailure.invoke(null, e)
  }
}
