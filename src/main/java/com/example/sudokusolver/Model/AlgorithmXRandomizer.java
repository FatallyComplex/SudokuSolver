package com.example.sudokusolver.Model;

import java.util.ArrayList;
import java.util.Collections;

public class AlgorithmXRandomizer extends AlgorithmXSolver{
    static int amount;

    public AlgorithmXRandomizer(int[][] initialGrid, int size, SudokuType t) {
        super(initialGrid, size, t);
    }

    @Override
    CircularDoubleLinkedList.ColumnNode choose(){
        return coverMatrix.chooseRandom();
    }

    @Override
    CircularDoubleLinkedList.ColumnNode performSelect(CircularDoubleLinkedList.ColumnNode cNode, int k){
        ArrayList<CircularDoubleLinkedList.Node> nodes = new ArrayList<>();

        for(CircularDoubleLinkedList.Node curRow = cNode.down; curRow != cNode; curRow = curRow.down){
            nodes.add(curRow);
        }
        Collections.shuffle(nodes);

        for(CircularDoubleLinkedList.Node n : nodes){
            solution.push(n);

            for(CircularDoubleLinkedList.Node curCol = n.right; curCol != n; curCol = curCol.right){
                curCol.head.cover();
            }

            searchV2(k+1);

            n = solution.pop();
            cNode = n.head;

            for(CircularDoubleLinkedList.Node curCol = n.left; curCol != n; curCol = curCol.left){
                curCol.head.uncover();
            }
        }
        return cNode;
    }

    @Override
    boolean checkTrue() {
        return getFoundSolutions().size() >= amount;
    }

    public static void setAmount(int amount) {
        AlgorithmXRandomizer.amount = amount;
    }

}
