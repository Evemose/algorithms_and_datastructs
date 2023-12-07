package org.example.lab2.algo.searchsolvers;

import lombok.Data;
import lombok.NonNull;
import org.example.lab2.EightPuzzle;
import org.example.lab2.algo.EightPuzzleSearchSolver;
import org.example.lab2.algo.SearchRecap;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class EightPuzzleLDFSSolver implements EightPuzzleSearchSolver {

    @Override
    public SearchRecap solve(EightPuzzle puzzle) {
        var maxDepth = 15;
        var stack = new ArrayDeque<State>();
        var nextLayer = new ArrayList<State>();
        long steps = 0, statesGenerated = 1, statesStored = 0;
        var found = false;
        var start = System.currentTimeMillis();
        stack.add(new State(puzzle, 0));
        for (; !found && System.currentTimeMillis() - start < 30 * 1000; steps++) {
            var state = stack.pop();
            if (state.getDepth() > maxDepth) {
                if (stack.isEmpty() && steps > 0) {
                    for (var lastLayerState : nextLayer) {
                        stack.push(lastLayerState);
                    }
                    nextLayer.clear();
                    maxDepth++;
                }
                else {
                    nextLayer.add(state);
                    continue;
                }
            }
            if (state.getPuzzle().isSolved()) {
                found = true;
                continue;
            }
            var childStates = state.getPuzzle().getChildStates();
            for (var childState : childStates) {
                stack.push(new State(childState, state.getDepth() + 1));
                statesGenerated++;
            }
            statesStored += stack.size();
        }
        // System.out.println(maxDepth);
        // System.out.println((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024f/1024f/1024f);
        return new SearchRecap(steps, found, statesGenerated, (float) statesStored / steps);
    }

    @Data
    private static class State implements Comparable<State> {
        private final EightPuzzle puzzle;
        private final int heuristic;
        private final int depth;

        public State(EightPuzzle puzzle, int depth) {
            this.puzzle = puzzle;
            this.heuristic = puzzle.getHeuristic();
            this.depth = depth;
        }

        @Override
        public int compareTo(@NonNull State o) {
            return heuristic - o.heuristic;
        }
    }
}
