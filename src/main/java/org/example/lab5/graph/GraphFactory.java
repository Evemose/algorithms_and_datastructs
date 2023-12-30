package org.example.lab5.graph;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GraphFactory {
    public static <T> Graph<T> createGraph(Class<? extends Graph> graphClass) {
        if (graphClass.equals(UndirectedGraph.class)) {
            return new UndirectedGraph<>();
        }
        throw new IllegalArgumentException("Unknown graph type");
    }
}
