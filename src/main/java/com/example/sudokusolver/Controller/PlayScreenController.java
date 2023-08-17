package com.example.sudokusolver.Controller;

import com.example.sudokusolver.Model.SudokuType;
import com.example.sudokusolver.SudokuSolver;
import javafx.css.Size;
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

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class PlayScreenController {
    static MediaPlayer mediaPlayer;
    static MediaPlayer soundPlayer;
    static MediaPlayer masterPlayer;

    private boolean isTaylorPlaying = false;

    static Media rosedoku;
    static Media click;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private void isTaylorPlaying(){
        if(isTaylorPlaying){
            double prevVolume = soundPlayer.getVolume();
            soundPlayer.pause();
            soundPlayer = new MediaPlayer(click);
            soundPlayer.setVolume(prevVolume);
            isTaylorPlaying = false;
        }
    }

    @FXML
    private void playSound() throws IOException {
        if(!isTaylorPlaying) {
            soundPlayer.seek(Duration.ZERO);
            soundPlayer.play();
        }
    }
    @FXML
    private void playTaylor(ActionEvent e) throws IOException{
        isTaylorPlaying = true;
        URL pathClick = SudokuSolver.class.getResource("/Music/taylorSwift.mp3");
        Media taylor = new Media(pathClick.toExternalForm());
        double prevVolume = soundPlayer.getVolume();
        soundPlayer = new MediaPlayer(taylor);
        soundPlayer.setVolume(mediaPlayer.getVolume());
        soundPlayer.play();
        soundPlayer.setOnEndOfMedia(()->{
            soundPlayer = new MediaPlayer(click);
            soundPlayer.setVolume(prevVolume);
            isTaylorPlaying = false;
        });
    }

    @FXML
    private void switchNormal(ActionEvent e) throws IOException{
        isTaylorPlaying();
        playSound();
        FXMLLoader loader = new FXMLLoader(SudokuSolver.class.getResource("size.fxml"));
        root = loader.load();
        SizeController.setType(SudokuType.NORMAL);
        GameController.setType(SudokuType.NORMAL);
        stage = (Stage) (((Node)e.getSource()).getScene().getWindow());
        scene = stage.getScene();
        scene.setRoot(root);
    }

    @FXML
    private void switchMain(ActionEvent e) throws IOException {
        isTaylorPlaying();
        playSound();
        FXMLLoader loader = new FXMLLoader(SudokuSolver.class.getResource("index.fxml"));
        root = loader.load();

        stage = (Stage) (((Node)e.getSource()).getScene().getWindow());
        scene = stage.getScene();
        scene.setRoot(root);
    }

    public static void setMediaPlayer(MediaPlayer mediaPlayer, MediaPlayer soundPlayer, MediaPlayer masterPlayer, Media rosedoku, Media click) {
        PlayScreenController.mediaPlayer = mediaPlayer;
        PlayScreenController.soundPlayer = soundPlayer;
        PlayScreenController.rosedoku = rosedoku;
        PlayScreenController.click = click;
        PlayScreenController.masterPlayer = masterPlayer;
    }
}
