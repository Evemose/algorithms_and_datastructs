package org.example.lab4.backpack;

import lombok.ToString;
import org.example.lab4.populations.DiscreteGenesPopulation;
import org.example.lab4.populations.EvolutionRecap;

import java.util.ArrayList;
import java.util.List;

public record Backpack(double[] values, double[] weights, double capacity) {

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("Backpack:\n");
        for (int i = 0; i < values.length; i++) {
            sb.append("Item ").append(i).append(": ");
            sb.append("value=").append(values[i]).append(", ")
                    .append("weight=").append(weights[i]).append("\n");
        }
        sb.append("Capacity: ").append(capacity).append("\n");
        return sb.toString();
    }

    public BackpackPacking getBestPacking() {
        var population = DiscreteGenesPopulation.random(100, values.length, capacity, values, weights);
        population.evolve(1000);
        var fittest = population.getFittest();
        var result = new ArrayList<Integer>();
        for (int i = 0; i < fittest.getGenes().length; i++) {
            if (fittest.getGenes()[i] > 0.9) {
                result.add(i);
            }
        }
        return new BackpackPacking(result.stream().map(i ->
                new BackpackPacking.BackpackItem(i, weights[i], values[i])).toList(),
                result.stream().mapToDouble(i -> values[i]).sum(),
                result.stream().mapToDouble(i -> weights[i]).sum());
    }

    public EvolutionRecap getBestPackingEvolution() {
        var population = DiscreteGenesPopulation.random(100, values.length, capacity, values, weights);
        return population.evolveObservable(1000, 20);
    }

    public static Backpack random(int itemsCount, double capacity, double minValue, double maxValue, double minWeight, double maxWeight) {
        var values = new double[itemsCount];
        var weights = new double[itemsCount];
        for (int i = 0; i < itemsCount; i++) {
            values[i] = Math.random() * maxValue + minValue;
            weights[i] = Math.random() * maxWeight + minWeight;
        }
        return new Backpack(values, weights, capacity);
    }

}
