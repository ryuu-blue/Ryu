package com.joellui.ryu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joellui.ryu.model.EpisodePost
import com.joellui.ryu.model.AnimePost
import com.joellui.ryu.model.RandomAnimePost
import com.joellui.ryu.repositry.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository) : ViewModel() {

    val myResponse: MutableLiveData<Response<AnimePost>> = MutableLiveData()
    val episodeResponse: MutableLiveData<Response<EpisodePost>> = MutableLiveData()
    val randomResponse: MutableLiveData<Response<RandomAnimePost>> = MutableLiveData()

    fun getPost(number: Int) {
        viewModelScope.launch {
            val response = repository.getPost(number)
            myResponse.value = response

        }
    }

    fun getRandomAnime(count: Int){
        viewModelScope.launch {
            val  response = repository.getRandomAnime(count)
            randomResponse.value = response
        }
    }

    fun getEpisode(
        number: Int,
        src: String = "gogoanime",
        local: String = "en",
        current_page: Int = 1
    ) {
        viewModelScope.launch {
            val response = repository.getEpisode(number, src, local, current_page)
            episodeResponse.value = response
        }
    }
}