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
        @Query("source") src: String = "dreamsub",
        @Query("locale") locale: String = "it",
        @Query("page") current_page: Int = 1
    ): Response<EpisodePost>

    @GET("/v1/random/anime/{count}")
    suspend fun getRandomAnime(
        @Path("count") number: Int = 9
    ): Response<RandomAnimePost>

    @GET("v1/anime")
    suspend fun getSearchAnime(
        @Query("title") title: String? = null,
        @Query("nsfw") nsfw: Boolean? = false,
        @Query("formats") formats: String? = null,
        @Query("status") status: String? = null,
        @Query("per_page") per_page: Int? = null,
        @Query("sort_fields") sort_fields: String? = null,
        @Query("sort_directions") sort_directions: Int? = null,
    ): Response<SearchPost>

}