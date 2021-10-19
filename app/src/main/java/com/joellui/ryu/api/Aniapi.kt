package com.joellui.ryu.api

import com.joellui.ryu.model.EpisodePost
import com.joellui.ryu.model.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Aniapi {

    @GET("/v1/anime/{id}")
    suspend fun getPost(
        @Path("id") number: Int
    ): Response<Post>

    @GET("/v1/episode")
    suspend fun getEpisode(
        @Query("anime_id") number: Int,
        @Query("source") src: String = "gogoanime",
        @Query("locale") local: String = "en"
    ): Response<EpisodePost>
}