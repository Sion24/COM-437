package com.reellist.app.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * All database reads and writes for movies.
 * LiveData = the screens update automatically whenever the data changes.
 * Write methods are called on a background thread (see MovieRepository).
 */
@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies ORDER BY dateAdded DESC")
    LiveData<List<Movie>> observeAll();

    @Query("SELECT * FROM movies WHERE id = :id")
    LiveData<Movie> observeById(long id);

    @Insert
    long insert(Movie movie);

    @Update
    void update(Movie movie);

    @Delete
    void delete(Movie movie);

    @Query("DELETE FROM movies WHERE watched = 1")
    void clearWatchedHistory();
}
