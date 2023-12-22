package org.example.lab4.backpack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BackpackTest {

    @Test
    public void testGetBestPacking() {
        var backpack = Backpack.random(100, 250, 2, 30, 1, 25);
        System.out.println(backpack);
        var bestFit = backpack.getBestPacking();
        Assertions.assertNotNull(bestFit);
        System.out.println("Best fit: " + bestFit);
    }

    @Test
    public void testGetBestPackingObservable() {
        var backpack = Backpack.random(100, 250, 2, 30, 1, 25);
        var evolution = backpack.getBestPackingEvolution();
        System.out.println(evolution);
    }
}
