package com.sweteam5.ladybugadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.text.Layout;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class BusLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_location);

        BusLineView view = new BusLineView(this);
        FrameLayout layout = findViewById(R.id.busLineContainer);
        layout.addView(view);

        drawBus();
    }

    private void drawBus()
    {
        FrameLayout layout = findViewById(R.id.busDrawContainer);
        ImageView busImg = new ImageView(this);

        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(size, size);

        busImg.setImageResource(R.drawable.ladybug_bus);
        busImg.setLayoutParams(param);
        busImg.setX(80);
        busImg.setY(0);

        layout.addView(busImg);
    }
}