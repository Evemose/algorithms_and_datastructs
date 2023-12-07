package org.example.lab2;

import org.example.lab2.algo.searchsolvers.EightPuzzleAStarSolver;
import org.example.lab2.algo.searchsolvers.EightPuzzleLDFSSolver;
import org.example.lab2.util.EightPuzzleUtil;

public class Lab2 {
    public static void main(String[] args) {
        var LDFSRecap = EightPuzzleUtil.getAverageStatistics(20, new EightPuzzleLDFSSolver());
        System.out.println("LDFS:" + LDFSRecap);
        var AStarRecap = EightPuzzleUtil.getAverageStatistics(20, new EightPuzzleAStarSolver());
        System.out.println("A*:" + AStarRecap);
    }
}
