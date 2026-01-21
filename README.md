# YAMAL - Yet Another MyAnimeList

[![CI](https://github.com/user/yamal/actions/workflows/ci.yml/badge.svg)](https://github.com/user/yamal/actions/workflows/ci.yml)

A Kotlin Multiplatform application for browsing and managing your MyAnimeList library. Built with Compose Multiplatform, targeting **Android**, **iOS**, and **Desktop**.

## Features

- Browse anime catalog with paging support
- Search anime and manga
- OAuth authentication with MyAnimeList
- User profile management
- Cross-platform UI with Compose Multiplatform

## Screenshots

*Coming soon*

## Tech Stack

| Technology | Version |
|------------|---------|
| Kotlin | 2.3.0 |
| Compose Multiplatform | 1.9.3 |
| Ktor | 3.3.3 |
| Koin | 4.1.1 |
| Arrow | 2.2.1.1 |
| Coil | 3.3.0 |
| AndroidX Paging | 3.4.0-beta01 |

## Architecture

The project follows a modular MVI (Model-View-Intent) architecture organized into two main umbrellas:

### Module Structure

```
yamal/
├── app/
│   ├── android/          # Android application
│   ├── desktop/          # Desktop (JVM) application
│   └── shared/           # Compose Multiplatform entry point
├── platform/             # Shared capabilities
│   ├── mvi/              # Base MVI framework
│   ├── network/          # Ktor-based networking (api/implementation)
│   ├── storage/          # Local storage (api/implementation)
│   ├── designsystem/     # UI components and theming
│   └── utils/            # Shared utilities
├── features/             # User-facing functionality
│   ├── anime/            # Anime feature (api/implementation/ui)
│   ├── manga/            # Manga feature (api/implementation/ui)
│   ├── search/           # Search feature (api/implementation/ui)
│   ├── login/            # OAuth authentication (api/implementation/ui)
│   ├── user/             # User profile (api/implementation/ui)
│   └── navigation/       # Navigation coordination
└── shared/               # DI aggregation module
```

### Key Patterns

- **MVI Architecture**: Presenters extend `Presenter<InternalState, UiState, Intent, Effect>` for unidirectional data flow
- **Dependency Injection**: Koin modules in each implementation module, aggregated in `:shared`
- **Error Handling**: Arrow's `Either<String, T>` for repository operations
- **Module Boundaries**: Features depend on Platform, never on each other

## Requirements

- JDK 21
- Android Studio Ladybug or later (for Android development)
- Xcode 15+ (for iOS development)

## Building

### Android

```bash
# Build debug APK
./gradlew :app:android:assembleDebug

# Build release APK
./gradlew :app:android:assembleRelease
```

### Desktop

```bash
# Run desktop app
./gradlew :app:desktop:run

# Package distribution
./gradlew :app:desktop:packageDistributionForCurrentOS
```

### iOS

Open `iosApp/iosApp.xcodeproj` in Xcode and build from there.

## Development

### Running Tests

```bash
./gradlew test
```

### Code Formatting

The project uses [ktlint](https://ktlint.github.io/) for code formatting.

```bash
# Check formatting
./gradlew ktlintCheck

# Auto-format code
./gradlew ktlintFormat
```

### Convention Plugins

The build uses custom convention plugins defined in `build-logic/`:

| Plugin | Purpose |
|--------|---------|
| `yamal.library` | Standard KMP library module |
| `yamal.mvi` | KMP module with Compose enabled |
| `yamal.shared` | Full Compose Multiplatform with iOS framework |
| `yamal.android.application` | Android app configuration |
| `yamal.ktlint` | Code formatting |

## Configuration

The app requires MyAnimeList API credentials. Create a `local.properties` file in the project root:

```properties
MAL_CLIENT_ID=your_client_id
```

Get your client ID from the [MyAnimeList API dashboard](https://myanimelist.net/apiconfig).

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

Please ensure your code passes `./gradlew ktlintCheck` before submitting.

## License

*License information here*

## Acknowledgments

- [MyAnimeList](https://myanimelist.net/) for providing the API
- [JetBrains](https://www.jetbrains.com/) for Kotlin and Compose Multiplatform
