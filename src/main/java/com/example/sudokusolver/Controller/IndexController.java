package com.example.sudokusolver.Controller;

import com.example.sudokusolver.SudokuSolver;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class IndexController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    static MediaPlayer mediaPlayer;
    static MediaPlayer soundPlayer;
    static MediaPlayer masterPlayer;
    static Media rosedoku;
    static Media click;

    @FXML
    private void switchGame(ActionEvent e) throws IOException{
        soundPlayer.seek(Duration.ZERO);
        soundPlayer.play();
        FXMLLoader loader = new FXMLLoader(SudokuSolver.class.getResource("playScreen.fxml"));
        root = loader.load();

        stage = (Stage) (((Node)e.getSource()).getScene().getWindow());
        scene = stage.getScene();
        scene.setRoot(root);
    }

    @FXML
    private void switchSettings(ActionEvent e) throws IOException {
        soundPlayer.seek(Duration.ZERO);
        soundPlayer.play();
        FXMLLoader loader = new FXMLLoader(SudokuSolver.class.getResource("settings.fxml"));
        root = loader.load();

        stage = (Stage) (((Node)e.getSource()).getScene().getWindow());
        scene = stage.getScene();
        scene.setRoot(root);
    }

    @FXML
    private void exit(ActionEvent e) throws IOException{
        Thread closeThread = new Thread(() -> {
            try {
                soundPlayer.seek(Duration.ZERO);
                soundPlayer.play();
                Thread.sleep(1000);
            } catch (InterruptedException ef) {
                ef.printStackTrace();
            }
            Platform.exit();
        });
        closeThread.start();
    }

    public static void setMediaPlayer(MediaPlayer mediaPlayer, MediaPlayer soundPlayer, MediaPlayer masterPlayer, Media rosedoku, Media click) {
        IndexController.mediaPlayer = mediaPlayer;
        IndexController.soundPlayer = soundPlayer;
        IndexController.masterPlayer = masterPlayer;
        IndexController.rosedoku = rosedoku;
        IndexController.click = click;
    }
}
