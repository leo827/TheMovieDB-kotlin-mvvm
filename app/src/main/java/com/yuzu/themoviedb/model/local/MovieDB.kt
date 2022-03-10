package com.yuzu.themoviedb.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yuzu.themoviedb.model.data.MovieData



@Database(entities = [MovieData::class], version = 1)
abstract class MovieDB: RoomDatabase() {
    abstract fun movieDAO(): MovieDAO
}