package com.joellui.ryu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import android.content.Intent
import android.net.Uri


class DevFragment : Fragment(R.layout.fragment_dev) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val mainLayout = inflater.inflate(R.layout.fragment_dev,container,false)

        val ryu = mainLayout.findViewById<CardView>(R.id.ryu_main)
        val aniapi = mainLayout.findViewById<CardView>(R.id.aniapi_main)
        val support = mainLayout.findViewById<CardView>(R.id.support_main)
        val discord = mainLayout.findViewById<CardView>(R.id.discord_main)

        aniapi.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = Uri.parse("https://aniapi.com/")
            startActivity(intent)
        }

        support.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = Uri.parse("https://www.buymeacoffee.com/joellui")
            startActivity(intent)
        }

        discord.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = Uri.parse("https://discord.gg/VqvtjPj8bt")
            startActivity(intent)
        }

        return mainLayout
    }
}