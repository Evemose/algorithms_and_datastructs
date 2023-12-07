package org.example.lab2.algo.searchsolvers;

import lombok.Data;
import lombok.NonNull;
import org.example.lab2.EightPuzzle;
import org.example.lab2.algo.EightPuzzleSearchSolver;
import org.example.lab2.algo.SearchRecap;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.PriorityQueue;

public class EightPuzzleAStarSolver implements EightPuzzleSearchSolver {

    @Override
    public SearchRecap solve(EightPuzzle puzzle) {
        var queue = new PriorityQueue<State>();
        int steps = 0, statesGenerated = 1;
        long statesStored = 0, heuristic = puzzle.getHeuristic();
        var found = false;
        var start = System.currentTimeMillis();
        queue.add(new State(puzzle, 0));
        HashSet<EightPuzzle> visited = new HashSet<>();
        visited.add(puzzle);
        for (; !queue.isEmpty() && System.currentTimeMillis() - start < 30 * 1000; steps++) {
            var state = Objects.requireNonNull(queue.poll());
            if (found && state.getHeuristic() > heuristic) {
                steps++;
                break;
            }
            if (state.getPuzzle().isSolved()) {
                found = true;
                heuristic = state.getHeuristic();
                continue;
            }
            var childStates = state.getPuzzle().getChildStates();
            for (var childState : childStates) {
                if (!visited.contains(childState)) {
                    queue.add(new State(childState, state.getDepth() + 1));
                    visited.add(childState);
                    statesGenerated++;
                }
            }
            statesStored += queue.size();
        }
        //System.out.println((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024f/1024f/1024f);
        return new SearchRecap(steps, found, statesGenerated, (float) statesStored / steps);
    }

    @Data
    private static class State implements Comparable<State> {
        private final EightPuzzle puzzle;
        private final long heuristic;
        private final long depth;

        public State(EightPuzzle puzzle, long depth) {
            this.puzzle = puzzle;
            this.heuristic = puzzle.getHeuristic() + depth;
            this.depth = depth;
        }

        @Override
        public int compareTo(@NonNull State o) {
            return Comparator.comparingLong(State::getHeuristic).compare(this, o);
        }
    }
}
