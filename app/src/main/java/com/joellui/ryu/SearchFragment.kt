package com.joellui.ryu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joellui.ryu.adapter.CoverData
import com.joellui.ryu.adapter.GridAdapter
import com.joellui.ryu.adapter.SearchAdapter
import com.joellui.ryu.model.AnimeData
import com.joellui.ryu.repositry.Repository

class SearchFragment : Fragment(), GridAdapter.OnClickListener, SearchAdapter.OnClickListener {

    private lateinit var viewModel: MainViewModel

    var search_result = mutableListOf<AnimeData>()
    //test
    var cover = mutableListOf<CoverData>(
        CoverData(
            "One Piece",
            "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/nx21-tXMN3Y20PIL9.jpg",
            "11",
        ),
        CoverData(
            "Saiyuuki RELOAD GUNLOCK",
            "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/131.jpg",
            "86"
        )
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mainLayout = inflater.inflate(R.layout.fragment_search, container, false)
        val stage = mainLayout.findViewById<RecyclerView>(R.id.stage_rv)
        val searchRV = mainLayout.findViewById<RecyclerView>(R.id.search_result)
        val searchView = mainLayout.findViewById<SearchView>(R.id.search_bar)
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        //search api
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    stage.visibility = View.GONE
                    searchRV.visibility = View.VISIBLE
                    viewModel.getSearchAnime(query)
                }
                return false
            }



            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText == "") {
                    stage.visibility = View.VISIBLE
                    searchRV.visibility = View.GONE
                }
                return false
            }

        })
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.searchResponse.observe(viewLifecycleOwner, Observer { result ->
            if (result.isSuccessful){


                val searchResultAdapter = SearchAdapter(result.body()?.data?.documents!!,this@SearchFragment)
                if(search_result.isEmpty()) {
                    search_result.addAll(result.body()?.data?.documents!!)
                    searchRV.adapter = searchResultAdapter
                }else{
                    search_result.clear()
                    search_result.addAll(result.body()?.data?.documents!!)
                    searchRV.adapter = searchResultAdapter
                }
            } else {
                Toast.makeText(context, result.message(), Toast.LENGTH_SHORT).show()
            }
        })



        // api reader
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.myResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response.isSuccessful) {
                //adapter that set cover img for the anime
                val adapter = GridAdapter(cover, this)

                //adding anime cover to the list
                cover.add(
                    CoverData(
                        response.body()?.data?.titles?.en.toString(),
                        response.body()?.data?.cover_image.toString(),
                        response.body()?.data?.id.toString()
                    )
                )

                stage.adapter = adapter

            } else {
                Log.d("Response", response.errorBody().toString())
            }
        })

        // List of 9 random anime generator
        if (cover.size < 9) {
            for (i in 1..9) {
                viewModel.getPost((0..100).random())
            }
        }


        val adapter = GridAdapter(cover, this)
        stage.adapter = adapter
        stage.layoutManager = GridLayoutManager(activity, 3)

        return mainLayout
    }

    //response  recyclerview onclick to open anime details page
    override fun onClick(position: Int) {
        Toast.makeText(context, "" + cover[position].id, Toast.LENGTH_SHORT).show()

        val intent = Intent(context, AnimeDetailsActivity::class.java)
        intent.putExtra("id", cover[position].id)
        intent.putExtra("title", cover[position].title)
        intent.putExtra("image", cover[position].img)
        startActivity(intent)
    }

    override fun searchResultClick(position: Int) {
        Toast.makeText(context, ""+search_result[position].id, Toast.LENGTH_SHORT).show()

        val intent = Intent(context, AnimeDetailsActivity::class.java)
        intent.putExtra("id",search_result[position].id.toString())
        intent.putExtra("title",search_result[position].titles.en)
        intent.putExtra("image",search_result[position].cover_image)
        startActivity(intent)
    }

}