package com.circuithouse.mymovies.data.service

import com.circuithouse.mymovies.data.remote.GenresResponse
import com.circuithouse.mymovies.data.remote.MovieDetailResponse
import com.circuithouse.mymovies.data.remote.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieService {
    // Fetch movies with query parameters for pagination and other options
    @GET("discover/movie")
    suspend fun fetchMovies(
        @Query("page") pageNumber: Int,
        // @QueryMap options: Map<String, String>
    ): Response<MoviesResponse>

    // Search movies with pagination, search query, and includeAdult flag
    @GET("search/movie")
    suspend fun search(
        @Query("page") pageNumber: Int,
        @Query("query") searchQuery: String,
        @Query("include_adult") includeAdult: Boolean = true
    ): Response<MoviesResponse>

    // Fetch genres
    @GET("genre/movie/list")
    suspend fun fetchGenres(): Response<GenresResponse>

    // Fetch movie details by movie ID
    @GET("movie/{id}")
    suspend fun fetchMovieDetail(
        @Path("id") movieId: Int
    ): Response<MovieDetailResponse>
}
