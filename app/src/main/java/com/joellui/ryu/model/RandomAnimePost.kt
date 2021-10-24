package com.joellui.ryu.model

data class RandomAnimePost(
    val status_code: Int,
    val message: String,
    val data: List<AnimeList>,
    val version: Int
)

data class AnimeList(
    val anilist_id: Int,
    val mal_id: Int,
    val format: String,
    val status: Int,
    val titles: AnimeTitle,
    val descriptions: AnimeDes,
    val start_date: String,
    val end_date: String,
    val season_period: Int,
    val season_year: Int,
    val episodes_count: Int,
    val episode_duration: Int,
    val cover_image: String,
    val cover_color:String,
    val genres: List<String>,
    val score: Int,
    val id: Int
)

data class AnimeTitle(
    val en:String,
    val jp: String
)

data class AnimeDes(
    val en: String
)
