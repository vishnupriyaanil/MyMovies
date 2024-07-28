package com.circuithouse.mymovies.data.remote

data class MoviesResponse(
    val page: Int,
    val results: List<MovieResponse>,
    val total_pages: Int,
    val total_results: Int,
)

data class MovieResponse(
    val id: Int,
    val release_date: String? = "",
    val title: String,
    val original_title: String,
    val original_language: String,
    val overview: String,
    val poster_path: String?,
    val vote_average: Double,
    val vote_count: Int,
)

