package com.circuithouse.mymovies.ui.movies.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.AsyncImagePainter.State.Error
import coil.compose.AsyncImagePainter.State.Loading
import coil.compose.rememberAsyncImagePainter
import com.circuithouse.mymovies.R
import com.circuithouse.mymovies.data.remote.MovieResponse
import com.circuithouse.mymovies.utils.toPosterUrl

@Composable
fun MovieContent(
    movie: MovieResponse,
    modifier: Modifier = Modifier,
    onMovieClicked: (Int) -> Unit = {},
    isFavorite: Boolean = false,
    onFavoriteClicked: () -> Unit = {}
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(size = 8.dp),
        //elevation = 8.dp,
        onClick = { onMovieClicked(movie.id) },
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

            TopRightImage(
                isFavorite = isFavorite,
                onFavoriteClick = onFavoriteClicked
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
        is Loading, is Error -> ColorFilter.tint(MaterialTheme.colorScheme.surfaceTint)
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
private fun MovieInfo(movie: MovieResponse, modifier: Modifier) {
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

@Composable
private fun BoxScope.TopRightImage(
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    Icon(
        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
        contentDescription = if (isFavorite) stringResource(R.string.unfavorite) else stringResource(
            R.string.favorite
        ),
        tint = if (isFavorite) Color.Red else Color.White,
        modifier = Modifier
            .size(60.dp)
            .align(Alignment.TopEnd)
            .padding(8.dp)
            .clickable {
                onFavoriteClick()
            }
    )
}
