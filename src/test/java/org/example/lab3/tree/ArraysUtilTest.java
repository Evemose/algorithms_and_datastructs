package org.example.lab3.tree;

import lombok.Value;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArraysUtilTest {

    private int[] arr = IntStream.range(0, 100).toArray();

    @BeforeEach
    void setUp() {
        arr = IntStream.range(0, 100).toArray();
    }

    public int[] values() {
        return arr;
    }
     
    @ParameterizedTest
    @MethodSource("values")
    public void homogeneousBinarySearchContainsTest(int key) {
        Assertions.assertEquals(Arrays.binarySearch(arr, key), ArraysUtil.homogeneousBinarySearch(arr, key));
    }

    @RepeatedTest(15)
    public void homogeneousBinarySearchContainsRandomTest() {
        var key = (int) (Math.random() * 201)  - 100;
        var result = ArraysUtil.homogeneousBinarySearch(arr, key);
//        System.out.println("Key: " + key + " Result: " + result);
        Assertions.assertEquals(Arrays.binarySearch(arr, key), result);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 101, 1000, 10000})
    void homogeneousBinarySearchNotContainsTest(int key) {
        assertEquals(-1, ArraysUtil.homogeneousBinarySearch(arr, key));
    }
}