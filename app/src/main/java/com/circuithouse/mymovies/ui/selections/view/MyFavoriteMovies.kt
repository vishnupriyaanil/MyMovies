package com.circuithouse.mymovies.ui.selections.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.circuithouse.mymovies.R
import com.circuithouse.mymovies.data.room.table.MovieResponseEntity
import com.circuithouse.mymovies.ui.main.view.LocalNavController
import com.circuithouse.mymovies.ui.navigation.Screen
import com.circuithouse.mymovies.ui.selections.viewmodel.MySelectionViewModel
import com.circuithouse.mymovies.utils.toPosterUrl


private const val COLUMN_COUNT = 2
private val GRID_SPACING = 8.dp

private val span: (LazyGridItemSpanScope) -> GridItemSpan = { GridItemSpan(COLUMN_COUNT) }

@Composable
fun MyFavoriteMovies(moviesViewModel: MySelectionViewModel) {
    val movies = moviesViewModel.favMovies.collectAsState()
    val state = rememberLazyGridState()
    LazyMoviesGrid(state, movies)
}

@Composable
private fun LazyMoviesGrid(state: LazyGridState, movies: State<List<MovieResponseEntity>>) {
    val navController = LocalNavController.current
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

            items(
                movies.value.size,
                key = { movies.value[it]?.id ?: -1 }
            ) { index ->
                val movie = movies.value[index]
                movie?.let {
                    MovieContent1(
                        it,
                        Modifier.height(320.dp)
                    )
                }
            }

        },
    )
}

@Composable
fun MovieContent1(movie: MovieResponseEntity, modifier:  Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(size = 8.dp),
        //elevation = 8.dp,
    ) {
        Box {
            MoviePoster(movie.poster_path!!.toPosterUrl(), movie.title)
            MovieInfo(
                movie,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color(0x97000000)),
            )
        }
    }
}

@Composable
private fun BoxScope.MoviePoster(posterPath: String, movieName: String) {
    val painter = rememberAsyncImagePainter(
        model = posterPath,
        error = rememberVectorPainter(Icons.Filled.BrokenImage),
        placeholder = rememberVectorPainter(Icons.Default.Movie),
    )
    val colorFilter = when (painter.state) {
        is AsyncImagePainter.State.Loading, is AsyncImagePainter.State.Error -> ColorFilter.tint(
            MaterialTheme.colorScheme.surfaceTint
        )

        else -> null
    }
    val scale =
        if (painter.state !is AsyncImagePainter.State.Success) ContentScale.Fit else ContentScale.FillBounds

    Image(
        painter = painter,
        colorFilter = colorFilter,
        contentDescription = stringResource(
            id = R.string.movie_poster_content_description,
            movieName
        ),
        contentScale = scale,
        modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center),
    )
}

@Composable
private fun MovieInfo(movie: MovieResponseEntity, modifier: Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.padding(horizontal = 6.dp, vertical = 4.dp),
    ) {
        MovieName(name = movie.title)
    }
}

@Composable
private fun MovieName(name: String) = Text(
    text = name,
    style = MaterialTheme.typography.titleSmall.copy(
        color = Color.White,
        letterSpacing = 1.5.sp,
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.W500,
    ),
    maxLines = 1,
    overflow = TextOverflow.Ellipsis,
)