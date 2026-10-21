# Spec 07: Local Favorites with Room Database

## Description
Allow users to bookmark movies as favorites from the detail screen and view them in a dedicated "My Favorites" screen, persisted locally using Room Database so it works offline.

## Database & Local Data Specification (Room)
- **Database:** `AppDatabase` (Version 1)
- **Table / Entity:** `favorite_movies`
    - `id`: Int (PrimaryKey)
    - `title`: String
    - `overview`: String
    - `posterUrl`: String
    - `releaseDate`: String
    - `rating`: Double
    - `addedAt`: Long (Timestamp for ordering)

## Domain Layer Requirements
- **Repository Interface Updates (`MovieRepository`):**
    - `fun getFavoriteMovies(): Flow<List<Movie>>`
    - `suspend fun isFavorite(movieId: Int): Boolean`
    - `suspend fun toggleFavorite(movie: Movie)`
- **UseCases:**
    - `GetFavoriteMoviesUseCase`
    - `ToggleFavoriteUseCase`
    - `IsFavoriteUseCase`

## Data Layer Requirements
- **Room Dependencies:** `@Entity`, `@Dao`, `@Database`.
- **DAO (`MovieDao`):**
    - `@Query("SELECT * FROM favorite_movies ORDER BY addedAt DESC")` -> `Flow<List<MovieEntity>>`
    - `@Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE id = :movieId)")` -> `suspend fun isFavorite(movieId: Int): Boolean`
    - `@Insert(onConflict = OnConflictStrategy.REPLACE)` -> `suspend fun insertFavorite(movie: MovieEntity)`
    - `@Query("DELETE FROM favorite_movies WHERE id = :movieId")` -> `suspend fun deleteFavorite(movieId: Int)`
- **Mapper:** `MovieEntity.toDomain()` and `Movie.toEntity()`.
- **Hilt Module:** Provide `AppDatabase` and `MovieDao` in `DatabaseModule`.

## Presentation Layer Requirements
1. **`MovieDetailScreen` Enhancements:**
    - Add a heart icon floating action button / top bar action.
    - Dynamic icon state: Filled heart ❤️ if favorite, outlined 🤍 if not.
    - Toggling updates the Room DB instantly and provides user feedback (Snackbar or Haptic/Animation).

2. **New `FavoritesScreen`:**
    - Displays a grid or list of bookmarked movies.
    - Shows an empty state with an illustrative message when no favorites exist ("No tienes películas favoritas aún").
    - Navigation support to view movie details directly from the favorites grid.

3. **Bottom Navigation Bar:**
    - Integrate a Material 3 `NavigationBar` on the main container with items: "Inicio" (Home), "Buscar" (Search), and "Favoritos" (Favorites).