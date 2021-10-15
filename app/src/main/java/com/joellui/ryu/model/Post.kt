package com.joellui.ryu.model


data class Post(
    val status_code: Int,
    val message: String,
    var data: Data
)

data class Data(
    val anilist_id:Int,
    val id: Int,
    val format: Int,
    val score: Int,
    val status: Int,
    val start_date: String,
    val end_date: String,
    val season_period: Int,
    val season_year: Int,
    val episodes_count: Int,
    val episode_duration: Int,
    val cover_image: String,
    val cover_color: String,
    val banner_image: String,
    val genres: List<String>,


    val descriptions: Descriptions,
    val titles: Title
)

data class Title(
    val en: String,
    val jp: String
)

data class Descriptions(
    val en: String?,
    val it: String?
)