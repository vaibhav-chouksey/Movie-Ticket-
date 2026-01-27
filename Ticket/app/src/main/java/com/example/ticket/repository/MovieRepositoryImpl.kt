package com.example.ticket.repository

import com.example.ticket.data.remote.TmdbApi
import com.example.ticket.model.ApiKey
import com.example.ticket.model.MovieResponse
import com.example.ticket.model.SearchResponse
import retrofit2.Response
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: TmdbApi

) : MovieRepository{
    override suspend fun getMovies(): Response<MovieResponse> {
        return api.getMovies(ApiKey.key)
    }

    override suspend fun searchMovies(query: String): Response<SearchResponse> {
        // We pass the API Key here, just like in getMovies()
        return api.searchMovies(query, ApiKey.key)
    }

}