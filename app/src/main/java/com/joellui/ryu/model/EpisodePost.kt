package com.joellui.ryu.model

data class EpisodePost(
    val status_code: Int,
    val message: String,
    var data: EpisodeData
)

data class EpisodeData(
    val current_page: Int,
    val count: Int,
    val documents: List<EpisodeDocument>
)

data class EpisodeDocument(
    val anime_id: Int,
    val number:Int,
    val title:String,
    val video:String,
    val source:String,
    val locale: String,
    val id: Int
)