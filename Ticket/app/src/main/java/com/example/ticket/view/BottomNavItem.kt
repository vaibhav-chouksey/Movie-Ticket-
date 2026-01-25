package com.example.ticket.view

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Favorite


sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Home")
    object Search : BottomNavItem("search", Icons.Default.Search, "Search")
    object Bookmark :
        BottomNavItem(route = "bookmark", Icons.Default.Favorite, label = "Bookmark")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Profile")
}