package com.example.myapplication.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myapplication.database.PlaceRoomDatabase;
import com.example.myapplication.model.Place;

import java.util.List;

public class PlaceRepository {
    private PlaceDao placeDao;
    private LiveData<List<Place>> allPlaces;

    public PlaceRepository(Application application) {
        PlaceRoomDatabase db = PlaceRoomDatabase.getInstance(application);
        placeDao = db.placeDao();
        allPlaces = placeDao.getAllPlaces();
    }

    public LiveData<List<Place>> getAllPlaces() {
        return allPlaces;
    }

    public LiveData<Place> getPlace(long id) {return placeDao.getPlace(id);}

    public void insert(Place place) {
        PlaceRoomDatabase.databaseWriteExecutor.execute(() -> placeDao.insert(place));
    }

    public void update(Place place) {
        PlaceRoomDatabase.databaseWriteExecutor.execute(() -> placeDao.update(place));
    }

    public void delete(Place place) {
        PlaceRoomDatabase.databaseWriteExecutor.execute(() -> placeDao.delete(place));
    }

}




