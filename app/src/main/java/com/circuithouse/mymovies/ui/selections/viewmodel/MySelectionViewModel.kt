package com.circuithouse.mymovies.ui.selections.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.circuithouse.mymovies.data.room.dao.GenreDao
import com.circuithouse.mymovies.data.room.dao.MovieResponseDao
import com.circuithouse.mymovies.data.room.table.GenreEntity
import com.circuithouse.mymovies.data.room.table.MovieResponseEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MySelectionViewModel @Inject constructor(
    private val genreDao: GenreDao,
    private val movieDao: MovieResponseDao
) : ViewModel() {
    private val _storedGenreIds = MutableStateFlow<List<GenreEntity>>(emptyList())
    val storedGenres: StateFlow<List<GenreEntity>> = _storedGenreIds

    private val _favMovies = MutableStateFlow<List<MovieResponseEntity>>(emptyList())
    val favMovies: StateFlow<List<MovieResponseEntity>> = _favMovies

    init {
        viewModelScope.launch {
            _storedGenreIds.value = genreDao.getAllGenres()
            _favMovies.value = movieDao.getAllMovies()
        }
    }
}