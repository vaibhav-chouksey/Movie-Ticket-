package com.example.ticket.viewmodel

// 1. IMPORT YOUR NEW CLASS NAME
import com.example.ticket.model.Movie

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.ticket.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
//class MovieViewModel @Inject constructor(
//    private val repository: MovieRepository
//) : ViewModel() {
//
//    // 2. USE 'Movie' HERE (Not Result)
//    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
//    val movies: StateFlow<List<Movie>> = _movies
//
//    init {
//        fetchMovies()
//    }
//
//    private fun fetchMovies() {
//        viewModelScope.launch {
//            try {
//                val response = repository.getMovies()
//                if (response.isSuccessful) {
//                    // 3. YOUR RESPONSE NOW CONTAINS List<Movie>
//                    val movieList = response.body()?.results ?: emptyList()
//
//                    _movies.value = movieList
//                    Log.d("MovieCheck", "Success! Found ${movieList.size} movies")
//                } else {
//                    Log.e("MovieCheck", "API Error: ${response.code()}")
//                }
//            } catch (e: Exception) {
//                Log.e("MovieCheck", "Exception: ${e.message}")
//            }
//        }
//    }
//}