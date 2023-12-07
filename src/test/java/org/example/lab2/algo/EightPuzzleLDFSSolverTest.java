package org.example.lab2.algo;


import org.example.lab2.EightPuzzle;
import org.example.lab2.algo.searchsolvers.EightPuzzleLDFSSolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EightPuzzleLDFSSolverTest {

    @Test
    public void testFound() {
        var puzzle = new EightPuzzle(new int[][]{
                {1, 2, 3},
                {4, 0, 6},
                {7, 5, 8}
        });
        var solver = new EightPuzzleLDFSSolver();
        var recap = solver.solve(puzzle);
        System.out.println(recap);
        Assertions.assertEquals(new SearchRecap(35, true, 58, 10.17F), recap);
    }

}
