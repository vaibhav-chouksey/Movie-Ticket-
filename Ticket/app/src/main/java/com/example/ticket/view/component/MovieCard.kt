package com.example.ticket.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.ticket.model.Movie
import coil.compose.AsyncImage // <--- Import this

@Composable
fun MovieCard(movie: Movie, onClick: () -> Unit) {
    // 1. Construct the full URL
    val imageUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}"

    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(140.dp) // Perfect width for posters
            .height(240.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp) // Rounded corners look modern
    ) {
        Column {
            // 2. The Image Component
            AsyncImage(
                model = imageUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp), // Takes up most of the card
                contentScale = ContentScale.Crop // Fills the space nicely
            )

            // 3. The Title
            Text(
                text = movie.title,
                maxLines = 1, // Only 1 line to keep it neat
                overflow = TextOverflow.Ellipsis, // Adds "..." if too long
                modifier = Modifier
                    .padding(8.dp),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}