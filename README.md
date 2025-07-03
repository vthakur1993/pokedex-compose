# 📱 Pokedex Pro - Gotta Catch 'Em All! 🌟
**A modern Android Pokedex application built with Jetpack Compose, Kotlin, and following best practices in Android development. Browse and discover your favorite Pokemon!**

---

## ✨ Features

*   Ϟ **Browse Pokemon:** Infinite scrolling list of Pokemon.
*   🎨 **Beautiful UI:** Crafted with Material Design 3 and Jetpack Compose.
*   📖 **Detailed View:** See stats, abilities, types, and more for each Pokemon.
*   ⭐ **Favourite:** Mark individual Pokémon as favorites
*   💖 **[WIP] Favorites:** Mark your favorite Pokemon.
*   🔄 **Offline First:** Data cached with Room for offline access after initial load.
*   🧪 **Well-Tested:** Unit and UI tests to ensure reliability.
*   🚀 **Modern Tech Stack:** Leveraging the latest Android Jetpack libraries.

---

## 📸 Screenshots & Demo

| List View                                     | Detail View                                       | 
| :--------------------------------------------: | :-----------------------------------------------: |
| <img src="https://github.com/user-attachments/assets/715a3d56-b557-4d83-9d8b-406f00b45fac" width="320"> | <img src="https://github.com/user-attachments/assets/2bd8a991-8cca-453a-ab14-f80d29b582f6" width="320"> |


**Watch a quick demo of the app in action!**
![Demo](https://github.com/user-attachments/assets/523df8f3-0dc2-45fd-9091-20b641b2ae34)



---

## 🏗️ Architecture

This project follows the **MVVM (Model-View-ViewModel)** architectural pattern, leveraging Android Architecture Components to create a robust and maintainable codebase. The architecture promotes separation of concerns and testability.

*   **View (UI Layer):**
    *   Built entirely with **Jetpack Compose** for a declarative and modern UI.
    *   Observes state changes from the ViewModel (`StateFlow`) and forwards user interactions.
    *   Consists of Composable functions, Activities.
*   **ViewModel (`PokedexViewmodel`):**
    *   Holds and manages UI-related data in a lifecycle-conscious way.
    *   Exposes UI state using `kotlinx.coroutines.flow.StateFlow`.
    *   Handles user interactions and delegates business logic to the Repository.
    *   Uses `viewModelScope` for coroutine operations.
*   **Repository (`PokedexRepository`, `PokedexNetworkRepository`):**
    *   Single source of truth for data.
    *   Abstracts data sources (network, local database).
    *   Decides whether to fetch data from the network or local cache.
    *   Handles data mapping between network DTOs and database entities/domain models.
*   **Data Sources:**
    *   **Remote (`PokedexNetworkRepository`):**
        *   Uses **Retrofit** for making HTTP requests to the [PokeAPI](https://pokeapi.co/).
        *   Uses **Gson/Moshi** for JSON parsing (TODO: Specify which one you used).
    *   **Local (`PokemonDao`):**
        *   Uses **Room Persistence Library** for storing and retrieving Pokemon data locally.
        *   Provides offline support.
*   **Dependency Injection:**
    *   **Hilt** is used for managing dependencies throughout the application, simplifying boilerplate and improving testability.
*   **Coroutines & Flow:**
    *   Asynchronous operations are handled using **Kotlin Coroutines**.
    *   **Kotlin Flow** is used for reactive data streams from the repository to the ViewModel and for handling sequences of data (like paginated API responses).

---

## 🛠️ Tech Stack & Libraries

This project leverages a modern tech stack:

*   **[Kotlin](https://kotlinlang.org/):** First-party programming language for Android.
*   **[Jetpack Compose](https://developer.android.com/jetpack/compose):** Modern toolkit for building native Android UI.
    *   `androidx.compose.ui`: Core UI elements.
    *   `androidx.compose.material3`: Material Design 3 components.
    *   `androidx.compose.runtime`: Core Compose runtime.
    *   `androidx.compose.foundation`: Layouts and foundational elements.
    *   `androidx.navigation:navigation-compose`: For in-app navigation between composable screens.
    *   `androidx.activity:activity-compose`: Integration with Activities.
*   **[Android Architecture Components](https://developer.android.com/topic/libraries/architecture):**
    *   **ViewModel:** `androidx.lifecycle:lifecycle-viewmodel-compose` - Manages UI-related data.
    *   **Room:** `androidx.room:room-runtime`, `androidx.room:room-ktx` - Local database persistence.
    *   **LiveData/StateFlow:** `kotlinx.coroutines.flow.StateFlow` - Observable data holder.
*   **[Coroutines & Flow](https://kotlinlang.org/docs/coroutines-overview.html):** For asynchronous programming and reactive streams.
    *   `kotlinx-coroutines-core`
    *   `kotlinx-coroutines-android`
*   **[Hilt](https://dagger.dev/hilt/):** For dependency injection.
    *   `com.google.dagger:hilt-android`
    *   `androidx.hilt:hilt-navigation-compose`
*   **[Retrofit2](https://square.github.io/retrofit/):** Type-safe HTTP client for Android and Java.
    *   `com.squareup.retrofit2:retrofit`
    *   `com.squareup.retrofit2:converter-gson` (or `converter-moshi` - TODO: Specify)
*   **[OkHttp3](https.square.github.io/okhttp/):** HTTP client, often used under the hood by Retrofit.
    *   `com.squareup.okhttp3:logging-interceptor` (For logging network requests)
*   **[Gson](https://github.com/google/gson) / [Moshi](https://github.com/square/moshi):** For JSON serialization/deserialization. (TODO: Specify which one and list the dependency)
*   **[Coil](https://coil-kt.github.io/coil/compose/) / [Glide](https://bumptech.github.io/glide/) / [Picasso](https://square.github.io/picasso/):** For image loading. (TODO: Specify which one you used and list the dependency)
    *   Example for Coil: `io.coil-kt:coil-compose`
*   **Testing:**
    *   **[JUnit4](https://junit.org/junit4/):** For unit testing. `junit:junit`
    *   **[Mockito-Kotlin](https://github.com/mockito/mockito-kotlin):** For creating mocks in tests. `org.mockito.kotlin:mockito-kotlin`
    *   **[Turbine](https://github.com/cashapp/turbine):** For testing Kotlin Flow emissions. `app.cash.turbine:turbine` (Highly recommended for Flow testing!)
    *   **[Robolectric](http://robolectric.org/):** For running Android unit tests on the JVM. `org.robolectric:robolectric` (If used)
    *   **[Espresso](https://developer.android.com/training/testing/espresso):** For UI testing. `androidx.test.espresso:espresso-core` (If you have Espresso tests)
    *   **Compose Testing:** `androidx.compose.ui:ui-test-junit4` - For Jetpack Compose UI tests.
    *   **Hilt Testing:** `com.google.dagger:hilt-android-testing` - For testing Hilt-injected components.
    *   **Coroutines Test:** `org.jetbrains.kotlinx:kotlinx-coroutines-test`
*   **[Material Icons Extended](https://developer.android.com/reference/kotlin/androidx/compose/material/icons/Icons.Extended):** `androidx.compose.material:material-icons-extended` (If used for a wider range of icons)
*   **Gradle Kotlin DSL:** For build scripts.

---

## 🧪 Testing

Ensuring the reliability and correctness of the Pokedex app is a top priority. The project includes:

*   **Unit Tests:**
    *   Located in `src/test/java/com/example/pokedex/`.
    *   Focus on testing individual components in isolation, primarily ViewModels and Repositories.
    *   **Mockito-Kotlin** is used for mocking dependencies.
    *   **Turbine** is used to test `Flow` emissions thoroughly.
    *   `kotlinx-coroutines-test` is used for managing test dispatchers and testing coroutines.
*   **Instrumentation (UI) Tests:**
    *   Located in `src/androidTest/java/com.example/pokedex/`.
    *   Focus on testing UI interactions and verifying screen states using **Jetpack Compose testing APIs**.
    *   **Hilt Android Testing** is used to provide test dependencies and manage the component lifecycle in UI tests.
    *   (TODO: Add specific examples of UI tests if you have them, e.g., "Tests navigation between list and detail screens.")

**How to Run Tests:**

1.  **Unit Tests:**: bash ./gradlew testDebugUnitTest
Or run them directly from Android Studio (right-click on the test class/method).
2.  **Instrumentation (UI) Tests:**
bash ./gradlew connectedDebugAndroidTest
Requires a connected device or emulator. Or run them from Android Studio.


## 🤝 Contributing

Contributions are welcome! If you have suggestions or want to improve the app, please feel free to:

1.  Fork the repository.
2.  Create a new branch (`git checkout -b feature/AmazingFeature`).
3.  Make your changes.
4.  Commit your changes (`git commit -m 'Add some AmazingFeature'`).
5.  Push to the branch (`git push origin feature/AmazingFeature`).
6.  Open a Pull Request.

Please make sure your code adheres to the existing style and that all tests pass.

---

## 🐛 Known Issues & TODOs

*   [x] Implement Favorite Pokemon feature.
*   [ ] Add more detailed error handling for specific network scenarios.
*   [ ] Improve UI animations and transitions.
*   [ ] Add accessibility improvements (Content Descriptions, etc.).

---

## 🙏 Acknowledgements

*   **[PokeAPI](https://pokeapi.co/):** For providing the comprehensive Pokemon data.
*   **Android Jetpack Team:** For the amazing libraries that make modern Android development a joy.
*   **Kotlin Team:** For a fantastic programming language.
*   **skydoves:** For guidance regarding compose [pokedex-compose](https://github.com/skydoves/pokedex-compose?tab=readme-ov-file)

---

## 👨‍💻 Author

**Vipul Thakur**

*   GitHub: https://github.com/vthakur1993
*   LinkedIn: https://www.linkedin.com/in/vipul-thakur-50141bbb/
*   Twitter: https://x.com/vipulth86952031

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
(TODO: Create a LICENSE.md file. MIT is a common choice for open-source projects.)
