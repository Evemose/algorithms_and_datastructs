package org.example.lab5.graph;

import java.util.List;
import java.util.Set;

public interface Graph<T> extends Cloneable {
    void addEdge(T start, T end, double weight);
    void addNode(AbstractGraph.Node<T> node);
    void removeNode(T value);
    void addNode(T value);
    void removeEdge(T start, T end);
    boolean hasEdge(T start, T end);
    Graph<T> getBiggestClick();
    List<AbstractGraph.Node<T>> getNodes();
    Graph<T> clone();
    boolean hasNode(T value);
}
