package com.example.ticket.view.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ticket.MainActivity
import com.example.ticket.viewmodel.BookTicketViewModel
import com.example.ticket.viewmodel.BookingState
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// --- DATA CLASSES (Ensure these are here) ---
data class Seat(val id: Int, val status: SeatStatus)
enum class SeatStatus { AVAILABLE, SELECTED, BOOKED }

@Composable
fun BookTicketScreen(
    onBookingComplete: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: BookTicketViewModel = hiltViewModel()
) {
    // 1. CONTEXT & VIEWMODEL STATE
    val context = LocalContext.current
    val bookingState by viewModel.bookingState.collectAsState()

    // 2. UI STATE (Seats, Dates, Price)
    // We need to bring these back so 'totalPrice' exists!
    val ticketPrice = 250.00
    val selectedSeats = remember { mutableStateListOf<Int>() }

    // THE MISSING VARIABLE IS HERE:
    val totalPrice = selectedSeats.size * ticketPrice

    val totalSeats = remember {
        List(42) { id ->
            val isBooked = id in listOf(2, 3, 8, 9, 25, 26, 35)
            Seat(id, if (isBooked) SeatStatus.BOOKED else SeatStatus.AVAILABLE)
        }
    }
    var selectedDateIndex by remember { mutableStateOf(0) }
    var selectedTimeIndex by remember { mutableStateOf(0) }
    val dates = remember { getNext7Days() } // Ensure getNext7Days() helper is in the file or Utils

    // 3. LISTEN FOR SUCCESS (Architecture Part)
    LaunchedEffect(bookingState) {
        if (bookingState is BookingState.Success) {
            onBookingComplete()
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, CircleShape)
                        .border(1.dp, Color.LightGray, CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Select Seats", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Iron Man 3 • King Class", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
        },
        bottomBar = {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                elevation = CardDefaults.cardElevation(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Total Price", color = Color.Gray, fontSize = 14.sp)
                        Text(
                            text = "₹${String.format("%.2f", totalPrice)}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // --- INDUSTRY LEVEL BUTTON ---
                    Button(
                        onClick = {
                            if (context is MainActivity) {
                                // Now 'totalPrice' is recognized because we defined it above
                                context.startPayment(totalPrice)
                            }
                        },
                        // Disable button if loading or no seats selected
                        enabled = selectedSeats.isNotEmpty() && bookingState !is BookingState.Loading,
                        modifier = Modifier.height(54.dp).width(180.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        if (bookingState is BookingState.Loading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Pay & Book", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(horizontal = 16.dp)
        ) {
            // --- RE-USE YOUR EXISTING UI COMPONENTS ---

            // 1. Date Selector
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(dates.size) { index ->
                    DateCard(
                        date = dates[index],
                        isSelected = selectedDateIndex == index,
                        onClick = { selectedDateIndex = index }
                    )
                }
            }

            // 2. Time Selector
            Spacer(modifier = Modifier.height(24.dp))
            val times = listOf("10:00 AM", "12:30 PM", "03:00 PM", "06:15 PM", "09:00 PM")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(times.size) { index ->
                    TimeChip(
                        time = times[index],
                        isSelected = selectedTimeIndex == index,
                        onClick = { selectedTimeIndex = index }
                    )
                }
            }

            // 3. Screen Visual
            Spacer(modifier = Modifier.height(40.dp))
            CinemaScreenVisual()
            Spacer(modifier = Modifier.height(20.dp))

            // 4. Seat Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(totalSeats) { seat ->
                    SeatComposable(
                        seat = seat,
                        isSelected = selectedSeats.contains(seat.id),
                        onSeatClick = {
                            if (seat.status == SeatStatus.AVAILABLE) {
                                if (selectedSeats.contains(seat.id)) selectedSeats.remove(seat.id)
                                else selectedSeats.add(seat.id)
                            }
                        }
                    )
                }
            }

            // 5. Legend
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                LegendItem(color = Color.White, text = "Available", hasBorder = true)
                LegendItem(color = MaterialTheme.colorScheme.primary, text = "Selected")
                LegendItem(color = Color(0xFFE0E0E0), text = "Booked")
            }
        }
    }
}
fun getNext7Days(): List<Date> {
    val calendar = Calendar.getInstance()
    val dates = mutableListOf<Date>()

    // Add today + next 6 days
    for (i in 0 until 7) {
        dates.add(calendar.time)
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }
    return dates
}

@Composable
fun DateCard(date: Date, isSelected: Boolean, onClick: () -> Unit) {
    // 2. STANDARD DATE FORMATTERS
    val dayFormatter = SimpleDateFormat("EEE", Locale.getDefault()) // "Mon"
    val numFormatter = SimpleDateFormat("dd", Locale.getDefault())  // "15"

    Card(
        modifier = Modifier
            .width(60.dp)
            .height(80.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.White
        ),
        elevation = CardDefaults.cardElevation(if (isSelected) 8.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = numFormatter.format(date),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else Color.Black
            )
            Text(
                text = dayFormatter.format(date),
                fontSize = 12.sp,
                color = if (isSelected) Color.White.copy(alpha = 0.8f) else Color.Gray
            )
        }
    }
}

// ... (Other components like TimeChip, SeatComposable, LegendItem remain the same) ...

@Composable
fun TimeChip(time: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .border(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray, RoundedCornerShape(50))
            .clip(RoundedCornerShape(50))
            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(time, color = if (isSelected) Color.White else Color.Black, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
fun SeatComposable(seat: Seat, isSelected: Boolean, onSeatClick: () -> Unit) {
    val seatColor = when {
        seat.status == SeatStatus.BOOKED -> Color(0xFFE0E0E0)
        isSelected -> MaterialTheme.colorScheme.primary
        else -> Color.White
    }
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(seatColor)
            .border(1.dp, if (seat.status == SeatStatus.AVAILABLE && !isSelected) Color.LightGray else Color.Transparent, RoundedCornerShape(8.dp))
            .clickable(enabled = seat.status == SeatStatus.AVAILABLE) { onSeatClick() }
    )
}

@Composable
fun CinemaScreenVisual() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Canvas(modifier = Modifier.fillMaxWidth().height(20.dp)) {
            val path = Path().apply {
                moveTo(0f, size.height)
                quadraticBezierTo(size.width / 2, 0f, size.width, size.height)
            }
            drawPath(path, Color.LightGray, style = Stroke(width = 5f))
        }
        Text("SCREEN", color = Color.Gray, fontSize = 10.sp, letterSpacing = 2.sp)
    }
}

@Composable
fun LegendItem(color: Color, text: String, hasBorder: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(color).border(if (hasBorder) 1.dp else 0.dp, Color.Gray, CircleShape))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = Color.Gray, fontSize = 12.sp)
    }
}