package org.example.lab4.populations;

import lombok.NonNull;
import org.example.lab4.populations.individuals.AbstractIndividual;
import org.example.lab4.populations.individuals.DiscreteIndividual;
import org.example.lab4.populations.individuals.Individual;

public class DiscreteGenesPopulation extends AbstractPopulation {
    public DiscreteGenesPopulation(@NonNull Individual[] individuals, double maxWeight, double[] values, double[] weights) {
        super(individuals, maxWeight, values, weights);
    }

    public static DiscreteGenesPopulation random(int size, int genesCount, double maxWeight, double[] values, double[] weights) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be > 0");
        }
        var individuals = new Individual[size];
        for (int i = 0; i < size; i++) {
            var genes = new double[genesCount];
            var randomIndex = (int) (Math.random() * genesCount);
            genes[randomIndex] = 1;
            individuals[i] = new DiscreteIndividual(genes);
        }
        return new DiscreteGenesPopulation(individuals, maxWeight, values, weights);
    }
}
