# 📄 Especificación Técnica (SDD) - Módulo 08: Transiciones Cinemáticas Compartidas (Shared Element Transitions)

## 1. Visión General
Implementar transiciones visuales fluidas (*Shared Element Transitions* o *Hero Animations*) entre las tarjetas del Home (`PopularMoviesScreen`, carruseles y favoritos) y la pantalla de detalle (`MovieDetailScreen`). El póster y el título de la película deben animar su tamaño y posición de manera continua durante la navegación.

---

## 2. Requisitos Técnicos y Arquitectura de UI

### A. Dependencias y Versiones de Compose
*   Asegurar el uso de **Jetpack Compose 1.7.x+** o las APIs experimentales de `SharedTransitionLayout` (disponibles en la última versión de Navigation Compose y Foundation).

### B. Estructura de Navegación con Animación Compartida
1.  **Contenedor Raíz (`SharedTransitionLayout`):**
    *   Debe envolver el componente principal de navegación de la app (`NavHost`) para permitir que los elementos compartidos mantengan su estado de animación entre diferentes pantallas de Compose.
2.  **Modificador de Animación (`sharedElement`):**
    *   En la tarjeta (`MovieCard`): Aplicar el modificador `Modifier.sharedElement(rememberSharedContentState(key = "movie_poster_${movie.id}"), animatedVisibilityScope = this)`.
    *   En la pantalla de detalle (`MovieDetailScreen`): Aplicar el mismo modificador con la **misma clave exacta** (`key = "movie_poster_${movie.id}"`) en la imagen del póster o cabecera.

### C. Especificación de Transición del Título y Contenedor
*   Opcionalmente, extender la animación compartida al título de la película para que el texto se desplace fluidamente desde la tarjeta hacia la cabecera del detalle.
*   Configurar la especificación de la animación (`boundsTransform` y `fadeIn`/`fadeOut`) para garantizar que la transición se sienta orgánica y a 60/120 FPS estables.

---