package com.circuithouse.mymovies.ui.movies.viewmodel

import com.circuithouse.mymovies.ui.movies.data.MoviesPagingSource
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.circuithouse.mymovies.data.remote.MovieResponse
import com.circuithouse.mymovies.data.room.dao.GenreDao
import com.circuithouse.mymovies.data.room.dao.MovieResponseDao
import com.circuithouse.mymovies.data.room.table.MovieResponseEntity
import com.circuithouse.mymovies.data.service.MovieService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieService: MovieService,
    private val movieDao: MovieResponseDao,
    private val genreDao: GenreDao
) : ViewModel() {
    private val pager: Pager<Int, MovieResponse> =
        Pager(config = PagingConfig(pageSize = 20), pagingSourceFactory = ::initPagingSource)
    val movies: Flow<PagingData<MovieResponse>> = pager.flow

    @VisibleForTesting
    val searchQuery = MutableStateFlow("")
    private val _searchQueryChanges = MutableSharedFlow<Unit>()
    val searchQueryChanges: SharedFlow<Unit> = _searchQueryChanges.asSharedFlow()

    private val _favoriteMovieIds = MutableStateFlow<List<Int>>(emptyList())
    val favoriteMovieIds: StateFlow<List<Int>> = _favoriteMovieIds

    private val _showMaxSelectionToast = MutableSharedFlow<Unit>()
    val showMaxSelectionToast: SharedFlow<Unit> = _showMaxSelectionToast.asSharedFlow()

    init {

        viewModelScope.launch {
            movieDao.getAllMovieIds().collect { ids ->
                _favoriteMovieIds.value = ids
            }
        }

        searchQuery
            .debounce(SEARCH_DEBOUNCE_MS)
            .distinctUntilChanged()
            .onEach { _searchQueryChanges.emit(Unit) }
            .launchIn(viewModelScope)
    }

    private fun initPagingSource() = MoviesPagingSource(
        movieService,
        searchQuery.value,
    )

    fun onSearch(query: String) {
        if (searchQuery.value.isEmpty() && query.isBlank()) return
        if (searchQuery.value.isBlank() && query.length < SEARCH_MINIMUM_LENGTH) return

        searchQuery.tryEmit(query)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_MS = 300L
        private const val SEARCH_MINIMUM_LENGTH = 3
    }

    fun toggleFavorite(movie: MovieResponse, isFavorite: Boolean) {
        viewModelScope.launch {
            if (!isFavorite) {
                val totalSelections = movieDao.getAllMovies().size + genreDao.getAllGenres().size
                if (totalSelections == 10) {
                    _showMaxSelectionToast.emit(Unit)
                    return@launch
                }
            }
            val movieEntity = MovieResponseEntity(
                id = movie.id,
                release_date = movie.release_date,
                title = movie.title,
                original_title = movie.original_title,
                original_language = movie.original_language,
                overview = movie.overview,
                poster_path = movie.poster_path,
                vote_average = movie.vote_average,
                vote_count = movie.vote_count
            )

            if (_favoriteMovieIds.value.contains(movie.id)) {
                movieDao.deleteMovieById(movieEntity.id)
            } else {
                movieDao.insertMovies(movieEntity)
            }
            _favoriteMovieIds.value = movieDao.getAllMovieIds().first()
        }
    }
}