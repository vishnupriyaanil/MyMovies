package com.circuithouse.mymovies.ui.selections.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.circuithouse.mymovies.data.room.table.GenreEntity
import com.circuithouse.mymovies.ui.main.view.LocalNavController
import com.circuithouse.mymovies.ui.selections.viewmodel.MySelectionViewModel

@Composable
fun GenresContent(moviesViewModel: MySelectionViewModel) {
    // Content for Genres tab
    Box(modifier = Modifier.fillMaxSize()) {
        val state = rememberLazyListState()
        val storedGenres by moviesViewModel.storedGenres.collectAsState()
        LazyGenreList(state, storedGenres)
    }
}

@Composable
private fun LazyGenreList(state: LazyListState, storedGenres: List<GenreEntity>) {
    val navController = LocalNavController.current
    /*val onMovieClicked: (Int) -> Unit =
        { movieId -> navController.navigate(Screen.DETAIL.createPath(movieId)) }*/

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
        items(storedGenres.size) { plant ->
            val genre = storedGenres[plant]
            GenreContent(genre)
        }
    }
}

@Composable
fun GenreContent(genre: GenreEntity) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardColors(Color.Blue, Color.Blue, Color.Blue, Color.Blue),
        shape = RoundedCornerShape(size = 8.dp),
        onClick = { },
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = genre.name,
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                modifier = Modifier.shadow(8.dp)
            )
        }
    }
}