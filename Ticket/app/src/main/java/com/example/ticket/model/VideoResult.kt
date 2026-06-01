package com.example.ticket.model



import com.google.gson.annotations.SerializedName

data class VideoResult(
    @SerializedName("key")      val key: String,
    @SerializedName("name")     val name: String,
    @SerializedName("site")     val site: String,
    @SerializedName("type")     val type: String,
    @SerializedName("official") val official: Boolean
)

data class VideosResponse(
    @SerializedName("id")      val id: Int,
    @SerializedName("results") val results: List<VideoResult>
)