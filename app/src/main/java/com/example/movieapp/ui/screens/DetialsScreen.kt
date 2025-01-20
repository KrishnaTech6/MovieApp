package com.example.movieapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.movieapp.viewmodel.DetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(movieId: Int, navController: NavController) {
    val viewModel: DetailsViewModel = hiltViewModel()
    val isLoading = viewModel.isLoading.collectAsState().value
    val error = viewModel.error.collectAsState().value

    // Trigger fetchTitleDetails only when the movieId changes
    LaunchedEffect(key1 = movieId) {
        viewModel.fetchTitleDetails(movieId)
    }

    val titleDetails = viewModel.titleDetails.collectAsState().value
    Log.d("TAG", "DetailsScreen: $titleDetails")
    val snackbarHostState = remember { SnackbarHostState() }

    // Scaffold to provide a consistent app structure
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Movie Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                })
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        // Content of the screen
        Box(modifier = Modifier.padding(paddingValues)) {
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
                    LaunchedEffect(error) {
                        snackbarHostState.showSnackbar(error)
                    }
                }
                else -> {
                    if (titleDetails != null) {
                        Log.d("TAG", "URL : ${titleDetails.poster}")
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            AsyncImage(
                                model = titleDetails.poster,
                                contentDescription = titleDetails.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                            )
                            Text(
                                titleDetails.title,
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(8.dp)
                            )
                            Text(
                                titleDetails.plot_overview,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                "Release Date: ${titleDetails.release_date}",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }
            }
        }
    }
}
