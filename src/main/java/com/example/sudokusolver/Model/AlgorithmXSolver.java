package com.example.sudokusolver.Model;

import java.util.ArrayList;
import java.util.Stack;

public class AlgorithmXSolver {
    /*
     *
     * This will be the class that will be solving the cover problem
     *
     * This class only takes as input the root node of the double linked list and then solves the problem
     *
     * None of the logic for creating the linked list will appear here
     *
     */

    final Stack<CircularDoubleLinkedList.Node> solution = new Stack<>();
    CoverMatrix coverMatrix;

    private final ArrayList<int[][]> foundSolutions = new ArrayList<>();

    public AlgorithmXSolver(int[][] initialGrid, int size, SudokuType t){
        if(t == SudokuType.NORMAL) {
            coverMatrix = new CoverMatrix(new SparseMatrixNormal(initialGrid, size, 4));
        }
        assert coverMatrix != null;
        coverMatrix.addMatrixCells();

        searchV2(0);
    }

    void searchV2(int k){
        if(coverMatrix.root.getRoot().right == coverMatrix.getRoot()){
            mapSolvedToGrid();
            return;
        }
        if(checkTrue()){
            return;
        }
        CircularDoubleLinkedList.ColumnNode cNode = choose();
        cNode.cover();

        cNode = performSelect(cNode, k);

        cNode.uncover();
    }

    CircularDoubleLinkedList.ColumnNode choose(){
        return coverMatrix.choose();
    }

    boolean checkTrue(){
        return false;
    }

    CircularDoubleLinkedList.ColumnNode performSelect(CircularDoubleLinkedList.ColumnNode cNode, int k){
        for(CircularDoubleLinkedList.Node curRow = cNode.down; curRow != cNode; curRow = curRow.down){
            solution.push(curRow);

            for(CircularDoubleLinkedList.Node curCol = curRow.right; curCol != curRow; curCol = curCol.right){
                curCol.head.cover();
            }

            searchV2(k+1);

            curRow = solution.pop();
            cNode = curRow.head;

            for(CircularDoubleLinkedList.Node curCol = curRow.left; curCol != curRow; curCol = curCol.left){
                curCol.head.uncover();
            }

        }
        return cNode;
    }


    private void mapSolvedToGrid(){
        int[][] result = new int[coverMatrix.N][coverMatrix.N];
        Stack<CircularDoubleLinkedList.Node> processedSolution = new Stack<>();
        while(!solution.isEmpty()){
            CircularDoubleLinkedList.Node solutionNode = solution.pop();
            processedSolution.push(solutionNode);

            int col, row;

            CircularDoubleLinkedList.Node thirdConNode = solutionNode;

            while(thirdConNode.head.constraint != 3){
                thirdConNode = thirdConNode.right;
            }

            result[(thirdConNode.head.position/ coverMatrix.N)% coverMatrix.N][thirdConNode.head.position%coverMatrix.N] = solutionNode.num;
        }
         foundSolutions.add(result);
        while(!processedSolution.isEmpty()){
            solution.push(processedSolution.pop());
        }
    }

    public ArrayList<int[][]> getFoundSolutions() {
        return foundSolutions;
    }

    void setCoverMatrix(CoverMatrix coverMatrix) {
        this.coverMatrix = coverMatrix;
    }
}
