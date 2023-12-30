package org.example.lab5.beecolony;

import lombok.Data;
import org.example.lab5.graph.Graph;

@Data
public abstract class GraphBeeColony<T> implements BeeColony<Graph<T>> {
    protected final Graph<T> graph;
    protected final int numberOfWorkers;
    protected final int numberOfScouts;
}
