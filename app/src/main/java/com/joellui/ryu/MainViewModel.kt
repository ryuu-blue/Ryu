package com.joellui.ryu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joellui.ryu.model.Post
import com.joellui.ryu.repositry.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository): ViewModel() {

    val myResponse: MutableLiveData<Response<Post>> = MutableLiveData()

    fun getPost(number: Int){
        viewModelScope.launch {
            val response = repository.getPost(number)

            myResponse.value = response

        }
    }
}