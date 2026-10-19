package com.reellist.app.logic;

/** Ways the Watchlist can be ordered (Outline V.B). */
public enum SortOption {
    DATE_ADDED("Date added"),
    TITLE("Title"),
    GENRE("Genre"),
    SERVICE("Streaming service"),
    RUNTIME("Runtime"),
    WATCHED_STATUS("Watched status");

    public final String label;

    SortOption(String label) {
        this.label = label;
    }
}
