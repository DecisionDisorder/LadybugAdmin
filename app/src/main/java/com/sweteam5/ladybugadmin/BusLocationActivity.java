package com.sweteam5.ladybugadmin;

import android.app.ActivityManager;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BusLocationActivity extends AppCompatActivity {

    private LinearLayout busLayout;
    private BusLineView busLineView;
    private BusView[] busViewsList = new BusView[3];

    private int[] busLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_location);

        busLayout = findViewById(R.id.busDrawContainer);
        busLineView = new BusLineView(this);
        FrameLayout lineLayout = findViewById(R.id.busLineContainer);
        lineLayout.addView(busLineView);

        getBusLocationFromServer();
    }

    private void drawBus(int locIndex, int busNum)
    {
        BusView busView = new BusView(this, busLineView);

        busViewsList[busNum] = busView;
        busLayout.addView(busViewsList[busNum]);
        busViewsList[busNum].updateLocation(locIndex);
    }

    private void deleteBus(int busNum) {
        busViewsList[busNum].stopAnimation();
        busLayout.removeView(busViewsList[busNum]);
        busViewsList[busNum] = null;
    }

    private void getBusLocationFromServer() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference("Location").orderByKey().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()) {
                            String message = task.getResult().getValue().toString();
                            busLocations = getBusLocations(message);
                            setBusLocations();
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 2000);
    }

    private void setBusLocations() {
        for(int i = 0; i < busViewsList.length; i++) {
            if(busViewsList[i] != null && busLocations[i] == -1)
                deleteBus(i);
            else if(busViewsList[i] == null && busLocations[i] != -1)
                drawBus(busLocations[i], i);
            else if(busViewsList[i] != null && busLocations[i] != -1)
                busViewsList[i].updateLocation(busLocations[i]);

        }
    }

    private int[] getBusLocations(String message) {
        int[] result = new int[3];
        message = message.trim();
        int index = Integer.parseInt(message.substring(message.indexOf('_') + 1, message.indexOf('_') + 2)) - 1;
        result[index] = Integer.parseInt(message.substring(message.indexOf('=') + 1, message.indexOf(',')));
        message = message.substring(message.indexOf(',') + 1);

        index = Integer.parseInt(message.substring(message.indexOf('_') + 1, message.indexOf('_') + 2)) - 1;
        result[index] = Integer.parseInt(message.substring(message.indexOf('=') + 1, message.indexOf(',')));
        message = message.substring(message.indexOf(',') + 1);

        index = Integer.parseInt(message.substring(message.indexOf('_') + 1, message.indexOf('_') + 2)) - 1;
        result[index] = Integer.parseInt(message.substring(message.indexOf('=') + 1, message.indexOf('}')));

        return result;
    }
}