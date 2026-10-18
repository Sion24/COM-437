package com.reellist.app.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * One row in the ReelList database = one movie or show the user is tracking.
 * These fields are the "Back End" list from the project outline (Section IV.B).
 */
@Entity(tableName = "movies")
public class Movie {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull public String title = "";
    @Nullable public Integer releaseYear;
    @NonNull public String genre = "";
    @NonNull public String whereToWatch = "";   // Netflix, Hulu, Max, Theater, Owned...
    @Nullable public Integer runtimeMinutes;
    @NonNull public String notes = "";
    public boolean watched;
    public int rating;                          // 0 = not rated yet, otherwise 1..5 stars
    public long dateAdded = System.currentTimeMillis();
    @Nullable public Long dateWatched;

    /** Room uses this constructor. */
    public Movie() { }

    @Ignore
    public Movie(@NonNull String title) {
        this.title = title;
    }

    /** Returns an independent copy, so screens never change a row without going through the repository. */
    @NonNull
    public Movie copy() {
        Movie m = new Movie();
        m.id = id;
        m.title = title;
        m.releaseYear = releaseYear;
        m.genre = genre;
        m.whereToWatch = whereToWatch;
        m.runtimeMinutes = runtimeMinutes;
        m.notes = notes;
        m.watched = watched;
        m.rating = rating;
        m.dateAdded = dateAdded;
        m.dateWatched = dateWatched;
        return m;
    }

    /** Used by the list adapters to decide whether a row needs redrawing. */
    public boolean sameContentAs(@NonNull Movie o) {
        return id == o.id && watched == o.watched && rating == o.rating && dateAdded == o.dateAdded
                && title.equals(o.title) && genre.equals(o.genre) && whereToWatch.equals(o.whereToWatch)
                && notes.equals(o.notes)
                && java.util.Objects.equals(releaseYear, o.releaseYear)
                && java.util.Objects.equals(runtimeMinutes, o.runtimeMinutes)
                && java.util.Objects.equals(dateWatched, o.dateWatched);
    }
}
