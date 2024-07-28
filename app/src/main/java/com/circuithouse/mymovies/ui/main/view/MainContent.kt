package com.circuithouse.mymovies.ui.main.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.circuithouse.mymovies.ui.genres.view.GenresScreen
import com.circuithouse.mymovies.ui.home.view.HomeScreen
import com.circuithouse.mymovies.ui.moviedetails.MovieDetailScreen
import com.circuithouse.mymovies.ui.moviedetails.viewmodel.MovieDetailViewModel
import com.circuithouse.mymovies.ui.movies.view.MoviesScreen
import com.circuithouse.mymovies.ui.navigation.ARG_MOVIE_ID
import com.circuithouse.mymovies.ui.navigation.Screen
import com.circuithouse.mymovies.ui.selections.view.MySelections

// Create a CompositionLocal for accessing NavHostController
val LocalNavController = compositionLocalOf<NavHostController> { error("No nav controller") }

@Composable
fun MainContent() {
    Scaffold(
        // Apply padding for status bars
        modifier = Modifier.statusBarsPadding(),
        // Define the top bar of the scaffold
        topBar = {
            // Surface component for the top bar with shadow and background
            Surface(modifier = Modifier.fillMaxWidth(), shadowElevation = 16.dp) {
                Column(
                    Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(bottom = 2.dp),
                ) {
                    // Custom AppBar composable
                    AppBar()
                }
            }
        },
        // Define the content of the scaffold
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .background(Color.White)
            ) {
                // Get the current NavHostController from CompositionLocal
                val navController = LocalNavController.current
                // Define the NavHost with the start destination
                NavHost(
                    navController = navController,
                    startDestination = Screen.HOME.route
                ) {
                    // Define composable destinations for navigation
                    composable(Screen.HOME.route) { HomeScreen(navController) }
                    composable(Screen.GENRES.route) { GenresScreen() }
                    composable(Screen.MOVIES.route) { MoviesScreen() }
                    composable(Screen.MY_SELECTION.route) { MySelections() }

                    // Define nested navigation for movie details
                    navigation(startDestination = Screen.DETAIL.route, route = "movie") {
                        // Define argument for the movie ID
                        navArgument(ARG_MOVIE_ID) { type = NavType.StringType }

                        // Extension function to extract movie ID from NavBackStackEntry
                        fun NavBackStackEntry.movieId(): Int {
                            return arguments?.getString(ARG_MOVIE_ID)!!.toInt()
                        }

                        // Define a composable lambda to create MovieDetailViewModel
                        val movieDetailViewModel: @Composable (movieId: Int) -> MovieDetailViewModel =
                            { hiltViewModel() }

                        // Define the composable for movie details screen
                        composable(route = Screen.DETAIL.route) {
                            MovieDetailScreen(movieDetailViewModel(it.movieId()))
                        }
                    }
                }
            }
        }
    )
}
