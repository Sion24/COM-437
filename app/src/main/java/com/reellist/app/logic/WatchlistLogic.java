package com.reellist.app.logic;

import com.reellist.app.data.Movie;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TreeSet;

/**
 * Plain Java rules for the lists. No Android code here, so it can be unit tested
 * on any computer (see WatchlistLogicTest).
 */
public final class WatchlistLogic {
    private WatchlistLogic() { }

    public static final String ALL_SERVICES = "All";

    private static final Comparator<Movie> BY_TITLE =
            Comparator.comparing(m -> m.title.toLowerCase(Locale.ROOT));

    public static List<Movie> toWatch(List<Movie> movies) {
        List<Movie> out = new ArrayList<>();
        for (Movie m : movies) if (!m.watched) out.add(m);
        return out;
    }

    /** Watched titles, most recently watched first. */
    public static List<Movie> watched(List<Movie> movies) {
        List<Movie> out = new ArrayList<>();
        for (Movie m : movies) if (m.watched) out.add(m);
        out.sort(Comparator.comparingLong((Movie m) -> m.dateWatched == null ? 0L : m.dateWatched).reversed());
        return out;
    }

    /** Service chips on the Watchlist: "All" plus every service the user has actually used. */
    public static List<String> serviceFilters(List<Movie> movies) {
        TreeSet<String> services = new TreeSet<>();
        for (Movie m : movies) if (!m.whereToWatch.trim().isEmpty()) services.add(m.whereToWatch);
        List<String> out = new ArrayList<>();
        out.add(ALL_SERVICES);
        out.addAll(services);
        return out;
    }

    public static List<Movie> filterByService(List<Movie> movies, String service) {
        if (ALL_SERVICES.equals(service)) return new ArrayList<>(movies);
        List<Movie> out = new ArrayList<>();
        for (Movie m : movies) if (m.whereToWatch.equals(service)) out.add(m);
        return out;
    }

    public static List<Movie> sort(List<Movie> movies, SortOption option) {
        List<Movie> out = new ArrayList<>(movies);
        switch (option) {
            case TITLE:
                out.sort(BY_TITLE);
                break;
            case GENRE:
                out.sort(Comparator.comparing((Movie m) -> m.genre.toLowerCase(Locale.ROOT)).thenComparing(BY_TITLE));
                break;
            case SERVICE:
                out.sort(Comparator.comparing((Movie m) -> m.whereToWatch.toLowerCase(Locale.ROOT)).thenComparing(BY_TITLE));
                break;
            case RUNTIME:
                // Unknown runtimes go to the bottom instead of the top
                out.sort(Comparator.comparing((Movie m) -> m.runtimeMinutes == null)
                        .thenComparingInt(m -> m.runtimeMinutes == null ? 0 : m.runtimeMinutes));
                break;
            case WATCHED_STATUS:
                out.sort(Comparator.comparing((Movie m) -> m.watched)
                        .thenComparing(Comparator.comparingLong((Movie m) -> m.dateAdded).reversed()));
                break;
            case DATE_ADDED:
            default:
                out.sort(Comparator.comparingLong((Movie m) -> m.dateAdded).reversed());
                break;
        }
        return out;
    }

    public static WatchStats stats(List<Movie> movies) {
        int toWatch = 0, watched = 0, minutes = 0;
        for (Movie m : movies) {
            if (m.watched) {
                watched++;
                if (m.runtimeMinutes != null) minutes += m.runtimeMinutes;
            } else {
                toWatch++;
            }
        }
        return new WatchStats(toWatch, watched, minutes / 60);
    }

    /** "Pick for Tonight": a random unwatched title. Avoids repeating the current pick when possible. */
    public static Movie pickForTonight(List<Movie> movies, Movie current, Random random) {
        List<Movie> pool = toWatch(movies);
        if (pool.isEmpty()) return null;
        List<Movie> choices = new ArrayList<>();
        for (Movie m : pool) if (current == null || m.id != current.id) choices.add(m);
        if (choices.isEmpty()) choices = pool;
        return choices.get(random.nextInt(choices.size()));
    }

    public static List<Movie> recentlyAdded(List<Movie> movies, int limit) {
        List<Movie> list = sort(toWatch(movies), SortOption.DATE_ADDED);
        return new ArrayList<>(list.subList(0, Math.min(limit, list.size())));
    }

    public static List<Movie> recentlyWatched(List<Movie> movies, int limit) {
        List<Movie> list = watched(movies);
        return new ArrayList<>(list.subList(0, Math.min(limit, list.size())));
    }

    /** One-line summary used in list rows: "2019 • Drama • 2h 12m". */
    public static String detailLine(Movie movie) {
        List<String> parts = new ArrayList<>();
        if (movie.releaseYear != null) parts.add(String.valueOf(movie.releaseYear));
        if (!movie.genre.trim().isEmpty()) parts.add(movie.genre);
        if (movie.runtimeMinutes != null) parts.add(formatRuntime(movie.runtimeMinutes));
        return String.join(" \u2022 ", parts);
    }

    public static String formatRuntime(int minutes) {
        if (minutes < 60) return minutes + "m";
        if (minutes % 60 == 0) return (minutes / 60) + "h";
        return (minutes / 60) + "h " + (minutes % 60) + "m";
    }
}
