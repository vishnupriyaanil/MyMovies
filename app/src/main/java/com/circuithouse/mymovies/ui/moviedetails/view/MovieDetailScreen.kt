package com.circuithouse.mymovies.ui.moviedetails

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.circuithouse.mymovies.R
import com.circuithouse.mymovies.data.remote.Genre
import com.circuithouse.mymovies.data.remote.MovieDetailResponse
import com.circuithouse.mymovies.ui.common.error.ErrorColumn
import com.circuithouse.mymovies.ui.common.loading.LoadingColumn
import com.circuithouse.mymovies.ui.main.view.LocalNavController
import com.circuithouse.mymovies.ui.moviedetails.viewmodel.MovieDetailViewModel
import com.circuithouse.mymovies.ui.widget.BottomArcShape
import com.circuithouse.mymovies.utils.GetVibrantColorFromPoster
import com.circuithouse.mymovies.utils.animation.springAnimation
import com.circuithouse.mymovies.utils.dpToPx
import com.circuithouse.mymovies.utils.openInChromeCustomTab
import com.circuithouse.mymovies.utils.toBackdropUrl
import com.circuithouse.mymovies.utils.toPosterUrl

val LocalVibrantColor =
    compositionLocalOf<Animatable<Color, AnimationVector4D>> { error("No vibrant color defined") }
val LocalMovieId = compositionLocalOf<Int> { error("No movieId defined") }

@Composable
fun MovieDetailScreen(movieDetailViewModel: MovieDetailViewModel) {
    val uiState = movieDetailViewModel.uiState.collectAsState().value

    when {
        uiState.loading -> {
            val title = stringResource(id = R.string.fetching_movie_detail)
            LoadingColumn(title)
        }

        uiState.error != null -> {
            ErrorColumn(uiState.error.message.orEmpty())
        }

        uiState.movieDetail != null -> {
            val defaultTextColor = MaterialTheme.colorScheme.onBackground
            val vibrantColor = remember { Animatable(defaultTextColor) }
            CompositionLocalProvider(
                LocalVibrantColor provides vibrantColor,
                LocalMovieId provides uiState.movieDetail.id,
            ) {
                MovieDetail(uiState.movieDetail)
            }
        }
    }
}

@Composable
private fun AppBar(modifier: Modifier, homepage: String?) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier,
    ) {
        val navController = LocalNavController.current
        val vibrantColor = LocalVibrantColor.current.value
        val scaleModifier = Modifier.scale(1.1f)
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back_icon_content_description),
                tint = vibrantColor,
                modifier = scaleModifier,
            )
        }
        if (!homepage.isNullOrBlank()) {
            val context = LocalContext.current
            IconButton(onClick = { homepage.openInChromeCustomTab(context, vibrantColor) }) {
                Icon(
                    Icons.AutoMirrored.Rounded.OpenInNew,
                    contentDescription = stringResource(id = R.string.open_website_content_description),
                    tint = vibrantColor,
                    modifier = scaleModifier,
                )
            }
        }
    }
}

@Composable
fun MovieDetail(movieDetail: MovieDetailResponse) {
    ConstraintLayout(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState()),
    ) {
        val (appbar, backdrop, poster, title, genres, specs, rateStars, tagline, overview) = createRefs()
        val startGuideline = createGuidelineFromStart(16.dp)
        val endGuideline = createGuidelineFromEnd(16.dp)

        GetVibrantColorFromPoster(movieDetail.poster_path, LocalVibrantColor.current)
        Backdrop(
            backdropUrl = movieDetail.backdrop_path!!.orEmpty().toBackdropUrl(),
            movieDetail.title,
            Modifier.constrainAs(backdrop) {})
        val posterWidth = 160.dp
        AppBar(
            homepage = movieDetail.homepage,
            modifier = Modifier
                .requiredWidth(posterWidth * 2.2f)
                .constrainAs(appbar) { centerTo(poster) }
                .offset(y = 24.dp),
        )
        Poster(
            movieDetail.poster_path.toPosterUrl(),
            movieDetail.title,
            Modifier
                .zIndex(17f)
                .width(posterWidth)
                .height(240.dp)
                .constrainAs(poster) {
                    centerAround(backdrop.bottom)
                    linkTo(startGuideline, endGuideline)
                },
        )
        Title(
            title = movieDetail.title,
            originalTitle = movieDetail.original_title,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(poster.bottom, 8.dp)
                linkTo(startGuideline, endGuideline)
            }
        )
        GenreChips(
            movieDetail.genres.take(4),
            modifier = Modifier.constrainAs(genres) {
                top.linkTo(title.bottom, 16.dp)
                linkTo(startGuideline, endGuideline)
            },
        )

        MovieFields(
            movieDetail,
            modifier = Modifier.constrainAs(specs) {
                top.linkTo(genres.bottom, 12.dp)
                linkTo(startGuideline, endGuideline)
            },
        )

        RateStars(
            movieDetail.vote_average,
            modifier = Modifier.constrainAs(rateStars) {
                top.linkTo(specs.bottom, 12.dp)
                linkTo(startGuideline, endGuideline)
            },
        )

        Text(
            text = movieDetail.tagline,
            color = LocalVibrantColor.current.value,
            style = MaterialTheme.typography.bodyLarge.copy(
                letterSpacing = 2.sp,
                lineHeight = 24.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .constrainAs(tagline) {
                    top.linkTo(rateStars.bottom, 32.dp)
                },
        )

        Text(
            text = movieDetail.overview,
            style = MaterialTheme.typography.bodyLarge.copy(
                letterSpacing = 2.sp,
                lineHeight = 30.sp,
                fontFamily = FontFamily.SansSerif,
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .constrainAs(overview) {
                    top.linkTo(tagline.bottom, 8.dp)
                    linkTo(startGuideline, endGuideline)
                },
        )
    }
}

@Composable
private fun Backdrop(backdropUrl: String, movieName: String, modifier: Modifier) {
    Card(
        elevation = 16.dp,
        shape = BottomArcShape(arcHeight = 120.dpToPx()),
        backgroundColor = LocalVibrantColor.current.value.copy(alpha = 0.1f),
        modifier = modifier.height(360.dp),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(data = backdropUrl)
                .crossfade(1500).build(),
            contentScale = ContentScale.FillHeight,
            contentDescription = stringResource(R.string.backdrop_content_description, movieName),
            modifier = modifier.fillMaxWidth(),
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Poster(posterUrl: String, movieName: String, modifier: Modifier) {
    val isScaled = remember { mutableStateOf(false) }
    val scale = animateFloatAsState(
        targetValue = if (isScaled.value) 2.2f else 1f,
        animationSpec = springAnimation,
        label = "scale"
    ).value

    Card(
        elevation = 24.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.scale(scale),
        onClick = { isScaled.value = !isScaled.value },
    ) {
        AsyncImage(
            model = posterUrl,
            contentDescription = stringResource(
                id = R.string.movie_poster_content_description,
                movieName
            ),
            contentScale = ContentScale.FillHeight,
        )
    }
}

@Composable
private fun Title(title: String, originalTitle: String, modifier: Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 26.sp,
                letterSpacing = 3.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            ),
        )
        if (originalTitle.isNotBlank() && title != originalTitle) {
            Text(
                text = "(${originalTitle})",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontStyle = FontStyle.Italic,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center,
                ),
            )
        }
    }
}

@Composable
private fun GenreChips(genres: List<Genre>, modifier: Modifier) {
    Row(
        modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
    ) {
        genres.map(Genre::name).forEachIndexed { index, name ->
            Text(
                text = name.orEmpty(),
                style = MaterialTheme.typography.titleSmall.copy(letterSpacing = 2.sp),
                modifier = Modifier
                    .border(1.25.dp, LocalVibrantColor.current.value, RoundedCornerShape(50))
                    .padding(horizontal = 6.dp, vertical = 3.dp),
            )

            if (index != genres.lastIndex) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
private fun RateStars(voteAverage: Double, modifier: Modifier) {
    Row(modifier.padding(start = 4.dp)) {
        val maxVote = 10
        val starCount = 5
        repeat(starCount) { starIndex ->
            val voteStarCount = voteAverage / (maxVote / starCount)
            val asset = when {
                voteStarCount >= starIndex + 1 -> Icons.Filled.Star
                voteStarCount in starIndex.toDouble()..(starIndex + 1).toDouble() -> Icons.AutoMirrored.Filled.StarHalf
                else -> Icons.Filled.StarOutline
            }
            Icon(
                imageVector = asset,
                contentDescription = null,
                tint = LocalVibrantColor.current.value
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
private fun MovieFields(movieDetail: MovieDetailResponse, modifier: Modifier) {
    Row(horizontalArrangement = Arrangement.spacedBy(20.dp), modifier = modifier) {
        val context = LocalContext.current
        MovieField(context.getString(R.string.release_date), movieDetail.release_date!!)
        MovieField(
            context.getString(R.string.duration),
            context.getString(R.string.duration_minutes, movieDetail.runtime.toString()),
        )
        MovieField(context.getString(R.string.vote_average), movieDetail.vote_average.toString())
        MovieField(context.getString(R.string.votes), movieDetail.vote_count.toString())
    }
}

@Composable
private fun MovieField(name: String, value: String) {
    Column {
        Text(
            text = name,
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize = 13.sp,
                letterSpacing = 1.sp
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 4.dp),
        )
    }
}
