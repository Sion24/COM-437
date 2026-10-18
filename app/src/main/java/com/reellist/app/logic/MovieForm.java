package com.reellist.app.logic;

import com.reellist.app.data.Movie;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * What the user types on the Add / Edit Movie screen, kept as text until Save.
 * validate() returns friendly error messages; toMovie() builds the database row.
 */
public final class MovieForm {
    public final String title;
    public final String year;
    public final String genre;
    public final String whereToWatch;
    public final String runtime;
    public final String notes;

    public MovieForm(String title, String year, String genre, String whereToWatch, String runtime, String notes) {
        this.title = nz(title);
        this.year = nz(year);
        this.genre = nz(genre);
        this.whereToWatch = nz(whereToWatch);
        this.runtime = nz(runtime);
        this.notes = nz(notes);
    }

    /** Field name -> error message. An empty map means the form can be saved. */
    public Map<String, String> validate(int currentYear) {
        Map<String, String> errors = new LinkedHashMap<>();
        if (title.trim().isEmpty()) errors.put("title", "Title is required");
        if (!year.trim().isEmpty()) {
            Integer y = parseInt(year);
            if (y == null || y < 1888 || y > currentYear + 5) {
                errors.put("year", "Enter a year between 1888 and " + (currentYear + 5));
            }
        }
        if (!runtime.trim().isEmpty()) {
            Integer r = parseInt(runtime);
            if (r == null || r <= 0 || r > 1000) errors.put("runtime", "Runtime must be minutes, e.g. 120");
        }
        return errors;
    }

    /** Creates a new Movie, or updates {@code existing} while keeping its id, watched status and rating. */
    public Movie toMovie(Movie existing) {
        Movie m = existing == null ? new Movie() : existing.copy();
        m.title = title.trim();
        m.releaseYear = parseInt(year);
        m.genre = genre.trim();
        m.whereToWatch = whereToWatch.trim();
        m.runtimeMinutes = parseInt(runtime);
        m.notes = notes.trim();
        return m;
    }

    public MovieForm withYear(String newYear) {
        return new MovieForm(title, newYear, genre, whereToWatch, runtime, notes);
    }

    public static MovieForm from(Movie movie) {
        return new MovieForm(
                movie.title,
                movie.releaseYear == null ? "" : String.valueOf(movie.releaseYear),
                movie.genre,
                movie.whereToWatch,
                movie.runtimeMinutes == null ? "" : String.valueOf(movie.runtimeMinutes),
                movie.notes);
    }

    private static String nz(String s) { return s == null ? "" : s; }

    private static Integer parseInt(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
