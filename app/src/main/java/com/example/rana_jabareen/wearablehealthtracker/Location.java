package com.example.rana_jabareen.wearablehealthtracker;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Location extends Fragment implements OnMapReadyCallback {
    private SupportMapFragment mapfragment;
    private GoogleMap map;
    private GPSTracker gps;
    private Polyline route = null;
    private PolylineOptions routeOpts = null;
    private boolean drawTrack = true;


    public Location() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_location, null, false);
        mapfragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        map = mapfragment.getMap();
        mapfragment.getMapAsync(this);
        setUpMapIfNeeded();

        return v;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setMyLocationEnabled(true);
        gps = new GPSTracker(getActivity());
        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            LatLng location = new LatLng(latitude, longitude);
            map.addMarker(new MarkerOptions().position(location));
            // map.moveCamera(CameraUpdateFactory.newLatLng(location));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            Toast.makeText(getActivity().getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();


        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.

            map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mapfragment.getMapAsync(this);


            map.setMyLocationEnabled(true);
            // Check if we were successful in obtaining the map.
            if (map != null) {

                setMap();

            }
        }
    }




    public void setMap() {
        routeOpts = new PolylineOptions().color(Color.BLUE)
                .width(3)
                .geodesic(true);
        route = map.addPolyline(routeOpts);
        route.setVisible(drawTrack);
        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {


            @Override
            public void onMyLocationChange(android.location.Location location) {
                LatLng newPoint = new LatLng(location.getLatitude(), location.getLongitude());
                map.addMarker(new MarkerOptions().position(newPoint));
                List<LatLng> points = route.getPoints();
                points.add(newPoint);
                route.setPoints(points);
            }
        });

    }


}
