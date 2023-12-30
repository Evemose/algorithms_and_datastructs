package org.example.lab5.graph;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class UndirectedGraph<T> extends AbstractGraph<T>{

    @Override
    public void addEdge(T start, T end, double weight) {
        super.addEdge(start, end, weight);
        super.addEdge(end, start, weight);
    }

    @Override
    public void removeEdge(T start, T end) {
        super.removeEdge(start, end);
        super.removeEdge(end, start);
    }

    UndirectedGraph() {
        super();
    }
}
