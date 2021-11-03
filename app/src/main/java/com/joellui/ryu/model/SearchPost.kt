package com.joellui.ryu.model

data class SearchPost(
    val status_code: Int,
    val message: String,
    val data: AnimeResult
)

data class AnimeResult(
    val current_page: Int,
    val count: Int,
    val documents: List<AnimeData>,
    val last_page: Int
)
