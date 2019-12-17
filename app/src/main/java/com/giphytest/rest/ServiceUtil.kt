package com.giphytest.rest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ServiceUtil {

    private val CLIENT_TIME_OUT = 10


    private val DATE_FORMAT = "yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'"


    fun <T> createService(clazz: Class<T>, endPoint: String): T {
        return getRetrofit(endPoint).create(clazz)
    }


    private fun getRetrofit(endPoint: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(endPoint)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
    }

    private val gson: Gson
        get() = GsonBuilder()
                .setDateFormat(DATE_FORMAT)
                .create()

    private val okHttpClient: OkHttpClient
        get() = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(CLIENT_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(CLIENT_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(CLIENT_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .build()
}
