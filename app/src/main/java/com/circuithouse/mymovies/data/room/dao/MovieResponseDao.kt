package com.circuithouse.mymovies.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.circuithouse.mymovies.data.room.table.MovieResponseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieResponseDao {
    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<MovieResponseEntity>

    @Query("SELECT id FROM movies")
    fun getAllMovieIds(): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(genres: MovieResponseEntity)

    @Query("DELETE FROM movies WHERE id = :id")
    suspend fun deleteMovieById(id: Int)
}