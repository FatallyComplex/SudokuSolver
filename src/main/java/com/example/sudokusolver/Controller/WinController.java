package com.example.sudokusolver.Controller;

import com.example.sudokusolver.SudokuSolver;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.AccessibleAction;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class WinController {
    static MediaPlayer mediaPlayer;
    static MediaPlayer soundPlayer;
    static MediaPlayer masterPlayer;

    private static int starsAmount;

    private static Long timeOneStar;
    private static Long timeTwoStar;
    private static Long timeThreeStar;

    private static Long completedTime;
    private static Long millisecondsElapsed = 0L;

    private static boolean hintsUsed;

    private static String fileName;

    private int SIZE, N;

    private static int[][] Grid; // should be original
    private static int[][] solveGrid;

    static Media rosedoku;
    static Media click;

    private Timeline timeline;

    @FXML
    private ImageView starC1;

    @FXML
    private ImageView starC2;

    @FXML
    private ImageView starC3;

    @FXML
    private Label timeLabel;

    @FXML
    private Button finishButton;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void initialize(){
        timeline = new Timeline(new KeyFrame(Duration.millis(30), e ->{
            millisecondsElapsed = millisecondsElapsed + 300;

            String formattedTime;

            Long hours = millisecondsElapsed/3600000;
            Long minutes = (millisecondsElapsed%3600000) /60000;
            Long seconds = (millisecondsElapsed%60000)/1000;
            Long milliseconds = millisecondsElapsed%1000/10;

            if(minutes > 60){
                formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            }else{
                formattedTime = String.format("%02d:%02d:%02d", minutes, seconds, milliseconds);
            }

            timeLabel.setText(formattedTime);

            if(millisecondsElapsed >= timeThreeStar){
                starC3.setVisible(false);
            }
            if(millisecondsElapsed >= timeTwoStar){
                starC2.setVisible(false);
            }
            if (millisecondsElapsed >= timeOneStar){
                starC1.setVisible(false);
            }
            if(hintsUsed){
                starC1.setVisible(false);
                starC2.setVisible(false);
                starC3.setVisible(false);
            }

            if(millisecondsElapsed >= completedTime){
                hours = completedTime/3600000;
                minutes = (completedTime%3600000) /60000;
                seconds = (completedTime%60000)/1000;
                milliseconds = completedTime%1000/10;
                if(minutes > 60){
                    formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                }else{
                    formattedTime = String.format("%02d:%02d:%02d", minutes, seconds, milliseconds);
                }
                timeLabel.setText(formattedTime);
                timeline.stop();
                finishButton.setDisable(false);
            }
        }));
        timeline.setDelay(Duration.millis(20));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    public void finish(ActionEvent e) throws IOException {
        N = Grid.length;
        SIZE = (int) Math.sqrt(Grid.length);

        File f = new File(System.getProperty("user.dir")+File.separator+"Saves"+File.separator + SIZE + "x" + SIZE+File.separator+fileName);

        FileWriter fw = new FileWriter(f);

        fw.write(starsAmount + "\n\n");
        fw.write((hintsUsed ? 1:0) + "\n\n");
        fw.write(completedTime + "\n\n");
        fw.write(timeThreeStar + "\n\n");
        fw.write(timeTwoStar + "\n\n");
        fw.write(timeOneStar + "\n\n");

        fw.write(prepareString(Grid) + "\n");
        fw.write(prepareString(solveGrid) + "\n");
        fw.write(1 + "\n\n");
        fw.write(1+"\n");
        fw.flush();
        fw.close();

        soundPlayer.seek(Duration.ZERO);
        soundPlayer.play();
        FXMLLoader loader = new FXMLLoader(SudokuSolver.class.getResource("stageSelector.fxml"));
        root = loader.load();

        stage = (Stage) (((Node)e.getSource()).getScene().getWindow());
        scene = stage.getScene();
        scene.setRoot(root);


    }

    /* Helper function for the printing of Sudoku puzzle. This function will print
     * out text, preceded by enough ' ' characters to make sure that the printint out
     * takes at least width characters.  */
    void printFixedWidth(StringBuilder sb, String text, int width) {
        for (int i = 0; i < width - text.length(); i++) {
            sb.append(" ");
        }
        sb.append(text);
    }

    /* The print() function outputs the Sudoku grid to a string, using a bit of
     * extra formatting to make the result clearly readable. */
    public String prepareString(int[][] grid) {
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

    public static void setMediaPlayer(MediaPlayer mediaPlayer, MediaPlayer soundPlayer, MediaPlayer masterPlayer, Media rosedoku, Media click) {
        WinController.mediaPlayer = mediaPlayer;
        WinController.soundPlayer = soundPlayer;
        WinController.rosedoku = rosedoku;
        WinController.click = click;
        WinController.masterPlayer = masterPlayer;
    }
    public static void setFileName(String f) {
        WinController.fileName = f;
    }
    public static void setMillisecond(long milli){
        WinController.completedTime = milli;
    }
    public static void setStarsAmount(int starsAmount) {
        WinController.starsAmount = starsAmount;
    }

    public static void setTimeOneStar(Long timeOneStar, Long timeTwoStar, Long timeThreeStar) {
        WinController.timeOneStar = timeOneStar;
        WinController.timeTwoStar = timeTwoStar;
        WinController.timeThreeStar = timeThreeStar;
    }

    public static void setGrid(int[][] grid) {
        WinController.Grid = grid;
    }

    public static void setSolveGrid(int[][] solveGrid) {
        WinController.solveGrid = solveGrid;
    }

    public static void setHintsUsed(boolean hintsUsed) {
        WinController.hintsUsed = hintsUsed;
    }
}
