package com.example.e_bus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;

import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.Build;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.location.LocationServices;
import android.location.Location;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;



public class Dmain extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    DrawerLayout layDL;
    NavigationView vNV;
    Toolbar toolbar;
    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private DatabaseReference databaseReference;
    // Handler for simulating continuous location updates
    private Handler handler;
    private static final int UPDATE_INTERVAL = 5000;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dmain);




        // Initialize the handler
        handler = new Handler();
        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        layDL = findViewById(R.id.layDL);
        vNV = findViewById(R.id.vNV);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, layDL, toolbar, R.string.nav_open, R.string.nav_close);

        layDL.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            vNV.setCheckedItem(R.id.nav_home);
        }
        NavClick();


    }
    private void NavClick() {
        vNV.setNavigationItemSelectedListener(item -> {
            Fragment frag = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                Intent intent =new Intent(this, Home.class);
                startActivity(intent);
            } else if (itemId == R.id.setting) {
                Intent intent =new Intent(this, Setting.class);
                startActivity(intent);
            }else if (itemId == R.id.logout) {
                Intent intent =new Intent(this, Login.class);
                startActivity(intent);
            }

            layDL.closeDrawer(GravityCompat.START);
            return true;
        });
    }
    @Override
    public void onBackPressed() {
        Fragment currFrag = getSupportFragmentManager().findFragmentById(R.id.layFL);
        if (layDL.isDrawerOpen(GravityCompat.START)){
            layDL.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        // Create a marker for the initial location
        mCurrLocationMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(33.5620696, 73.0740113))
                .title("Marker in current location")
                .icon(BitmapFromDrawable(this, R.drawable.bus1)));

        // Start simulating continuous updates
        simulateContinuousUpdates();
    }

    private void simulateContinuousUpdates() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Simulate location change by moving the marker
                simulateLocationChange();
                // Schedule the next update
                handler.postDelayed(this, UPDATE_INTERVAL);
            }

            private void simulateLocationChange() {
                LatLng currentLatLng = mCurrLocationMarker.getPosition();
                double newLat = currentLatLng.latitude + 0.0001; // Simulate a small change in latitude
                double newLng = currentLatLng.longitude + 0.0001; // Simulate a small change in longitude

                // Update the marker position
                LatLng newLatLng = new LatLng(newLat, newLng);
                mCurrLocationMarker.setPosition(newLatLng);

                // Move camera to the updated position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(newLatLng));
            }
        }, UPDATE_INTERVAL);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
       // Sets the interval at which location updates are requested
        mLocationRequest.setInterval(1000);
        //It ensures that the app won't receive updates more frequently than the specified interval.
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //If the location permission is granted, this line requests location updates from the
            // Fused Location Provider using the
            // mGoogleApiClient, mLocationRequest, and this (referring to the current instance of the class).
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // Remove the previous location marker if it exists
        mLastLocation = location;


        LocationHelper helper = new LocationHelper(
                location.getLongitude(),
                location.getLatitude()
        );
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Save the current location to Firebase Realtime Database
        FirebaseFirestore.getInstance().collection("Drivers")
                .document(userId)
                .update("Latitude", location.getLatitude(),
                        "Longitude", location.getLongitude()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Dmain.this, "Location saved", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Dmain.this, "Location not saved", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
// Update the marker position
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mCurrLocationMarker.setPosition(latLng);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    private BitmapDescriptor BitmapFromDrawable(Dmain dmain, int bus1) {
        // on below line we are creating a drawable from its id.
        Drawable imageDrawable = ContextCompat.getDrawable(dmain, bus1);
        // below line is use to set bounds to our vector drawable.
        imageDrawable.setBounds(0, 0, imageDrawable.getIntrinsicWidth(), imageDrawable.getIntrinsicHeight());
        // on below line is use to create a bitmap for our drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(imageDrawable.getIntrinsicWidth(), imageDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        // on below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);
        // below line is use to draw our
        // vector drawable in canvas.
        imageDrawable.draw(canvas);
        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}



