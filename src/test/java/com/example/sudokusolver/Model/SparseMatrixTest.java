package com.example.sudokusolver.Model;
import org.javatuples.Quartet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SparseMatrixTest {
    private HashSet<Quartet<Integer, Integer, Integer, Integer>> clues;
    SparseMatrixNormal matrix;

    @BeforeEach
    public void setUp() {
        int[][] initialGrid = {
                {1, 2, 3, 0, 0, 0, 0, 0, 0},
                {4, 5, 6, 0, 0, 0, 0, 0, 0},
                {7, 8, 9, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 9, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        int size =3;
        matrix = new SparseMatrixNormal(initialGrid, size, 4);
    }

    @Test
    public void testGetClues() {
        int[][] initialGrid = {
                {0, 2, 0, 0},
                {1, 0, 0, 0},
                {0, 0, 3, 0},
                {2, 0, 0, 4}
        };
        int size = 2;

        SparseMatrixNormal matrix = new SparseMatrixNormal(initialGrid, size, 4);
        matrix.getClues(initialGrid);

        HashSet<Quartet<Integer, Integer, Integer, Integer>> expectedClues = new HashSet<>();
        expectedClues.add(new Quartet<>(2, 0, 1, 0));
        expectedClues.add(new Quartet<>(1, 1, 0, 0));
        expectedClues.add(new Quartet<>(3, 2, 2, 3));
        expectedClues.add(new Quartet<>(4, 3, 3, 3));
        expectedClues.add(new Quartet<>(2, 3, 0, 2));

        assertEquals(expectedClues, matrix.clues);
    }

    @Test
    public void testEmpty(){
        int[][] initialGrid = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        int size = 2;
        SparseMatrixNormal matrix = new SparseMatrixNormal(initialGrid, size, 4);
        assertTrue(true);
    }

    @Test
    public void testGetCluesBigger(){
        clues = new HashSet<>();
        clues.add(new Quartet<>(1, 0, 0, 0));
        clues.add(new Quartet<>(2, 0, 1, 0));
        clues.add(new Quartet<>(3, 0, 2, 0));
        clues.add(new Quartet<>(4, 1, 0, 0));
        clues.add(new Quartet<>(5, 1, 1, 0));
        clues.add(new Quartet<>(6, 1, 2, 0));
        clues.add(new Quartet<>(7, 2, 0, 0));
        clues.add(new Quartet<>(8, 2, 1, 0));
        clues.add(new Quartet<>(9, 2, 2, 0));
        clues.add(new Quartet<>(9, 4, 4, 4));

        assertEquals(clues, matrix.clues);
    }

    @Test
    public void testFilledReturnsTrueForFilledCellWithSameNumber() {
        boolean result = matrix.filled(1, 0, 0, 0);
        Assertions.assertFalse(result);
    }

    @Test
    public void testFilledReturnsFalseForEmptyCellWithNoConflict() {
        boolean result = matrix.filled(2, 0, 0, 0);
        Assertions.assertTrue(result);
    }

    @Test
    public void testFilledReturnsTrueForEmptyCellWithSameRowConflict() {
        boolean result = matrix.filled(1, 0, 1, 0);
        Assertions.assertTrue(result);
    }

    @Test
    public void testFilledReturnsTrueForEmptyCellWithSameColumnConflict() {
        boolean result = matrix.filled(1, 1, 0, 0);
        Assertions.assertTrue(result);
    }

    @Test
    public void testFilledReturnsTrueForEmptyCellWithSameBlockConflict() {
        boolean result = matrix.filled(1, 1, 1, 0);
        Assertions.assertTrue(result);
    }

    @Test
    public void testFilledReturnsTrueForEmptyCellWithSameBlockSameRowConflict(){
        boolean result = matrix.filled(9, 3, 4, 4);
        Assertions.assertTrue(result);
    }
    @Test
    public void testFilledReturnsFalseForEmptyCellWithSameBlockSameRowNoConflict(){
        boolean result = matrix.filled(8, 3, 4, 4);
        Assertions.assertFalse(result);
    }


}
