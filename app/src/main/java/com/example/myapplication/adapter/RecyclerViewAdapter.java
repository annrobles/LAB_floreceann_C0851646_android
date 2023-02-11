package com.example.myapplication.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.R;
import com.example.myapplication.model.Place;
import com.example.myapplication.view.FavoriteActivity;
import com.example.myapplication.view.MainActivity;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "Cannot invoke method length() on null object";

    private List<Place> places;
    private Context context;
    private boolean isGridSelected;

    public RecyclerViewAdapter(List<Place> tList, Context context) {
        this.places = tList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.address.setText(places.get(position).getAddress());
            holder.createdDate.setText((CharSequence) places.get(position).getCreatedDate());

            holder.cardView.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), FavoriteActivity.class);
                intent.putExtra("place", places.get(position));
                view.getContext().startActivity(intent);
            });

            Log.d(TAG, "onBindViewHolder: contact");

        Log.d(TAG, "onBindViewHolder: none");
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView address;
        private TextView createdDate;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            address = itemView.findViewById(R.id.address);
            createdDate = itemView.findViewById(R.id.createdDate);
            cardView = itemView.findViewById(R.id.placeCard);

        }

    }

}
