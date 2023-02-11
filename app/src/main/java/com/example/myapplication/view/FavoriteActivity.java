package com.example.myapplication.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityFavoriteBinding;
import com.example.myapplication.model.Place;
import com.example.myapplication.viewmodel.PlaceViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FavoriteActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int REQUEST_CODE = 1;
    private Marker homeMarker;

    LocationManager locationManager;
    LocationListener locationListener;

    private ActivityFavoriteBinding binding;
    private PlaceViewModel placeViewModel;
    private long placeId;
    Place place;

    Polyline line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        placeViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);

        place = (Place) this.getIntent().getSerializableExtra("place");

        binding.backButton.setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                setHomeMarker(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (!hasLocationPermission())
            requestLocationPermission();
        else
            startUpdateLocation();

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (place != null) {
                    clearMap();
                }

                Location location = new Location("Your Destination");
                location.setLatitude(latLng.latitude);
                location.setLongitude(latLng.longitude);
                setMarker(latLng, true);

                drawLine(homeMarker.getPosition(), latLng);
                calculateDistance(homeMarker.getPosition(), latLng);
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener()
        {
            @Override
            public void onMarkerDragStart(Marker marker)
            {
                // TODO Auto-generated method stub
            }

            @Override
            public void onMarkerDragEnd(Marker marker)
            {
                if (place != null) {
                    clearMap();
                }

                Location location = new Location("Your Destination");
                location.setLatitude(marker.getPosition().latitude);
                location.setLongitude(marker.getPosition().longitude);
                setMarker(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude), true);

                drawLine(homeMarker.getPosition(), new LatLng(marker.getPosition().latitude, marker.getPosition().longitude));
                calculateDistance(homeMarker.getPosition(), new LatLng(marker.getPosition().latitude, marker.getPosition().longitude));
            }

            @Override
            public void onMarkerDrag(Marker marker)
            {
            }
        });
    }

    private void clearMap() {
        mMap.clear();
        line.remove();
    }
    private void drawLine(LatLng origin, LatLng destination) {
        Polyline polyline = mMap.addPolyline(new PolylineOptions()
                .add(origin, destination)
                .width(5)
                .color(Color.RED));

        line = polyline;
    }

    private void calculateDistance(LatLng origin, LatLng destination) {
        Double distance = SphericalUtil.computeDistanceBetween(origin, destination) / 1000;
        String msgA = Double.toString(Math.round(distance * 100.0) / 100.0) + "km";
        displayDistance(this, mMap, getPolylineCenter(origin, destination, distance), msgA, 2, 18);
    }

    private LatLng getPolylineCenter(LatLng origin, LatLng destination, Double distance) {
        return SphericalUtil.interpolate(origin,destination, 0.5);
    }

    public Marker displayDistance(final Context context, final GoogleMap map,
                                  final LatLng location, final String text, final int padding,
                                  final int fontSize) {

        Marker marker = null;

        if (context == null || map == null || location == null || text == null
                || fontSize <= 0) {
            return marker;
        }

        final TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextSize(fontSize);

        final Paint paintText = textView.getPaint();

        final Rect boundsText = new Rect();
        paintText.getTextBounds(text, 0, textView.length(), boundsText);
        paintText.setTextAlign(Paint.Align.CENTER);

        final Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        final Bitmap bmpText = Bitmap.createBitmap(boundsText.width() + 2
                * padding, boundsText.height() + 2 * padding, conf);

        final Canvas canvasText = new Canvas(bmpText);
        paintText.setColor(Color.BLACK);

        canvasText.drawText(text, canvasText.getWidth() / 2,
                canvasText.getHeight() - padding - boundsText.bottom, paintText);

        final MarkerOptions markerOptions = new MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.fromBitmap(bmpText))
                .anchor(0.5f, 1);

        return mMap.addMarker(markerOptions);
    }

    private void setMarker(LatLng latLng, boolean shouldQueryCompleteAddress) {
        if (place != null) {
            MarkerOptions options = new MarkerOptions().position(latLng)
                    .title(place.getAddress())
                    .draggable(true);
            mMap.addMarker(options);
        }
        else {
            MarkerOptions options = new MarkerOptions().position(latLng)
                    .title("Your destination")
                    .draggable(true);
            mMap.addMarker(options);
        }

        if (shouldQueryCompleteAddress) {
            getCompleteAddress(latLng);
        }
    }

    private void getCompleteAddress(LatLng latLng) {
        String address = "";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addressList != null && addressList.size() > 0) {

                // street name
                if (addressList.get(0).getThoroughfare() != null)
                    address += addressList.get(0).getThoroughfare();
                if (addressList.get(0).getLocality() != null)
                    address += addressList.get(0).getLocality() + " ";
                if (addressList.get(0).getPostalCode() != null)
                    address += addressList.get(0).getPostalCode() + " ";
                if (addressList.get(0).getAdminArea() != null)
                    address += addressList.get(0).getAdminArea();


                if (place != null && place.getId() != 0) {
                    placeId = place.getId();
                }
                place = new Place(address, latLng.latitude, latLng.longitude, new Date());
                if (place != null && placeId != 0) {
                    place.setId(placeId);
                    placeViewModel.update(place);
                }
                else {
                    placeViewModel.insert(place);
                }
            }
            else {
                place = new Place("", latLng.latitude, latLng.longitude, new Date());

                if (place != null && placeId != 0) {
                    place.setId(placeId);
                    placeViewModel.update(place);
                }
                else {
                    placeViewModel.insert(place);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (REQUEST_CODE == requestCode) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
            }
        }
    }

    private void setHomeMarker(Location location) {
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions options = new MarkerOptions().position(userLocation)
                .title("You are here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("Your Location");
        homeMarker = mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10));
    }
    private void startUpdateLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);

        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        setHomeMarker(lastKnownLocation);

        if (place != null) {
            setMarker(new LatLng(place.getLatitude(), place.getLongitude()), false);
            drawLine(homeMarker.getPosition(), new LatLng(place.getLatitude(), place.getLongitude()));
            calculateDistance(homeMarker.getPosition(), new LatLng(place.getLatitude(), place.getLongitude()));
        }
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


}
