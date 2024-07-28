package com.circuithouse.mymovies.ui.movies.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.circuithouse.mymovies.R
import com.circuithouse.mymovies.data.remote.MovieResponse
import com.circuithouse.mymovies.ui.common.error.ErrorColumn
import com.circuithouse.mymovies.ui.common.loading.LoadingColumn
import com.circuithouse.mymovies.ui.main.view.LocalNavController
import com.circuithouse.mymovies.ui.movies.viewmodel.MoviesViewModel
import com.circuithouse.mymovies.ui.navigation.Screen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

private const val COLUMN_COUNT = 2
private val GRID_SPACING = 8.dp

private val span: (LazyGridItemSpanScope) -> GridItemSpan = { GridItemSpan(COLUMN_COUNT) }

@Composable
fun MoviesGrid(moviesViewModel: MoviesViewModel) {
    val movies = moviesViewModel.movies.collectAsLazyPagingItems()
    val state = rememberLazyGridState()
    LaunchedEffect(Unit) {
        merge(moviesViewModel.searchQueryChanges)
            .onEach {
                state.scrollToItem(0)
                movies.refresh()
            }
            .launchIn(this)
    }

    when (movies.loadState.refresh) {
        is LoadState.Loading -> {
            LoadingColumn(stringResource(id = R.string.fetching_movies))
        }

        is LoadState.Error -> {
            val error = movies.loadState.refresh as LoadState.Error
            ErrorColumn(error.error.message.orEmpty())
        }

        else -> {
            LazyMoviesGrid(state, movies, moviesViewModel)
        }
    }
}

@Composable
private fun LazyMoviesGrid(
    state: LazyGridState,
    moviePagingItems: LazyPagingItems<MovieResponse>,
    moviesViewModel: MoviesViewModel
) {
    val navController = LocalNavController.current
    val storedGenreIds by moviesViewModel.favoriteMovieIds.collectAsState()
    val onMovieClicked: (Int) -> Unit =
        { movieId -> navController.navigate(Screen.DETAIL.createPath(movieId)) }
    LazyVerticalGrid(
        columns = GridCells.Fixed(COLUMN_COUNT),
        contentPadding = PaddingValues(
            start = GRID_SPACING,
            end = GRID_SPACING,
            top = GRID_SPACING,
            bottom = WindowInsets.navigationBars.getBottom(LocalDensity.current).dp.plus(
                GRID_SPACING
            ),
        ),
        horizontalArrangement = Arrangement.spacedBy(GRID_SPACING, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(GRID_SPACING, Alignment.CenterVertically),
        state = state,
        content = {

            if (moviePagingItems.itemCount == 0 && moviePagingItems.loadState.refresh !is LoadState.Loading) {
                item(span = span) {
                    // ErrorRow(stringResource(R.string.no_movies_found))
                }
            }
            items(
                moviePagingItems.itemCount,
                key = { moviePagingItems[it]?.id ?: -1 }
            ) { index ->
                val movie = moviePagingItems.peek(index)
                movie?.let {
                    val isFavorite = storedGenreIds.contains(it.id)
                    MovieContent(it,
                        Modifier.height(320.dp),
                        onMovieClicked,
                        isFavorite = isFavorite,
                        onFavoriteClicked = {
                            moviesViewModel.toggleFavorite(it,isFavorite)
                        })
                }
            }
            renderLoading(moviePagingItems.loadState)
            renderError(moviePagingItems.loadState)
        },
    )
}

private fun LazyGridScope.renderLoading(loadState: CombinedLoadStates) {
    if (loadState.append !is LoadState.Loading) return

    item(span = span) {
        // val title = stringResource(R.string.fetching_more_movies)
        // LoadingRow(title = title, modifier = Modifier.padding(vertical = GRID_SPACING))
    }
}

private fun LazyGridScope.renderError(loadState: CombinedLoadStates) {
    val message = (loadState.append as? LoadState.Error)?.error?.message ?: return

    item(span = span) {
        // ErrorRow(title = message, modifier = Modifier.padding(vertical = GRID_SPACING))
    }
}
