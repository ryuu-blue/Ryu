package com.joellui.ryu.api

import com.joellui.ryu.model.EpisodePost
import com.joellui.ryu.model.AnimePost
import com.joellui.ryu.model.RandomAnimePost
import com.joellui.ryu.model.SearchPost
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Aniapi {

    @GET("/v1/anime/{id}")
    suspend fun getPost(
        @Path("id") number: Int
    ): Response<AnimePost>


    @GET("/v1/episode")
    suspend fun getEpisode(
        @Query("anime_id") number: Int,
        @Query("source") src: String = "gogoanime",
        @Query("locale") locale: String = "en",
        @Query("page") current_page: Int = 1
    ): Response<EpisodePost>

    @GET("/v1/random/anime/{count}")
    suspend fun getRandomAnime(
        @Path("count") number: Int = 9
    ): Response<RandomAnimePost>

    @GET("v1/anime")
    suspend fun getSearchAnime(
        @Query("title") title: String,
        @Query("status") number: String = "0,1,3",
        @Query("nsfw") nsfw: Boolean = false,
    ): Response<SearchPost>

}