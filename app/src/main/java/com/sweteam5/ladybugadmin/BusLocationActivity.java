package com.sweteam5.ladybugadmin;

import android.app.ActivityManager;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class BusLocationActivity extends AppCompatActivity {

    private BusLineView busLineView;
    private int i = 0; //index

    ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
    List proInfos = activityManager.getRunningAppProcesses();


    private BusView busView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_location);

        busLineView = new BusLineView(this);
        FrameLayout layout = findViewById(R.id.busLineContainer);
        layout.addView(busLineView);

        busView = drawBus(i);

        Thread busThread = new Thread(new busThread());
        busThread.start();

//        Button test = findViewById(R.id.button);
//        test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                i++;
//                if(i >= busLineView.getNames().length * 2 - 1)
//                    i = 0;
//
//            }
//        });
    }

    private BusView drawBus(int locIndex)
    {
        FrameLayout layout = findViewById(R.id.busDrawContainer);

        BusView busView = new BusView(this, busLineView);

        layout.addView(busView);
        busView.updateLocation(locIndex);

        return busView;
    }

    class busThread extends Thread{
        @Override
        public void run() {
            //when there is running activity
            while (proInfos.size() > 0 ) {
                //get Bus location
                //convert bus location to i
                busView.updateLocation(i);

            }

        }
    }


}