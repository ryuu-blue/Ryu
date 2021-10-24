package com.joellui.ryu

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


class VideoActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    var episode = emptyList<EpisodeDocument>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        val bundle: Bundle? = intent.extras

        val anime_id = bundle?.getString("id")
        val banner = bundle?.getString("banner")

        val animePlay: VideoView = findViewById(R.id.Video)
        val setBanner: ImageView = findViewById(R.id.IVbanner)
        val setDev: TextView = findViewById(R.id.dev)
        val dropDownButton: Spinner = findViewById(R.id.episodePage)
        val stage: RecyclerView = findViewById(R.id.rvEpisodes)

        val repo = Repository()
        val viewModelFactory = MainViewModelFactory(repo)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.episodeResponse.observe(this, Observer { response ->

            if (response.isSuccessful) {

                episode = response.body()?.data!!.documents

                val adapter = EpisodeAdapter(episode)
                stage.adapter = adapter
                stage.layoutManager = GridLayoutManager(this@VideoActivity, 5)

                Log.d("Response", response.body()?.message.toString())
                setDev.text = response.body()?.message.toString()
                val lastPage = response.body()?.data?.last_page

                // if there is more than one page
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

    fun arrayAppend(arr: Array<String>, element: Int): Array<String> {
        val list: MutableList<String> = arr.toMutableList()
        list.add("Part $element")
        return list.toTypedArray()
    }

}