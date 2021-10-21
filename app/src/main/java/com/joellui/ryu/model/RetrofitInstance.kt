package com.joellui.ryu.model

import android.R.attr
import android.util.Log
import com.google.gson.Gson
import com.joellui.ryu.api.Aniapi
import com.joellui.ryu.utils.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.JsonElement

import android.R.attr.data

import com.google.gson.JsonObject

import com.google.gson.JsonParser
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.IOException


class ErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val request: Request = chain.request()
        val response = chain.proceed(request)

        val data = response.body

        if (data != null) {

            val m = response.peekBody(Long.MAX_VALUE).string()
            val jsonObject: JsonObject = JsonParser().parse(m).asJsonObject;
            val s = jsonObject.get("status_code").asInt

            Log.d("Response", "api-> $s")


            if (s == 400 || s==401 || s==403 || s==404 || s==429 || s==500) {
                Log.d("Response", "api check 404-> $s")
                Log.d("Response", "jsonObject-> $jsonObject")

                val newResponse = response.newBuilder().message(jsonObject.get("message").toString()).code(404).build();

                val jsonObj: JsonObject = JsonParser().parse(newResponse.peekBody(Long.MAX_VALUE).string()).asJsonObject
                Log.d("Response", "jsonObject new response -> $jsonObj")

                return newResponse
            }
        }



        return response
    }
}

object RetrofitInstance {
    private val retrofit by lazy {
//        val clientBuilder = OkHttpClient.Builder()
//        val okHttpClient = clientBuilder.addInterceptor { chain ->
//            val request = chain.request()
//            val response = chain.proceed(request)
//
//            if (response.body != null) {
//                try {
//                    var data = response.body!!.string()
//
//                    val parser = JsonParser()
//                    val jo = parser.parse(data) as JsonObject
//                    val je = jo["status_code"]
//
//                    Log.v("MSS", "Hi ${je}")
//                } catch(e: Exception) {
//                    //Log.v("MSS", e.toString())
//                }
//            }
//
//
//            return@addInterceptor response
//        }.build()

        val clientBuilder = OkHttpClient.Builder()
        val okHttpClient = clientBuilder.addInterceptor(ErrorInterceptor()).build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    }
    val api: Aniapi by lazy {
        retrofit.create(Aniapi::class.java)
    }
}