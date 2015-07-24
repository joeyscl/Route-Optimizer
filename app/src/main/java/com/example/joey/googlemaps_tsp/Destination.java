package com.example.joey.googlemaps_tsp;

import android.location.Location;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by Joey on 2015-07-12.
 */
public class Destination {

    private Marker marker;

    public Destination(Marker marker) {
        this.marker = marker;
    }


    public double distanceTo(Destination destination) {

        double lat1 = Math.toRadians(this.marker.getPosition().latitude);
        double lng1 = Math.toRadians(this.marker.getPosition().longitude);
        double lat2 = Math.toRadians(destination.marker.getPosition().longitude);
        double lng2 = Math.toRadians(destination.marker.getPosition().longitude);

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

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    @Override
    public String toString(){
        return String.valueOf(marker.getPosition().latitude) + ", " + String.valueOf(marker.getPosition().longitude);
    }

}
