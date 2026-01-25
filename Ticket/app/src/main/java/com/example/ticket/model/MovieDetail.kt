package com.example.ticket.model

import com.google.gson.annotations.SerializedName

// In model/MovieDetail.kt
data class MovieDetail(
    val id: Int,
    @SerializedName("title") val name: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val poster_path: String?,
    @SerializedName("backdrop_path") val backdrop_path: String?,
    @SerializedName("vote_average") val vote_average: Double?,
    @SerializedName("release_date") val release_date: String?
)