package com.example.joey.googlemaps_tsp;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Joey on 2015-07-12.
 */
public class Destination {

    private Marker marker;
    private LatLng pos;

    public Destination(Marker marker) {
        this.marker = marker;
        this.pos = marker.getPosition();
    }


    public double distanceTo(Destination destination) {

        double lat1 = Math.toRadians(this.pos.latitude);
        double lng1 = Math.toRadians(this.pos.longitude);
        double lat2 = Math.toRadians(destination.getLatLng().latitude);
        double lng2 = Math.toRadians(destination.getLatLng().longitude);

        Location loc1 = new Location("loc1");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lng1);
        Location loc2 = new Location("loc2");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lng2);
        return loc1.distanceTo(loc2);

        // Alternative distance calcution: Haversine Formula (Distance of points on sphere)
//        double Radius = 6371; // radius of earth in km
//
//        double dLat = lat2 - lat1;
//        double dLng = lng2 - lng1;
//        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLng / 2),2) * Math.cos(lat1) * Math.cos(lat2);
//        double c = 2 * Math.asin(Math.sqrt(a));
//        return Radius * c;
    }

    public Marker getMarker() {
        return marker;
    }

    public LatLng getLatLng() { return pos; }

    @Override
    public String toString(){
        return String.valueOf(marker.getPosition().latitude) + ", " + String.valueOf(marker.getPosition().longitude);
    }


}
