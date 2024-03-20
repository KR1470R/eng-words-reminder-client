package com.example.eng_words_reminder_client.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NetworkService {
    private val httpLoginInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(httpLoginInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://kr1470r.xyz/eng-words-reminder/dev/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    val requestAPI: RequestAPI = retrofit.create(RequestAPI::class.java)
}