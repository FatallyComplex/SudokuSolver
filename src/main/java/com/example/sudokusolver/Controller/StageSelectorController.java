package com.example.sudokusolver.Controller;

import com.example.sudokusolver.Model.SudokuType;
import com.example.sudokusolver.SudokuSolver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class StageSelectorController {
    static MediaPlayer mediaPlayer;
    static MediaPlayer soundPlayer;
    static MediaPlayer masterPlayer;

    private static SudokuType type;
    private static int SIZE, N;

    private static int[][] Grid;

    private int maxLevel;
    private int minLevel;

    static Media rosedoku;
    static Media click;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private Image uncompletedStar = new Image(SudokuSolver.class.getResource("/Icons/RoseStarNotCompleted.PNG").toURI().toString());
    private Image completedStar = new Image(SudokuSolver.class.getResource("/Icons/RoseStarCompleted.PNG").toURI().toString());

    private String saveDirectory;

    @FXML
    private GridPane levelGrid;

    @FXML
    private Button prevButton;

    @FXML
    private Button nextButton;

    public StageSelectorController() throws URISyntaxException {
    }


    public void initialize() throws Exception {
        String workingDirectory = System.getProperty("user.dir");
        String savesDirectory = workingDirectory + File.separator + "Saves" + File.separator + SIZE + "x" + SIZE;
        //String directoryPath = "src/main/java/com/example/sudokusolver/Controller/Saves/" + SIZE + "x" + SIZE;
        File directory = new File(savesDirectory);
        saveDirectory = savesDirectory;


        File[] files = directory.listFiles();
        int numberOfFiles = files.length;
        
        outerLoop:
        for(int row = 0; row < 3; row++){
            for(int col = 0; col <3; col++){
                int level = row * 3 + col + 1;
                if(level > numberOfFiles){
                    break outerLoop;
                }
                HBox stars = new HBox();

                String levelString = String.format("%02d", level);

                File f = files[level-1];

                String fileName = f.getName();

                loadLevel(row, col, stars, f, fileName, levelString);
            }
        }
        minLevel = 1;
        maxLevel = 9;
    }

    @FXML
    private void next(ActionEvent e) throws Exception {
        soundPlayer.seek(Duration.ZERO);
        soundPlayer.play();
        minLevel = maxLevel + 1;
        maxLevel = maxLevel+9;
        levelGrid.getChildren().clear();
        loadLevels(minLevel, maxLevel);
        prevButton.setDisable(false);
    }

    @FXML
    private void prev(ActionEvent e) throws Exception{
        soundPlayer.seek(Duration.ZERO);
        soundPlayer.play();
        maxLevel = minLevel - 1;
        minLevel = minLevel - 9;
        levelGrid.getChildren().clear();
        loadLevels(minLevel, maxLevel);

        if(minLevel == 1){
            prevButton.setDisable(true);
        }
        nextButton.setDisable(false);
    }

    private void loadLevels(int min, int max) throws Exception {
        File directory = new File(saveDirectory);


        File[] files = directory.listFiles();

        if(files.length <= maxLevel){
            nextButton.setDisable(true);
        }

        File[] loadingFiles = new File[9];

        int j = 0;
        assert files != null;
        for(File file: files){
            if(file.getName().equals(String.format("%02d", min) + ".txt")){
                loadingFiles[j] = file;
                j++;
                min++;
                if(min > max){
                    break;
                }
            }
        }
        int numberOfFiles = loadingFiles.length;

        outerLoop:
        for(int row = 0; row < 3; row++){
            for(int col = 0; col <3; col++){
                int level = row * 3 + col + 1;
                if(level > numberOfFiles){
                    break outerLoop;
                }
                HBox stars = new HBox();

                File f = loadingFiles[level-1];

                String fileName = f.getName();
                String levelString = String.format("%02d", Integer.parseInt(fileName.substring(0, 2)));

                loadLevel(row, col, stars, f, fileName, levelString);
            }
        }
    }

    private void loadLevel(int row, int col, HBox stars, File f, String fileName, String levelString) throws Exception {
        AtomicReference<FileInputStream> in = new AtomicReference<>(new FileInputStream(f));

        int starsAmount = readInteger(in.get());

        int hintsUsed = readInteger(in.get());

        int millisecond = readInteger(in.get());

        int starTimeThree = readInteger(in.get());
        int starTimeTwo = readInteger(in.get());
        int starTimeOne = readInteger(in.get());
        in.get().close();

        for(int i = 0; i < starsAmount; i++){
            stars.getChildren().add(new ImageView(completedStar));
        }
        for(int i = 0; i < 3 - starsAmount; i++){
            stars.getChildren().add(new ImageView(uncompletedStar));
        }

        stars.setAlignment(Pos.CENTER);

        Button levelTitle = new Button(levelString);
        levelTitle.setStyle("-fx-font-size: 92;");

        levelTitle.setOnAction((e) ->{
            try {
                in.set(new FileInputStream(f));
                readInteger(in.get());
                GameController.setHintsUsed(readInteger(in.get()));
                readInteger(in.get());
                readInteger(in.get());
                readInteger(in.get());
                readInteger(in.get());
                read(in.get()); // original grid
                GameController.setCurrentGrid(readSave(in.get())); // current grid
                GameController.setGrid(Grid); // original grid setter
                GameController.setSIZE(SIZE); // setting puzzle size
                GameController.setMillisecond(millisecond); //setting millisecond
                GameController.setStarTime(starTimeOne, starTimeTwo, starTimeThree); // setting star time
                int started = readInteger(in.get());
                int completed = readInteger(in.get());

                switchGame(e, fileName, started, completed, in.get());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox levelBox = new VBox(levelTitle, stars);
        levelBox.setSpacing(15);

        levelBox.setAlignment(Pos.CENTER);

        levelGrid.add(levelBox, col, row);
    }


    private void switchGame(ActionEvent e, String level, int started, int completed, FileInputStream in) throws IOException {
        soundPlayer.seek(Duration.ZERO);
        soundPlayer.play();
        if (started == 1 && completed != 1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("This level has been started");
            Label content = new Label("Do you want to load a new saved game or load the existing");
            content.setWrapText(true);
            alert.getDialogPane().setContent(content);

            alert.getDialogPane().getStylesheets().clear();
            alert.getDialogPane().getStylesheets().add(SudokuSolver.class.getResource("alert.css").toExternalForm());
            alert.getDialogPane().getStyleClass().add("dialog");


            ButtonType buttonTypeYes = new ButtonType("New");
            ButtonType buttonTypeNo = new ButtonType("Load");

            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo, ButtonType.CANCEL);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                // User clicked the X button, do nothing
                return;
            }
            else if (result.isPresent() && result.get() == buttonTypeYes){
                // User clicked OK, continue to switch the scene
                soundPlayer.seek(Duration.ZERO);
                soundPlayer.play();
                GameController.setCurrentGrid(Arrays.stream(Grid).map(int[]::clone).toArray(int[][]::new));
                GameController.setMillisecond(0);
                GameController.setPuzzleStarted(false);
                GameController.setCompleted(false);
                loadGame(e, level);
                in.close();
            }else{
                soundPlayer.seek(Duration.ZERO);
                soundPlayer.play();
                GameController.setPuzzleStarted(true);
                GameController.setCompleted(false);
                loadGame(e, level);// User clicked Cancel or closed the dialog, do nothing
                in.close();
            }

        }else if(completed == 1){
            soundPlayer.seek(Duration.ZERO);
            soundPlayer.play();
            GameController.setCurrentGrid(Arrays.stream(Grid).map(int[]::clone).toArray(int[][]::new));
            GameController.setMillisecond(0);
            GameController.setPuzzleStarted(false);
            GameController.setCompleted(false);
            loadGame(e, level);
            in.close();
        }
        else {
            soundPlayer.seek(Duration.ZERO);
            soundPlayer.play();
            loadGame(e, level);
            in.close();
        }

    }

    private void loadGame(ActionEvent e, String level) throws IOException {
        FXMLLoader loader = new FXMLLoader(SudokuSolver.class.getResource("game.fxml"));
        GameController.setFileName(level);
        GameController.setType(type);
        root = loader.load();

        stage = (Stage) (((Node) e.getSource()).getScene().getWindow());
        scene = stage.getScene();
        scene.setRoot(root);
    }

    @FXML
    private void switchBack(ActionEvent e) throws IOException {
        soundPlayer.seek(Duration.ZERO);
        soundPlayer.play();
        FXMLLoader loader = new FXMLLoader(SudokuSolver.class.getResource("size.fxml"));
        root = loader.load();

        stage = (Stage) (((Node)e.getSource()).getScene().getWindow());
        scene = stage.getScene();
        scene.setRoot(root);
    }


    /* readInteger is a helper function for the reading of the input file.  It reads
     * words until it finds one that represents an integer. For convenience, it will also
     * recognize the string "x" as equivalent to "0". */
    static int readInteger( InputStream in ) throws Exception
    {
        int result = 0;
        boolean success = false;

        while( !success ) {
            String word = readWord( in );

            try {
                result = Integer.parseInt( word );
                success = true;
            } catch( Exception e ) {
                // Convert 'x' words into 0's
                if( word.compareTo("x") == 0 ) {
                    result = 0;
                    success = true;
                }
                // Ignore all other words that are not integers
            }
        }

        return result;
    }


    /* readWord is a helper function that reads a word separated by white space. */
    static String readWord( InputStream in ) throws Exception
    {
        StringBuffer result = new StringBuffer();
        int currentChar = in.read();
        String whiteSpace = " \t\r\n";
        // Ignore any leading white space
        while( whiteSpace.indexOf(currentChar) > -1 ) {
            currentChar = in.read();
        }

        // Read all characters until you reach white space
        while( whiteSpace.indexOf(currentChar) == -1 ) {
            result.append( (char) currentChar );
            currentChar = in.read();
        }
        return result.toString();
    }


    /* This function reads a Sudoku puzzle from the input stream in.  The Sudoku
     * grid is filled in one row at at time, from left to right.  All non-valid
     * characters are ignored by this function and may be used in the Sudoku file
     * to increase its legibility. */
    public void read( InputStream in ) throws Exception
    {
        for( int i = 0; i < N; i++ ) {
            for( int j = 0; j < N; j++ ) {
                Grid[i][j] = readInteger( in );
            }
        }
    }
    public int[][] readSave( InputStream in ) throws Exception
    {
        int[][] saveGrid = new int[N][N];
        for( int i = 0; i < N; i++ ) {
            for( int j = 0; j < N; j++ ) {
                saveGrid[i][j] = readInteger( in );
            }
        }
        return saveGrid;
    }


    public static void setMediaPlayer(MediaPlayer mediaPlayer, MediaPlayer soundPlayer, MediaPlayer masterPlayer, Media rosedoku, Media click) {
        StageSelectorController.mediaPlayer = mediaPlayer;
        StageSelectorController.soundPlayer = soundPlayer;
        StageSelectorController.rosedoku = rosedoku;
        StageSelectorController.click = click;
        StageSelectorController.masterPlayer = masterPlayer;
    }

    public static void setType(SudokuType type){
        StageSelectorController.type = type;
    }

    public static void setSize(int size){
        SIZE = size;
        N = size*size;

        Grid = new int[N][N];
        for( int i = 0; i < N; i++ )
            for( int j = 0; j < N; j++ )
                Grid[i][j] = 0;
    }
}
