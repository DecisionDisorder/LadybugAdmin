package com.sweteam5.ladybugadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.text.Layout;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

public class BusLocationActivity extends AppCompatActivity {

    private BusLineView busLineView;
    private int i = 0; //TEST

    private BusView busView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_location);

        busLineView = new BusLineView(this);
        FrameLayout layout = findViewById(R.id.busLineContainer);
        layout.addView(busLineView);

        busView = drawBus(i);

        Button test = findViewById(R.id.button);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                if(i >= busLineView.getNames().length * 2 - 1)
                    i = 0;
                busView.updateLocation(i);
            }
        });
    }

    private BusView drawBus(int locIndex)
    {
        FrameLayout layout = findViewById(R.id.busDrawContainer);

        BusView busView = new BusView(this, busLineView);

        layout.addView(busView);
        busView.updateLocation(locIndex);

        return busView;
    }


}