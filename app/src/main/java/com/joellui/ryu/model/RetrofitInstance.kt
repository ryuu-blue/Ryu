package com.joellui.ryu.model

import com.joellui.ryu.api.Aniapi
import com.joellui.ryu.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
    val api: Aniapi by lazy {
        retrofit.create(Aniapi::class.java)
    }
}