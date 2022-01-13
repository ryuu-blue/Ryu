package com.joellui.ryu

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.adapters.LinearLayoutBindingAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.joellui.ryu.adapter.BannerAdapter
import com.joellui.ryu.adapter.BannerCover
import com.joellui.ryu.adapter.CoverData
import com.joellui.ryu.adapter.ListAdapter
import com.joellui.ryu.repositry.Repository

class MainFragment : Fragment(), BannerAdapter.OnClickListener, ListAdapter.OnClickListener {

    private lateinit var viewModel: MainViewModel

    var bannerlist = mutableListOf<BannerCover>()
    var coverlist = mutableListOf<CoverData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val mainLayout = inflater.inflate(R.layout.fragment_main, container, false)
        val stage = mainLayout.findViewById<ViewPager2>(R.id.VPbanner)
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        //top anime
        val rvTopAnime: RecyclerView = mainLayout.findViewById(R.id.rv_topAnime);
        rvTopAnime.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)

        //api reader
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.randomResponse.observe(viewLifecycleOwner, Observer { response ->

            if (response.isSuccessful) {

                val banner = BannerAdapter(bannerlist,this)

                for (i in response.body()?.data!!) {

                    if (i.cover_color != null){
                    bannerlist.add(
                        BannerCover(
                            i.titles.en,
                            i.cover_image,
                            i.id.toString(),
                            i.score.toString(),
                            i.cover_color
                        )
                    )
                    }
                }
                stage.adapter = banner
            }
        })

        // Top Anime
        viewModel.searchResponse.observe(viewLifecycleOwner, Observer { response ->

            if (response.isSuccessful) {
                val adapter = ListAdapter(coverlist,this)

                for (i in response.body()?.data!!.documents) {

                    if (i.cover_color != null){
                        coverlist.add(
                            CoverData(
                                i.titles.en,
                                i.cover_image,
                                i.id.toString()
                            )
                        )
                    }
                }

                rvTopAnime.adapter = adapter
            }
        })

        if (bannerlist.size < 5) {
            viewModel.getRandomAnime(5,false)
        }

        if (coverlist.isEmpty()){
        viewModel.getSearchAnime(
            formats = "0,1,2,3,4,5,6",
            status = "0,1,2,3",
            per_page = 20,
            sort_fields = "score",
            sort_directions = -1
        )}else{
            val adapter = ListAdapter(coverlist,this)
            rvTopAnime.adapter = adapter
        }

        val banner = BannerAdapter(bannerlist,this)
        stage.adapter = banner


        return mainLayout
    }

    override fun onClick(position: Int) {
        Toast.makeText(context, "" + bannerlist[position].id, Toast.LENGTH_SHORT).show()


        val intent = Intent(context, AnimeDetailsActivity::class.java)
        intent.putExtra("id", bannerlist[position].id)
        intent.putExtra("title", bannerlist[position].title)
        intent.putExtra("image", bannerlist[position].img)
        startActivity(intent)
    }

    override fun onClickItemList(position: Int) {
        Toast.makeText(context, "" + coverlist[position].id, Toast.LENGTH_SHORT).show()


        val intent = Intent(context, AnimeDetailsActivity::class.java)
        intent.putExtra("id", coverlist[position].id)
        intent.putExtra("title", coverlist[position].title)
        intent.putExtra("image", coverlist[position].img)
        startActivity(intent)
    }

}