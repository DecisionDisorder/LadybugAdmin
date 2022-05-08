package com.sweteam5.ladybugadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MngInfoActivity extends AppCompatActivity {

    private ArrayList<CodeInfo> driverInfoList;
    private ArrayList<CodeInfo> busInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mng_info);

        driverInfoList = new ArrayList<CodeInfo>();
        busInfoList = new ArrayList<CodeInfo>();

        Button addDriverButton = findViewById(R.id.addDriverButton);
        addDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDriverCode();
            }
        });

        Button addBusButton = findViewById(R.id.addBusButton);
        addBusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBusCode();
            }
        });
    }

    private void addDriverCode()
    {
        LinearLayout driverCodeContainer = findViewById(R.id.driverCodeContainer);
        CodeInfo newDriverCode = new CodeInfo(getApplicationContext(), getResources().getString(R.string.driver)
                + " " + driverInfoList.size());
        driverInfoList.add(newDriverCode);
        driverCodeContainer.addView(newDriverCode);
    }
    private void addBusCode()
    {
        LinearLayout busCodeContainer = findViewById(R.id.busCodeContainer);
        CodeInfo newBusCode = new CodeInfo(getApplicationContext(), getResources().getString(R.string.bus)
                + " " + busInfoList.size());
        busInfoList.add(newBusCode);
        busCodeContainer.addView(newBusCode);
    }
}