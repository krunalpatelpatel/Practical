package com.example.practicaltest.API

import com.example.practicaltest.utils.Prefs
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {

    /*
   * RestApiList class contain all apis request require in application
   * */

    val baseUrl: String = "https://api.invupos.com/invuApiPos/"

    private val retrofit by lazy {
        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                request.header("APIKEY", Prefs.authAPI)
                chain.proceed(request.build())
            }
            .addInterceptor(logging)
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val api by lazy {
        retrofit.create(API::class.java)
    }

}