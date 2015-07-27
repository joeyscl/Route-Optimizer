package com.example.joey.googlemaps_tsp;

import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity {

    private Boolean EMULATOR;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ArrayList<Polyline> polylines = new ArrayList();
//    private TourRenderer tourRenderer = new TourRenderer(this, destinations);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mMap.setMyLocationEnabled(false);

        ApplicationInfo ai = null;
        try {
            ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            EMULATOR = bundle.getBoolean("emulator", false);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
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

                if (TourManager.numberOfDestinations() >= 12) {
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
        SA_Task task = new SA_Task();
        task.execute();
    }

    public void TSP_GA(View view) {
        GA_Task task = new GA_Task();
        task.execute();
    }

    public void TSP_TA(View view) {
    }

    // solves and displays TSP using GA
    class GA_Task extends AsyncTask<Void, Tour, Population> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MapsActivity.this.setProgressBarIndeterminateVisibility(true);

            //change color of button to indicate processing
            Button GA_button = (Button) findViewById(R.id.graphGAButton);
            GA_button.setBackgroundColor(0xb0FF9933);
        }

        @Override
        protected Population doInBackground(Void... voids) {

            // Initialization
            Population pop = new Population(50, true);
            Tour fittest = pop.getFittest();
            publishProgress(fittest);

            long time = System.currentTimeMillis();
            long lastPublishTime = time;

            for (int i = 0; i < 200; i++) {
                time = System.currentTimeMillis();
                pop = GA.evolvePopulation(pop);
                if (time - lastPublishTime > 200) {
                    lastPublishTime = time;
                    publishProgress(pop.getFittest());
                }

                if (EMULATOR) {
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return pop;
        }

        @Override
        protected void onPostExecute(Population pop) {
            super.onPostExecute(pop);
            Tour fittest = pop.getFittest();
            graphMap(fittest);
            System.out.println("GA Final distance: " + pop.getFittest().getDistance());

            TextView tv1 = (TextView) findViewById(R.id.final_distance);
            double finalDistance = Math.round(pop.getFittest().getDistance());
            tv1.setText("FINAL DISTANCE: " + finalDistance + " km");

            //change color of button to indicate finish
            Button GA_button = (Button) findViewById(R.id.graphGAButton);
            GA_button.setBackgroundColor(0xb0ffffff);

            MapsActivity.this.setProgressBarIndeterminateVisibility(false);
        }

        @Override
        protected void onProgressUpdate(Tour... tours) {
            super.onProgressUpdate(tours[0]);
            Tour currentBest = tours[0];
            graphMap(currentBest);
            System.out.println("Current distance: " + currentBest.getDistance());
        }
    }

    // solves and displays TSP using SA
    class SA_Task extends AsyncTask<Void, Void, Void> {

        volatile Tour current;
        volatile Tour best;
        double temp = 100000000;
        double coolingRate = 0.002;

        long time = System.currentTimeMillis();
        long lastPublishTime = time;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MapsActivity.this.setProgressBarIndeterminateVisibility(true);

            //change color of button to indicate processing
            Button GA_button = (Button) findViewById(R.id.graphSAButton);
            GA_button.setBackgroundColor(0xb0FF9933);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            // Initialization
            current = new Tour();
            current.generateIndividual();
            System.out.println("Initial distance: " + current.getDistance());
            best = new Tour(current);
            publishProgress(); // Initial graph

            while (temp > 1) {

                long time = System.currentTimeMillis();

                Tour newSolution = new Tour(current);
                newSolution.mutateIndividual();

                double currentEnergy = current.getDistance();
                double neighbourEnergy = newSolution.getDistance();

                // Decide if we should accept the neighbour
                if (TSP_SA.acceptanceProbability(currentEnergy, neighbourEnergy, temp) > Math.random()) {
                    current = new Tour(newSolution);
                }

                // Keep track of the best solution found
                if (current.getDistance() < best.getDistance()) {
                    best = current;
                }

                if (time - lastPublishTime > 200) {
                    lastPublishTime = time;
                    publishProgress();
                }

                if (EMULATOR) {
                    try {
                        Thread.sleep(0, 5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                temp *= 1 - coolingRate;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            graphMap(best);
            System.out.println("SA Final distance: " + best.getDistance());

            TextView tv1 = (TextView) findViewById(R.id.final_distance);
            int finalDistance = (int) best.getDistance();
            tv1.setText("FINAL DISTANCE: " + finalDistance + " km");

            //change color of button to indicate finish
            Button GA_button = (Button) findViewById(R.id.graphSAButton);
            GA_button.setBackgroundColor(0xb0ffffff);

            MapsActivity.this.setProgressBarIndeterminateVisibility(false);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            System.out.println("Current distance: " + best.getDistance());
            graphMap(best);
        }
    }
}
