package com.reellist.app.data;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Single doorway between the screens and the database.
 * Every change to a movie goes through here, which matches the design goal of
 * "every change to a movie record originates from the same place".
 * Room does not allow database writes on the main (UI) thread, so writes run on one background thread.
 */
public class MovieRepository {

    /** Simple callback used to hand a result back to the UI thread. */
    public interface Callback<T> { void onResult(T value); }

    private final MovieDao dao;
    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private final Handler main = new Handler(Looper.getMainLooper());

    public MovieRepository(MovieDao dao) {
        this.dao = dao;
    }

    public LiveData<List<Movie>> getMovies() { return dao.observeAll(); }

    public LiveData<Movie> movie(long id) { return dao.observeById(id); }

    public void add(Movie movie, @Nullable Callback<Long> onAdded) {
        Movie m = movie.copy();
        m.title = m.title.trim();
        io.execute(() -> {
            long id = dao.insert(m);
            if (onAdded != null) main.post(() -> onAdded.onResult(id));
        });
    }

    public void save(Movie movie) {
        Movie m = movie.copy();
        m.title = m.title.trim();
        io.execute(() -> dao.update(m));
    }

    public void delete(Movie movie) {
        Movie m = movie.copy();
        io.execute(() -> dao.delete(m));
    }

    public void markWatched(Movie movie, boolean watched) {
        Movie m = movie.copy();
        m.watched = watched;
        m.dateWatched = watched ? System.currentTimeMillis() : null;
        if (!watched) m.rating = 0;
        io.execute(() -> dao.update(m));
    }

    public void rate(Movie movie, int stars) {
        Movie m = movie.copy();
        m.rating = Math.max(0, Math.min(5, stars));
        io.execute(() -> dao.update(m));
    }

    public void clearWatchedHistory() {
        io.execute(dao::clearWatchedHistory);
    }
}
