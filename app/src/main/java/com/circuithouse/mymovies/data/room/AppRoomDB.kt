package com.circuithouse.mymovies.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.circuithouse.mymovies.data.room.dao.GenreDao
import com.circuithouse.mymovies.data.room.dao.MovieResponseDao
import com.circuithouse.mymovies.data.room.table.GenreEntity
import com.circuithouse.mymovies.data.room.table.MovieResponseEntity

@Database(entities = [GenreEntity::class, MovieResponseEntity::class], version = 2)
abstract class AppRoomDB : RoomDatabase() {
    abstract fun genreDao(): GenreDao
    abstract fun movieDao(): MovieResponseDao
}