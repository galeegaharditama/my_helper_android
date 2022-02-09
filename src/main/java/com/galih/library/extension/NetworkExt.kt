package com.galih.library.extension

import com.galih.library.ApiResponseException
import retrofit2.Response

suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Result<T> {
  return try {
    val response = call.invoke()
    if (response.isSuccessful) Result.success(response.body()!!)
    else Result.failure(ApiResponseException(response.errorBody()?.string() ?: "Something goes wrong"))
  } catch (e: Exception) {
    Result.failure(e)
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
