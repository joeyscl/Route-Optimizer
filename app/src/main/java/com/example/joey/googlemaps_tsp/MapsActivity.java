package com.example.joey.googlemaps_tsp;

import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ArrayList<Polyline> polylines = new ArrayList();
//    private TourRenderer tourRenderer = new TourRenderer(this, destinations);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mMap.setMyLocationEnabled(false);

//        tourRenderer = (TourRenderer) findViewById(R.id.tourRendererOverlay);
    }

    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link com.google.android.gms.maps.SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        // Setting a click event handler for the map

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (TourManager.numberOfDestinations() == 10) {
                    // ADD ALERT DIALOG
                    return;
                }
                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

//                // Animating to the touched position
//                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                Marker marker = mMap.addMarker(markerOptions);
                // Add the new Destination/Marker to TourManager and to mapMarkers
                TourManager.addDestination(new Destination(marker));
            }
        });

        // Disable clicking on markers
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return true;
            }
        });

    }

    public void clearMap(View view) {
        mMap.clear();
        TourManager.removeAll();
    }

    public void graphMap(Tour tour) {

        ArrayList<Marker> markers = new ArrayList<Marker>();
        for (Destination D: tour.getAllDest()) {
            markers.add(D.getMarker());
        }

        if (markers.size()==0) return;

        // remove existing polylines
        for(Polyline pl : polylines) {
            pl.remove();
        }

        ArrayList<LatLng> latLngs = new ArrayList<>();
        for (Marker M: markers) {
            latLngs.add(M.getPosition());
        }
        latLngs.add(markers.get(0).getPosition());
        PolylineOptions poly = new PolylineOptions().addAll(latLngs);

        polylines.add(mMap.addPolyline(poly));
    }

    public void TSP_SA(View view) {
        Tour bestTour = TSP_SA.simulate();
        if (bestTour == null) return;
        graphMap(bestTour);
    }

    public void TSP_GA(View view) {
        Tour bestTour = TSP_GA.simulate();
        if (bestTour == null) return;
        graphMap(bestTour);
    }

    public void TSP_TA(View view) {
//        Tour bestTour = TSP_TA.simulate();
//        if (bestTour == null) return;
//        graphMap(bestTour);

        GA_Task task = new GA_Task();
        task.execute();
    }

    // solves and displays TSP using GA
    class GA_Task extends AsyncTask<Void, Void, Void> {

        private volatile Population pop;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MapsActivity.this.setProgressBarIndeterminateVisibility(true);
            pop = new Population(50, true);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            long time = System.currentTimeMillis();
            long lastPublishTime = time;

            for (int i = 0; i < 200; i++) {
                time = System.currentTimeMillis();
//                pop = GA.evolvePopulation(pop);
                if (time - lastPublishTime > 200) {
                    lastPublishTime = time;
                    publishProgress();
                }

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            graphMap(pop.getFittest());
            System.out.println("GA Final distance: " + pop.getFittest().getDistance());
            MapsActivity.this.setProgressBarIndeterminateVisibility(false);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            int num = (int) (Math.random() * pop.populationSize());
            graphMap(pop.getTour(num));
            System.out.println("Current distance: " + pop.getFittest().getDistance());
        }
    }
}
