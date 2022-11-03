package com.aajeevika.buyer.location.koin

import com.aajeevika.buyer.location.MapApiService
import com.google.gson.GsonBuilder
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val locationNetworkModule = module {
    single {
        val fac = GsonConverterFactory.create(GsonBuilder().setLenient().create())
        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .client(get())
            .addConverterFactory(fac)
            .build()
        retrofit.create(MapApiService::class.java)
    }
}