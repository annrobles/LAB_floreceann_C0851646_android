package com.example.myapplication.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.model.Place;

import java.util.List;

@Dao
public abstract class PlaceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insert(Place place);

    @Update
    public abstract void update(Place place);

    @Delete
    public abstract void delete(Place place);

    @Query("DELETE FROM place_table")
    public abstract void deleteAll();

    @Query("SELECT * FROM place_table ORDER BY address ASC")
    public abstract LiveData<List<Place>> getAllPlaces();

    @Query("SELECT * FROM place_table WHERE id == :id")
    public abstract LiveData<Place> getPlace(long id);
}
