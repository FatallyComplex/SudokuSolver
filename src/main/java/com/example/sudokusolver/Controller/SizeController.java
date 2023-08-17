package com.example.sudokusolver.Controller;

import com.example.sudokusolver.Model.SudokuType;
import com.example.sudokusolver.SudokuSolver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class SizeController {
    static MediaPlayer mediaPlayer;
    static MediaPlayer soundPlayer;
    static MediaPlayer masterPlayer;

    private static SudokuType type;

    static Media rosedoku;
    static Media click;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void initialize() {
        StageSelectorController.setType(type);
    }

    @FXML
    private void playSound(ActionEvent e) throws IOException {
        soundPlayer.seek(Duration.ZERO);
        soundPlayer.play();
        System.out.println(type);
    }

    @FXML
    private void switchLevel(ActionEvent e) throws IOException{
        soundPlayer.seek(Duration.ZERO);
        soundPlayer.play();
        FXMLLoader loader = new FXMLLoader(SudokuSolver.class.getResource("stageSelector.fxml"));
        StageSelectorController.setType(type);
        StageSelectorController.setSize(Integer.parseInt(((Button) e.getSource()).getText().charAt(0)+""));
        root = loader.load();

        stage = (Stage) (((Node)e.getSource()).getScene().getWindow());
        scene = stage.getScene();
        scene.setRoot(root);
    }

    @FXML
    private void switchBack(ActionEvent e) throws IOException {
        soundPlayer.seek(Duration.ZERO);
        soundPlayer.play();
        FXMLLoader loader = new FXMLLoader(SudokuSolver.class.getResource("playScreen.fxml"));
        root = loader.load();

        stage = (Stage) (((Node)e.getSource()).getScene().getWindow());
        scene = stage.getScene();
        scene.setRoot(root);
        type = null;
    }

    public static void setType(SudokuType type) {
        SizeController.type = type;
    }

    public static void setMediaPlayer(MediaPlayer mediaPlayer, MediaPlayer soundPlayer, MediaPlayer masterPlayer, Media rosedoku, Media click) {
        SizeController.mediaPlayer = mediaPlayer;
        SizeController.soundPlayer = soundPlayer;
        SizeController.rosedoku = rosedoku;
        SizeController.click = click;
        SizeController.masterPlayer = masterPlayer;
    }

}
