package com.circuithouse.mymovies.ui.moviedetails.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.circuithouse.mymovies.data.remote.MovieDetailResponse
import com.circuithouse.mymovies.data.service.MovieService
import com.circuithouse.mymovies.ui.navigation.ARG_MOVIE_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val movieService: MovieService
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailUiState())
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    init {
        val movieId: Int = savedStateHandle.get<String>(ARG_MOVIE_ID)!!.toInt()
        fetchMovieDetail(movieId = movieId)
    }

    private fun fetchMovieDetail(movieId: Int) = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(loading = true, error = null)
        _uiState.value = try {
            coroutineScope {
                val response = async { movieService.fetchMovieDetail(movieId) }
                val movieDetailResponse = response.await()

                if (movieDetailResponse.isSuccessful) {
                    val movieDetail = movieDetailResponse.body()
                    movieDetail?.let {
                        _uiState.value.copy(
                            movieDetail = movieDetail,
                            loading = false,
                        )
                    } ?: _uiState.value.copy(
                        error = Throwable("Empty response body"),
                        loading = false
                    )
                } else {
                    _uiState.value.copy(
                        error = Throwable(movieDetailResponse.message()),
                        loading = false
                    )
                }
            }
        } catch (exception: Exception) {
            _uiState.value.copy(error = exception, loading = false)
        }
    }

    data class MovieDetailUiState(
        val movieDetail: MovieDetailResponse? = null,
        val loading: Boolean = false,
        val error: Throwable? = null,
    )
}
