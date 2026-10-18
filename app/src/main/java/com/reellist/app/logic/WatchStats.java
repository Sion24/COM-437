package com.reellist.app.logic;

/** Numbers shown on the Home dashboard. */
public final class WatchStats {
    public final int toWatch;
    public final int watched;
    public final int hoursWatched;

    public WatchStats(int toWatch, int watched, int hoursWatched) {
        this.toWatch = toWatch;
        this.watched = watched;
        this.hoursWatched = hoursWatched;
    }
}
