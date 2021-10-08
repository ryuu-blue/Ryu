package com.joellui.ryu.api

import com.joellui.ryu.model.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface Aniapi {

    @GET("/v1/anime/{id}")
    suspend fun getPost(
        @Path("id") number: Int
    ): Response<Post>
}