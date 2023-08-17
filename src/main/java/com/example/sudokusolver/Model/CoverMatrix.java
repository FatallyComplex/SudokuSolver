package com.example.sudokusolver.Model;

import java.time.chrono.MinguoDate;

/*
 *
 * This will be the matrix
 *
 *
 */
public class CoverMatrix {

    CircularDoubleLinkedList root = new CircularDoubleLinkedList();
    SparseMatrixContract sMatrix;
    int N;

    public CoverMatrix(SparseMatrixContract sMatrix){
        // Create the column heads
        this.sMatrix = sMatrix;
        this.N = sMatrix.N;
        for(int i = 0; i < sMatrix.lengthConstraint; i++){
            CircularDoubleLinkedList.ColumnNode temp = new CircularDoubleLinkedList.ColumnNode();
            root.insertSide(temp);
            temp.position = i;
            if(i < N*N) {
                temp.number = (i % N) + 1;
                temp.constraint = 0;
            }
            else if(i < 4*N*N && i > 3*N*N-1){
                temp.constraint = 3;
            }

        }

    }

    void addMatrixCells(){
        for(int row = 0; row < sMatrix.sparseMatrix.length; row++){
            CircularDoubleLinkedList.ColumnNode currColumn = (CircularDoubleLinkedList.ColumnNode) root.getRoot().getRight();
            CircularDoubleLinkedList.Node lastAdded = null;

            int[] matrixRow = sMatrix.sparseMatrix[row];
            
            for(int col = 0; col< matrixRow.length; col++){
                if(sMatrix.sparseMatrix[row][col] > 0){ //Constraint was found
                    CircularDoubleLinkedList.Node newNode = new CircularDoubleLinkedList.Node();

                    newNode.num = sMatrix.sparseMatrix[row][col];
                    
                    root.insertOnColumn(currColumn, newNode);

                    if (lastAdded != null) {
                        lastAdded.insertRight(newNode);
                    }
                    lastAdded = newNode;
                }
                if(currColumn.getRight() != root.getRoot()) {
                    currColumn = (CircularDoubleLinkedList.ColumnNode) currColumn.getRight();
                }
            }
        }
    }

    public CircularDoubleLinkedList.Node getRoot() {
        return root.getRoot();
    }

    public CircularDoubleLinkedList.ColumnNode choose(){
        return root.choose();
    }

    public CircularDoubleLinkedList.ColumnNode chooseRandom(){return root.chooseCreate();}

    public int getNumCols(){
        return sMatrix.lengthConstraint;
    }

    CircularDoubleLinkedList.Node getColumnNode(int col){
        CircularDoubleLinkedList.Node currNode = root.getRoot().getRight();

        while(col > 0){
            currNode = currNode.getRight();
            col--;
        }
        return currNode;
    }
}
