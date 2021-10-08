package com.joellui.ryu.repositry

import com.joellui.ryu.model.Post
import com.joellui.ryu.model.RetrofitInstance
import retrofit2.Response

class Repository {
    suspend fun getPost(number: Int): Response<Post> {
        return RetrofitInstance.api.getPost(number)
    }
}