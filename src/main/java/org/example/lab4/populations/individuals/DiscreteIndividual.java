package org.example.lab4.populations.individuals;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = false)
public class DiscreteIndividual extends AbstractIndividual {
    private final boolean[] genes;
    public double[] getGenes() {
        var genes = new double[this.genes.length];
        for (int i = 0; i < genes.length; i++) {
            genes[i] = this.genes[i] ? 1d : 0d;
        }
        return genes;
    }

    @Override
    public void setGene(int index, double value) {
        this.genes[index] = Math.abs(Math.round(value) - 1d) < 0.000001;
    }

    @Override
    public Individual clone() {
        return new DiscreteIndividual(this.genes);
    }

    protected DiscreteIndividual(int size) {
        this.genes = new boolean[size];
    }

    protected DiscreteIndividual(boolean[] genes) {
        this(genes.length);
        System.arraycopy(genes, 0, this.genes, 0, genes.length);
    }

    public DiscreteIndividual(double[] genes) {
        this(toBooleanGenes(genes));
    }

    private static boolean[] toBooleanGenes(double[] genes) {
        var booleanGenes = new boolean[genes.length];
        for (int i = 0; i < genes.length; i++) {
            booleanGenes[i] = Math.abs(Math.round(genes[i]) - 1d) < 0.000001;
        }
        return booleanGenes;
    }
}
