package com.example.ticket.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ticket.view.screen.AuthScreen
import com.example.ticket.view.screen.BookTicketScreen
import com.example.ticket.view.screen.MainScreen
import com.example.ticket.view.screen.MovieDetailScreen
import com.example.ticket.view.screen.ProfileSetupScreen
import com.example.ticket.view.screen.TicketSuccessScreen

@Composable
fun AppNavigation() {
    val rootNavController = rememberNavController()

    NavHost(navController = rootNavController, startDestination = "login") {

        // 1. AUTH SCREEN
        composable("login") {
            AuthScreen(navController = rootNavController)
        }

        // 2. PROFILE SETUP
        composable("profile_setup") {
            ProfileSetupScreen(
                onSetupComplete = {
                    rootNavController.navigate("main_app") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // 3. MAIN APP (Home/Search/Profile)
        composable("main_app") {
            MainScreen(rootNavController = rootNavController)
        }

        // 4. MOVIE DETAIL
        composable(
            route = "movie_detail/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.StringType })
        ) { backStackEntry ->
            // We capture the ID (even if not used yet) to prevent crashes
            val movieId = backStackEntry.arguments?.getString("movieId") ?: "0"

            MovieDetailScreen(
                onBackClick = { rootNavController.popBackStack() },
                onMovieClick = { newMovieId ->
                    rootNavController.navigate("movie_detail/$newMovieId")
                },
                onBookTicketClick = { id ->
                    // PASS THE ID to the booking screen route
                    rootNavController.navigate("book_ticket/$id")
                }
            )
        }

        // 5. BOOK TICKET SCREEN
        composable(
            route = "book_ticket/{movieId}", // <--- FIXED: Added {movieId} to match the navigate call
            arguments = listOf(navArgument("movieId") { type = NavType.StringType })
        ) {
            BookTicketScreen(
                // Matches the name in your BookTicketScreen.kt
                onBookingComplete = {
                    rootNavController.navigate("ticket_success_screen") {
                        // Clear backstack so user goes to Main App, not back to seats
                        popUpTo("main_app") { inclusive = false }
                    }
                },
                onBackClick = { rootNavController.popBackStack() }
                // viewModel is injected automatically by hiltViewModel() inside the screen
            )
        }

        // 6. TICKET SUCCESS SCREEN
        composable("ticket_success_screen") {
            TicketSuccessScreen(
                onGoHome = {
                    // FIXED: Navigate to 'main_app', not 'home' (which doesn't exist)
                    rootNavController.navigate("main_app") {
                        popUpTo("main_app") { inclusive = true }
                    }
                }
            )
        }
    }
}