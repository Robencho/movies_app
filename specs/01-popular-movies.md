# Spec 01: Popular Movies List

## Description
Fetch and display a grid of popular movies from TMDb API with loading, success, and error states.

## API Specification
- **Endpoint:** `GET /3/movie/popular`
- **Base URL:** `https://api.themoviedb.org/3/`
- **Query Params:** `page` (Int, default 1), `language` (String, default "es-ES")

## Domain Layer Requirements
- **Model:** `Movie` (id, title, overview, posterUrl, releaseDate, rating)
- **Repository Interface:** `MovieRepository` with `suspend fun getPopularMovies(page: Int): Result<List<Movie>>`
- **UseCase:** `GetPopularMoviesUseCase` returning `Flow<Result<List<Movie>>>`

## Data Layer Requirements
- `PopularMoviesResponseDto` and `MovieDto` with `@SerializedName`
- `TmdbApiService` interface for Retrofit
- Extension function `PopularMoviesResponseDto.toDomain()` to map DTOs to `List<Movie>`
- Base image URL handling: `https://image.tmdb.org/t/p/w500` prefixed to `poster_path`

## Presentation Layer Requirements
- `PopularMoviesUiState` (`Loading`, `Success(movies: List<Movie>)`, `Error(message: String)`)
- `PopularMoviesViewModel` exposing `StateFlow<PopularMoviesUiState>`
- Compose UI:
    - `PopularMoviesScreen`
    - `MovieGrid` (`LazyVerticalGrid`)
    - `MovieCard` with Coil `AsyncImage`
    - Handling of Loading indicator and Error view with retry action