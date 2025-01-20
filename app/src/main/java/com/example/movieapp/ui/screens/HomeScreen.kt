package com.example.movieapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movieapp.viewmodel.MainViewModel


@Composable
fun HomeScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit
) {
    val shows by viewModel.titles.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isShowingMovies by viewModel.isShowingMovies.collectAsState() // Moved to ViewModel

    // Display either movies or TV shows
    val items = if (isShowingMovies)
        shows.filter { it.tmdb_type == "movie" }
    else
        shows.filter { it.tmdb_type == "tv" }

    Column {
        // Toggle buttons for switching between Movies and TV Shows
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            TextButton(
                onClick = { viewModel.setShowingMovies(true) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Movies")
            }
            TextButton(
                onClick = { viewModel.setShowingMovies(false) },
                modifier = Modifier.weight(1f)
            ) {
                Text("TV Shows")
            }
        }

        // Main content based on state
        when {
            isLoading -> {
                // Shimmer effect for loading
                LazyColumn(modifier = Modifier.padding(8.dp)) {
                    items(10) {
                        ShimmerPlaceholder()
                    }
                }
            }
            error != null -> {
                // Display error message with retry button
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = error!!,
                        color = Color.Red,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    TextButton(onClick = { viewModel.fetchTitles() }) {
                        Text("Retry")
                    }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    items(items.size) { index ->
                        val item = items[index]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    onItemClick(item.id)
                                },
                            shape = MaterialTheme.shapes.medium,
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = item.year.toString(),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }

                if (items.isEmpty()) {
                    Text(
                        text = "No items available.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}
