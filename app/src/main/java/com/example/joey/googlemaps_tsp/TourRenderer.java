package com.example.joey.googlemaps_tsp;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

/**
 * Created by Joey on 2015-07-11.
 */
public class TourRenderer extends SurfaceView implements Runnable{

    ArrayList<Marker> destinations;
    Thread thread = null;
    SurfaceHolder holder;
    volatile boolean isRunning = false;

    public TourRenderer(Context context, ArrayList<Marker> destinations) {
        super(context);
        holder = getHolder();
        destinations = destinations;
    }

    @Override
    public void run() {
        while (isRunning) {
            if (!holder.getSurface().isValid()) {
                continue;
            }
            Canvas canvas = holder.lockCanvas();
            canvas.drawARGB(100,100,100,100);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        isRunning = false;
        while(true) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            break;
        }
        thread = null;
    }

    public void resume() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }
}
