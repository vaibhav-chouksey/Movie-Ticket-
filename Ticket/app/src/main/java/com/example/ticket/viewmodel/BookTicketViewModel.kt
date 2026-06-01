package com.example.ticket.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticket.data.remote.TmdbApi
import com.example.ticket.manager.PaymentManager
import com.example.ticket.manager.PaymentStatus
import com.example.ticket.model.ApiKey
import com.example.ticket.model.MovieDetail
import com.example.ticket.model.MovieTicket
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BookTicketViewModel @Inject constructor(
    private val paymentManager: PaymentManager,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val api: TmdbApi,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val movieId: String = checkNotNull(savedStateHandle["movieId"])

    private val _bookingState = MutableStateFlow<BookingState>(BookingState.Idle)
    val bookingState = _bookingState.asStateFlow()

    private val _movieDetail = MutableStateFlow<MovieDetail?>(null)
    val movieDetail = _movieDetail.asStateFlow()

    // Price configuration
    val ticketPrice = 250.00

    // Selection States hoisted to ViewModel
    private val _selectedSeats = MutableStateFlow<List<Int>>(emptyList())
    val selectedSeats = _selectedSeats.asStateFlow()

    private val _selectedDateIndex = MutableStateFlow(0)
    val selectedDateIndex = _selectedDateIndex.asStateFlow()

    private val _selectedTimeIndex = MutableStateFlow(0)
    val selectedTimeIndex = _selectedTimeIndex.asStateFlow()

    // Calculated total price
    val totalPrice: StateFlow<Double> = _selectedSeats
        .map { it.size * ticketPrice }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    val dates = getNext7Days()
    val times = listOf("10:00 AM", "12:30 PM", "03:00 PM", "06:15 PM", "09:00 PM")

    init {
        fetchMovieDetail()
        observePaymentResult()
    }

    private fun fetchMovieDetail() {
        viewModelScope.launch {
            try {
                val response = api.getMovieDetails(movieId, ApiKey.key)
                if (response.isSuccessful) {
                    _movieDetail.value = response.body()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun selectDate(index: Int) {
        _selectedDateIndex.value = index
    }

    fun selectTime(index: Int) {
        _selectedTimeIndex.value = index
    }

    fun toggleSeat(seatId: Int) {
        val current = _selectedSeats.value.toMutableList()
        if (current.contains(seatId)) {
            current.remove(seatId)
        } else {
            current.add(seatId)
        }
        _selectedSeats.value = current
    }

    private fun observePaymentResult() {
        viewModelScope.launch {
            paymentManager.paymentResult.collect { status ->
                when (status) {
                    is PaymentStatus.Success -> {
                        confirmBookingInDatabase(status.paymentId)
                    }
                    is PaymentStatus.Error -> {
                        _bookingState.value = BookingState.Error(status.message)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun confirmBookingInDatabase(paymentId: String) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            _bookingState.value = BookingState.Error("User not logged in")
            return
        }
        val movie = _movieDetail.value
        if (movie == null) {
            _bookingState.value = BookingState.Error("Movie details not loaded")
            return
        }
        if (_selectedSeats.value.isEmpty()) {
            _bookingState.value = BookingState.Error("No seats selected")
            return
        }

        _bookingState.value = BookingState.Loading

        val dateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val selectedDateStr = dateFormatter.format(dates[_selectedDateIndex.value])
        val selectedTimeStr = times[_selectedTimeIndex.value]

        // Create booking ID
        val bookingId = firestore.collection("bookings").document().id

        val ticket = MovieTicket(
            ticketId = bookingId,
            userId = uid,
            movieId = movieId,
            movieTitle = movie.name,
            posterUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
            seats = _selectedSeats.value,
            date = selectedDateStr,
            time = selectedTimeStr,
            totalPrice = _selectedSeats.value.size * ticketPrice,
            paymentId = paymentId,
            bookingTimestamp = System.currentTimeMillis()
        )

        firestore.collection("bookings").document(bookingId).set(ticket)
            .addOnSuccessListener {
                _bookingState.value = BookingState.Success
            }
            .addOnFailureListener { e ->
                _bookingState.value = BookingState.Error(e.message ?: "Failed to save booking details")
            }
    }

    private fun getNext7Days(): List<Date> {
        val calendar = Calendar.getInstance()
        val datesList = mutableListOf<Date>()
        for (i in 0 until 7) {
            datesList.add(calendar.time)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return datesList
    }
}

sealed class BookingState {
    object Idle : BookingState()
    object Loading : BookingState()
    object Success : BookingState()
    data class Error(val msg: String) : BookingState()
}