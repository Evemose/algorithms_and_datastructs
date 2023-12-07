package org.example.lab2.util;

import lombok.SneakyThrows;
import org.example.lab2.EightPuzzle;
import org.example.lab2.algo.AlgorithmRecap;
import org.example.lab2.algo.EightPuzzleSearchSolver;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EightPuzzleUtil {
    public static EightPuzzle generateRandomPuzzle() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 0);
        while (true) {
            try {
                Collections.shuffle(list);
                return new EightPuzzle(new int[][]{
                        list.subList(0, 3).stream().mapToInt(i -> i).toArray(),
                        list.subList(3, 6).stream().mapToInt(i -> i).toArray(),
                        list.subList(6, 9).stream().mapToInt(i -> i).toArray()
                });
            } catch (IllegalArgumentException ignored) {}
        }
    }

    @SneakyThrows
    public static AlgorithmRecap getAverageStatistics(int iterations, EightPuzzleSearchSolver solver) {
        var totalSteps = 0L;
        var totalFound = 0;
        var totalStatesGenerated = 0L;
        var totalStatesStored = 0F;
        for (int i = 0; i < iterations; i++) {
            var recap =  solver.solve(generateRandomPuzzle());
            System.out.println(recap);
            totalSteps += recap.steps();
            totalFound += recap.isFound() ? 1 : 0;
            totalStatesGenerated += recap.statesGenerated();
            totalStatesStored += recap.statesStoredAvg();
        }
        return new AlgorithmRecap((float) totalSteps / iterations,
                (float) totalFound / iterations,
                (float) totalStatesGenerated / iterations,
                totalStatesStored / iterations);
    }
}
