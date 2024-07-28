package com.circuithouse.mymovies.ui.selections.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.circuithouse.mymovies.R
import com.circuithouse.mymovies.ui.selections.viewmodel.MySelectionViewModel

@Composable
fun MySelections() {
    // State to hold the current tab index
    val currentTabIndex = remember { mutableIntStateOf(0) }
    val moviesViewModel = hiltViewModel<MySelectionViewModel>()
    Column {
        TabRow(
            selectedTabIndex = currentTabIndex.intValue,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[currentTabIndex.intValue]),
                    color = Color.Magenta
                )
            },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            // Tab for Genres
            Tab(text = {
                Text(
                    text = stringResource(id = R.string.my_genres),
                    style = TextStyle(fontSize = 18.sp),
                    color = if (currentTabIndex.intValue == 0) MaterialTheme.colorScheme.onPrimary else Color.Gray
                )
            },
                selected = currentTabIndex.intValue == 0,
                onClick = { currentTabIndex.intValue = 0 })

            // Tab for Movies
            Tab(text = {
                Text(
                    text = stringResource(id = R.string.my_movies),
                    style = TextStyle(fontSize = 18.sp),
                    color = if (currentTabIndex.intValue == 1) MaterialTheme.colorScheme.onPrimary else Color.Gray
                )
            },
                selected = currentTabIndex.intValue == 1,
                onClick = { currentTabIndex.intValue = 1 })
        }

        // Display content based on the selected tab
        when (currentTabIndex.intValue) {
            0 -> GenresContent(moviesViewModel)
            1 -> MyFavoriteMovies(moviesViewModel)
        }
    }
}
