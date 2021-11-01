package com.joellui.ryu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.joellui.ryu.adapter.BannerAdapter
import com.joellui.ryu.adapter.BannerCover
import com.joellui.ryu.adapter.CoverData
import com.joellui.ryu.adapter.GridAdapter
import com.joellui.ryu.repositry.Repository

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    var bannerlist = mutableListOf<BannerCover>(

    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val mainLayout = inflater.inflate(R.layout.fragment_main, container, false)

        val stage = mainLayout.findViewById<ViewPager2>(R.id.VPbanner)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        //api reader
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.randomResponse.observe(viewLifecycleOwner, Observer { response ->

            if (response.isSuccessful) {
                val banner = BannerAdapter(bannerlist)

                for (i in response.body()?.data!!) {

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
                stage.adapter = banner

            }
        })


        if (bannerlist.size < 3) {
            viewModel.getRandomAnime(3)
        }

        val banner = BannerAdapter(bannerlist)
        stage.adapter = banner


        return mainLayout
    }
}