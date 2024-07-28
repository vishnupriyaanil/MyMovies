package com.circuithouse.mymovies.data.remote

data class GenresResponse(
    val genres: List<Genre>
)

data class Genre(
    val id: Int,
    val name: String?
)

