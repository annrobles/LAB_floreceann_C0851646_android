package com.example.myapplication.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.RecyclerViewAdapter;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.helper.SwipeHelper;
import com.example.myapplication.helper.SwipeUnderlayButtonClickListener;
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

        swipeHelper = new SwipeHelper(this, 300, binding.recyclerView) {
            @Override
            protected void instantiateSwipeButton(RecyclerView.ViewHolder viewHolder, List<SwipeUnderlayButton> buffer) {
                buffer.add(new SwipeUnderlayButton(MainActivity.this,
                        "Delete",
                        R.drawable.ic_delete_white,
                        30,
                        50,
                        Color.parseColor("#ff3c30"),
                        SwipeDirection.LEFT,
                        new SwipeUnderlayButtonClickListener() {
                            @Override
                            public void onClick(int position) {
                                Place place = places.get(position);
                                placeViewModel.delete(place);
                                adapter.notifyItemRemoved(position);
                            }
                        }));
            }
        };
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