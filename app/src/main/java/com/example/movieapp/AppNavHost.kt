package com.example.movieapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movieapp.ui.screens.DetailsScreen
import com.example.movieapp.ui.screens.HomeScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onItemClick = { id -> navController.navigate("details/${id}") }
            )
        }
        composable("details/{movieId}") { backStackEntry ->
            // Retrieve the movie ID from arguments
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()

            // Pass the movieId to DetailsScreen
            movieId?.let {
                DetailsScreen(it, navController)
            } ?: run {
                Text("Movie not found", style = MaterialTheme.typography.headlineMedium)
            }
        }
    }
}

