package com.joellui.ryu

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.BlurTransformation
import coil.transform.RoundedCornersTransformation
import com.joellui.ryu.repositry.Repository

class AnimeDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime_details)

        val cover : ImageView = findViewById(R.id.IVanimeCover)
        val title : TextView = findViewById(R.id.TVtitle)
        val japaneseTitle : TextView = findViewById(R.id.japaneseTitle)
        val bundle : Bundle?= intent.extras

        val heading = bundle!!.getString("title")
        val image = bundle.getString("image")
        val id = bundle.getString("id")


        title.text = heading
        cover.load(image){
            crossfade(true)
            crossfade(1000)
            size(500,750)
            transformations(BlurTransformation(applicationContext))
        }

        //api
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.myResponse.observe(this, Observer { response ->
            if (response.isSuccessful){

                val getBanner = response.body()?.data?.banner_image
                val jpTitle = response.body()?.data?.titles?.jp
                val color = response.body()?.data?.cover_color
                val description = response.body()?.data?.descriptions

                japaneseTitle.text = jpTitle
                japaneseTitle.setTextColor(Color.parseColor(color))



            }else{
                Log.d("Response", response.errorBody().toString())

            }
        })

        viewModel.getPost(id!!.toInt())
    }
}