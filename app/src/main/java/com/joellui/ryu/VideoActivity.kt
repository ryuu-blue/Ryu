package com.joellui.ryu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.joellui.ryu.repositry.Repository

class VideoActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        val bundle : Bundle? = intent.extras

        val id = bundle?.getString("id")
        val banner = bundle?.getString("banner")

        val animePlay: VideoView = findViewById(R.id.Video)
        val setBanner: ImageView = findViewById(R.id.IVbanner)
        val setDev: TextView = findViewById(R.id.dev)

        val repo = Repository()
        val viewModelFactory = MainViewModelFactory(repo)
        viewModel = ViewModelProvider(this,viewModelFactory).get(MainViewModel::class.java)
        viewModel.episodeResponse.observe(this, Observer { response ->
            if (response.isSuccessful){

                Log.d("Response",response.body()?.message.toString())

                setDev.text = response.body()?.message.toString()


            }else{
                Log.d("Response", response.errorBody().toString())
            }

        })

        viewModel.getEpisode(id!!.toInt())


        setBanner.load(banner){
            crossfade(true)
            crossfade(1000)
        }




    }
}