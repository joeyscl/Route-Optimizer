package com.example.joey.googlemaps_tsp;

/**
 * Created by Joey on 2015-07-12.
 */

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

public class TourManager {

    //List of Destinations
    private static ArrayList<Destination> destinations = new ArrayList<Destination>();

    public  static void addDestination(Destination destination) {
        destinations.add(destination);
    }

    //get the destination by index
    public static Destination getDestination(int index) {
        return (Destination) destinations.get(index);
    }

    public static void removeAll() {

        for (Destination D: destinations) {
            Marker marker = D.getMarker();
            marker.remove();
        }
        destinations = new ArrayList<Destination>();
    }

    public static ArrayList<Destination> allDestinations() {
        return destinations;
    }

    // get the number of Destinations
    public static int numberOfDestinations() {
        return destinations.size();
    }
}
