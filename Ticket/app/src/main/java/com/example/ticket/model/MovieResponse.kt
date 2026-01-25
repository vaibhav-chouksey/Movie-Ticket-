package com.example.ticket.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val page: Int,
    @SerializedName("results")
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)