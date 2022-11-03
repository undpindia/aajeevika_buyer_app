package com.aajeevika.buyer

import android.app.Application
import android.content.Context
import com.aajeevika.buyer.koin.*
import com.aajeevika.buyer.location.koin.locationNetworkModule
import com.aajeevika.buyer.location.koin.locationViewModelModule
import com.aajeevika.buyer.utility.AppPreferencesHelper
import com.aajeevika.buyer.utility.LocaleHelper
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApp : Application() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(
            LocaleHelper.updateLanguage(
                newBase,
                AppPreferencesHelper(newBase.getSharedPreferences(
                    "${BuildConfig.APPLICATION_ID}_app",
                    Context.MODE_PRIVATE
                )).appLanguage
            )
        )
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(listOf(appModule, networkModule, myViewModel, locationNetworkModule, locationViewModelModule))
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if(it.isSuccessful) {
                val preferences: AppPreferencesHelper = get()
                preferences.fcmToken = it.result ?: ""
            }
        }
    }
}