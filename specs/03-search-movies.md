# Spec 03: Movie Search

## Description
Allow users to search for movies in real-time using a search bar, consuming the TMDb search endpoint with debounce handling.

## API Specification
- **Endpoint:** `GET /3/search/movie`
- **Base URL:** `https://api.themoviedb.org/3/`
- **Query Params:** `query` (String), `language` (String, default "es-ES"), `page` (Int, default 1)

## Domain Layer Requirements
- **UseCase:** `SearchMoviesUseCase(query: String)` returning `Flow<Result<List<Movie>>>`

## Data Layer Requirements
- `TmdbApiService` method for `search/movie`
- Repository implementation in `MovieRepositoryImpl`

## Presentation Layer Requirements
- `SearchUiState` (`Idle`, `Loading`, `Success(movies: List<Movie>)`, `Empty`, `Error(message: String)`)
- `SearchViewModel` implementing coroutine debounce (~400ms) on text changes
- Compose UI (`SearchScreen`) with Material 3 search bar and result list/grid.