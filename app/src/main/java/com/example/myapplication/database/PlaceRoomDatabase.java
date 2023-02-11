package com.example.myapplication.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapplication.data.PlaceDao;
import com.example.myapplication.helper.Converters;
import com.example.myapplication.model.Place;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Place.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class PlaceRoomDatabase extends RoomDatabase {
    public abstract PlaceDao placeDao();

    private static volatile PlaceRoomDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    // executor service helps to do tasks in background thread
    public static final ExecutorService databaseWriteExecutor
            = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static PlaceRoomDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlaceRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    PlaceRoomDatabase.class,
                                    "place_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);

                    databaseWriteExecutor.execute(() -> {
                        PlaceDao placeDao = INSTANCE.placeDao();
                        placeDao.deleteAll();
                    });
                }
            };
}

