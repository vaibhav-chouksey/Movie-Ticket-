package com.example.ticket.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    // The API sends "results", so we map it to our list
    @SerializedName("results")
    val results: List<SearchMovie>
)