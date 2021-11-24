package com.joellui.ryu

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import coil.load
import com.joellui.ryu.adapter.EpisodeAdapter
import com.joellui.ryu.model.EpisodeDocument
import com.joellui.ryu.repositry.Repository
import android.content.SharedPreferences
import android.net.Uri
import android.webkit.WebView
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.TrackSelection
import java.text.FieldPosition

import com.google.android.exoplayer2.trackselection.DefaultTrackSelector

import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView

import android.widget.TextView
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProviders
import com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.upstream.*

import com.google.android.exoplayer2.util.DebugTextViewHelper
import java.net.URI
import com.google.android.exoplayer2.util.MimeTypes
import kotlinx.android.synthetic.main.activity_video.*

open class VideoActivity : AppCompatActivity(), EpisodeAdapter.OnClickListener {

    private lateinit var viewModel: MainViewModel

    lateinit var videoViewModel: VideoViewModel
    var episode = emptyList<EpisodeDocument>()
    var episodePlaylist = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        val bundle: Bundle? = intent.extras

        val anime_id = bundle?.getString("id")
        val banner = bundle?.getString("banner")

        val setBanner: ImageView = findViewById(R.id.IVbanner)
        val setDev: TextView = findViewById(R.id.dev)
        val dropDownButton: Spinner = findViewById(R.id.episodePage)
        val stage: RecyclerView = findViewById(R.id.rvEpisodes)
        var currentPage = -1

        // VideoViewModel
        videoViewModel = ViewModelProvider(this).get(VideoViewModel::class.java)
        videoViewModel.player.observe(this, Observer {
            playerView.player = it
        })
        videoViewModel.error.observe(this, Observer {
            playerErrorView.text = it
            playerErrorView.visibility = View.VISIBLE
        })

        val repo = Repository()
        val viewModelFactory = MainViewModelFactory(repo)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.episodeResponse.observe(this, Observer { response ->

            if (response.isSuccessful && response.body()?.data!!.current_page != currentPage) {
                currentPage = response.body()?.data!!.current_page
                episode = response.body()?.data!!.documents

                val adapter = EpisodeAdapter(episode,this)
                stage.adapter = adapter
                stage.layoutManager = GridLayoutManager(this@VideoActivity, 5)

                Log.d("Response", response.body()?.message.toString())
                setDev.text = response.body()?.message.toString()
                val lastPage = response.body()?.data?.last_page

                // MultiPage
                if (lastPage != 1) {
                    val view: View = findViewById(R.id.llspinner)
                    view.isVisible = true

                    var episodePages = emptyArray<String>()
                    var i = 1
                    while (i <= lastPage!!.toInt()) {
                        episodePages = arrayAppend(episodePages, i)
                        ++i
                    }

                    // Adapter
                    if (dropDownButton.adapter == null) {
                        val arrayAdapter = ArrayAdapter(this, R.layout.spinner_text, episodePages)
                        dropDownButton.adapter = arrayAdapter
                        arrayAdapter.setDropDownViewResource(R.layout.spinner_checked)
                    }

                    val dataSourceFactory = DefaultHttpDataSource.Factory()
                    val userAgent = WebView(application).settings.userAgentString
                    dataSourceFactory.setUserAgent(userAgent)

                    // one page
                } else {

                    var episodePages = emptyArray<String>()
                    var i = 1
                    while (i <= lastPage.toInt()) {
                        episodePages = arrayAppend(episodePages, i)
                        ++i
                    }

                    val arrayAdapter = ArrayAdapter(this, R.layout.spinner_text, episodePages)
                    dropDownButton.adapter = arrayAdapter
                    arrayAdapter.setDropDownViewResource(R.layout.spinner_checked)

                    Log.d("Response", "only 1 page")
                }

                //adding content to media list
                episodePlaylist.clear()

                for (i in episode) {
                    episodePlaylist.add(i.video)
                }

                Log.v("MSS", "WE ARE READY!")
                videoViewModel.setPlaylist(episodePlaylist)


            } else {

                Log.d("Response--$", response.code().toString())
                Log.d("Response--$", response.toString())

                when (response.code()) {
                    404 ->
                        setDev.text = response.message()
                    500 ->
                        setDev.text = response.message()
                    429 ->
                        setDev.text = response.message()
                    400 ->
                        setDev.text = response.message()
                    401 ->
                        setDev.text = response.message()
                    403 ->
                        setDev.text = response.message()
                }
            }
        })

        dropDownButton.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                Toast.makeText(this@VideoActivity, "${position + 1}", Toast.LENGTH_SHORT).show()
                viewModel.getEpisode(number = anime_id!!.toInt(), current_page = position + 1)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.getEpisode(anime_id!!.toInt())
            }
        }

        viewModel.getEpisode(anime_id!!.toInt())


        setBanner.load(banner) {
            crossfade(true)
            crossfade(1000)
        }

    }

    private fun arrayAppend(arr: Array<String>, element: Int): Array<String> {
        val list: MutableList<String> = arr.toMutableList()
        list.add("Part $element")
        return list.toTypedArray()
    }

    override fun OnClick(position: Int) {
        Toast.makeText(this, "episode -> "+episode[position].number, Toast.LENGTH_SHORT).show()
        videoViewModel.setEpisode(position)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}