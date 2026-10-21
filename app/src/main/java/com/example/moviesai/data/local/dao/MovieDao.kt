package com.example.moviesai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviesai.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM favorite_movies ORDER BY addedAt DESC")
    fun getFavoriteMovies(): Flow<List<MovieEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE id = :movieId)")
    suspend fun isFavorite(movieId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(movie: MovieEntity)

    @Query("DELETE FROM favorite_movies WHERE id = :movieId")
    suspend fun deleteFavorite(movieId: Int)
}
