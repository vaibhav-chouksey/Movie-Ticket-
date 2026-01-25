package com.example.ticket.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticket.data.remote.TmdbApi
import com.example.ticket.model.ApiKey
import com.example.ticket.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
// ... imports ...

@HiltViewModel
class HomeViewModel @Inject constructor(private val api: TmdbApi) : ViewModel() {

    private val _latestMovie = MutableStateFlow<Movie?>(null)
    val latestMovie = _latestMovie.asStateFlow()

    // 1. NEW: A list for the rest of the "Now Playing" movies
    private val _nowPlayingList = MutableStateFlow<List<Movie>>(emptyList())
    val nowPlayingList = _nowPlayingList.asStateFlow()

    private val _shawshankRecommendations = MutableStateFlow<List<Movie>>(emptyList())
    val shawshankRecommendations = _shawshankRecommendations.asStateFlow()

    private val _artRecommendations = MutableStateFlow<List<Movie>>(emptyList())
    val artRecommendations = _artRecommendations.asStateFlow()

    init {
        fetchHomeData()
    }

    private fun fetchHomeData() {
        viewModelScope.launch {
            val apiKey = "3a98302ee481b37dc0696c1cadb3cfb1"

            try {
                // --- A. FETCH NOW PLAYING ---
                val featuredResponse = api.getNowPlaying(apiKey)
                if (featuredResponse.isSuccessful) {
                    val movies = featuredResponse.body()?.results
                    if (!movies.isNullOrEmpty()) {
                        // 1. Take the FIRST movie for the big Banner
                        _latestMovie.value = movies[0]

                        // 2. Take the REST (drop the first one) for the list
                        // This prevents showing the same movie twice!
                        _nowPlayingList.value = movies.drop(1)
                    }
                }

                // --- B. Shawshank Logic (Keep as is) ---
                val shawshankResponse = api.getRecommendations("278", apiKey)
                if (shawshankResponse.isSuccessful) {
                    _shawshankRecommendations.value = shawshankResponse.body()?.results ?: emptyList()
                }

                // --- C. Art Logic (Keep as is) ---
                val artResponse = api.getRecommendations("1402", apiKey)
                if (artResponse.isSuccessful) {
                    _artRecommendations.value = artResponse.body()?.results ?: emptyList()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}