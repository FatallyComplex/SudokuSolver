package com.example.sudokusolver;

import com.example.sudokusolver.Controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SudokuSolver extends Application {
    public static MediaPlayer mediaPlayer;
    public static MediaPlayer soundPlayer;
    public static MediaPlayer masterPlayer;
    public static Media rosedokuMenuMusic;
    public static Media click;

    @Override
    public void start(Stage stage) throws IOException {
        music();
        FXMLLoader fxmlLoader = new FXMLLoader(SudokuSolver.class.getResource("index.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Rosedoku");
        stage.setScene(scene);
        stage.getIcons().add(new Image("icon.PNG"));
        stage.show();
        SettingsController.setMediaPlayer(mediaPlayer, soundPlayer, masterPlayer, rosedokuMenuMusic, click);
        IndexController.setMediaPlayer(mediaPlayer, soundPlayer, masterPlayer, rosedokuMenuMusic, click);
        PlayScreenController.setMediaPlayer(mediaPlayer, soundPlayer, masterPlayer, rosedokuMenuMusic, click);
        SizeController.setMediaPlayer(mediaPlayer, soundPlayer, masterPlayer, rosedokuMenuMusic, click);
        StageSelectorController.setMediaPlayer(mediaPlayer, soundPlayer, masterPlayer, rosedokuMenuMusic, click);
        GameController.setMediaPlayer(mediaPlayer, soundPlayer, masterPlayer, rosedokuMenuMusic, click);
        WinController.setMediaPlayer(mediaPlayer, soundPlayer, masterPlayer, rosedokuMenuMusic, click);
        Font.loadFont(getClass().getResourceAsStream("/com/example/css/fonts/DancingScript-VariableFont_wght.ttf"), 12);

    }

    public void music(){
        URL path = getClass().getResource("/Music/Rosedoku.mp3");
        assert path != null;
        rosedokuMenuMusic = new Media(path.toExternalForm());

        mediaPlayer = new MediaPlayer(rosedokuMenuMusic);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.15);

        URL pathClick = getClass().getResource("/Music/clickButton.mp3");
        assert pathClick != null;
        click = new Media(pathClick.toExternalForm());

        soundPlayer = new MediaPlayer(click);
        soundPlayer.setVolume(0.25);

        URL pathSilence = getClass().getResource("/Music/silence.mp3");
        assert pathSilence != null;
        Media silence = new Media(pathSilence.toExternalForm());
        masterPlayer = new MediaPlayer(silence);
        masterPlayer.setVolume(1.0);
    }

    public static void main(String[] args) {
        launch();
    }
}