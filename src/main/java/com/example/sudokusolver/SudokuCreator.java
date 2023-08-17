package com.example.sudokusolver;

import com.example.sudokusolver.Model.AlgorithmXRandomizer;
import com.example.sudokusolver.Model.AlgorithmXSolver;
import com.example.sudokusolver.Model.SudokuType;
import com.example.sudokusolver.SudokuSolver;
import org.w3c.dom.Node;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SudokuCreator {

    private static int N = 9;
    private static int SIZE = 3;



    public static void main(String[] args) throws IOException {
        create(3, 1);
    }

    private static void create(int SIZE, int amount) throws IOException {

        String directoryPath = "src/main/java/com/example/sudokusolver/Controller/Saves/" + SIZE + "x" + SIZE;
        File directory = new File(directoryPath);

        File[] files = directory.listFiles();

        File lastFile = files[files.length-1];
        int name = Integer.parseInt(lastFile.getName().substring(0, 2))+1;


        AlgorithmXRandomizer.setAmount(1);

        ArrayList<int[][]> grids = new ArrayList<>();
        for(int amountSolutions = 0; amountSolutions < 20; amountSolutions++){
            AlgorithmXRandomizer a = new AlgorithmXRandomizer(new int[9][9], 3, SudokuType.NORMAL);
            grids.add(a.getFoundSolutions().get(0));
        }
        ArrayList<Integer> randomNumbers = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            randomNumbers.add(ThreadLocalRandom.current().nextInt(30, 52));
        }

        Collections.sort(randomNumbers);

        int current = 0;
        for(int[][] grid: grids){

            takeAway(randomNumbers.get(current), grid);
            current++;

            File f = new File("src/main/java/com/example/sudokusolver/Controller/Saves/" + SIZE + "x" + SIZE+"/"+String.format("%02d", name)+".txt");

            FileWriter fw = new FileWriter(f);

            fw.write(0 + "\n\n");
            fw.write(0 + "\n\n");
            fw.write(0 + "\n\n");
            fw.write(800000 + "\n\n");
            fw.write(1200000 + "\n\n");
            fw.write(2400000 + "\n\n");

            fw.write(prepareString(grid) + "\n");
            fw.write(prepareString(grid) + "\n");
            fw.write(0 + "\n\n");
            fw.write(0+"\n");
            fw.flush();
            fw.close();

            name++;
        }
    }

    private static void takeAway(int clueTaken, int[][] grid){

        while(clueTaken > 0){
            int randRow = new Random().nextInt(9);
            int randCol = new Random().nextInt(9);

            if(grid[randRow][randCol] != 0){
                int cur = grid[randRow][randCol];
                grid[randRow][randCol] = 0;

                AlgorithmXSolver a = new AlgorithmXSolver(grid, 3, SudokuType.NORMAL);

                if(a.getFoundSolutions().size() == 0){
                    grid[randRow][randCol] = cur;
                }
                else{
                    clueTaken--;
                }

            }
        }
    }

    /* Helper function for the printing of Sudoku puzzle. This function will print
     * out text, preceded by enough ' ' characters to make sure that the printint out
     * takes at least width characters.  */
    static void printFixedWidth(StringBuilder sb, String text, int width) {
        for (int i = 0; i < width - text.length(); i++) {
            sb.append(" ");
        }
        sb.append(text);
    }

    /* The print() function outputs the Sudoku grid to a string, using a bit of
     * extra formatting to make the result clearly readable. */
    public static String prepareString(int[][] grid) {
        // Compute the number of digits necessary to print out each number in the Sudoku puzzle
        int digits = (int) Math.floor(Math.log(N) / Math.log(10)) + 1;

        // Create a dashed line to separate the boxes
        int lineLength = (digits + 1) * N + 2 * SIZE - 3;
        StringBuilder sb = new StringBuilder();
        sb.append("-".repeat(Math.max(0, lineLength)));
        sb.append("\n");

        // Go through the Grid, printing out its values separated by spaces
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                printFixedWidth(sb, String.valueOf(grid[i][j]), digits);
                // Print the vertical lines between boxes
                if ((j < N - 1) && ((j + 1) % SIZE == 0)) {
                    sb.append(" |");
                }
                sb.append(" ");
            }
            sb.append("\n");

            // Print the horizontal line between boxes
            if ((i < N - 1) && ((i + 1) % SIZE == 0)) {
                sb.append("-".repeat(Math.max(0, lineLength)));
                sb.append("\n");
            }
        }
        return sb.toString();
    }

}
