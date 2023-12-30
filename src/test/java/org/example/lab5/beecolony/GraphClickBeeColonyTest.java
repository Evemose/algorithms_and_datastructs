package org.example.lab5.beecolony;

import org.example.lab5.graph.AbstractGraph;
import org.example.lab5.graph.Graph;
import org.example.lab5.graph.GraphFactory;
import org.example.lab5.graph.UndirectedGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class GraphClickBeeColonyTest {

    @Test
    public void testFindBestSolution1() {
        Graph<Integer> graph = GraphFactory.createGraph(UndirectedGraph.class);
        for (int i = 0; i < 10; i++) {
            graph.addNode(i);
        }
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(0, 3, 1);
        graph.addEdge(0, 4, 1);
        graph.addEdge(1, 0, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(1, 4, 1);
        graph.addEdge(2, 0, 1);
        graph.addEdge(2, 1, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(2, 4, 1);
        graph.addEdge(3, 0, 1);
        graph.addEdge(3, 1, 1);
        graph.addEdge(3, 2, 1);
        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 0, 1);
        graph.addEdge(4, 1, 1);
        graph.addEdge(4, 2, 1);
        graph.addEdge(4, 3, 1);
        var solution = graph.clone();
        for (int i = 5; i < 10; i++) {
            solution.removeNode(i);
        }
        graph.addEdge(5, 6, 1);
        graph.addEdge(5, 7, 1);
        graph.addEdge(6, 2, 1);
        graph.addEdge(6, 3, 1);
        graph.addEdge(6, 4, 1);
        graph.addEdge(7, 2, 1);
        graph.addEdge(7, 3, 1);
        graph.addEdge(7, 9, 1);
        graph.addEdge(8, 2, 1);
        graph.addEdge(8, 3, 1);
        graph.addEdge(8, 0, 1);
        graph.addEdge(9, 3, 1);
        graph.addEdge(9, 0, 1);
        graph.addEdge(9, 4, 1);
        var beeColony = new GraphClickBeeColony<>(graph);
        for (var node : graph.getNodes()) {
            System.out.println("Value: " + node.getValue() + " Neighbours: " + node.getNeighbours());
        }
        var bestSolution = beeColony.getBestSolution();
        System.out.println("Biggest click: " + bestSolution);
        Assertions.assertEquals(5, bestSolution.getNodes().size());
        assertThat(bestSolution.getNodes().stream().map(AbstractGraph.Node::getValue).toList())
                .containsExactlyInAnyOrderElementsOf(solution.getNodes().stream().map(AbstractGraph.Node::getValue).toList());
    }

    @Test
    public void testFindBestSolution2() {
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

        for (var node : graph.getNodes()) {
            System.out.println("Value: " + node.getValue() + " Neighbours: " + node.getNeighbours());
        }
        var beeColony = new GraphClickBeeColony<>(graph);
        var bestSolution = beeColony.getBestSolution();
        System.out.println("Biggest click: " + bestSolution);
    }
}
