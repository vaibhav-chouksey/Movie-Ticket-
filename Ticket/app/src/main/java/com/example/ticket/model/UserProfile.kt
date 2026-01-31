package com.example.ticket.model

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val dateOfBirth: String = "",
    val country: String = "",
    val favoriteGenres: List<String> = emptyList()
)