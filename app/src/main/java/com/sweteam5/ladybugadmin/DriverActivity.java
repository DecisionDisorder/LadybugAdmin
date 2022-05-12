package com.sweteam5.ladybugadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class DriverActivity extends AppCompatActivity {
    private TextView locationTextView;

    private boolean isInOperation = false;
    private Spinner busNumberSpinner;
    private TextView currentLocationTextView;
    private TextView currentStateTextView;
    private Button changeStatusButton;

    LocationListener locationListener;
    private LocationManager locationManager = null;

    private LinearLayout startStationContainer;
    private LinearLayout currentLocationContainer;
    private Spinner startStationSpinner;
    private boolean isPassedStartingPoint;

    private BusLocator busLocator;
    private StationDataManager stationDataManager;

    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        locationTextView = findViewById(R.id.locationTextView);

        busNumberSpinner = findViewById(R.id.busNumberSpinner);
        currentStateTextView = findViewById(R.id.currentStateTextView);
        changeStatusButton = findViewById(R.id.changeStatusButton);
        currentLocationTextView = findViewById(R.id.currentLocationTextView);

        startStationContainer = findViewById(R.id.startStationContainer);
        currentLocationContainer = findViewById(R.id.currentLocationContainer);
        startStationSpinner = findViewById(R.id.startStationSpinner);

        setOperation(false);

        changeStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOperation(!isInOperation);
            }
        });

        permissionCheckGps();
        stationDataManager = new StationDataManager();
        stationDataManager.init(this);

        busLocator = new BusLocator(stationDataManager);
        initStartStationSpinner();
    }

    private void initStartStationSpinner() {
        String[] items = stationDataManager.getStationNameArray();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startStationSpinner.setAdapter(adapter);
    }

    private void setOperation(boolean active) {
        isInOperation = active;
        if (isInOperation) {
            /* Swap StartStation and CurrentLocation */
            startStationContainer.setVisibility(View.GONE);
            currentLocationContainer.setVisibility(View.VISIBLE);

            /* Initialize with starting station information */
            String stationName = startStationSpinner.getSelectedItem().toString();
            currentLocationTextView.setText("Going to " + stationName);
            busLocator.initStartIndex(busLocator.getIndexByName(stationName));
            isPassedStartingPoint = false;

            /* Change design of current state and change status button */
            currentStateTextView.setBackground(getResources().getDrawable(R.drawable.state_background_active));
            currentStateTextView.setText(getResources().getString(R.string.in_operation));
            currentStateTextView.setTextColor(Color.BLACK);
            changeStatusButton.setBackground(getResources().getDrawable(R.drawable.state_background_unactive));
            changeStatusButton.setText(getResources().getString(R.string.stop_driving));
            changeStatusButton.setTextColor(Color.WHITE);
            startGetLocation();
        } else {
            /* Swap StartStation and CurrentLocation */
            startStationContainer.setVisibility(View.VISIBLE);
            currentLocationContainer.setVisibility(View.GONE);

            /* Change design of current state and change status button */
            currentStateTextView.setBackground(getResources().getDrawable(R.drawable.state_background_unactive));
            currentStateTextView.setText(getResources().getString(R.string.stopped));
            currentStateTextView.setTextColor(Color.WHITE);
            changeStatusButton.setBackground(getResources().getDrawable(R.drawable.state_background_active));
            changeStatusButton.setText(getResources().getString(R.string.start_driving));
            changeStatusButton.setTextColor(Color.BLACK);
            stopGetLocation();
        }
        busNumberSpinner.setEnabled(!active);
    }

    private void permissionCheckGps() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location permission is needed.", Toast.LENGTH_SHORT).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Location information is required to share the location of the bus.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                Toast.makeText(this, "Location information is required to share the location of the bus.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Permission is granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "It hasn't been approved yet.", Toast.LENGTH_SHORT).show();
                }
            }
            return;

        }
    }

    private void startGetLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            locationTextView.setText( "위도 : " + latitude+ ", 경도 : " + longitude + ", 거리 : " + busLocator.getDistance(location));

        }
        locationListener = gpsLocationListener;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, locationListener);

    }

    private void stopGetLocation() {
        if(locationManager != null){
            locationManager.removeUpdates(locationListener);
        }
    }

    final LocationListener gpsLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            locationTextView.setText( "위도 : " + latitude+ ", 경도 : " + longitude + ", 거리 : " + busLocator.getDistance(location));
            setCurrentLocation(location);
        }
    };

    private void setCurrentLocation(Location currentLocation) {
        if(!isPassedStartingPoint) {
            double dist = busLocator.getDistance(currentLocation);
            if(dist < BusLocator.ERROR_RANGE) {
                currentLocationTextView.setText(busLocator.getCurrentStationName());
                isPassedStartingPoint = true;
            }
        }
        else {
            busLocator.setCurrentIndex(currentLocation);
            int currentIndex = busLocator.getCurrentIndex();
            if(currentIndex % 2 == 0)
                currentLocationTextView.setText(busLocator.getCurrentStationName());
            else
                currentLocationTextView.setText(busLocator.getCurrentStationName() + " → " + busLocator.getNextStationName());
        }
    }

    private void test() {
        //Coordinate pos1 = new Coordinate(37.453530, 127.134233);
        //Coordinate pos2 = new Coordinate(37.453099, 127.133622);
        //System.out.println(Coordinate.getDistance(pos1, pos2));
        Location loc1 = new Location("A");
        loc1.setLatitude(37.233299);
        loc1.setLongitude(127.062446);
        Location loc2 = new Location("B");
        loc2.setLatitude(37.232849);
        loc2.setLongitude(127.062335);
        System.out.println(loc1.distanceTo(loc2));
    }

}