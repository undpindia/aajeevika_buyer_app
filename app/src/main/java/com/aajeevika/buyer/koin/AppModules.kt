package com.aajeevika.buyer.koin

import android.content.Context
import com.aajeevika.buyer.BuildConfig
import com.aajeevika.buyer.utility.AppPreferencesHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single {
        androidContext().getSharedPreferences("${BuildConfig.APPLICATION_ID}_app", Context.MODE_PRIVATE)
    }

    single {
        AppPreferencesHelper(get())
    }
}