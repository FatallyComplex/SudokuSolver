package com.example.sudokusolver.Model;

import org.javatuples.*;

import java.util.HashSet;

/*
 *
 * The logic for creating the sparse byte matrix based on the original sudoku matrix
 *
 *
 */
public class SparseMatrixNormal extends SparseMatrixContract{

    public SparseMatrixNormal(int[][] initialGrid, int size, int constraintAmount){
        super(initialGrid, size, constraintAmount);
    }

    @Override
    void extraConstraint() {

    }

    public int[][] getSparseMatrix() {
        return sparseMatrix;
    }
}
