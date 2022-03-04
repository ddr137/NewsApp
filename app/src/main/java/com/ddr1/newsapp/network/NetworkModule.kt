package com.ddr1.newsapp.network

import com.ddr1.newsapp.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit( get() ) }
    single { provideNewsApi(get()) }
}

fun provideOkHttpClient() : OkHttpClient {
    return  OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        .readTimeout(60, TimeUnit.MINUTES)
        .connectTimeout(60, TimeUnit.MINUTES)
        .writeTimeout(60, TimeUnit.MINUTES)
        .build()
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client( okHttpClient )
        .addConverterFactory (
            GsonConverterFactory.create(
                GsonBuilder().serializeNulls().create()
            )
        )
        .build()
}

fun provideNewsApi(retrofit: Retrofit): ApiClient = retrofit.create(ApiClient::class.java)