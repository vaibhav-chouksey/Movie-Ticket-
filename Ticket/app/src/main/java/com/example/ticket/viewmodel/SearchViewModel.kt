package com.example.ticket.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticket.model.MovieItem
import com.example.ticket.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<MovieItem>>(emptyList())
    val searchResults: StateFlow<List<MovieItem>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var searchJob: Job? = null

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()

        if (query.isEmpty()) {
            _searchResults.value = emptyList()
            return
        }

        searchJob = viewModelScope.launch {
            delay(500)
            _isLoading.value = true
            Log.d("SearchDebug", "1. Starting search for: $query") // <--- LOG 1

            try {
                val response = repository.searchMovies(query)
                Log.d("SearchDebug", "2. API Response Code: ${response.code()}") // <--- LOG 2

                if (response.isSuccessful) {
                    val apiResults = response.body()?.results ?: emptyList()
                    Log.d("SearchDebug", "3. Found ${apiResults.size} movies") // <--- LOG 3

                    val mappedList = apiResults.map { movie ->
                        val validPosterUrl = if (movie.poster_path != null) {
                            "https://image.tmdb.org/t/p/w500${movie.poster_path}"
                        } else {
                            "https://via.placeholder.com/500x750?text=No+Image" // Fallback
                        }
                        MovieItem(
                            id = movie.id.toString(),
                            title = movie.title,
                            posterUrl = validPosterUrl,
                            rating = String.format("%.1f", movie.vote_average)
                        )
                    }
                    _searchResults.value = mappedList
                } else {
                    // Log the error body to see why it failed
                    val errorBody = response.errorBody()?.string()
                    Log.e("SearchDebug", "4. API Error: $errorBody") // <--- LOG 4
                }
            } catch (e: Exception) {
                Log.e("SearchDebug", "5. Exception: ${e.message}") // <--- LOG 5
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}