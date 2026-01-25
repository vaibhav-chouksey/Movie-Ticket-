package com.example.ticket.view.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ticket.model.Movie
import com.example.ticket.view.component.MovieCard
import com.example.ticket.viewmodel.HomeViewModel
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onMovieClick: (String) -> Unit
) {
    val latest = viewModel.latestMovie.collectAsState().value

    // 1. Collect the new list
    val nowPlaying = viewModel.nowPlayingList.collectAsState().value

    val shawshankRecs = viewModel.shawshankRecommendations.collectAsState().value
    val artRecs = viewModel.artRecommendations.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
    ) {
        // --- 1. HERO BANNER ---
        if (latest != null) {
            HeroBanner(movie = latest, onClick = { onMovieClick(latest.id.toString()) })
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 2. NEW RELEASES (The rest of the list) ---
        // Only show this if we actually have movies
        if (nowPlaying.isNotEmpty()) {
            SectionTitle(title = "New Releases")
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = nowPlaying) { movie ->
                    MovieCard(movie = movie, onClick = { onMovieClick(movie.id.toString()) })
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }

        // --- 3. SHAWSHANK RECS ---
        SectionTitle(title = "Because you liked Shawshank")
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = shawshankRecs) { movie ->
                MovieCard(movie = movie, onClick = { onMovieClick(movie.id.toString()) })
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // --- 4. ART RECS ---
        SectionTitle(title = "Feel Good Movies")
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = artRecs) { movie ->
                MovieCard(movie = movie, onClick = { onMovieClick(movie.id.toString()) })
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}


@Composable
fun HeroBanner(movie: Movie, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp) // Tall, cinematic height
            .clickable { onClick() }
    ) {
        // 1. Full Screen Image
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://image.tmdb.org/t/p/w780${movie.poster_path}")
                .crossfade(true)
                .build(),
            contentDescription = "Featured Movie",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.6f),
                            Color.Black.copy(alpha = 0.9f)
                        ),
                        startY = 300f
                    )
                )
        )

        // 3. Text Content
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            Text(
                text = "NOW SHOWING",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )

            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Button
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
            ) {
                Text(text = "Book Tickets", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// THIS IS THE MISSING PART CAUSING YOUR RED ERROR
@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}