package com.example.ticket.view.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

//import com.example.ticket.model.Movie
//import com.example.ticket.viewmodel.MovieViewModel
//
//@Composable
//fun MovieScreen(
//    viewModel: MovieViewModel = hiltViewModel(),
//    onMovieClick: (Movie) -> Unit // <--- Add this parameter
//) {
//    val movies = viewModel.movies.collectAsState().value
//
//    LazyColumn {
//        items(
//            movies) { movie ->
//            // Make the row clickable
//            Row(
//                modifier = Modifier
//                    .clickable { onMovieClick(movie) } // <--- Trigger the click
//                    .padding(16.dp)
//            ) {
//                MovieRow(movie) // Your existing row design
//            }
//        }
//    }
//}
//@Composable
//fun MovieRow(movie: Movie) {
//    Column(modifier = Modifier.padding(16.dp)) {
//        Text(
//            text = movie.title, // Display the title
//            style = MaterialTheme.typography.titleMedium
//        )
//        Text(
//            text = "Rating: ${movie.vote_average}",
//            style = MaterialTheme.typography.bodySmall
//        )
//    }
//}