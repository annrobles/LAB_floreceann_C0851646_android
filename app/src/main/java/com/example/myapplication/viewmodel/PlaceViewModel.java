package com.example.myapplication.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication.data.PlaceRepository;
import com.example.myapplication.model.Place;

import java.util.List;

public class PlaceViewModel extends AndroidViewModel {
    private PlaceRepository repository;
    private final LiveData<List<Place>> allPlaces;

    public PlaceViewModel(@NonNull Application application) {
        super(application);

        repository = new PlaceRepository(application);
        allPlaces = repository.getAllPlaces();
    }

    public LiveData<List<Place>> getAllPlaces() {return allPlaces;}

    public LiveData<Place> getPlace(long id) {return repository.getPlace(id);}

    public void insert(Place place) {repository.insert(place);}

    public void update(Place place) {repository.update(place);}

    public void delete(Place place) {repository.delete(place);}
}

