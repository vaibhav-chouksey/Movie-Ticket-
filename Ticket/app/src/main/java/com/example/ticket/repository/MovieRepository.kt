package com.example.ticket.repository

import android.graphics.Movie
import com.example.ticket.model.MovieResponse
import retrofit2.Response

interface MovieRepository{
    suspend fun getMovies(): Response<MovieResponse>
}
