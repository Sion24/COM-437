package com.reellist.app.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Choices shown in the Add Movie dropdowns. Users can still type their own value. */
public final class MovieOptions {
    private MovieOptions() { }

    public static final List<String> GENRES = Collections.unmodifiableList(Arrays.asList(
            "Action", "Animation", "Comedy", "Documentary", "Drama",
            "Family", "Horror", "Romance", "Sci-Fi", "Thriller", "Other"));

    public static final List<String> SERVICES = Collections.unmodifiableList(Arrays.asList(
            "Netflix", "Hulu", "Max", "Disney+", "Prime Video",
            "Apple TV+", "Peacock", "Paramount+", "Theater", "Owned", "Other"));
}
