package com.circuithouse.mymovies.data.room.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieResponseEntity(
    @PrimaryKey val id: Int,
    val release_date: String? = "",
    val title: String,
    val original_title: String,
    val original_language: String,
    val overview: String,
    val poster_path: String?,
    val vote_average: Double,
    val vote_count: Int,
)