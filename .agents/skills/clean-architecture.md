# Skill: Clean Architecture Guidelines

## Layers Overview
1. **Domain (`domain/`)**
    - Must be pure Kotlin (NO Android dependencies or libraries like Retrofit/Room).
    - Contains: `model/` (business entities), `repository/` (interfaces only), `usecase/` (single responsibility interactors).

2. **Data (`data/`)**
    - Implements domain repository interfaces.
    - Contains: `remote/` (DTOs, ApiServices, Retrofit), `local/` (DAOs, Entities, Room if needed), `repository/` (Concrete implementations and Mappers).
    - Mappers transform DTOs/Entities to Domain Models.

3. **Presentation (`presentation/`)**
    - Uses Jetpack Compose for UI and ViewModels for state management.
    - Follows Unidirectional Data Flow (UDF).
    - Communicates ONLY with Domain UseCases (or Domain Repositories when UseCase is redundant).
    - Uses `StateFlow` or `SharedFlow` to expose UI states (`Loading`, `Success`, `Error`).