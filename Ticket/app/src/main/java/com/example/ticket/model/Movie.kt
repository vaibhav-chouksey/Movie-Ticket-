package com.example.ticket.model

import com.google.gson.annotations.SerializedName

data class Movie(
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path")
    val poster_path: String,
    val release_date: String,
    @SerializedName("title")
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)