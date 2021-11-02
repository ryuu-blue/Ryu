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


        val mf = MainFragment()
        val df = DevFragment()
        val sf = SearchFragment()
        val pf = ProfileFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.Flfragment, mf)
            commit()
        }

        //changing fragments form one on selecting
        btnNavigation.selectedItemId = R.id.miHome
        btnNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.miDev -> {

                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.Flfragment, df)
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
                        replace(R.id.Flfragment, sf)
                        commit()
                    }
                    true
                }

                R.id.miProfile -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.Flfragment,pf)
                        commit()
                    }

                    true
                }
                else -> false
            }
        }
    }


}

