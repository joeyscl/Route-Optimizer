package com.example.joey.googlemaps_tsp;

/**
 * Created by Joey on 2015-07-12.
 */
// SIMULATED ANNEALING
public class TSP_SA {

    // Calculate the acceptance probability
    public static double acceptanceProbability(double energy, double newEnergy, double temperature) {

        // NOTE: lower energy is higher fitness (ie: we can use distance directly as our "energy")
        // If the new solution is better, accept it
        if (newEnergy < energy) {
            return 1.0;
        }
        // If the new solution is worse, calculate an acceptance probability
        return Math.exp((energy - newEnergy) / temperature);
    }


    // NOT USED
    public static Tour simulate() {

        if (TourManager.numberOfDestinations() == 0) return null;

        double temp = 200000000;
        double coolingRate = 0.005;

        // Initialize initial solution
        Tour currentSolution = new Tour();
        currentSolution.generateIndividual();

        System.out.println("Initial distance: " + currentSolution.getDistance());

        // Set as current best
        Tour best = new Tour(currentSolution);

        while (temp > 1) {

            Tour newSolution = new Tour(currentSolution);
            newSolution.mutateIndividual();

            double currentEnergy = currentSolution.getDistance();
            double neighbourEnergy = newSolution.getDistance();

            // Decide if we should accept the neighbour
            if (acceptanceProbability(currentEnergy, neighbourEnergy, temp) > Math.random()) {
                currentSolution = new Tour(newSolution);
            }

            // Keep track of the best solution found
            if (currentSolution.getDistance() < best.getDistance()) {
                best = currentSolution;
            }
            temp *= 1 - coolingRate;
        }
        System.out.println("SA Final distance: " + best.getDistance());
//        System.out.println(best);

        return best;
    }
}


