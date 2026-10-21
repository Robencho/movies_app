# Skill: Jetpack Compose & UI Guidelines

- **UI State:** Model UI state using `sealed interface` or `sealed class`.
- **State Collection:** Always collect states in Compose using `collectAsStateWithLifecycle()`.
- **Preview:** Provide `@Preview` annotations for reusable components and screens with dummy data.
- **Design System:** Use Material 3 (`MaterialTheme.colorScheme`, typography, etc.).
- **Images:** Use Coil (`AsyncImage`) for loading remote images. Pass placeholder and error drawables.
- **Recomposability:** Keep composables stateless whenever possible by hoisting state to the parent or ViewModel.