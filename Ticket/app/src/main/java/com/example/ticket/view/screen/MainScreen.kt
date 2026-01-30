package com.example.ticket.view.screen



import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ticket.navigation.BottomNavItem
import com.example.ticket.viewmodel.HomeViewModel

@Composable
fun MainScreen(
    rootNavController: NavController // Passed down to handle "Full Screen" navigations (like Details)
) {
    // 1. CREATE INNER NAV CONTROLLER (Manages Tabs only)
    val tabNavController = rememberNavController()

    val navItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Bookmark,
        BottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                navItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        // Check if we are on this route OR a child of this route
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            tabNavController.navigate(item.route) {
                                // 2. BEST PRACTICE NAVIGATION LOGIC

                                // Pop up to the start destination to avoid a huge stack of tabs
                                popUpTo(tabNavController.graph.findStartDestination().id) {
                                    saveState = true // SAVES SCROLL POSITION
                                }
                                // Avoid multiple copies of the same screen
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // 3. INNER NAV HOST (Swaps tabs)
        NavHost(
            navController = tabNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            // --- TAB 1: HOME ---
            composable(BottomNavItem.Home.route) {
                // HILT INJECTION HAPPENS HERE
                val viewModel: HomeViewModel = hiltViewModel()

                HomeScreen(
                    viewModel = viewModel,
                    onMovieClick = { movieId ->
                        // Use ROOT controller to go Full Screen (cover bottom bar)
                        rootNavController.navigate("movie_detail/$movieId")
                    }
                )
            }

            // --- TAB 2: SEARCH ---
            composable(BottomNavItem.Search.route) {
                // val searchViewModel: SearchViewModel = hiltViewModel()
                    SearchScreen(
                        onMovieClick = { movieId ->
                            // Navigates to the Detail Screen (hiding bottom bar)
                            rootNavController.navigate("movie_detail/$movieId")
                        }
                    )
            }

            // --- TAB 3: BOOKMARK ---
            composable(BottomNavItem.Bookmark.route) {
                // UPDATE THIS PART:
                BookmarkScreen(
                    onMovieClick = { movieId ->
                        // Pass navigation up to the root controller to hide bottom bar
                        rootNavController.navigate("movie_detail/$movieId")
                    }
                )
            }

            // --- TAB 4: PROFILE ---
            composable(BottomNavItem.Profile.route) {
                ProfileScreen(onLogout = {
                    // Use ROOT controller to go back to Login
                    rootNavController.navigate("login") {
                        popUpTo(0)
                    }
                })
            }
        }
    }
}