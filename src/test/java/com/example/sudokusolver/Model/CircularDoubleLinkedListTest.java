package com.example.sudokusolver.Model;
import org.testng.annotations.Test;
import static org.junit.Assert.*;

public class CircularDoubleLinkedListTest {

    CircularDoubleLinkedList.Node node1 = new CircularDoubleLinkedList.Node();
    CircularDoubleLinkedList.Node node2 = new CircularDoubleLinkedList.Node();
    CircularDoubleLinkedList.Node node3 = new CircularDoubleLinkedList.Node();

    CircularDoubleLinkedList.Node node4 = new CircularDoubleLinkedList.Node();
    CircularDoubleLinkedList.Node node5 = new CircularDoubleLinkedList.Node();
    CircularDoubleLinkedList.Node node6 = new CircularDoubleLinkedList.Node();

    CircularDoubleLinkedList.Node node7 = new CircularDoubleLinkedList.Node();
    CircularDoubleLinkedList.Node node8 = new CircularDoubleLinkedList.Node();
    CircularDoubleLinkedList.Node node9 = new CircularDoubleLinkedList.Node();

    CircularDoubleLinkedList.Node node10 = new CircularDoubleLinkedList.Node();

    CircularDoubleLinkedList.ColumnNode cNode1 = new CircularDoubleLinkedList.ColumnNode();
    CircularDoubleLinkedList.ColumnNode cNode2 = new CircularDoubleLinkedList.ColumnNode();
    CircularDoubleLinkedList.ColumnNode cNode3 = new CircularDoubleLinkedList.ColumnNode();

    CircularDoubleLinkedList testList = new CircularDoubleLinkedList();

    @Test
    public void insertColumnEmpty(){
        CircularDoubleLinkedList list = new CircularDoubleLinkedList();

        cNode1.clear();

        list.insertSide(cNode1);

        assertEquals(cNode1, list.getRoot().getRight());
        assertEquals(cNode1, list.getRoot().getLeft());
        assertEquals(cNode1.getRight(), list.getRoot());
        assertEquals(cNode1.getLeft(), list.getRoot());
    }

    @Test
    public void insertColumnOne(){
        CircularDoubleLinkedList list = new CircularDoubleLinkedList();

        cNode1.clear();
        cNode2.clear();

        list.insertSide(cNode1);
        list.insertSide(cNode2);

        assertEquals(cNode1, list.getRoot().getRight());
        assertEquals(cNode2, list.getRoot().getLeft());
        assertEquals(cNode1.getRight(), cNode2);
        assertEquals(cNode1.getLeft(), list.getRoot());
        assertEquals(cNode2.getRight(), list.getRoot());
        assertEquals(cNode2.getLeft(), cNode1);
    }

    @Test
    public void insertColumnTwo(){
        CircularDoubleLinkedList list = new CircularDoubleLinkedList();

        cNode1.clear();
        cNode2.clear();
        cNode3.clear();

        list.insertSide(cNode1);
        list.insertSide(cNode2);
        list.insertSide(cNode3);

        assertEquals(cNode1, list.getRoot().getRight());
        assertEquals(cNode3, list.getRoot().getLeft());
        assertEquals(cNode1.getRight(), cNode2);
        assertEquals(cNode1.getLeft(), list.getRoot());
        assertEquals(cNode2.getRight(), cNode3);
        assertEquals(cNode2.getLeft(), cNode1);
        assertEquals(cNode3.getRight(), list.getRoot());
        assertEquals(cNode3.getLeft(), cNode2);
    }

    @Test
    public void insertDownOne(){
        CircularDoubleLinkedList list = new CircularDoubleLinkedList();

        cNode1.clear();
        cNode2.clear();
        cNode3.clear();

        node1.clear();

        list.insertSide(cNode1);
        list.insertSide(cNode2);
        list.insertSide(cNode3);

        cNode1.insertDown(node1);

        assertEquals(node1, cNode1.getDown());
        assertEquals(node1, cNode1.getUp());
        assertEquals(cNode1, node1.getUp());
        assertEquals(cNode1, node1.getDown());
    }

    @Test
    public void insertUpTwo(){
        CircularDoubleLinkedList list = new CircularDoubleLinkedList();

        cNode1.clear();
        cNode2.clear();
        cNode3.clear();

        node1.clear();
        node2.clear();

        list.insertSide(cNode1);
        list.insertSide(cNode2);
        list.insertSide(cNode3);

        cNode1.insertUp(node1);
        cNode1.insertUp(node2);

        assertEquals(node1, node2.getUp());
        assertEquals(node1, cNode1.getDown());
        assertEquals(node2, cNode1.getUp());
        assertEquals(node2, node1.getDown());
        assertEquals(cNode1, node1.getUp());
        assertEquals(cNode1, node2.getDown());
    }


    @Test
    public void insertCoverList(){
        CircularDoubleLinkedList list = new CircularDoubleLinkedList();

        cNode1.clear();
        cNode2.clear();
        cNode3.clear();

        node1.clear();
        node2.clear();
        node3.clear();
        node4.clear();
        node5.clear();
        node6.clear();

        list.insertSide(cNode1);
        list.insertSide(cNode2);
        list.insertSide(cNode3);

        list.insertOnColumn(cNode1, node1);
        list.insertOnColumn(cNode1, node3);

        list.insertOnColumn(cNode2, node4);
        list.insertOnColumn(cNode2, node5);

        list.insertOnColumn(cNode3, node2);
        list.insertOnColumn(cNode3, node6);

        node1.insertRight(node2);

        node4.insertLeft(node3);

        node5.insertRight(node6);

        assertEquals(node1, node2.getLeft());
        assertEquals(node4, node3.getRight());
        assertEquals(node6, node1.getRight().getDown());
        assertEquals(cNode3, node5.getUp().getLeft().getUp().getRight().getHead());
    }

    public void startCover(CircularDoubleLinkedList list){
        cNode1.clear();
        cNode2.clear();
        cNode3.clear();

        node1.clear();
        node2.clear();
        node3.clear();
        node4.clear();
        node5.clear();
        node6.clear();

        list.insertSide(cNode1);
        list.insertSide(cNode2);
        list.insertSide(cNode3);

        list.insertOnColumn(cNode1, node1);
        list.insertOnColumn(cNode1, node3);
        list.insertOnColumn(cNode1, node7);

        list.insertOnColumn(cNode2, node4);
        list.insertOnColumn(cNode2, node5);
        list.insertOnColumn(cNode2, node8);

        list.insertOnColumn(cNode3, node2);
        list.insertOnColumn(cNode3, node6);
        list.insertOnColumn(cNode3, node9);

        node1.insertRight(node2);

        node4.insertLeft(node3);

        node5.insertRight(node6);

        node7.insertRight(node8);

        node8.insertRight(node9);
    }

    @Test
    public void coverColumnOne(){
        startCover(testList);

        cNode1.cover();

        assertEquals(testList.getRoot().getRight(), cNode2);
        assertEquals(testList.getRoot(), cNode2.getLeft());
        assertEquals(node6, cNode3.getDown());
        assertEquals(node6, cNode3.getUp());

    }

    @Test
    public void uncoverColumnOne(){

        CircularDoubleLinkedList list = new CircularDoubleLinkedList();

        cNode1.clear();
        cNode2.clear();
        cNode3.clear();

        node1.clear();
        node2.clear();
        node3.clear();
        node4.clear();
        node5.clear();
        node6.clear();
        node7.clear();
        node8.clear();
        node9.clear();
        node10.clear();

        list.insertSide(cNode1);
        list.insertSide(cNode2);
        list.insertSide(cNode3);

        list.insertOnColumn(cNode1, node1);
        list.insertOnColumn(cNode1, node3);
        list.insertOnColumn(cNode1, node7);

        list.insertOnColumn(cNode2, node4);
        list.insertOnColumn(cNode2, node5);
        list.insertOnColumn(cNode2, node8);


        list.insertOnColumn(cNode3, node2);
        list.insertOnColumn(cNode3, node6);
        list.insertOnColumn(cNode3, node9);

        node1.insertRight(node2);

        node4.insertLeft(node3);

        node5.insertRight(node6);

        node7.insertRight(node8);

        node8.insertRight(node9);

        cNode1.cover();
        cNode1.uncover();

        assertEquals(list.getRoot().getRight(), cNode1);
        assertEquals(cNode2.getLeft(), cNode1);
        assertEquals(cNode3.getDown(), node2);
        assertEquals(cNode3.getUp(), node9);
    }

    @Test
    public void chooseSmallest(){
        CircularDoubleLinkedList list = new CircularDoubleLinkedList();

        cNode1.clear();
        cNode2.clear();
        cNode3.clear();

        node1.clear();
        node2.clear();
        node3.clear();
        node4.clear();
        node5.clear();
        node6.clear();
        node7.clear();
        node8.clear();
        node9.clear();

        list.insertSide(cNode1);
        list.insertSide(cNode2);
        list.insertSide(cNode3);

        list.insertOnColumn(cNode1, node1);
        list.insertOnColumn(cNode1, node3);
        list.insertOnColumn(cNode1, node7);

        list.insertOnColumn(cNode2, node4);
        list.insertOnColumn(cNode2, node5);
        list.insertOnColumn(cNode2, node8);
        list.insertOnColumn(cNode2, node10);

        list.insertOnColumn(cNode3, node2);
        list.insertOnColumn(cNode3, node6);
        list.insertOnColumn(cNode3, node9);

        node1.insertRight(node2);

        node4.insertLeft(node3);

        node5.insertRight(node6);

        node7.insertRight(node8);

        node8.insertRight(node9);

        cNode1.cover();

        CircularDoubleLinkedList.ColumnNode smallest = list.choose();
        assertEquals(cNode3, smallest);
    }

}
