package com.example.ticket.view.screen



import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

// 1. DATA CLASS (Same as your reference)
data class NavItem(
    val label: String,
    val icon: ImageVector
)

@Composable
fun BaseSCreen(modifier: Modifier = Modifier, navController: NavController) {

    // 2. LIST OF TABS
    val navItemList = listOf(
        NavItem(label = "Home", icon = Icons.Default.Home),
        NavItem(label = "Search", icon = Icons.Default.Search),
        NavItem(label = "Bookmark", icon = Icons.Default.Bookmark),
        NavItem(label = "Profile", icon = Icons.Default.Person),
    )

    // 3. STATE FOR SELECTED TAB
    var selectedIndex by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = index == selectedIndex,
                        onClick = {
                            selectedIndex = index
                        },
                        icon = {
                            Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                        },
                        label = {
                            Text(text = navItem.label)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // 4. CONTENT SWITCHER
        // We pass the padding to avoid content hiding behind the bar
        ContentScreen(
            modifier = modifier.padding(innerPadding),
            selectedIndex = selectedIndex,
            navController = navController
        )
    }
}

@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    navController: NavController
) {
    when (selectedIndex) {
        0 -> HomeScreen(
            viewModel = TODO(),
            onMovieClick = TODO()
        ) // Real Home Page
        1 -> SearchScreen()
        2 -> BookmarkScreen()
        3 -> ProfileScreen(onLogout = {
            // Logic to logout
            navController.navigate("login") { popUpTo(0) }
        })
    }
}

