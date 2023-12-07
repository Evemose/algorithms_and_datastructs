package org.example.lab2.algo;


import org.example.lab2.EightPuzzle;
import org.example.lab2.algo.searchsolvers.EightPuzzleAStarSolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EightPuzzleAStarSolverTest {

    @Test
    public void testFound() {
        var puzzle = new EightPuzzle(new int[][]{
                {1, 2, 3},
                {4, 0, 6},
                {7, 5, 8}
        });
        var solver = new EightPuzzleAStarSolver();
        var recap = solver.solve(puzzle);
        Assertions.assertEquals(new SearchRecap(3, true, 8, 3.3333333F), recap);
    }

}
