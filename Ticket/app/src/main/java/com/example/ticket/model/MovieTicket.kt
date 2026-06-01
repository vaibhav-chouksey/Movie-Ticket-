package com.example.ticket.model

data class MovieTicket(
    val ticketId: String = "",
    val userId: String = "",
    val movieId: String = "",
    val movieTitle: String = "",
    val posterUrl: String = "",
    val seats: List<Int> = emptyList(),
    val date: String = "",
    val time: String = "",
    val totalPrice: Double = 0.0,
    val paymentId: String = "",
    val bookingTimestamp: Long = 0L
)
