package com.galeegaharditama.helper

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.galeegaharditama.helper.extension.isNetworkConnected
import com.galeegaharditama.helper.remote.NetworkResponseAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.io.IOException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

fun provideOkHttpClient(isDebug: Boolean, timeout: Long = 30): OkHttpClient.Builder {
  val sslContext = SSLContext.getInstance("SSL")
  sslContext.init(null, trustAllCerts, java.security.SecureRandom())
  return OkHttpClient.Builder()
    .apply {
      if (isDebug) {
        val loggingInterceptor =
          HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        addInterceptor(loggingInterceptor)
      }
    }
    .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0])
    .connectTimeout(timeout, TimeUnit.SECONDS)
    .readTimeout(timeout, TimeUnit.SECONDS)
    .writeTimeout(timeout, TimeUnit.SECONDS)
}

fun provideRetrofit(baseUrl: String, okHttpClient: OkHttpClient.Builder, moshi: Moshi): Retrofit {
  return Retrofit.Builder()
    .client(okHttpClient.build())
    .baseUrl(baseUrl)
    .addCallAdapterFactory(NetworkResponseAdapterFactory())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()
}

open class DebugTree : Timber.DebugTree() {
  override fun createStackElementTag(element: StackTraceElement): String? {
    return String.format(
      "Class:%s: Line: %s, Method: %s",
      super.createStackElementTag(element),
      element.lineNumber,
      element.methodName
    )
  }
}

open class ReleaseTree : Timber.Tree() {
  override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
    if (priority == Log.VERBOSE || priority == Log.DEBUG) {
      return
    }

    // log your crash to your favourite
    // Sending crash report to Firebase CrashAnalytics

    // FirebaseCrash.report(message);
    // FirebaseCrash.report(new Exception(message));
  }
}

class ConnectivityInterceptorImpl(context: Context) : ConnectivityInterceptor {
  private val appContext = context.applicationContext
  override fun intercept(chain: Interceptor.Chain): Response {
    if (!appContext.isNetworkConnected()) throw NoInternetException()
    return chain.proceed(chain.request())
  }
}

private val trustAllCerts = arrayOf(object : X509TrustManager {
  @SuppressLint("TrustAllX509TrustManager")
  override fun checkClientTrusted(
    chain: Array<out X509Certificate>?,
    authType: String?
  ) {
    // empty function
  }

  @SuppressLint("TrustAllX509TrustManager")
  override fun checkServerTrusted(
    chain: Array<out X509Certificate>?,
    authType: String?
  ) {
    // empty function
  }

  override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
})

interface ConnectivityInterceptor : Interceptor
class NoInternetException(message: String = "Tidak Ada Koneksi Internet") : IOException(message)
