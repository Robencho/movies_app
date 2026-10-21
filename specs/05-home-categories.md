# Spec 05: Multi-Category Home Screen

## Description
Evolve the Home screen into a rich, multi-category discovery dashboard. Instead of showing a single grid, display multiple horizontal carousels for different movie collections, topped with a Featured Movie Header.

## API Specifications (TMDb)
1. **Now Playing:** `GET /3/movie/now_playing`
2. **Upcoming:** `GET /3/movie/upcoming`
3. **Top Rated:** `GET /3/movie/top_rated`
4. **Popular:** `GET /3/movie/popular`

## Domain Layer Requirements
- **Repository Interface:** Add functions to `MovieRepository`:
    - `suspend fun getNowPlayingMovies(): Result<List<Movie>>`
    - `suspend fun getUpcomingMovies(): Result<List<Movie>>`
    - `suspend fun getTopRatedMovies(): Result<List<Movie>>`
- **UseCases:** Create corresponding UseCases for each section or a consolidated `GetHomeContentUseCase` returning a data class with all categories.

## Data Layer Requirements
- Add Retrofit endpoints to `TmdbApiService` for `now_playing`, `upcoming`, and `top_rated`.
- Implement data fetching and mapping to domain models in `MovieRepositoryImpl`.

## Presentation Layer Requirements
- **`HomeUiState`:**
  ```kotlin
  data class HomeContent(
      val featuredMovie: Movie?,
      val nowPlaying: List<Movie>,
      val popular: List<Movie>,
      val topRated: List<Movie>,
      val upcoming: List<Movie>
  )

  sealed interface HomeUiState {
      object Loading : HomeUiState
      data class Success(val content: HomeContent) : HomeUiState
      data class Error(val message: String) : HomeUiState
  }
  ```
- **UI Compose Components (`HomeScreen`):**
    - **Featured Header Carousel / Banner:** Highlight the first movie with a large backdrop banner and a "Ver Detalle" button.
    - **Horizontal Movie Carousels:** `LazyRow` for each category ("En Cines", "Próximamente", "Mejor Valoradas", "Populares").
    - Maintain the immersive dark styling and shimmer loading states from Spec 04.