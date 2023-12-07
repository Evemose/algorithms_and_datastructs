package org.example.lab3.tree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class BTreeTest {
    private Tree mockTree;
    private final static List<Integer> mockValues =
            new Random()
                    .ints(Integer.MIN_VALUE/2, Integer.MAX_VALUE)
                    .limit(100).boxed().toList();

    @BeforeEach
    void setUp() {
        mockTree = new BTree(mockValues, 5);
    }

    @ParameterizedTest
    @ValueSource(ints = {30, 40, 50, 60, 70, 80, 90, 100, 200, 300, 400})
    public void testInsert(int val) {
        mockTree.insert(val);
        Assertions.assertTrue(mockTree.contains(val));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 10, 11, 12, 13, 14, 15})
    public void testContains(int val) {
        System.out.println(mockTree);
        Assertions.assertEquals(mockValues.contains(val), mockTree.contains(val));
    }

    private static int[] deleteSource() {
        return mockValues.stream().mapToInt(Integer::intValue).toArray();
    }

    @ParameterizedTest
    @MethodSource("deleteSource")
    public void testRemove(int val) {
        mockTree.remove(val);
        for (var mockValue : mockValues) {
            if (mockValue != val) {
                Assertions.assertTrue(mockTree.contains(mockValue));
            }
        }
        Assertions.assertFalse(mockTree.contains(val));
    }

    @Test
    public void testVisualize() {
        mockTree.visualize();
        new Scanner(System.in).nextLine();
    }

    @Test
    public void testToList() {
        var list = mockTree.toList();
        assertThat(list).containsExactlyInAnyOrderElementsOf(mockValues.stream().distinct().toList());
        assertThat(list).isSorted();
    }
}
