package com.example.streetlity_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ArrayList<MarkerOptions> markList = new ArrayList<MarkerOptions>();
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        MarkerOptions option = new MarkerOptions();
        MarkerOptions option2 = new MarkerOptions();
        MarkerOptions option3 = new MarkerOptions();

        option.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_fuel));
        option.title("Marker in Sydney");
        option.position(sydney);
        markList.add(option);

        sydney = new LatLng(-34, 161);
        option2.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_fuel));
        option2.title("Marker in awaswa");
        option2.position(sydney);
        markList.add(option2);

        sydney = new LatLng(-54, 161);
        option3.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_fuel));
        option3.title("Marker in awaswa");
        option3.position(sydney);
        markList.add(option3);

        for (int i = 0; i < markList.size(); i++) {
            mMap.addMarker(markList.get(i));
            Log.e("abc", "aa" );
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
