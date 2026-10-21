# Spec 06: Movie Cast & Similar Recommendations

## Description
Enhance the `MovieDetailScreen` by adding two new essential dynamic sections:
1. **Cast & Crew Carousel:** Display actor profile images, real names, and character names.
2. **Similar Movies Recommendations:** Show a horizontal list of similar movies to keep users discovering content.

## API Specifications (TMDb)
1. **Movie Credits:** `GET /3/movie/{movie_id}/credits`
2. **Similar Movies:** `GET /3/movie/{movie_id}/similar`

## Domain Layer Requirements
- **Models:**
    - `CastMember` (id, name, character, profileUrl)
    - Updated `MovieDetail` to include `cast: List<CastMember>` and `similarMovies: List<Movie>`
- **Repository Interface:**
    - Add functions to fetch credits and similar movies for a given `movieId`.
- **UseCase:** Update `GetMovieDetailUseCase` (or create dedicated UseCases) to zip/combine detail, credits, and similar movies concurrently using Coroutines.

## Data Layer Requirements
- `CastDto` and `CreditsResponseDto` with `@SerializedName`.
- Add Retrofit endpoints to `TmdbApiService`:
    - `GET movie/{movie_id}/credits`
    - `GET movie/{movie_id}/similar`
- Base profile image URL handling: `https://image.tmdb.org/t/p/w185` for actor profile images.

## Presentation Layer Requirements
- **UI Compose Components (`MovieDetailScreen`):**
    - **Reparto Principal (Cast):** A horizontal `LazyRow` featuring circular or rounded actor profile images (using Coil), bold actor names, and subtle character names.
    - **Películas Similares (Similar Movies):** A horizontal `LazyRow` re-using the compact `MovieCard` component, allowing users to tap and navigate directly to another movie detail view.