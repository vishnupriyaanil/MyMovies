package com.circuithouse.mymovies.ui.movies.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.circuithouse.mymovies.data.remote.MovieResponse
import com.circuithouse.mymovies.data.remote.MoviesResponse
import com.circuithouse.mymovies.data.service.MovieService
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class MoviesPagingSource(
    private val movieService: MovieService,
    private val searchQuery: String = "",
) : PagingSource<Int, MovieResponse>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResponse> {
        return try {
            val page = params.key ?: 1
            val moviesResponse: Response<MoviesResponse> = if (searchQuery.isNotBlank()) {
                movieService.search(page, searchQuery)
            } else {
                movieService.fetchMovies(page)
            }

            // Check for HTTP success and parse the response body
            if (moviesResponse.isSuccessful) {
                val movies = moviesResponse.body()?.results?: emptyList()
                val totalPages = moviesResponse.body()?.total_pages ?: 1

                LoadResult.Page(
                    data = movies,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (page >= totalPages) null else page + 1,
                )
            } else {
                LoadResult.Error(HttpException(moviesResponse))
            }
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieResponse>): Int = 1
}
