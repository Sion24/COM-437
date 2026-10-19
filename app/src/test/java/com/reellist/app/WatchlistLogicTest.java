package com.reellist.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.reellist.app.data.Movie;
import com.reellist.app.logic.MovieForm;
import com.reellist.app.logic.SortOption;
import com.reellist.app.logic.WatchStats;
import com.reellist.app.logic.WatchlistLogic;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class WatchlistLogicTest {

    private static Movie movie(long id, String title, String genre, String service, Integer runtime,
                               boolean watched, int rating, long added, Long dateWatched) {
        Movie m = new Movie(title);
        m.id = id;
        m.genre = genre;
        m.whereToWatch = service;
        m.runtimeMinutes = runtime;
        m.watched = watched;
        m.rating = rating;
        m.dateAdded = added;
        m.dateWatched = dateWatched;
        return m;
    }

    private final List<Movie> movies = Arrays.asList(
            movie(1, "Dune", "Sci-Fi", "Max", 155, false, 0, 100, null),
            movie(2, "Coco", "Animation", "Disney+", 105, false, 0, 300, null),
            movie(3, "Arrival", "Sci-Fi", "Netflix", 116, true, 5, 200, 400L),
            movie(4, "Heat", "Thriller", "Netflix", null, true, 0, 50, 500L));

    private static List<Long> ids(List<Movie> list) {
        List<Long> out = new ArrayList<>();
        for (Movie m : list) out.add(m.id);
        return out;
    }

    @Test public void statsCountListsAndHours() {
        WatchStats s = WatchlistLogic.stats(movies);
        assertEquals(2, s.toWatch);
        assertEquals(2, s.watched);
        assertEquals(1, s.hoursWatched); // 116 min watched; Heat has no runtime
    }

    @Test public void filterByServiceAndChips() {
        assertEquals(Arrays.asList("All", "Disney+", "Max", "Netflix"), WatchlistLogic.serviceFilters(movies));
        assertEquals(2, WatchlistLogic.filterByService(movies, "Netflix").size());
        assertEquals(4, WatchlistLogic.filterByService(movies, "All").size());
    }

    @Test public void sortOptions() {
        assertEquals(Arrays.asList(2L, 3L, 1L, 4L), ids(WatchlistLogic.sort(movies, SortOption.DATE_ADDED)));
        assertEquals(Arrays.asList(3L, 2L, 1L, 4L), ids(WatchlistLogic.sort(movies, SortOption.TITLE)));
        List<Movie> byRuntime = WatchlistLogic.sort(movies, SortOption.RUNTIME);
        assertEquals(4L, byRuntime.get(byRuntime.size() - 1).id); // unknown runtime last
        List<Movie> byStatus = WatchlistLogic.sort(movies, SortOption.WATCHED_STATUS);
        assertTrue(!byStatus.get(0).watched && !byStatus.get(1).watched);
    }

    @Test public void pickOnlyUnwatchedAndAvoidRepeat() {
        for (int seed = 0; seed < 20; seed++) {
            Movie pick = WatchlistLogic.pickForTonight(movies, movies.get(0), new Random(seed));
            assertEquals(2L, pick.id); // only Coco is unwatched and not the current pick
        }
        assertNull(WatchlistLogic.pickForTonight(WatchlistLogic.watched(movies), null, new Random()));
    }

    @Test public void watchedHistoryNewestFirst() {
        assertEquals(Arrays.asList(4L, 3L), ids(WatchlistLogic.watched(movies)));
    }

    @Test public void detailLineAndRuntime() {
        assertEquals("Sci-Fi \u2022 2h 35m", WatchlistLogic.detailLine(movies.get(0)));
        assertEquals("2h", WatchlistLogic.formatRuntime(120));
        assertEquals("45m", WatchlistLogic.formatRuntime(45));
    }

    @Test public void formValidation() {
        assertEquals(new HashSet<>(Arrays.asList("title")),
                new MovieForm("  ", "", "", "", "", "").validate(2026).keySet());
        assertEquals(new HashSet<>(Arrays.asList("year", "runtime")),
                new MovieForm("X", "20x5", "", "", "-3", "").validate(2026).keySet());
        assertTrue(new MovieForm("Up", "2009", "", "", "96", "").validate(2026).isEmpty());
    }

    @Test public void formEditKeepsIdAndStatus() {
        Movie edited = MovieForm.from(movies.get(2)).withYear("2016").toMovie(movies.get(2));
        assertEquals(3L, edited.id);
        assertEquals(Integer.valueOf(2016), edited.releaseYear);
        assertTrue(edited.watched);
        assertEquals(5, edited.rating);
    }
}
