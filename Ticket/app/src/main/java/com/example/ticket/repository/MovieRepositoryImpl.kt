package com.example.ticket.repository

import com.example.ticket.data.local.dao.BookmarkDao
import com.example.ticket.data.local.entity.BookmarkEntity
import com.example.ticket.data.remote.TmdbApi
import com.example.ticket.model.ApiKey
import com.example.ticket.model.MovieResponse
import com.example.ticket.model.SearchResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: TmdbApi,
    private val dao: BookmarkDao

) : MovieRepository{
    override suspend fun getMovies(): Response<MovieResponse> {
        return api.getMovies(ApiKey.key)
    }

    override suspend fun searchMovies(query: String): Response<SearchResponse> {
        // We pass the API Key here, just like in getMovies()
        return api.searchMovies(query, ApiKey.key)
    }

    // Database Calls
    override suspend fun saveBookmark(movie: BookmarkEntity) = dao.insertBookmark(movie)
    override suspend fun removeBookmark(movieId: String) = dao.deleteBookmark(movieId)
    override fun isBookmarked(movieId: String): Flow<Boolean> = dao.isBookmarked(movieId)
    override fun getBookmarks(): Flow<List<BookmarkEntity>> = dao.getAllBookmarks()


}