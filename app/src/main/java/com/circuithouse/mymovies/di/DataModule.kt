package com.circuithouse.mymovies.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.circuithouse.mymovies.data.local.LocalDataStore
import com.circuithouse.mymovies.data.local.PreferencesDataStore
import com.circuithouse.mymovies.data.room.AppRoomDB
import com.circuithouse.mymovies.data.room.dao.GenreDao
import com.circuithouse.mymovies.data.room.dao.MovieResponseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context): LocalDataStore {
        return PreferencesDataStore(context.preferencesDataStore)
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): AppRoomDB {
        return Room.databaseBuilder(context, AppRoomDB::class.java, "my_movies.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideGenreDao(db: AppRoomDB): GenreDao {
        return db.genreDao()
    }

    @Provides
    fun provideMovieDao(db: AppRoomDB): MovieResponseDao {
        return db.movieDao()
    }

}
