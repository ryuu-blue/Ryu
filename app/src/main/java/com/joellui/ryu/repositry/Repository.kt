package com.joellui.ryu.repositry

import com.joellui.ryu.model.EpisodePost
import com.joellui.ryu.model.AnimePost
import com.joellui.ryu.model.RetrofitInstance
import retrofit2.Response

class Repository {
    suspend fun getPost(number: Int): Response<AnimePost> {
        return RetrofitInstance.api.getPost(number)
    }

    suspend fun getEpisode(number: Int):Response<EpisodePost> {
        return  RetrofitInstance.api.getEpisode(number)
    }
}