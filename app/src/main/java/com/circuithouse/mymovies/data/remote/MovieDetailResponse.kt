package com.circuithouse.mymovies.data.remote


data class MovieDetailResponse(
    val adult: Boolean,
    val backdrop_path: String?,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String?,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val production_companies: List<ProductionCompanyResponse>,
    val release_date: String? = "",
    val revenue: Double,
    val runtime: Int?,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    val title: String,
    val vote_average: Double,
    val vote_count: Int,
)

data class ProductionCompanyResponse(
    val id: Int,
    val logo_path: String?,
    val name: String,
    val origin_country: String,
)

data class SpokenLanguage(
    val iso_639_1: String,
    val name: String,
)
