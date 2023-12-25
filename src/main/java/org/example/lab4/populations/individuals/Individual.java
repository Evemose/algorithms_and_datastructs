package org.example.lab4.populations.individuals;

public interface Individual extends Cloneable {
    double[] getGenes();
    public boolean isGeneActive(int index);
    double getFitnessFor(double[] values, double[] weights, double maxWeight);
    void mutate();
    Individual crossover(Individual other);
    Individual clone();
    boolean isDead(double[] weights, double maxWeight);
    void improve(double[] values, double[] weights, double maxWeight);
    void setGene(int index, double value);
}
