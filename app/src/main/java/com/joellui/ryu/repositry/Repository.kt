package com.joellui.ryu.repositry

import com.joellui.ryu.model.*
import retrofit2.Response

class Repository {
    suspend fun getPost(number: Int): Response<AnimePost> {
        return RetrofitInstance.api.getPost(number)
    }

    suspend fun getRandomAnime(count: Int): Response<RandomAnimePost>{
        return RetrofitInstance.api.getRandomAnime(count)
    }

    suspend fun getEpisode(
        number: Int,
        src: String = "gogoanime",
        local: String,
        current_page: Int
    ): Response<EpisodePost> {
        return RetrofitInstance.api.getEpisode(number, src, local, current_page)
    }

    suspend fun getSearchAnime(
        title: String,
        nsfw: Boolean,
    ): Response<SearchPost> {
        return  RetrofitInstance.api.getSearchAnime(title,nsfw)
    }
}