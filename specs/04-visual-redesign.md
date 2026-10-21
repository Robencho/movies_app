# Spec 04: Immersive Visual Redesign (Cinema Mode)

## Description
Transform the application UI into a professional streaming platform experience (similar to Netflix or Disney+) by applying modern Material 3 design principles, immersive dark mode, gradients, and custom components.

## UI & Design Requirements

### 1. Global Theme & Dark Mode
- Force dark theme by default across the app (`darkTheme = true`).
- Base background colors: Deep dark / charcoal (`#121212` / `#0A0A0A`).
- Accent color: Cinematic gold (`#F5C518`) for ratings and primary highlights.

### 2. Enhanced MovieCard Component
- Use `Card` with rounded corners (`RoundedCornerShape(16.dp)`).
- Apply a vertical gradient overlay (`Brush.verticalGradient`) at the bottom of the poster image so text remains perfectly legible.
- Display movie title (bold, white) and rating (with a gold star icon and 1 decimal format).

### 3. Immersive MovieDetailScreen
- **Backdrop Header:** The movie backdrop image takes the top area (~350.dp height) with a smooth gradient fade into the background color.
- **Floating Back Button:** A translucent circular glassmorphism button on the top-left corner.
- **Overlapping Layout:** Title, release date, runtime, and genre chips displayed cleanly over a dark surface card.
- **Genres & Overview:** Styled genre chips and clean typography for the synopsis.

### 4. Shimmer Loading Effect
- Replace standard `CircularProgressIndicator` with a smooth shimmer skeleton layout mimicking the movie grid and detail views while data is loading.