package com.example.collegeschedule.data.network

import com.example.collegeschedule.data.api.ScheduleApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {


    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }


    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit = Retrofit.Builder()

        .baseUrl("http://192.168.1.103:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val api: ScheduleApi = retrofit.create(ScheduleApi::class.java)
}