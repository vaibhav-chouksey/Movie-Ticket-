package com.example.ticket.navigation



import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite

import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("tab_home", Icons.Default.Home, "Home")
    object Search : BottomNavItem("tab_search", Icons.Default.Search, "Search")
    object Bookmark : BottomNavItem(
        route = "tab_watchlist",
        icon = Icons.Default.RemoveRedEye,
        label = "WatchList"
    )

    object Profile : BottomNavItem("tab_profile", Icons.Default.Person, "Profile")
}