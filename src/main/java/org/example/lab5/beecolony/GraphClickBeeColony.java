package org.example.lab5.beecolony;

import lombok.ToString;
import org.example.lab5.graph.AbstractGraph.Node;
import org.example.lab5.graph.Graph;
import org.example.lab5.graph.GraphFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@ToString
public class GraphClickBeeColony<T> extends GraphBeeColony<T> {

    private final Random random = new Random();

    public GraphClickBeeColony(Graph<T> graph, int numberOfWorkers, int numberOfScouts) {
        super(graph, numberOfWorkers, numberOfScouts);
    }

    public GraphClickBeeColony(Graph<T> graph) {
        super(graph, 900, 100);
    }

    @Override
    public Graph<T> getBestSolution() {
        var click = getInitialClick();
        if (click.getNodes().isEmpty()) {
            return click;
        }
        return enhanceClick(click);
    }


    /***
     * Enhances click using bee colony algorithm
     * @param click click to enhance
     * @return enhanced click
     */
    private Graph<T> enhanceClick(Graph<T> click) {
        var count = 0;
        while (count < 100) {
            var scoutedGraphs = scout(click);
            scoutedGraphs.forEach(g -> {
                for (var i = 0; i < numberOfWorkers / numberOfScouts; i++) {
                    workOnScoutedGraph(g);
                }
            });
            var bestOpt = scoutedGraphs.stream()
                    .max((o1, o2) -> evaluateClick(o1) - evaluateClick(o2));
            var best = click;
            if (bestOpt.isPresent()) {
                best = bestOpt.get();
            }
            if (evaluateClick(best) > evaluateClick(click)) {
                click = best;
                count = 0;
            } else {
                count++;
            }
        }
        return click;
    }

    private int evaluateClick(Graph<T> click) {
        if (!isClick(click)) {
            return -1;
        }
        return click.getNodes().size() * 10 + graph.getNodes().stream().filter(node ->
                        click.hasNode(node.getValue()))
                .mapToInt(node -> node.getNeighbours().size() - click.getNodes().size()).sum();
    }

    private void workOnScoutedGraph(Graph<T> scoutedGraph) {
        graph.getNodes().stream().filter(node ->
                        !scoutedGraph.hasNode(node.getValue()))
                .skip(random.nextInt(0, graph.getNodes().size() - scoutedGraph.getNodes().size() + 1))
                .findAny().ifPresent(node -> {
                    scoutedGraph.addNode(getTrimmedNode(node, scoutedGraph));
                    node.getNeighbours().stream().filter(neighbour ->
                            scoutedGraph.hasNode(neighbour.getValue())).forEach(neighbour ->
                            scoutedGraph.addEdge(node.getValue(), neighbour.getValue(), 1));
                    if (!isClick(scoutedGraph)) {
                        scoutedGraph.removeNode(node.getValue());
                    }
                });
    }

    private List<Graph<T>> scout(Graph<T> initialClick) {
        var scoutedGraphs = new ArrayList<Graph<T>>();
        IntStream.range(0, numberOfScouts * 9 / 10).forEach(i ->
                graph.getNodes().stream().filter(node -> !initialClick.hasNode(node.getValue()))
                        .skip(random.nextInt(0, graph.getNodes().size() - initialClick.getNodes().size() + 1))
                        .findAny().ifPresent(node -> {
                            if (graph.getNodes().stream().filter(n -> initialClick.hasNode(n.getValue()))
                                    .allMatch(n -> graph.hasEdge(node.getValue(), n.getValue()))) {
                                var newClick = initialClick.clone();
                                newClick.addNode(getTrimmedNode(node, newClick));
                                node.getNeighbours().stream().filter(neighbour ->
                                        newClick.hasNode(neighbour.getValue())).forEach(neighbour ->
                                        newClick.addEdge(node.getValue(), neighbour.getValue(), 1));
                                scoutedGraphs.add(newClick);
                            }
                        }));
        IntStream.range(numberOfScouts * 9 / 10, (int) (numberOfScouts * 9.8 / 10)).forEach(i -> {
            var newClick = initialClick.clone();
            newClick.removeNode(newClick.getNodes().stream().skip(random.nextInt(newClick.getNodes().size())).findAny().get().getValue());
            graph.getNodes().stream().filter(n -> !newClick.hasNode(n.getValue()))
                    .skip(random.nextInt(graph.getNodes().size())).findAny().ifPresent(n ->
                            newClick.addNode(getTrimmedNode(n, newClick)));
            for (var node : newClick.getNodes()) {
                for (var neighbour : node.getNeighbours()) {
                    if (newClick.hasNode(neighbour.getValue())) {
                        newClick.addEdge(node.getValue(), neighbour.getValue(), 1);
                    }
                }
            }
            if (isClick(newClick)) {
                scoutedGraphs.add(newClick);
            }
        });
        IntStream.range((int) (numberOfScouts * 9.8 / 10), numberOfScouts).forEach(i -> {
            var newClick = getInitialClick();
            scoutedGraphs.add(newClick);
        });
        return scoutedGraphs;
    }

    private boolean isClick(Graph<T> click) {
        return click.getNodes().stream().allMatch(node ->
                click.getNodes().stream().filter(n -> !n.equals(node))
                        .allMatch(n -> click.hasEdge(node.getValue(), n.getValue())));
    }

    private Node<T> getTrimmedNode(Node<T> node, Graph<T> click) {
        var trimmedNode = new Node<>(node.getValue());
        node.getNeighbours().stream().filter(neighbour -> click.hasNode(neighbour.getValue()))
                .forEach(trimmedNode::addNeighbour);
        return trimmedNode;
    }

    private Graph<T> getInitialClick() {
        Graph<T> click = GraphFactory.createGraph(graph.getClass());
        var nodes = new ArrayList<>(graph.getNodes());
        Collections.shuffle(nodes);
        for (var node : nodes) {
            var neighbours = new ArrayList<>(node.getNeighbours());
            Collections.shuffle(neighbours);
            for (var neighbour : neighbours) {
                if (neighbour.hasNeighbour(node.getValue())) {
                    click.addNode(getTrimmedNode(node, click));
                    click.addNode(getTrimmedNode(neighbour, click));
                    click.addEdge(node.getValue(), neighbour.getValue(), 1);
                    return click;
                }
            }
        }
        return click;
    }

}