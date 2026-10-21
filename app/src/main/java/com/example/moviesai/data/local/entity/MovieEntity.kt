package com.example.moviesai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class MovieEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val releaseDate: String,
    val rating: Double,
    val addedAt: Long = System.currentTimeMillis()
)
