package com.example.sudokusolver.Model;

import org.controlsfx.control.NotificationPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class CircularDoubleLinkedList {
    /*
     *
     * I will be writing my own version of a circular double linked list in here
     *
     */

    private final ColumnNode root = new ColumnNode(); // This is the reference node, not an actual column

    static class Node{
        // A node has pointers to all four neighbours and the head of the column
        Node left = this;
        Node right = this;
        Node up = this;
        Node down = this;
        ColumnNode head;

        int num;

        public Node getRight() {
            return right;
        }
        public Node getDown() {
            return down;
        }
        public Node getLeft() {
            return left;
        }
        public Node getUp() {
            return up;
        }
        public ColumnNode getHead() {
            return head;
        }
        public void setHead(ColumnNode head) {
            this.head = head;
        }

        public void clear(){
            left = this;
            right = this;
            up = this;
            down = this;
        }

        public void insertRight(Node newNode){
            if(this.right == this){
                this.left = newNode;
                newNode.left = this;
                newNode.right = this;
            }else{
                Node curr = this.right;
                newNode.left = this;
                newNode.right = curr;
                curr.left = newNode;
            }
            this.right = newNode;
        }

        public void insertLeft(Node newNode){
            if(this.left == this){
                this.right = newNode;
                newNode.right = this;
                newNode.left = this;
            }else{
                Node curr = this.left;
                newNode.right = this;
                newNode.left = curr;
                curr.right = newNode;
            }
            this.left = newNode;
        }

        public void insertDown(Node newNode){
            if(this.down == this){
                this.up = newNode;
                newNode.up = this;
                newNode.down = this;
            }else{
                Node curr = this.down;
                newNode.up = this;
                newNode.down = curr;
                curr.up = newNode;
            }
            this.down = newNode;
        }

        public void insertUp(Node newNode){
            if(this.up == this){
                this.down = newNode;
                newNode.down = this;
                newNode.up = this;
            }else{
                Node curr = this.up;
                newNode.down = this;
                newNode.up = curr;
                curr.down = newNode;
            }
            this.up = newNode;
        }

        public int getNum() {
            return num;
        }
    }

    static class ColumnNode extends Node{
        int size = 0;
        int constraint = -1;
        int number = -1;
        int position = -1;

        public int getPosition() {
             return position;
         }
        public int getConstraint() {
             return constraint;
         }
        public int getNumber() {
             return number;
         }
        public int getSize() {
             return size;
         }

        public void increaseSize(){
            size++;
        }
        public void decreaseSize(){
            size--;
        }

        public void delete(){
            this.right.left = this.left;
            this.left.right = this.right;
        }

        public void cover(){
            this.right.left = this.left;
            this.left.right = this.right;

            for(Node curRow = this.down; curRow != this; curRow = curRow.down){
                for(Node curCol = curRow.right; curCol != curRow; curCol = curCol.right){
                    curCol.down.up = curCol.up;
                    curCol.up.down = curCol.down;
                    curCol.head.size--;
                }
            }
        }

        public void uncover(){
            for(Node curRow = this.up; curRow != this; curRow = curRow.up){
                for(Node curCol = curRow.left; curCol != curRow; curCol = curCol.left){
                    curCol.head.size++;
                    curCol.down.up = curCol;
                    curCol.up.down = curCol;
                }
            }

            this.right.left = this;
            this.left.right = this;
        }
     }

    public Node getRoot() {
        return root;
    }

    public void insertSide(ColumnNode newNode){ // Inserting the column heads
        if(root.right == root){
            root.right = newNode;
            newNode.left = root;
            newNode.right = root;
        }else{
            Node curr = root.left; // last added node
            newNode.left = curr;
            newNode.right = root;
            curr.right = newNode;
        }
        root.left = newNode;
    }

    public void insertOnColumn(ColumnNode cNode, Node newNode){
        cNode.insertUp(newNode);
        cNode.increaseSize();
        newNode.setHead(cNode);
    }

    public ColumnNode choose(){
        ColumnNode rightOfRoot = (ColumnNode) root.right; // we cast the node to the right of the root to be a ColumnNode
        ColumnNode smallest = rightOfRoot;
        while(rightOfRoot.right != root)
        {
            rightOfRoot = (ColumnNode)rightOfRoot.right;
            if(rightOfRoot.size < smallest.size) // choosing which column has the lowest size
            {
                smallest = rightOfRoot;
                if(smallest.size == 1){
                    break;
                }
            }
        }
        return smallest;
    }

    public ColumnNode chooseCreate(){
        ColumnNode rightOfRoot = (ColumnNode) root.right; // we cast the node to the right of the root to be a ColumnNode
        ColumnNode smallest = rightOfRoot;

        ArrayList<ColumnNode> choices = new ArrayList<>();
        choices.add(smallest);
        while(rightOfRoot.right != root)
        {
            rightOfRoot = (ColumnNode)rightOfRoot.right;
            if(rightOfRoot.size < smallest.size) // choosing which column has the lowest size
            {
                smallest = rightOfRoot;
                choices.clear();
                choices.add(rightOfRoot);
            }
            else if(rightOfRoot.size == smallest.size){
                choices.add(rightOfRoot);
            }
        }

        if(choices.size() == 0){
            System.out.println("Zero choices");
        }

        int newInt = new Random().nextInt(choices.size());

        return choices.get(newInt);
    }

}
