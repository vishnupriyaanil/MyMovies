package com.circuithouse.mymovies.ui.genres.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.circuithouse.mymovies.R
import com.circuithouse.mymovies.data.remote.Genre
import com.circuithouse.mymovies.ui.common.error.ErrorColumn
import com.circuithouse.mymovies.ui.common.loading.LoadingColumn
import com.circuithouse.mymovies.ui.genres.viewmodel.GenresViewModel
import kotlin.reflect.KFunction2

@Composable
fun GenresScreen() {
    val context = LocalContext.current
    val genresViewModel = hiltViewModel<GenresViewModel>()
    LaunchedEffect(Unit) {
        genresViewModel.showMaxSelectionToast.collect {
            Toast.makeText(
                context,
                context.getString(R.string.max_selection_reached),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                val genres = genresViewModel.movies.collectAsLazyPagingItems()
                val state = rememberLazyListState()
                val storedGenreIds by genresViewModel.storedGenreIds.collectAsState()

                when (genres.loadState.refresh) {
                    is LoadState.Loading -> {
                        LoadingColumn(stringResource(id = R.string.fetching_genres))
                    }

                    is LoadState.Error -> {
                        val error = genres.loadState.refresh as LoadState.Error
                        ErrorColumn(error.error.message.orEmpty())
                    }

                    else -> {
                        LazyGenreList(
                            state,
                            genres,
                            storedGenreIds,
                            genresViewModel::onGenreClicked
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun LazyGenreList(
    state: LazyListState, moviePagingItems: LazyPagingItems<Genre>,
    storedGenreIds: List<Int>, onGenreClicked: KFunction2<Genre, Boolean, Unit>
) {
    LazyColumn(
        state = state,
        contentPadding = PaddingValues(
            start = 8.dp,
            end = 8.dp,
            top = 8.dp,
            bottom = WindowInsets.navigationBars.getBottom(LocalDensity.current).dp.plus(8.dp),
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(moviePagingItems.itemCount) { plant ->
            val genre = moviePagingItems[plant]!!
            GenreContent(genre, storedGenreIds.contains(genre.id), onGenreClicked)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GenreContent(
    movie: Genre,
    isStored: Boolean,
    onGenreClicked: KFunction2<Genre, Boolean, Unit>
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(size = 8.dp),
        backgroundColor = if (isStored) Color.Blue else Color.Gray,
        onClick = { onGenreClicked(movie, isStored) },
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = movie.name.orEmpty(),
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                modifier = Modifier.shadow(8.dp)
            )
        }
    }
}