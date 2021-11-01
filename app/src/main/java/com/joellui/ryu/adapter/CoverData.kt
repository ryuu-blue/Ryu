package com.joellui.ryu.adapter

data class CoverData(
    val title: String,
    val img: String,
    val id: String
)

data class BannerCover(
    val title: String,
    val img: String,
    val id: String,
    val score: String,
    val cover_color: String
)