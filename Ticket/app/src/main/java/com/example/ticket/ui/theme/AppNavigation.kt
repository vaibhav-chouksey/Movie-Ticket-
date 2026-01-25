package com.example.ticket.ui.theme

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ticket.view.screen.AuthScreen
import com.example.ticket.view.screen.HomeScreen
import com.example.ticket.view.screen.MovieDetailScreen
import kotlin.jvm.java

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // 1. START DESTINATION IS "home"
    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            // Clean: No ViewModel passing needed here!
            AuthScreen(navController = navController)
        }

        // 2. THIS ROUTE MUST ALSO BE "home"
        composable("home") {
            HomeScreen(
                onMovieClick = { movieId ->
                    navController.navigate("movie_detail/$movieId")
                }
            )
        }

        // Inside AppNavigation.kt

        composable(
            route = "movie_detail/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.StringType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId") ?: "0"

            MovieDetailScreen(
                onBackClick = { navController.popBackStack() },
                onMovieClick = { newMovieId ->
                    // This pushes a NEW Detail Screen on top of the current one
                    navController.navigate("movie_detail/$newMovieId")
                }
            )
        }
    }
}