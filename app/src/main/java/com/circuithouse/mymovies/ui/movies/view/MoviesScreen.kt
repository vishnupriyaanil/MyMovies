package com.circuithouse.mymovies.ui.movies.view

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HighlightOff
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.circuithouse.mymovies.R
import com.circuithouse.mymovies.ui.movies.viewmodel.MoviesViewModel

@Composable
fun MoviesScreen() {
    val context = LocalContext.current
    val moviesViewModel = hiltViewModel<MoviesViewModel>()
    val searchQuery = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        moviesViewModel.showMaxSelectionToast.collect {
            Toast.makeText(
                context,
                context.getString(R.string.max_selection_reached),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    Scaffold(
        content = { paddingValues ->
            Column(
                Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(paddingValues),
            ) {
                SearchBar(searchQuery, moviesViewModel::onSearch)
                MoviesGrid(moviesViewModel)
            }
        }

    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(searchQuery: MutableState<String>, onSearch: (String) -> Unit) {
    TextField(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .heightIn(max = 50.dp)
            .fillMaxWidth(),
        value = searchQuery.value,
        textStyle = MaterialTheme.typography.titleSmall,
        singleLine = true,
        shape = RoundedCornerShape(50),
        placeholder = { Text(stringResource(id = R.string.search_movies), color = Color.Gray) },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            AnimatedVisibility(visible = searchQuery.value.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.HighlightOff,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        searchQuery.value = ""
                        onSearch("")
                    },
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        onValueChange = { query ->
            searchQuery.value = query
            onSearch(query)
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = MaterialTheme.colorScheme.surface,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
        ),
    )
}
