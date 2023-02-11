package com.example.myapplication.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.RecyclerViewAdapter;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.helper.SwipeHelper;
import com.example.myapplication.model.Place;
import com.example.myapplication.viewmodel.PlaceViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private PlaceViewModel placeViewModel;

    private SwipeHelper swipeHelper;
    private RecyclerViewAdapter adapter;

    private List<Place> places = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        placeViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(PlaceViewModel.class);

        adapter = new RecyclerViewAdapter(places, getApplicationContext());
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1, RecyclerView.VERTICAL,false));
        binding.recyclerView.setAdapter(adapter);

        binding.fabContact.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
            startActivity(intent);
        });
    }

    protected void onResume() {
        super.onResume();
        placeViewModel.getAllPlaces().observe(this, allPlaces -> {
            places.clear();
            places.addAll(allPlaces);
            Toast.makeText(this, "Data Set changed", Toast.LENGTH_SHORT).show();
        });
    }

}