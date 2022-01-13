package com.joellui.ryu.repositry

import com.joellui.ryu.model.*
import retrofit2.Response
import retrofit2.http.Query

class Repository {
    suspend fun getPost(number: Int): Response<AnimePost> {
        return RetrofitInstance.api.getPost(number)
    }

    suspend fun getRandomAnime(
        count: Int,
        nsfw: Boolean = false
    ): Response<RandomAnimePost>{
        return RetrofitInstance.api.getRandomAnime(count,nsfw)
    }

    suspend fun getEpisode(
        number: Int,
        src: String,
        local: String,
        current_page: Int
    ): Response<EpisodePost> {
        return RetrofitInstance.api.getEpisode(number, src, local, current_page)
    }

    suspend fun getSearchAnime(
        title: String? = null,
        nsfw: Boolean? = false,
        formats: String? = null,
        status: String? = null,
        per_page: Int? = null,
        sort_fields: String? = null,
        sort_directions: Int? = null,
        ): Response<SearchPost> {
        return  RetrofitInstance.api.getSearchAnime(title,nsfw,formats,status,per_page,sort_fields,sort_directions)
    }
}