package org.example.lab4.populations;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import org.example.lab4.backpack.BackpackPacking;
import org.example.lab4.populations.individuals.AbstractIndividual;
import org.example.lab4.populations.individuals.Individual;

import java.util.Arrays;
import java.util.TreeMap;
import java.util.stream.IntStream;

@ToString
@EqualsAndHashCode
public abstract class AbstractPopulation implements Population, ObservablePopulation {
    private Individual[] individuals;
    private final double maxWeight;
    private final double[] values;
    private final double[] weights;

    protected AbstractPopulation(@NonNull Individual[] individuals, double maxWeight, double[] values, double[] weights) {
        if (individuals.length == 0) {
            throw new IllegalArgumentException("Individuals array must not be empty");
        }
        this.individuals = individuals;
        this.maxWeight = maxWeight;
        this.values = values;
        this.weights = weights;
    }

    @Override
    public Individual getFittest() {
        return Arrays.stream(individuals).max((a, b) -> {
            var aFitness = a.getFitnessFor(values, weights, maxWeight);
            var bFitness = b.getFitnessFor(values, weights, maxWeight);
            return Double.compare(aFitness, bFitness);
        }).orElseThrow();
    }

    @Override
    public void evolve() {
        var fittest = getFittest();
        var newIndividuals = new Individual[individuals.length];
        newIndividuals[0] = fittest;
        for (int i = 1; i < individuals.length; i++) {
            var randomIndex = (int) (Math.random() * individuals.length);
            var randomIndividual = individuals[randomIndex];
            var newIndividual = fittest.crossover(randomIndividual);
            if (newIndividual.isDead(weights, maxWeight)) {
                newIndividual = fittest.clone();
            }
            newIndividual.mutate();
            if (newIndividual.isDead(weights, maxWeight)) {
                newIndividual = fittest.clone();
            }
            newIndividual.improve(values, weights, maxWeight);
            newIndividuals[i] = newIndividual;
        }
        individuals = newIndividuals;
    }

    @Override
    public void evolve(int generations) {
        for (int i = 0; i < generations; i++) {
            evolve();
        }
    }

    @Override
    public EvolutionRecap evolveObservable(int generations, int interval) {
        var recap = new EvolutionRecap(new TreeMap<>());
        for (int i = 0; i < generations; i++) {
            if ((i+1) % interval == 0) {
                var fittest = getFittest();
                var activeGenes = IntStream.range(0, fittest.getGenes().length)
                        .filter(fittest::isGeneActive).boxed().toList();
                recap.evolutionPath().put(i+1,
                        new BackpackPacking(activeGenes.stream().map(j ->
                                new BackpackPacking.BackpackItem(j, weights[j], values[j])).toList(),
                        activeGenes.stream().mapToDouble(j -> values[j]).sum(),
                                activeGenes.stream().mapToDouble(j -> weights[j]).sum()));
            }
            evolve();
        }
        return recap;
    }
}
