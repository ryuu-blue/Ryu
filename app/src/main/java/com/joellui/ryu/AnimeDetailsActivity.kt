package com.joellui.ryu

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.view.marginStart
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.flexbox.FlexboxLayout
import com.joellui.ryu.repositry.Repository
import com.google.android.material.chip.Chip




class AnimeDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    var statusList: List<String> = listOf("FINISHED","RELEASING","NOT_YET_RELEASED","CANCELLED")
    var formatList: List<String> = listOf("TV","TV_SHORT","MOVIE","SPECIAL","OVA","ONA","MUSIC")
    var seasonPeriodList: List<String> = listOf("WINTER","SPRING","SUMMER","FALL","UNKNOWN")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime_details)

        val cover: ImageView = findViewById(R.id.IVanimeCover)
        val title: TextView = findViewById(R.id.TVtitle)
        val score: TextView = findViewById(R.id.TVscore)
        val status: TextView = findViewById(R.id.TVstatus)
        val description: TextView = findViewById(R.id.TVdescription)
        val jp: TextView = findViewById(R.id.TVjpinput)
        val seasonYear: TextView = findViewById(R.id.TVseasoninput)
        val bundle: Bundle? = intent.extras
        val btnPlay: ImageButton = findViewById(R.id.btnPlay)
        val genres: FlexboxLayout = findViewById(R.id.CGgenres)

        val heading = bundle!!.getString("title")
        val image = bundle.getString("image")
        val id = bundle.getString("id")


        title.text = heading
        cover.load(image) {
            crossfade(true)
            crossfade(1000)
            size(500, 750)
        }

        //api
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.myResponse.observe(this, Observer { response ->
            if (response.isSuccessful) {

                val getBanner = response.body()?.data?.banner_image
                val jpTitle = response.body()?.data?.titles?.jp
                val color = response.body()?.data?.cover_color
                val descriptionRes = response.body()?.data?.descriptions!!.en
                val statusRes = response.body()?.data?.status
                val format = response.body()?.data?.format
                val sc = response.body()?.data?.score
                val gen = response.body()?.data?.genres
                val season = response.body()?.data?.season_period
                val season_year = response.body()?.data?.season_year


                val seasonSetText = "${seasonPeriodList[season!!]} - ${season_year}"

                seasonYear.text = seasonSetText

                jp.text = jpTitle

                if(descriptionRes != "") {
                    description.isVisible = true
                    description.text = "Description: \n\n $descriptionRes"
                }else{
                    description.isVisible = false
                }

                val setStatus = "${statusList[statusRes!!]}  ||  ${formatList[format!!]}"
                status.text = setStatus

                // creating and add genres to the app
                if (gen != null) {
                    for(s in gen){
                        val chip = Chip(this)
                        chip.text = s
                        chip.isClickable = false

                        val params = FlexboxLayout.LayoutParams(
                            FlexboxLayout.LayoutParams.WRAP_CONTENT,
                            FlexboxLayout.LayoutParams.WRAP_CONTENT
                        )
                        params.setMargins(10,0,10,0)
                        chip.layoutParams = params
                        genres.addView(chip)
                    }
                }


                score.text = sc.toString()
                score.setTextColor(Color.parseColor(color))




                btnPlay.setOnClickListener {
                    val intent = Intent(this, VideoActivity::class.java)
                    intent.putExtra("id", id)
                    intent.putExtra("banner", getBanner)
                    startActivity(intent)
                }

            } else {
                Log.d("Response", response.errorBody().toString())

            }
        })


        viewModel.getPost(id!!.toInt())
    }


}