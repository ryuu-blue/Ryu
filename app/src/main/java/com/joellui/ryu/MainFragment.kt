package com.joellui.ryu

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.joellui.ryu.adapter.BannerAdapter
import com.joellui.ryu.adapter.BannerCover
import com.joellui.ryu.repositry.Repository

class MainFragment : Fragment(), BannerAdapter.OnClickListener {

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


        if (bannerlist.size < 5) {
            viewModel.getRandomAnime(5)
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

}