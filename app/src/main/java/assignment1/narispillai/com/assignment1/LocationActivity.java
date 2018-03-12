package assignment1.narispillai.com.assignment1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback{

    //Declaration for the google map.
    private GoogleMap mMap;

    //Map layout type static variable
    static int maptypevalue = 1;

    //Latitude and Longitude value declaration
    double lat;
    double log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Floating icon declaration for the marker and map type.
        FloatingActionButton marker = (FloatingActionButton) findViewById(R.id.markerposition);
        FloatingActionButton type = (FloatingActionButton) findViewById(R.id.maptype);

        //Programmatically clicking the marker button
        marker.performClick();


        if(mMap != null){
            onMapReady(mMap);
        }
        else{
            ActivityCompat.requestPermissions(LocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        //Marker floating button declaration
        marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMapReady(mMap);
            }
        });

        //Map type floating button declaration
        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //If else to change the map type.

                if(maptypevalue == 0){

                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    maptypevalue=1;
                    Snackbar snackbar = Snackbar
                            .make(view, "Map is now changed to NORMAL type.", Snackbar.LENGTH_SHORT);

                    snackbar.show();
                }
                else if(maptypevalue == 1){

                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    maptypevalue=2;
                    Snackbar snackbar = Snackbar
                            .make(view, "Map is now changed to HYBRID type.", Snackbar.LENGTH_SHORT);

                    snackbar.show();
                }
                else if(maptypevalue == 2){

                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    maptypevalue=3;
                    Snackbar snackbar = Snackbar
                            .make(view, "Map is now changed to SATELLITE type.", Snackbar.LENGTH_SHORT);

                    snackbar.show();
                }
                else if(maptypevalue == 3){

                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    maptypevalue=0;
                    Snackbar snackbar = Snackbar
                            .make(view, "Map is now changed to TERRAIN type.", Snackbar.LENGTH_SHORT);

                    snackbar.show();
                }
                else{
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    Snackbar snackbar = Snackbar
                            .make(view, "No changes", Snackbar.LENGTH_SHORT);

                    snackbar.show();
                }

            }
        });



    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
    }


    public boolean havelocationPermissions(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionReadSDCard = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if(permissionReadSDCard == PackageManager.PERMISSION_GRANTED){
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return true;
        }
    }

    public void onLocationChanged(Location location) {
        mMap.clear();

        MarkerOptions mp = new MarkerOptions();
        mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
        mp.title("my position");

        mMap.addMarker(mp);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 16));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        View v = null;
        
        Intent intent = getIntent();
        String adminaddress = intent.getExtras().getString("ADDRESS");
        String adminname = intent.getExtras().getString("Name");

        Geocoder gc = new Geocoder(this);

        //String to lat and log conversion using GEOCODER
        try {
            List<Address> list = gc.getFromLocationName(adminaddress,1);

            try {
                Address add = list.get(0);
                String locality = add.getLocality();

                lat = add.getLatitude();
                log = add.getLongitude();

                Toast.makeText(getApplicationContext(),adminaddress,Toast.LENGTH_LONG).show();
            }
            catch(Exception e){
                Toast.makeText(getApplicationContext(),"Sorry, Invalid Address",Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"Sorry, Invalid Address",Toast.LENGTH_LONG).show();
        }

        mMap = googleMap;


        LatLng teamtarget = new LatLng(lat,log);
        int permissionLocation = ContextCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionLocation == PackageManager.PERMISSION_GRANTED){

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            //moveToCurrentLocation(teamtarget);
        }
        moveToCurrentLocation(teamtarget);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().isMyLocationButtonEnabled();
        googleMap.getUiSettings().isMapToolbarEnabled();
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.addMarker(new
                MarkerOptions().position(teamtarget).title(adminname));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(teamtarget));

    }


    private void moveToCurrentLocation(LatLng currentLocation)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


    }

}