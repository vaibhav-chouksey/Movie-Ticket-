package com.example.ticket.repository

import com.example.ticket.data.remote.TmdbApi
import com.example.ticket.model.ApiKey
import com.example.ticket.model.MovieResponse
import retrofit2.Response
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: TmdbApi

) : MovieRepository{
    override suspend fun getMovies(): Response<MovieResponse> {
        return api.getMovies(ApiKey.key)
    }

}