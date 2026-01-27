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

@Composable
fun AppNavigation() {
    val rootNavController = rememberNavController()

    NavHost(navController = rootNavController, startDestination = "login") {

        // 1. LOGIN
        composable("login") {
            AuthScreen(navController = rootNavController)
        }

        // 2. MAIN APP (The Container with Tabs)
        composable("main_app") {
            // We pass the root controller so the tabs can ask to open full-screen pages
            MainScreen(rootNavController = rootNavController)
        }

        // 3. GLOBAL SCREENS (Covers the entire screen, hides bottom bar)
        composable(
            route = "movie_detail/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.StringType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId") ?: "0"
            MovieDetailScreen(
                onBackClick = { rootNavController.popBackStack() },
                onMovieClick = { newMovieId ->
                    rootNavController.navigate("movie_detail/$newMovieId")
                },
//                viewModel = TODO(),
                onBookTicketClick = { id ->
                    rootNavController.navigate("book_ticket/$id")
                }
            )
        }

        composable("book_ticket/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")

            // This is your new screen (we will create it next)
            BookTicketScreen(
                onPaymentSuccess = {
                    // When done, go all the way back to Home
                    rootNavController.navigate("main_app") {
                        popUpTo("main_app") { inclusive = true }
                    }
                },
                onBackClick = { rootNavController.popBackStack() }
            )
        }
    }
}