# COM-437
Movie List application project Project Description

A. App Name: ReelList B. Purpose:  a mobile application designed to help users keep track of the movies and shows they want to watch, are currently watching, and have already finished.  C. Target Users: Casual movie and TV watchers, People who subscribe to multiple streaming services, Film students and movie enthusiasts who track what they have seen, Families who want a shared list of what to watch together, Anyone who wants a simple way to organize what they plan to watch. D. Overall Goal: To give users one simple place to store everything they plan to watch, so they stop losing track of recommendations and stop wasting time scrolling through menus trying to decide on something.

II. Problem Addressing

A. People hear about movies from many different places. A friend recommends something over lunch, a trailer shows up on social media, a title gets saved on one streaming service, and another gets written down in a notes app. B. By the time the person finally sits down to watch something, they cannot remember any of the titles they wanted to see. C. ReelList addresses this problem by providing a single, platform neutral watchlist that belongs to the user rather than to any one streaming service. D. Instead of using screenshots, notes apps, and four different service watchlists, users can keep everything in one application.

III. Platform

A. Primary Platform: Android mobile devices. B. Development Environment: Android Studio. C. Programming Language: Java or Kotlin. D. Operating System: Android. E. The application will be designed for compatibility with commonly used Android smartphones.

IV. Front-End and Back-End Support

A. Front End

Home/Dashboard
Watchlist
Watched History
Add Movie
Movie Details
Settings
B. Back End

Movie title
Release year
Genre
Streaming service or where to watch
Runtime
Personal notes
Watched status
Personal rating
Date added
V. Functionality

A. Users can add a movie to their watchlist and enter: Movie title, Release year, Genre,Where to watch (Netflix, Hulu, theater, owned, etc.) Runtime,and Notes B. Users can view everything they plan to watch in one place. The list can be organized by: Date added, Genre, Streaming service, Runtime, Watched status. C. Users can mark a movie as watched once they finish it. D. Users can mark a movie as watched, and can give it a personal star rating from 1 to 5. E. Users can change information about a movie after adding it, such as correcting the year or updating where it is streaming. F. Users can remove movies they are no longer interested in. G. The home screen will show useful information such as: Total movies on the watchlist,Movies watched, A "What should I watch?" random pick from the list, Recently added titles, Recently watched titles 

VI. Design and Wireframes

A. Home Screen

App title/logo.
Welcome message.
Watchlist statistics (To Watch, Watched, Hours Watched).
"Pick for Tonight" random movie suggestion with shuffle button.
Recently added movies list.
Add Movie button.
Bottom navigation menu.
B. Watchlist Screen

Screen title.
Filter buttons by streaming service (All, Netflix, Max).
Scrollable list of unwatched movies.
Checkbox to mark each movie as watched.
Movie detail line (year, genre, runtime).
Add Movie button.
Bottom navigation menu.
C. Watched History Screen

Screen title.
List of completed movies.
Star rating for each movie.
Date the movie was watched.
Total watched counter.
Bottom navigation menu.
D. Movie Screen

Screen title.
Movie title text field.
Release year number field.
Genre dropdown menu.
Where to Watch dropdown menu.
Runtime number field.
Notes text area.
Save button.
E. Basic Navigation Flow
            
Home Dashboard → Watchlist → Add Movie → Movie Details
Home Dashboard → Watched → View Ratings
Home Dashboard → Settings → Preferences
Movie Details → Mark as Watched / Rate / Edit / Delete
