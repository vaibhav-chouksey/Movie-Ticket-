package com.example.ticket.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticket.data.remote.TmdbApi
import com.example.ticket.model.Cast
import com.example.ticket.model.Movie
import com.example.ticket.model.MovieDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val api: TmdbApi,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _movie = MutableStateFlow<MovieDetail?>(null)
    val movie = _movie.asStateFlow()

    private val _castList = MutableStateFlow<List<Cast>>(emptyList())
    val castList = _castList.asStateFlow()

    private val _recommendations = MutableStateFlow<List<Movie>>(emptyList())
    val recommendations = _recommendations.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        // Get the ID passed from Home Screen
        val movieId = savedStateHandle.get<String>("movieId")
        if (movieId != null) {
            loadMovieData(movieId)
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

                // 2. Fetch Cast (NEW)
                val creditsResp = api.getMovieCredits(id, apiKey)
                if (creditsResp.isSuccessful) {
                    _castList.value = creditsResp.body()?.cast ?: emptyList()
                }

                // 3. Fetch Recommendations (NEW - Based on this specific ID)
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