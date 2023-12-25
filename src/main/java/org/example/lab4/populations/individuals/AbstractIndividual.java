package org.example.lab4.populations.individuals;

import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.stream.IntStream;
@EqualsAndHashCode
public abstract class AbstractIndividual implements Individual {

    public abstract Individual clone();

    private static final float ACTIVE_VALUE = 0.02f;

    @Override
    public boolean isGeneActive(int index) {
        return Math.abs(getGenes()[index] - 1) < ACTIVE_VALUE;
    }

    @Override
    public double getFitnessFor(double[] values, double[] weights, double maxWeight) {
        var totalWeight = IntStream.range(0, getGenes().length).mapToDouble(i -> {
            if (Math.abs(getGenes()[i] - 1) < 0.000001) {
                return weights[i];
            }
            return 0;
        }).sum();
        var totalValue = IntStream.range(0, getGenes().length).mapToDouble(i -> {
            if (Math.abs(getGenes()[i] - 1) < 0.000001) {
                return values[i];
            }
            return 0;
        }).sum();

        if (totalWeight > maxWeight) {
            return -1;
        }
        return totalValue;
    }

    @Override
    public void mutate() {
        if ((int)(Math.random() * 20) == 0) {
            var randomIndex = (int) (Math.random() * getGenes().length);
            var randomIndex2 = (int) (Math.random() * getGenes().length);
            while (randomIndex2 == randomIndex) {
                randomIndex2 = (int) (Math.random() * getGenes().length);
            }
            var temp = getGenes()[randomIndex];
            setGene(randomIndex, getGenes()[randomIndex2]);
            setGene(randomIndex2, temp);
        }
    }

    private void copyPartOfGenes(Individual other, int left, int right) {
        for (int i = left; i < right; i++) {
            setGene(i, other.getGenes()[i]);
        }
    }

    @Override
    public Individual crossover(Individual other) {
        var newIndividual = (AbstractIndividual) this.clone();
        var pivots = IntStream.range(0, 3).map(i -> (int) (Math.random() * getGenes().length)).sorted().toArray();
        newIndividual.copyPartOfGenes(other, 0, pivots[0]);
        newIndividual.copyPartOfGenes(other, pivots[1], pivots[2]);
        return newIndividual;
    }

    @Override
    public boolean isDead(double[] weights, double maxWeight) {
        var sum = 0d;
        for (int i = 0; i < getGenes().length; i++) {
            if (Math.abs(getGenes()[i] - 1) < ACTIVE_VALUE) {
                sum += weights[i];
            }
        }
        return sum > maxWeight;
    }

    @Override
    public void improve(double[] values, double[] weights, double maxWeight) {
        var totalWeight = IntStream.range(0, getGenes().length).mapToDouble(i -> {
            if (Math.abs(getGenes()[i] - 1) < 0.000001) {
                return weights[i];
            }
            return 0;
        }).sum();

        var min = Double.MAX_VALUE;
        var minIndex = -1;
        for (int i = 0; i < getGenes().length; i++) {
            if (Math.abs(getGenes()[i] - 1) < 0.000001) {
                continue;
            }
            var newWeight = totalWeight + weights[i];
            if (newWeight > maxWeight) {
                continue;
            }
            var newMin = maxWeight - newWeight;
            if (newMin < min) {
                min = newMin;
                minIndex = i;
            }
        }
        if (minIndex != -1) {
            setGene(minIndex, 1);
        }
    }
}
