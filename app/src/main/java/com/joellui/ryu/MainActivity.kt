package com.joellui.ryu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.joellui.ryu.repositry.Repository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val repository = Repository()
//        val viewModelFactory =  MainViewModelFactory(repository)
//        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
//        viewModel.getPost(11)
//        viewModel.myResponse.observe(this, Observer { response ->
//            if(response.isSuccessful){
//                Log.d("Response", response.body()?.status_code.toString())
//                Log.d("Response", response.body()?.message!!)
//                Log.d("Response", response.body()?.data?.titles?.en.toString())
//                Log.d("Response", response.body()?.data?.cover_image.toString())
//
//            }else{
//                Log.d("Response",response.errorBody().toString())
//            }
//        })



        val mf = MainFragment()
        val df = DevFragment()
        val sf = SearchFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.Flfragment, mf)
            commit()
        }


        btnNavigation.selectedItemId = R.id.miHome
        btnNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.miDev -> {

                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.Flfragment,df)
                        commit()
                    }

                    true
                }
                R.id.miHome -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.Flfragment, mf)
                        commit()
                    }
                    true
                }
                R.id.miSearch -> {

                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.Flfragment,sf)
                        commit()
                    }
                    true
                }

                R.id.miProfile -> {

                    //TODO: This is Yet to be made

                    Toast.makeText(applicationContext,"Profile",Toast.LENGTH_SHORT).show();
                    true
                }
                else -> false
            }
        }
    }


}

