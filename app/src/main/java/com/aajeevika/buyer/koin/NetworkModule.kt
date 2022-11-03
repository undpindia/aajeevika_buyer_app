package com.aajeevika.buyer.koin

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.aajeevika.buyer.BuildConfig
import com.aajeevika.buyer.R
import com.aajeevika.buyer.model.network_request.ApiService
import com.aajeevika.buyer.utility.AppPreferencesHelper
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        OkHttpClient.Builder().addInterceptor(logging)
            .readTimeout(180, TimeUnit.SECONDS)
            .writeTimeout(180, TimeUnit.SECONDS)
            .connectTimeout(180, TimeUnit.SECONDS)
            .addInterceptor(ConnectivityInterceptor(androidContext().applicationContext))
            .addInterceptor(RequestInterceptor(get()))
            .build()
    }

    single {
        val gsonConverterFactory = GsonConverterFactory.create(GsonBuilder().setLenient().create())

        Retrofit.Builder()
            .baseUrl(BaseUrls.baseUrl)
            .client(get())
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    single {
        val retrofit: Retrofit = get()
        retrofit.create(ApiService::class.java)
    }
}

class ConnectivityInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isOnline(context)) throw NoConnectivityException(context)

        return chain.proceed(chain.request())
    }

    @Suppress("DEPRECATION")
    private fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.activeNetwork?.run {
                connectivityManager.getNetworkCapabilities(this)?.run {
                    return when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                        else -> false
                    }
                } ?: false
            } ?: false
        } else {
            connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }
}

class RequestInterceptor(private val preferencesHelper: AppPreferencesHelper) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("RequestInte"," "+preferencesHelper.latitude+","+preferencesHelper.longitude)
        Log.d("RequestInte","authToken "+preferencesHelper.authToken)
        val request = chain.request().newBuilder()
            .addHeader("app-key", "laravelUNDP")
            .addHeader("app-type", "Buyer")
            .addHeader("platform", "android")
            .addHeader("version_code", BuildConfig.VERSION_CODE.toString())
            .addHeader("version_name", BuildConfig.VERSION_NAME)
            .addHeader("language", preferencesHelper.appLanguage)
            .addHeader("apitoken", preferencesHelper.authToken)
            .addHeader("devicetoken", preferencesHelper.fcmToken)
            .addHeader("lat", preferencesHelper.latitude.toString())
            .addHeader("log", preferencesHelper.longitude.toString())
            .build()

        return chain.proceed(request)
    }
}

class NoConnectivityException(context: Context) : IOException() {
    override val message = context.getString(R.string.no_internet_connection)
}