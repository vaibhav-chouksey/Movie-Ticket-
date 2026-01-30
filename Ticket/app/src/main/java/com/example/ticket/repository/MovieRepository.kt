package com.example.ticket.repository

import android.graphics.Movie
import com.example.ticket.data.local.entity.BookmarkEntity
import com.example.ticket.model.MovieResponse
import com.example.ticket.model.SearchResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MovieRepository{
    suspend fun getMovies(): Response<MovieResponse>
    suspend fun searchMovies(query: String): Response<SearchResponse>

    suspend fun saveBookmark(movie: BookmarkEntity)
    suspend fun removeBookmark(movieId: String)
    fun isBookmarked(movieId: String): Flow<Boolean>
    fun getBookmarks(): Flow<List<BookmarkEntity>>

}
