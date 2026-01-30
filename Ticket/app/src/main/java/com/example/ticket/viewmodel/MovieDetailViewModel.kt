package com.example.ticket.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticket.data.local.entity.BookmarkEntity
import com.example.ticket.data.remote.TmdbApi
import com.example.ticket.model.Cast
import com.example.ticket.model.Movie
import com.example.ticket.model.MovieDetail
import com.example.ticket.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val api: TmdbApi,
    private val repository: MovieRepository, // <--- 1. INJECT REPOSITORY
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: String = checkNotNull(savedStateHandle["movieId"]) // Get ID immediately

    private val _movie = MutableStateFlow<MovieDetail?>(null)
    val movie = _movie.asStateFlow()

    private val _castList = MutableStateFlow<List<Cast>>(emptyList())
    val castList = _castList.asStateFlow()

    private val _recommendations = MutableStateFlow<List<Movie>>(emptyList())
    val recommendations = _recommendations.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    // --- 2. DATABASE STATE (Is this movie saved?) ---
    val isBookmarked: StateFlow<Boolean> = repository.isBookmarked(movieId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    init {
        loadMovieData(movieId)
    }

    // --- 3. TOGGLE BOOKMARK FUNCTION ---
    fun toggleBookmark(currentMovie: MovieDetail) {
        viewModelScope.launch {
            if (isBookmarked.value) {
                repository.removeBookmark(currentMovie.id.toString())
            } else {
                // Convert API Data -> Database Entity
                val entity = BookmarkEntity(
                    id = currentMovie.id.toString(),
                    title = currentMovie.name, // or .title depending on your model
                    posterUrl = "https://image.tmdb.org/t/p/w500${currentMovie.poster_path}",
                    rating = currentMovie.vote_average ?: 0.0
                )
                repository.saveBookmark(entity)
            }
        }
    }

    private fun loadMovieData(id: String) {
        viewModelScope.launch {
            val apiKey = "3a98302ee481b37dc0696c1cadb3cfb1"
            _isLoading.value = true

            try {
                // 1. Fetch Details
                val detailResp = api.getMovieDetails(id, apiKey)
                if (detailResp.isSuccessful) _movie.value = detailResp.body()

                // 2. Fetch Cast
                val creditsResp = api.getMovieCredits(id, apiKey)
                if (creditsResp.isSuccessful) {
                    _castList.value = creditsResp.body()?.cast ?: emptyList()
                }

                // 3. Fetch Recommendations
                val recsResp = api.getRecommendations(id, apiKey)
                if (recsResp.isSuccessful) {
                    _recommendations.value = recsResp.body()?.results ?: emptyList()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}