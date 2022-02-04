package com.joellui.ryu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joellui.ryu.model.EpisodePost
import com.joellui.ryu.model.AnimePost
import com.joellui.ryu.model.RandomAnimePost
import com.joellui.ryu.model.SearchPost
import com.joellui.ryu.repositry.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository) : ViewModel() {

    val myResponse: MutableLiveData<Response<AnimePost>> = MutableLiveData()
    val episodeResponse: MutableLiveData<Response<EpisodePost>> = MutableLiveData()
    val randomResponse: MutableLiveData<Response<RandomAnimePost>> = MutableLiveData()
    val searchResponse: MutableLiveData<Response<SearchPost>> = MutableLiveData()
    val searchResponse2: MutableLiveData<Response<SearchPost>> = MutableLiveData()

    fun getPost(number: Int) {
        viewModelScope.launch {
            val response = repository.getPost(number)
            myResponse.value = response

        }
    }

    fun getRandomAnime(count: Int, nsfw: Boolean?){
        viewModelScope.launch {
            val  response = repository.getRandomAnime(count, nsfw=false)
            randomResponse.value = response
        }
    }

    fun getEpisode(
        number: Int,
        src: String = "dreamsub",
        local: String = "it",
        current_page: Int = 1
    ) {
        viewModelScope.launch {
            val response = repository.getEpisode(number, src, local, current_page)
            episodeResponse.value = response
        }
    }

    fun getSearchAnime(
        title: String? = null,
        nsfw: Boolean? = false,
        status: String? = null,
        formats: String? = null,
        per_page: Int? = null,
        season: Int? = null,
        sort_fields: String? = null,
        sort_directions: Int? = null,
        result_response_1: Boolean = true
    ){
        viewModelScope.launch {
            val response = repository.getSearchAnime(title,nsfw,formats,status,per_page,season ,sort_fields,sort_directions)

            if (result_response_1) {
                searchResponse.value = response
            } else {
                searchResponse2.value = response
            }
        }
    }


}