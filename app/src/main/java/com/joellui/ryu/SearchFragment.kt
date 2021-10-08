package com.joellui.ryu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joellui.ryu.adapter.CoverData
import com.joellui.ryu.adapter.GridAdapter
import com.joellui.ryu.repositry.Repository
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    private lateinit var viewModel: MainViewModel



    var cover = mutableListOf<CoverData>(
//        CoverData("One Piece", "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/nx21-tXMN3Y20PIL9.jpg"),
    )



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mainLayout = inflater.inflate(R.layout.fragment_search,container,false);
        val stage = mainLayout.findViewById<RecyclerView>(R.id.stage_rv);

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.myResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response.isSuccessful) {
//                    Log.d("Response", response.body()?.status_code.toString())
//                    Log.d("Response", response.body()?.message!!)
//                    Log.d("Response",response.body()?.data?.id.toString())
                Log.d("Response", response.body()?.data?.titles?.en.toString())
//                    Log.d("Response", response.body()?.data?.cover_image.toString())


                val adapter = GridAdapter(cover)

                cover.add(
                    CoverData(
                        response.body()?.data?.titles?.en.toString(),
                        response.body()?.data?.cover_image.toString()
                    )
                )

                stage.adapter = adapter

            } else {
                Log.d("Response", response.errorBody().toString())
            }
        })

        if (cover.size < 9) {
            for (i in 1..9) {
                viewModel.getPost((0..100).random())
            }
        }

        Log.d("Response", cover.toString())


        val adapter = GridAdapter(cover)
        stage.adapter = adapter
        stage.layoutManager = GridLayoutManager(activity, 3);

        return mainLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//
//        stage_rv.adapter = adapter
//        stage_rv.layoutManager = GridLayoutManager(activity,3)


    }



}