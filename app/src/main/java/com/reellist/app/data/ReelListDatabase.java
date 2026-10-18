package com.reellist.app.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/** The on-device SQLite database. Data stays on the phone, so the app works offline. */
@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class ReelListDatabase extends RoomDatabase {

    public abstract MovieDao movieDao();

    private static volatile ReelListDatabase instance;

    public static ReelListDatabase get(Context context) {
        if (instance == null) {
            synchronized (ReelListDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            ReelListDatabase.class,
                            "reellist.db").build();
                }
            }
        }
        return instance;
    }
}
