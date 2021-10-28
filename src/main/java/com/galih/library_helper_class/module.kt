package com.galih.library_helper_class

import android.content.Context
import android.util.Log
import com.galih.library_helper_class.extension.isNetworkConnected
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.TimeUnit

private const val REQUEST_TIMEOUT = 60

fun provideOkHttpClient(context: Context, isDebug: Boolean, apiKey:String?): OkHttpClient.Builder {
    return OkHttpClient.Builder()
        .apply {
            apiKey?.let { _token ->
                addInterceptor {
                    val original = it.request()
                    val requestBuilder = original.newBuilder()
                        .addHeader("Accept", "application/json")
                    requestBuilder.addHeader("api-token", _token)
                    val request = requestBuilder.build()
                    it.proceed(request)
                }
            }
            addInterceptor(HttpLoggingInterceptor().apply {
                level = if (isDebug) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            })
        }
        .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .addInterceptor(ConnectivityInterceptorImpl(context))
}

fun provideRetrofit(okHttpClient: OkHttpClient.Builder, url:String): Retrofit {
    return Retrofit.Builder()
        .client(okHttpClient.build())
        .baseUrl(url)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideLogging(isDebug: Boolean) : Timber.Tree{
    return if (isDebug){
        DebugTree()
    } else{
        ReleaseTree()
    }
}

open class DebugTree : Timber.DebugTree(){
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
interface ConnectivityInterceptor : Interceptor
class NoInternetException(message: String = "Tidak Ada Koneksi Internet") : IOException(message)