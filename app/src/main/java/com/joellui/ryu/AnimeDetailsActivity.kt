package com.joellui.ryu

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.marginStart
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.flexbox.AlignContent
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.JustifyContent
import com.joellui.ryu.repositry.Repository
import com.google.android.material.chip.Chip
import com.joellui.ryu.model.AnimePost
import retrofit2.Response


class AnimeDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var LL: LinearLayout
    lateinit var seq: Map<Int, String>

    var statusList: List<String> = listOf("FINISHED", "RELEASING", "NOT_YET_RELEASED", "CANCELLED")
    var formatList: List<String> =
        listOf("TV", "TV_SHORT", "MOVIE", "SPECIAL", "OVA", "ONA", "MUSIC")
    var seasonPeriodList: List<String> = listOf("WINTER", "SPRING", "SUMMER", "FALL", "UNKNOWN")
    var isFirstTime = true


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
                if (isFirstTime) {
                    isFirstTime = false
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


                    //setting sequel and prequel if any
                    val se = response.body()?.data?.sequel
                    val pre = response.body()?.data?.prequel
                    LL = findViewById(R.id.LLbtn)
                    Log.d("Response", se.toString())

                    if (se != 0 && pre != 0) {

                        seq = mapOf(se!! to "pre", pre!! to "seq")!!
                        apiPreSeq(se)
                        apiPreSeq(pre)

                        LL.visibility = View.VISIBLE

//                    //prequel
//                    val viewpre = layoutInflater.inflate(R.layout.anime_cover_item, null)
//                    viewpre.setPadding(20, 20, 20, 20)
//                    val tvItempre: TextView = viewpre.findViewById(R.id.TVtitle)
//                    val coverpre: ImageView = viewpre.findViewById(R.id.IVanimeCover)
//                    tvItempre.text = "Anime :: Prequel"
//                    LL.addView(viewpre)
//
//                    //sequel
//                    val viewse = layoutInflater.inflate(R.layout.anime_cover_item, null)
//                    viewse.setPadding(20, 20, 20, 20)
//                    val tvItemse: TextView = viewse.findViewById(R.id.TVtitle)
//                    val coverse: ImageView = viewse.findViewById(R.id.IVanimeCover)
//                    tvItemse.text = "Anime :: Sequel"
//                    LL.addView(viewse)
                    }
                    if (se != 0 && pre == 0) {
                        LL.visibility = View.VISIBLE

                        //only sequel
                        seq = mapOf(se!! to "pre", pre!! to "seq")!!
                        apiPreSeq(se)


//                        val viewse = layoutInflater.inflate(R.layout.anime_cover_item, null)
//                        val tvItem: TextView = viewse.findViewById(R.id.TVtitle)
//                        val cover: ImageView = viewse.findViewById(R.id.IVanimeCover)
//                        tvItem.text = "Anime -- sequal"
//                        LL.addView(viewse)

                    }
                    if (pre != 0 && se == 0) {
                        LL.visibility = View.VISIBLE

                        //only prequel
                        seq = mapOf(se!! to "pre", pre!! to "seq")!!
                        apiPreSeq(pre)

//                        val viewpre = layoutInflater.inflate(R.layout.anime_cover_item, null)
//                        val tvItem: TextView = viewpre.findViewById(R.id.TVtitle)
//                        val cover: ImageView = viewpre.findViewById(R.id.IVanimeCover)
//                        tvItem.text = "Anime -- prequal"
//                        LL.addView(viewpre)
                    }

                    // Creating season - year
                    val seasonSetText = "${seasonPeriodList[season!!]} - ${season_year}"
                    seasonYear.text = seasonSetText

                    //setting alternative title
                    jp.text = jpTitle

                    //setting description if available
                    if (descriptionRes != "") {
                        description.isVisible = true
                        var desc = "Description: \n\n $descriptionRes"
                        description.text = desc
                    } else {
                        description.isVisible = false
                    }

                    // Setting state || format of the anime
                    val setStatus = "${statusList[statusRes!!]}  ||  ${formatList[format!!]}"
                    status.text = setStatus

                    // creating and add genres to the app
                    if (gen != null) {
                        for (s in gen) {
                            val chip = Chip(this)
                            chip.text = s
                            chip.isClickable = false

                            val params = FlexboxLayout.LayoutParams(
                                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                                FlexboxLayout.LayoutParams.WRAP_CONTENT
                            )
                            params.setMargins(10, 0, 10, 0)
                            chip.layoutParams = params
                            genres.addView(chip)
                        }
                    }

                    //setting score and color of the anime
                    score.text = sc.toString()
                    if (color != null) {
                        score.setTextColor(Color.parseColor(color))
                    }

                    // play button listener when clicked opens activity of the anime episode
                    btnPlay.setOnClickListener {
                        val intent = Intent(this, VideoActivity::class.java)
                        intent.putExtra("id", id)
                        intent.putExtra("banner", getBanner)
                        startActivity(intent)
                    }
                } else {
                    val id: Int = response.body()?.data!!.id

                    addSeq(seq[id] == "pre", response)
                }

            } else {
                Log.d("Response", response.errorBody().toString())

            }
        })

        // API call
        viewModel.getPost(id!!.toInt())
    }

    private fun apiPreSeq(id: Int) {
        viewModel.getPost(id)
    }

    private fun addSeq(isPreseq: Boolean, response: Response<AnimePost>) {

        val viewpre = layoutInflater.inflate(R.layout.anime_cover_item, null)
        viewpre.setPadding(20, 20, 20, 20)
        val tvItempre: TextView = viewpre.findViewById(R.id.TVtitle)
        val coverpre: ImageView = viewpre.findViewById(R.id.IVanimeCover)

        val id = response.body()?.data?.id
        val title = response.body()?.data?.titles?.en
        val cover = response.body()?.data?.cover_image

        tvItempre.text = title
        coverpre.load(cover){
            crossfade(true)
            crossfade(1000)
            size(500, 750)
            transformations(RoundedCornersTransformation(30f))
        }

        viewpre.setOnClickListener {
            val intent = Intent(this, AnimeDetailsActivity::class.java)
            intent.putExtra("id", id.toString())
            intent.putExtra("title", title)
            intent.putExtra("image", cover)
            startActivity(intent) }

        if (isPreseq) {
            LL.addView(viewpre, 0)
        } else {
            LL.addView(viewpre)
        }
    }


    // ending current activity if the back button is pressed
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}