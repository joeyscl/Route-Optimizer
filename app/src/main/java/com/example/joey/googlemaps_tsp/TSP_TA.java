package com.example.joey.googlemaps_tsp;

/**
 * Created by Joey on 2015-07-20.
 */
public class TSP_TA {

    public static Tour simulate() {
        Population pop = new Population(5000, true);

        Tour best = pop.getFittest();
        System.out.println("TA Final distance: " + best.getDistance());

        return best;
    }
}
