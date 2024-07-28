package com.circuithouse.mymovies.ui.genres.viewmodel

import GenersPagingSource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.circuithouse.mymovies.data.remote.Genre
import com.circuithouse.mymovies.data.room.dao.GenreDao
import com.circuithouse.mymovies.data.room.dao.MovieResponseDao
import com.circuithouse.mymovies.data.room.table.GenreEntity
import com.circuithouse.mymovies.data.service.MovieService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenresViewModel @Inject constructor(
    private val movieService: MovieService,
    private val genreDao: GenreDao,
    private val movieDao: MovieResponseDao
) : ViewModel() {
    private val pager: Pager<Int, Genre> =
        Pager(config = PagingConfig(pageSize = 1), pagingSourceFactory = ::initPagingSource)
    val movies: Flow<PagingData<Genre>> = pager.flow

    private val _storedGenreIds = MutableStateFlow<List<Int>>(emptyList())
    val storedGenreIds: StateFlow<List<Int>> = _storedGenreIds

    private val _showMaxSelectionToast = MutableSharedFlow<Unit>()
    val showMaxSelectionToast: SharedFlow<Unit> = _showMaxSelectionToast.asSharedFlow()

    init {
        viewModelScope.launch {
            genreDao.getAllGenreIds().collect { ids ->
                _storedGenreIds.value = ids
            }
        }
    }

    private fun initPagingSource() = GenersPagingSource(
        movieService,
    )

    fun onGenreClicked(genre: Genre, isChecked: Boolean) {
        toggleGenreInDatabase(genre, isChecked)
    }

    // Function to toggle genre in the database
    private fun toggleGenreInDatabase(genre: Genre, isChecked: Boolean) {
        viewModelScope.launch {
            if (!isChecked) {
                val totalSelections = movieDao.getAllMovies().size + genreDao.getAllGenres().size
                if (totalSelections == 10) {
                    _showMaxSelectionToast.emit(Unit)
                    return@launch
                }
            }
            val currentIds = genreDao.getAllGenreIds().first()
            if (currentIds.contains(genre.id)) {
                genreDao.deleteGenreById(genre.id)
            } else {
                genreDao.insertGenres(
                    listOf(
                        GenreEntity(
                            id = genre.id,
                            name = genre.name.orEmpty()
                        )
                    )
                )
            }
            _storedGenreIds.value = genreDao.getAllGenreIds().first()
        }
    }
}
