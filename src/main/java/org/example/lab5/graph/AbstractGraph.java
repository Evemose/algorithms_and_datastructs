package org.example.lab5.graph;

import lombok.*;
import org.example.lab5.beecolony.GraphClickBeeColony;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@SuppressWarnings("unchecked")
public abstract class AbstractGraph<T> implements Graph<T>, AdjustableAlgorithmsGraph<T> {

    protected List<Node<T>> nodes = new ArrayList<>();

    @Override
    public boolean hasNode(T value) {
        return nodes.stream().anyMatch(node -> node.getValue().equals(value));
    }

    @Override
    public String toString() {
        return "Graph{" +
                "nodes=" + nodes.stream().map(Node::getValue).toList() +
                '}';
    }

    @Override
    public void addEdge(T start, T end, double weight) {
        validateNodes(start, end);
        var startNode = nodes.stream().filter(node -> node.getValue().equals(start)).findFirst().orElseThrow();
        var endNode = nodes.stream().filter(node -> node.getValue().equals(end)).findFirst().orElseThrow();
        startNode.addNeighbour(endNode);
    }

    private void validateNodes(@NonNull T start, @NonNull T end) {
        if (nodes.stream().noneMatch(node -> node.getValue().equals(start))
                || nodes.stream().noneMatch(node -> node.getValue().equals(end))) {
            throw new IllegalArgumentException("Start node is not in graph");
        }
    }

    @Override
    public void removeEdge(T start, T end) {
        validateNodes(start, end);
        var startNode = nodes.stream().filter(node -> node.getValue().equals(start)).findFirst().orElseThrow();
        var endNode = nodes.stream().filter(node -> node.getValue().equals(end)).findFirst().orElseThrow();
        startNode.removeNeighbour(endNode);
    }

    @Override
    public boolean hasEdge(T start, T end) {
        validateNodes(start, end);
        var startNode = nodes.stream().filter(node -> node.getValue().equals(start)).findFirst().orElseThrow();
        var endNode = nodes.stream().filter(node -> node.getValue().equals(end)).findFirst().orElseThrow();
        return startNode.hasNeighbour(endNode);
    }

    @Override
    public Graph<T> getBiggestClick() {
        return new GraphClickBeeColony<>(this).getBestSolution();
    }

    @Override
    public Graph<T> getBiggestClick(int workers, int scouts) {
        return new GraphClickBeeColony<>(this, workers, scouts).getBestSolution();
    }

    @Override
    public void addNode(@NonNull Node<T> node) {
        if (node.getNeighbours().stream().anyMatch(n -> !nodes.contains(n))) {
            throw new IllegalArgumentException("Node has neighbours that are not in graph");
        }
        nodes.add(node);
        for (var n : node.getNeighbours()) {
            n.addNeighbour(node);
        }
    }

    @Override
    public void removeNode(T value) {
        nodes.removeIf(node -> node.getValue().equals(value));
        for (var node : nodes) {
            node.removeNeighbour(value);
        }
    }

    @Override
    public void addNode(T value) {
        addNode(new Node<>(value));
    }

    @Override
    public Graph<T> clone() {
        try {
            var graph = (AbstractGraph<T>) super.clone();
            graph.nodes = new ArrayList<>();
            for (var node : nodes) {
                graph.nodes.add(node.clone());
            }
            return graph;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Builder
    @Data
    @RequiredArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Node<T> implements Cloneable {

        @Override
        public String toString() {
            return "Node{" +
                    "value=" + value +
                    ", edges=" + edges.stream().map(edge -> "end: " + edge.end.value + " weight: " + edge.weight).toList() +
                    '}';
        }

        private final T value;

        @Getter(AccessLevel.NONE)
        @EqualsAndHashCode.Exclude
        private final Set<Edge<T>> edges = new HashSet<>();

        public void addNeighbour(Node<T> node) {
            this.addNeighbour(node, 1);
        }

        public void addNeighbour(Node<T> node, int weight) {
            edges.add(new Edge<>(node, weight));
        }

        public void removeNeighbour(Node<T> node) {
            edges.removeIf(edge -> edge.end.equals(node));
        }

        public void removeNeighbour(T value) {
            edges.removeIf(edge -> edge.end.value.equals(value));
        }

        public boolean hasNeighbour(Node<T> node) {
            return hasNeighbour(node.value);
        }

        public boolean hasNeighbour(T value) {
            return edges.stream().anyMatch(edge -> edge.end.value.equals(value));
        }

        public List<Node<T>> getNeighbours() {
            return edges.stream().map(edge -> edge.end).toList();
        }

        public double getWeight(Node<T> node) {
            return edges.stream().filter(edge -> edge.end.equals(node)).findFirst().orElseThrow().weight;
        }

        @Override
        @SuppressWarnings("MethodDoesntCallSuperMethod")
        public Node<T> clone() {
            var clone = new Node<>(value);
            clone.edges.addAll(edges);
            return clone;
        }


        private record Edge<T>(Node<T> end, int weight) implements Cloneable {
            @Override
            public Edge<T> clone() {
                try {
                    return (Edge<T>) super.clone();
                } catch (CloneNotSupportedException e) {
                    throw new AssertionError();
                }
            }
        }

    }
}
