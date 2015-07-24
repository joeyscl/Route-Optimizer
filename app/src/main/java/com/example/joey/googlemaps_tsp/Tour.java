package com.example.joey.googlemaps_tsp;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Joey on 2015-07-12.
 */
public class Tour {

    // Holds our tour of cities
    public ArrayList<Destination> tour = new ArrayList<Destination>();
    // Cache of distance of tour
    private double distance = 0;
    private double fitness = 0;

    // Construct a blank tour
    public Tour() {
        for (int i = 0; i < TourManager.numberOfDestinations(); i++) {
            tour.add(null);
        }
    }

    // Constructs a tour from another tour
    @SuppressWarnings("unchecked")
    public Tour(Tour someTour){
        this.tour = (ArrayList<Destination>) someTour.tour.clone();
    }

    // generate a random tour
    public void generateIndividual() {
        // Loop through all our destinations in TM and add them to our tour -- shallow copy
        for (int index = 0; index < TourManager.numberOfDestinations(); index++) {
            setDestination(index, TourManager.getDestination(index));
        }
        // Randomly reorder the tour
        Collections.shuffle(tour);

//        // deep copy (uses more memory)
//        tour = new ArrayList<Destination>(TourManager.allDestinations());
//        Collections.shuffle(tour);
    }

    // make small mutation - swap order of 2 random destinations
    public void mutateIndividual() {

        int tourPos1 = (int) (tourSize() *Math.random());
        int tourPos2 = (int) (tourSize() *Math.random());

        Collections.swap(tour, tourPos1, tourPos2);

//        Destination dest1 = getDestination(tourPos1);
//        Destination dest2 = getDestination(tourPos2);
//        //swap the 2 destinations
//        setDestination(tourPos2, dest1);
//        setDestination(tourPos1, dest2);
    }

    public ArrayList<Destination> getAllDest() {
        return tour;
    }

    public Destination getDestination(int tourPosition) {
        return tour.get(tourPosition);
    }

    // sets a Destination to certain position in the tour
    public void setDestination(int tourPosition, Destination destination) {
        tour.set(tourPosition, destination);
        // we have altered the tour, so reset distance and fitness
        distance = 0;
    }

    public double getDistance(){
        if (distance == 0) {
            int tourDistance = 0;
            // Loop through our tour's destinations
            for (int Index=0; Index < tourSize(); Index++) {
                // Get  we're travelling from
                Destination fromDestination = getDestination(Index);
                // Destination we're travelling to
                Destination destinationDestination;
                // Check we're not on our tour's last , if we are set our
                // tour's final destination to our starting
                if(Index+1 < tourSize()){
                    destinationDestination = getDestination(Index+1);
                }
                else{
                    destinationDestination = getDestination(0);
                }
                // Get the distance between the two cities
                tourDistance += fromDestination.distanceTo(destinationDestination);
            }
            distance = tourDistance;
        }
        return distance;
    }

    public double getFitness() {
        if (fitness == 0) {
            fitness = 1/ getDistance();
        }
        return fitness;
    }

    // Check if the tour contains a 
    public boolean containsDestination(Destination destination){
        return tour.contains(destination);
    }

    public int tourSize() {
        return tour.size();
    }

    @Override
    public String toString() {
        String geneString = "|";
        for (int i = 0; i < tourSize(); i++) {
            geneString += getDestination(i)+"|";
        }
        return geneString;
    }
}
