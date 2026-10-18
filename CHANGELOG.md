# ReelList Changelog

All notable changes to ReelList are recorded here. Versions follow *major.minor.patch*.
Each entry is grouped as **Added**, **Changed**, **Fixed** or **Planned**.

---

## [Future] v0.4.0 to v1.0.0 (Modules 7–8, final submission)
### Planned
- Search box on the Watchlist screen
- "Currently Watching" status for TV shows with season/episode progress
- "Pick for Tonight" filters (by service, or by runtime such as "under 2 hours")
- Final launcher icon, real device screenshots in the README, accessibility pass (TalkBack labels, font scaling)
- Final presentation (10 slides or fewer) and final outline report
- Stretch: movie lookup through a public API, CSV export, cloud backup, Google Play internal test release

---

## [Current] v0.3.1 (Module 6, Java edition)
### Changed
- Programming language switched from **Kotlin to Java** (the outline allowed "Java or Kotlin")
- Front end rebuilt with XML layouts, Material Components and Fragments, because Jetpack Compose only supports Kotlin
- Navigation now uses the Jetpack Navigation component with an XML navigation graph (`res/navigation/nav_graph.xml`)
- Screen state uses `AndroidViewModel` + `LiveData` instead of Kotlin Flow
- Room now uses the Java annotation processor instead of KSP; database writes run on a background executor
- Build scripts converted from the Gradle Kotlin DSL (`.gradle.kts`) to the Groovy DSL (`.gradle`)
- Landscape layouts moved to `res/layout-land/` (navigation rail, poster grid, Watched rating panel)

### Fixed
- Unit tests ported to Java (`WatchlistLogicTest.java`); all 8 tests pass

---

## [Previous] v0.3.0 (Module 6, Project Submission 3)
### Added
- Android Studio project (first written in Kotlin with Jetpack Compose; replaced by Java in v0.3.1)
- **Back end:** Room database with a `movies` table (title, release year, genre, where to watch, runtime, notes, watched status, rating, date added, date watched), a DAO, a repository and SharedPreferences settings
- **Front end:** Home dashboard, Watchlist, Watched ("Seen"), Add/Edit Movie, Movie Details, Settings
- Bottom navigation (Home / List / Seen) in portrait and a navigation rail in landscape
- Landscape poster grid, plus a rating side panel on the Watched screen (from the new landscape wireframe)
- "Pick for Tonight" random pick with Shuffle; To Watch / Watched / Hours Watched counters
- Service filter chips and sort options (date added, title, genre, service, runtime)
- Undo snackbar after deleting a movie; optional delete confirmation
- Unit tests (`WatchlistLogicTest`) for sorting, filtering, stats, random pick and form validation
- `docs/wireframes/` folder and Wiki outline pages

### Changed
- Programming language first set to Kotlin (changed to Java in v0.3.1)
- Add Movie and Edit Movie now share one form; only Title is required, so saving a recommendation is fast
- Movie Details is now the single hub for Mark as Watched, Rate, Edit and Delete
- Settings moved to a gear icon on Home so the bottom bar stays at three tabs (matches the sketch)
- Genre and Where-to-Watch dropdowns also accept custom values (e.g., smaller streaming services)

---

## [Previous] v0.2.0 (Module 5, Project Submission 2)
### Added
- Design and Wireframes section (Home, Watchlist, Watched History, Add Movie screens)
- Basic navigation flow and UX reasoning (consistent title bar / content / bottom navigation layout, checkbox meaning, Add Movie on Home and Watchlist)
- Portrait and landscape hand sketch and digital mockup

## [Previous] v0.1.0 (Module 4, Project Submission 1)
### Added
- Initial project outline: description, problem addressed, platform, front/back-end support, functionality
- GitHub repository `COM-437` created with an initial README
