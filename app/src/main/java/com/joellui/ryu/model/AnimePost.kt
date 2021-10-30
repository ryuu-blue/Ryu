package com.joellui.ryu.model


data class AnimePost(
    val status_code: Int,
    val message: String,
    var data: AnimeData
)

data class AnimeData(
    val anilist_id: Int,
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
    val sequel: Int,
    val prequel: Int,


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