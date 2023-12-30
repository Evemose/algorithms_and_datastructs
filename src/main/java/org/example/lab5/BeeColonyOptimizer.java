package org.example.lab5;

import org.example.lab5.graph.AbstractGraph;
import org.example.lab5.graph.Graph;
import org.example.lab5.graph.GraphFactory;
import org.example.lab5.graph.UndirectedGraph;

import java.util.ArrayList;
import java.util.Random;

public class BeeColonyOptimizer {
    private static final int MIN_SCOUTS = 1;
    private static final int MAX_SCOUTS = 1000;
    private static final int MIN_WORKERS = 1;
    private static final int MAX_WORKERS = 10000;

    public static void main(String[] args) {
        AbstractGraph<Integer> graph = getIntGraph();
        int bestScouts = -1;
        int bestWorkers = -1;
        double bestResult = Double.NEGATIVE_INFINITY;

        for (int scouts = MIN_SCOUTS; scouts <= MAX_SCOUTS; scouts+=25) {
            for (int workers = MIN_WORKERS; workers <= MAX_WORKERS; workers+=250) {
                var time = 0L;
                var start = System.currentTimeMillis();
                var result = graph.getBiggestClick(workers, scouts);
                var end = System.currentTimeMillis();
                time += end - start;
                for (int i = 0; i < 9; i++) {
                    start = System.currentTimeMillis();
                    result = graph.getBiggestClick(workers, scouts);
                    end = System.currentTimeMillis();
                    time += end - start;
                }
                double score = evaluateResult(result, time / 10d);

                if (score > bestResult) {
                    bestResult = score;
                    bestScouts = scouts;
                    bestWorkers = workers;
                }
                System.out.println("Scouts: " + scouts + " Workers: " + workers + " Score: " + score);
            }
        }

        System.out.println("Best parameters: ");
        System.out.println("Scouts: " + bestScouts);
        System.out.println("Workers: " + bestWorkers);
    }

    private static double evaluateResult(Graph<Integer> result, double time) {
        return result.getNodes().size() * 1000000d / time;
    }

    private static AbstractGraph<Integer> getIntGraph() {
        Graph<Integer> graph = GraphFactory.createGraph(UndirectedGraph.class);
        for (int i = 0; i < 13; i++) {
            graph.addNode(i);
        }
        for (var i = 0; i < 4; i++) {
            for (var j = 0; j < 4; j++) {
                if (i != j) graph.addEdge(i, j, 1);
            }
        }
        var random = new Random();
        for (var i = 0; i < 10; i++) {
            graph.addEdge(random.nextInt(0, 4), random.nextInt(4, 13), 1);
        }
        for (var i = 4; i < 13; i++) {
            for (var j = 4; j < 13; j++) {
                if (i != j) graph.addEdge(i, j, 1);
            }
        }

        for (var i = 13; i < 1000; i++) {
            graph.addNode(i);
        }

        for (var i = 0; i < 10000; i++) {
            graph.addEdge(random.nextInt(0, 1000), random.nextInt(0, 1000), 1);
        }

        return (AbstractGraph<Integer>) graph;
    }
}
