package com.example.ticket.view.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.ticket.view.component.MovieCard
import com.example.ticket.viewmodel.MovieDetailViewModel
import java.util.Locale

@Composable
fun MovieDetailScreen(
    viewModel: MovieDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onMovieClick: (String) -> Unit,
    onBookTicketClick: (String) -> Unit
) {
    val movie = viewModel.movie.collectAsState().value
    val cast = viewModel.castList.collectAsState().value
    val recommendations = viewModel.recommendations.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value

    // Observe Bookmark State
    val isBookmarked by viewModel.isBookmarked.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        if (isLoading || movie == null) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // --- 1. HEADER SECTION ---
                Box(modifier = Modifier.height(300.dp)) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("https://image.tmdb.org/t/p/w780${movie.backdrop_path}")
                            .crossfade(true)
                            .build(),
                        contentDescription = "Backdrop",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(250.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                                    startY = 50f
                                )
                            )
                    )

                    // Back Button Only
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopStart)
                            .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    // Floating Poster
                    Card(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 20.dp)
                            .width(110.dp)
                            .height(160.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                            contentDescription = movie.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                // --- 2. INFO SECTION ---
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = movie.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107))
                        Text(
                            text = String.format(Locale.US, "%.1f", movie.vote_average ?: 0.0),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Text(
                            text = movie.release_date?.take(4) ?: "N/A",
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color.LightGray.copy(alpha = 0.5f))

                    Text(text = "Storyline", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = movie.overview,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray,
                        lineHeight = 22.sp
                    )
                }

                // --- 3. CAST SECTION ---
                if (cast.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Cast",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(cast) { actor ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.width(80.dp)
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data("https://image.tmdb.org/t/p/w200${actor.profilePath}")
                                        .crossfade(true)
                                        .placeholder(android.R.drawable.ic_menu_gallery)
                                        .error(android.R.drawable.ic_menu_report_image)
                                        .build(),
                                    contentDescription = actor.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(CircleShape)
                                        .background(Color.LightGray)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = actor.name,
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }
                }

                // --- 4. RECOMMENDATIONS ---
                if (recommendations.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "You might also like",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(recommendations) { recMovie ->
                            Box(modifier = Modifier.width(140.dp)) {
                                MovieCard(movie = recMovie, onClick = { onMovieClick(recMovie.id.toString()) })
                            }
                        }
                    }
                }

                // --- 5. ACTION BUTTONS (BOOK & BOOKMARK) ---
                Spacer(modifier = Modifier.height(32.dp))

                // BUTTON 1: BOOK TICKET
                Button(
                    onClick = { onBookTicketClick(movie.id.toString()) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = "Book Ticket", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp)) // Spacing between buttons

                // BUTTON 2: BOOKMARK / WATCHLIST
                if (isBookmarked) {
                    // IF SAVED: Show "Remove" button (Red Outline)
                    OutlinedButton(
                        onClick = { viewModel.toggleBookmark(movie) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color.Red),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                    ) {
                        Icon(Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Remove from Watchlist", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                } else {
                    // IF NOT SAVED: Show "Add" button (Primary Color Outline)
                    OutlinedButton(
                        onClick = { viewModel.toggleBookmark(movie) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.BookmarkBorder, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Add to Watchlist", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}