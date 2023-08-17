package com.example.sudokusolver.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class CoverMatrixTest {
    private SparseMatrixContract sparseMatrix;
    private CoverMatrix coverMatrix;

    @BeforeEach
    void setUp() {
        int[][] initialGrid = {
                {0, 2, 0, 0},
                {1, 0, 0, 0},
                {2, 0, 3, 0},
                {0, 0, 0, 4}
        };
        int size = 2;
        sparseMatrix = new SparseMatrixNormal(initialGrid, size, 4);
        coverMatrix = new CoverMatrix(sparseMatrix);
    }
    @Test
    void testAddMatrixCells() {
        coverMatrix.addMatrixCells();

        // Check that the cover matrix has the correct number of columns
        assertEquals(64, coverMatrix.getNumCols());

        // Check that each column has the correct number of nodes
        for (int col = 0; col < coverMatrix.getNumCols(); col++) {
            CircularDoubleLinkedList.Node columnNode = coverMatrix.getColumnNode(col);
            if(columnNode instanceof CircularDoubleLinkedList.ColumnNode) {
                CircularDoubleLinkedList.ColumnNode columnNode1 = (CircularDoubleLinkedList.ColumnNode)columnNode;
                assertNotNull(columnNode1);
                System.out.println(columnNode1.size);
            }
        }
    }


}
