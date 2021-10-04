package com.example.kolo2assignment.network

import com.example.kolo2assignment.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val instance: APIService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        instance = retrofit.create(APIService::class.java)
    }
}