package org.example.lab4.populations;

import org.example.lab4.populations.individuals.Individual;

public interface Population {
    Individual getFittest();
    void evolve();
    void evolve(int generations);
}
