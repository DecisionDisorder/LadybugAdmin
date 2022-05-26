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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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

    private boolean[] busOperationCheckList;

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

        setOperation(false);

        setOperationBus();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        setOperation(false);
    }

    private void setOperationBus() {
        FirebaseDatabase.getInstance().getReference("Operation").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    System.out.println(task.getResult().getValue().toString());
                    busOperationCheckList = getAvailableBusList(task.getResult().getValue().toString());

                    getAvailableBus();
                }
            }
        });
    }

    private boolean[] getAvailableBusList(String codeInDictionary) {
        String subStr = codeInDictionary.substring(codeInDictionary.indexOf(',') + 2);
        subStr = subStr.replaceAll("]", "");
        subStr = subStr.replaceAll(" ", "");
        String[] res = subStr.split(",");
        boolean[] result = new boolean[res.length];
        for (int i = 0; i < result.length; i++)
            result[i] = Boolean.parseBoolean(res[i]);

        return result;
    }

    private void getAvailableBus() {
        ArrayList<Integer> operationableBus = new ArrayList<>();
        for(int i = 0; i < busOperationCheckList.length; i++) {
            if(!busOperationCheckList[i])
                operationableBus.add(i + 1);
        }

        int[] busNumberArr = new int[operationableBus.size()];
        for(int i = 0; i < operationableBus.size(); i++)
            busNumberArr[i] = operationableBus.get(i);

        Integer[] busNumbers = new Integer[busNumberArr.length];
        for (int i = 0; i < busNumberArr.length; i++)
            busNumbers[i] = Integer.valueOf(busNumberArr[i]);

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, busNumbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        busNumberSpinner.setAdapter(adapter);
    }

    private void initStartStationSpinner() {
        String[] items = stationDataManager.getStationNameArray();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startStationSpinner.setAdapter(adapter);
    }

    private void setOperation(boolean active) {
        isInOperation = active;
        setBusOperationToServer(active);
        if (isInOperation) {
            /* Swap StartStation and CurrentLocation */
            startStationContainer.setVisibility(View.GONE);
            currentLocationContainer.setVisibility(View.VISIBLE);

            /* Initialize with starting station information */
            String stationName = startStationSpinner.getSelectedItem().toString();
            currentLocationTextView.setText("Going to " + stationName);
            busLocator.initStartIndex(busLocator.getIndexByName(stationName), getCurrentBusNum());
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
            busLocator.initStartIndex(-1, getCurrentBusNum());
        }
        busNumberSpinner.setEnabled(!active);
    }

    public String getCurrentBusNum() {
        return busNumberSpinner.getSelectedItem().toString();
    }

    private void setBusOperationToServer(boolean active) {
        String busNum = getCurrentBusNum();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Operation");
        db.child(busNum).setValue(active).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });
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

            locationTextView.setText( "위도 : " + latitude+ ", 경도 : " + longitude + ", 거리 : " + busLocator.getDistance(location)
                    + ", Index: " + busLocator.getCurrentIndex());

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
            locationTextView.setText( "위도 : " + latitude+ ", 경도 : " + longitude + ", 거리 : " + busLocator.getDistance(location)
                    + ", Index: " + busLocator.getCurrentIndex());
            setCurrentLocation(location);
        }
    };

    private void setCurrentLocation(Location currentLocation) {
        if(!isPassedStartingPoint) {
            double dist = busLocator.getDistance(currentLocation);
            if(dist < BusLocator.ERROR_RANGE) {
                currentLocationTextView.setText(busLocator.getCurrentStationName());
                isPassedStartingPoint = true;
                busLocator.setCurrentIndex(currentLocation, getCurrentBusNum());
            }
        }
        else {
            busLocator.setCurrentIndex(currentLocation, getCurrentBusNum());
            int currentIndex = busLocator.getCurrentIndex();
            if(currentIndex % 2 == 0)
                currentLocationTextView.setText(busLocator.getCurrentStationName());
            else
                currentLocationTextView.setText(busLocator.getCurrentStationName() + " → " + busLocator.getNextStationName());
        }
    }
}