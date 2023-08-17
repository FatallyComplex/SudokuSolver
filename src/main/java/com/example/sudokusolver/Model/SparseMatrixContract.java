package com.example.sudokusolver.Model;

import org.javatuples.Quartet;

import java.util.HashSet;
import java.util.stream.Collectors;
/*
 *
 * The logic for creating the sparse byte matrix based on the original sudoku matrix
 *
 * Abstract class so that I can add different constraints later in the development process
 *
 */

public abstract class SparseMatrixContract {
    int N, SIZE, lengthConstraint;

    HashSet<Quartet<Integer, Integer, Integer, Integer>> clues = new HashSet<>();

    int[][] sparseMatrix;

    SparseMatrixContract(int[][] initialGrid, int size, int constraintAmount){
        this.SIZE = size;
        this.N = size*size;

        createMatrix(size, constraintAmount);

        // The rows of our matrix represent all the possibilities, whereas the columns represent the constraints.
        // Hence, there are N^3 rows (N rows * N columns * N numbers), and N^2 * 4 columns (N rows * N columns * 4 constraints)

        getClues(initialGrid);

        createSparse();
    }

    // If I want to add more constraints, this will need to be changed
    void createMatrix(int size, int constraintAmount){
        lengthConstraint = constraintAmount*N*N;
        sparseMatrix = new int[N*N*N][lengthConstraint];
    }

    void getClues(int[][] initialGrid){

        for(int r = 0; r < N; r++){
            for(int c = 0; c < N; c++){
                if(initialGrid[r][c] > 0){
                    // Triplet: Digit, Row, Column, Block
                    int b = (c/SIZE) + (r/SIZE)*SIZE;
                    clues.add(new Quartet<>(initialGrid[r][c], r, c, b));
                }
            }
        }
    }

    void createSparse(){
        for(int row = 0; row < N; row++){
            for(int col = 0; col < N; col++){
                int blockIndex = (col/SIZE) + (row/SIZE)*SIZE;
                for(int dig = 1; dig < N+1; dig++){
                    if(!filled(dig, row, col, blockIndex)) {
                        int rowIndex = (row * N + col) * N + (dig-1);
                        int colIndexRow = N * row + dig -1; // Row constraint
                        int colIndexCol = (N * N) + (col*N) + dig - 1; // Column constraint
                        int colIndexBlock = (N * N * 2) + (N * blockIndex) + dig - 1; // Block constraint
                        int colIndexSimple = (N * N * 3) + (col + row * N); // Cell constraint

                        sparseMatrix[rowIndex][colIndexRow] = dig;
                        sparseMatrix[rowIndex][colIndexCol] = dig;
                        sparseMatrix[rowIndex][colIndexBlock] = dig;
                        sparseMatrix[rowIndex][colIndexSimple] = dig;

                        extraConstraint();
                    }
                }
            }
        }
    }

    boolean filled(int digit, int row, int col, int bloc){
        Quartet<Integer, Integer, Integer, Integer> coords = new Quartet<>(digit, row, col, bloc);

        // filter for same row
        HashSet<Quartet<Integer, Integer, Integer, Integer>> sameRow = clues.stream()
                .filter(q -> q.getValue1() == row && q.getValue0() == digit && !q.equals(coords))
                .collect(Collectors.toCollection(HashSet::new));

        // filter for same column
        HashSet<Quartet<Integer, Integer, Integer, Integer>> sameCol = clues.stream()
                .filter(q -> q.getValue2() == col && q.getValue0() == digit && !q.equals(coords))
                .collect(Collectors.toCollection(HashSet::new));

        // filter for same block
        HashSet<Quartet<Integer, Integer, Integer, Integer>> sameBlock = clues.stream()
                .filter(q -> q.getValue3() == bloc && q.getValue0() == digit && !q.equals(coords))
                .collect(Collectors.toCollection(HashSet::new));

        // If the row, column and block are all empty, that means the particular cell trying to be added is either
        // a clue given or a possible cell to be added


        return !(sameRow.size() == 0 && sameCol.size() == 0 && sameBlock.size() == 0);
    }

    abstract void extraConstraint();
}
