package com.example.ticket.data.remote

import com.example.ticket.model.CreditsResponse
import com.example.ticket.model.Movie
import com.example.ticket.model.MovieDetail
import com.example.ticket.model.MovieResponse
import com.example.ticket.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    // 1. DISCOVER (General list)
    @GET("discover/movie")
    suspend fun getMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("sort_by") sortBy: String = "popularity.desc"
    ): Response<MovieResponse>

    // 2. NOW PLAYING (Added this! Essential for your Home Banner)
    @GET("movie/now_playing")
    suspend fun getNowPlaying(
        @Query("api_key") apiKey: String
    ): Response<MovieResponse>

    // 3. MOVIE DETAILS
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String
    ): Response<MovieDetail>

    // 4. LATEST (Kept it, but 'Now Playing' is safer for UI)
    @GET("movie/latest")
    suspend fun getLatestMovie(
        @Query("api_key") apiKey: String
    ): Response<Movie>

    // 5. RECOMMENDATIONS
    @GET("movie/{movie_id}/recommendations")
    suspend fun getRecommendations(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String
    ): Response<MovieResponse>

    // ... existing functions ...

    // 6. GET CAST / CREDITS
    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String
    ): Response<CreditsResponse>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("api_key") apiKey: String // <--- Added this param
    ): Response<SearchResponse>
}