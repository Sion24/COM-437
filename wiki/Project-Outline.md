# ReelList Project Outline (v0.3.1)

## I. Project Description
- **A. App name:** ReelList
- **B. Purpose:** A mobile application that helps users keep track of the movies and shows they want to watch, are currently watching, and have already finished.
- **C. Target users**
  1. Casual movie and TV watchers
  2. People who subscribe to multiple streaming services
  3. Film students and movie enthusiasts who track what they have seen
  4. Families who want a shared list of what to watch together
  5. Anyone who wants a simple way to organize what they plan to watch
- **D. Overall goal:** Give users one simple place to store everything they plan to watch, so they stop losing track of recommendations and stop wasting time scrolling through menus trying to decide.

## II. Problem Addressed
- **A.** People hear about movies from many places: a friend at lunch, a trailer on social media, a title saved on one streaming service, a line in a notes app.
- **B.** By the time they sit down to watch, they cannot remember the titles they wanted to see.
- **C.** ReelList provides a single, platform-neutral watchlist that belongs to the user rather than to any one streaming service.
- **D.** Users replace screenshots, notes apps and several service watchlists with one application.

## III. Platform
- **A. Primary platform:** Android smartphones
- **B. Development environment:** Android Studio
- **C. Programming language:** Java *(v0.3.1: switched from Kotlin to Java)*
- **D. Operating system:** Android 8.0 (API 26) and newer; targets Android 15 (API 35)
- **E. Compatibility:** Phone layouts for both portrait and landscape orientation

## IV. Front-End and Back-End Support
### A. Front end (XML layouts + Material Components, one Fragment per screen)
1. Home / Dashboard
2. Watchlist
3. Watched History ("Seen")
4. Add / Edit Movie
5. Movie Details
6. Settings

### B. Back end (on-device Room/SQLite database)
1. Fields stored for each title: movie title, release year, genre, streaming service (where to watch), runtime, personal notes, watched status, personal rating (1–5), date added and date watched
2. Repository layer: all changes to a movie go through one class
3. Settings: default sort order and the delete-confirmation preference

## V. Functionality
- **A. Add a movie:** title (required), release year, genre, where to watch (Netflix, Hulu, theater, owned…), runtime and notes
- **B. View the watchlist** in one place; organize by date added, title, genre, streaming service or runtime; filter by service
- **C. Mark a movie as watched** with a checkbox (list) or a button (details)
- **D. Rate watched movies** from 1 to 5 stars
- **E. Edit a movie** after adding it (e.g., correct the year or update where it is streaming)
- **F. Remove movies** no longer of interest, with Undo
- **G. Home dashboard:** total to watch, total watched, hours watched, the "Pick for Tonight" random pick, recently added and recently watched titles

## VI. Design (Wireframes)
- **A. Home screen:** title/logo, welcome message, statistics, Pick for Tonight with Shuffle, recently added list, Add Movie button, bottom navigation
- **B. Watchlist screen:** title, service filter chips, scrollable list of unwatched titles, checkbox to mark watched, detail line (year • genre • runtime), Add Movie button, bottom navigation
- **C. Watched history screen:** list of completed titles, star rating, date watched, total counter, bottom navigation
- **D. Add Movie screen:** title field, year field, genre dropdown, where-to-watch dropdown, runtime field, notes area, Save button
- **E. Landscape view (new):** side navigation rail, poster grid, and a rating panel beside the grid on the Watched screen
- **F. Navigation flow**
  1. Home → Watchlist → Add Movie → Movie Details
  2. Home → Watched → View Ratings
  3. Home → Settings → Preferences
  4. Movie Details → Mark as Watched / Rate / Edit / Delete
- **G. Design reasoning:** Every screen uses the same title bar / content / navigation pattern. A checkbox always means "watched". Add Movie is reachable from Home and Watchlist. Movie Details is the single hub for editing a title.

![Hand sketch](https://raw.githubusercontent.com/Sion24/COM-437/main/docs/wireframes/hand_sketch_portrait_landscape.jpeg)
![Digital mockup](https://raw.githubusercontent.com/Sion24/COM-437/main/docs/wireframes/digital_mockup_portrait_landscape.png)

## VII. Changes (Version Changelog)
| Version | Status | Summary |
|---|---|---|
| v0.1.0 | Previous (M4) | Initial outline (sections I–V) and GitHub repository |
| v0.2.0 | Previous (M5) | Wireframes, navigation flow and UX reasoning |
| v0.3.0 | Previous (M6) | First project pushed (Kotlin/Compose); Room database; all six screens; landscape layout; unit tests |
| v0.3.1 | **Current (M6)** | Rewritten in Java: XML layouts + Fragments, Navigation component, ViewModel + LiveData, Groovy Gradle |
| v0.4.0 | Future (M7) | Search, "Currently Watching" for shows, Pick for Tonight filters |
| v1.0.0 | Future (M8) | Final polish, screenshots, final report and investor presentation |

See [Changelog](Changelog) for full details.

## VIII. Links
- GitHub repository: https://github.com/Sion24/COM-437
- README: https://github.com/Sion24/COM-437/blob/main/README.md
