package com.example.moviesai.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviesai.data.local.dao.MovieDao
import com.example.moviesai.data.local.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
