package com.sweteam5.ladybugadmin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.Nullable;

public class BusView extends View {
    private BusLineView busLineView;

    private Paint paint;
    private Bitmap bus;
    private Bitmap scaledBus;
    private int locIndex = 0;
    private int size = 0;

    private Context context;

    public BusView(Context context, BusLineView busLineView) {
        super(context);

        init(context, busLineView);
    }

    public BusView(Context context, @Nullable AttributeSet attrs, BusLineView busLineView) {
        super(context, attrs);

        init(context, busLineView);
    }

    public void init(Context context, BusLineView busLineView) {
        this.busLineView = busLineView;
        this.context = context;

        paint = new Paint();
        bus = BitmapFactory.decodeResource(getResources(), R.drawable.ladybug_bus);
        size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        scaledBus = Bitmap.createScaledBitmap(bus, size, size, true);
    }

    public void updateLocation(int locIndex) {
        this.locIndex = locIndex;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(scaledBus, busLineView.getPosX() - size / 2, getBusY(locIndex) - size / 2, paint);
    }

    private int getBusY(int locIndex) {
        int height = busLineView.getLineHeight();
        int middleIndex = busLineView.getMiddleIndex();
        int startY = busLineView.getStartY();
        int len = busLineView.getNames().length;
        int stationY = busLineView.getY(locIndex / 2, middleIndex, len, height, startY);

        if(locIndex < middleIndex * 2) {
            if(locIndex % 2 == 0) {
                return stationY;
            }
            else {
                return stationY + height / 2 / middleIndex / 2;
            }
        }
        else if(locIndex > middleIndex * 2) {
            if(locIndex % 2 == 0) {
                return stationY;
            }
            else {
                return stationY + height / 2 / (len - middleIndex - 1) / 2;
            }
        }
        else {
            return stationY;
        }
    }
}
