package com.circuithouse.mymovies.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.circuithouse.mymovies.data.room.table.GenreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {
    @Query("SELECT * FROM genres")
    suspend fun getAllGenres(): List<GenreEntity>

    @Query("SELECT id FROM genres")
    fun getAllGenreIds(): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<GenreEntity>)

    @Query("DELETE FROM genres WHERE id = :id")
    suspend fun deleteGenreById(id: Int)
}