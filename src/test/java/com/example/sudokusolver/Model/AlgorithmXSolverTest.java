package com.example.sudokusolver.Model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AlgorithmXSolverTest {
    @Test
    void testSolveNormalSudoku() {
        int[][] initialGrid = {
                {4, 2, 1, 0},
                {1, 3, 4, 2},
                {2, 4, 3, 1},
                {3, 1, 2, 4}
        };

        int size = 2;

        AlgorithmXSolver solver = new AlgorithmXSolver(initialGrid, size, SudokuType.NORMAL);
        ArrayList<int[][]> solutions = solver.getFoundSolutions();

        assertEquals(1, solutions.size());

        int[][] expected = {
                {4, 2, 1, 3},
                {1, 3, 4, 2},
                {2, 4, 3, 1},
                {3, 1, 2, 4}
        };

        assertArrayEquals(expected, solutions.get(0));
    }
    @Test
    void testSolveNormalSudokuDoubleSolutions() {
        int[][] initialGrid = {
                {4, 0, 1, 0},
                {1, 0, 4, 0},
                {2, 4, 3, 1},
                {3, 1, 2, 4}
        };

        int size = 2;

        AlgorithmXSolver solver = new AlgorithmXSolver(initialGrid, size, SudokuType.NORMAL);
        ArrayList<int[][]> solutions = solver.getFoundSolutions();

        assertEquals(2, solutions.size());

        int[][] expected = {
                {4, 2, 1, 3},
                {1, 3, 4, 2},
                {2, 4, 3, 1},
                {3, 1, 2, 4}
        };

        assertArrayEquals(expected, solutions.get(0));
        int[][] expected2 = {
                {4, 3, 1, 2},
                {1, 2, 4, 3},
                {2, 4, 3, 1},
                {3, 1, 2, 4}
        };

        assertArrayEquals(expected2, solutions.get(1));
    }
}
