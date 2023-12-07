package org.example.lab2.algo;

import org.example.lab2.EightPuzzle;

public interface EightPuzzleSearchSolver {
    SearchRecap solve(EightPuzzle puzzle) throws InterruptedException;
}
