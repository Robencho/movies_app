# Spec 02: Movie Detail View

## Description
Fetch and display detailed information about a selected movie (backdrop image, runtime, genres, overview, rating, release date) when tapped from the popular list.

## API Specification
- **Endpoint:** `GET /3/movie/{movie_id}`
- **Base URL:** `https://api.themoviedb.org/3/`
- **Path Param:** `movie_id` (Int)
- **Query Params:** `language` (String, default "es-ES")

## Domain Layer Requirements
- **Model:** `MovieDetail` (id, title, overview, posterUrl, backdropUrl, releaseDate, rating, runtime, genres)
- **Repository Interface:** Add `suspend fun getMovieDetail(movieId: Int): Result<MovieDetail>` to `MovieRepository`
- **UseCase:** `GetMovieDetailUseCase` returning `Flow<Result<MovieDetail>>`

## Data Layer Requirements
- `MovieDetailDto` with `@SerializedName` for runtime, genres list, backdrop_path, etc.
- `TmdbApiService` method implementation for `movie/{movie_id}`
- Extension function `MovieDetailDto.toDomain()`

## Presentation Layer Requirements
- Navigation route setup: `"movie_detail/{movieId}"`
- `MovieDetailUiState` (`Loading`, `Success(movie: MovieDetail)`, `Error(message: String)`)
- `MovieDetailViewModel` retrieving `movieId` via `SavedStateHandle`
- Compose UI (`MovieDetailScreen`) with backdrop header image, floating poster, title, genre chips, and scrollable overview.