package com.example.spmons;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;


import android.os.Bundle;

import java.util.ArrayList;

public class ParkingLocations  extends FragmentActivity implements OnMapReadyCallback {

     GoogleMap mMap;

    ArrayList<LatLng> locationArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_locations);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // in below line we are initializing our array list.
        //locationArrayList = new ArrayList<>();

        // on below line we are adding our
        // locations in our array list.
       // locationArrayList.add(ps1);
        //locationArrayList.add(ps2);
        //locationArrayList.add(ps3);
        //locationArrayList.add(ps4);
        //locationArrayList.add(ps5);
        //locationArrayList.add(ps6);
        //locationArrayList.add(ps7);
        //locationArrayList.add(ps8);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.addMarker(new MarkerOptions().position(new LatLng(65.058, 25.47)).title("MY LOCATION") .snippet("kochi").draggable(true));

        // below line is use to move our camera to the specific location.

        }

    }
