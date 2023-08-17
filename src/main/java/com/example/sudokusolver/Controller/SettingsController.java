package com.example.sudokusolver.Controller;

import com.example.sudokusolver.SudokuSolver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class SettingsController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    static MediaPlayer mediaPlayer;
    static MediaPlayer soundPlayer;
    static MediaPlayer masterPlayer;

    static Media rosedoku;
    static Media click;

    @FXML
    private Slider masterVolumeSlider;

    @FXML
    private Slider sfxVolumeSlider;

    @FXML
    private Slider musicVolumeSlider;

    public void initialize() {
        // Set up your MediaPlayers and play/pause them as necessary
        musicVolumeSlider.setValue((mediaPlayer.getVolume()/masterPlayer.getVolume()));
        sfxVolumeSlider.setValue((soundPlayer.getVolume()/masterPlayer.getVolume()));
        masterVolumeSlider.setValue(masterPlayer.getVolume());


        // Bind the master volume slider to the volume properties of both MediaPlayers
        masterVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            soundPlayer.setVolume((newValue.doubleValue() * sfxVolumeSlider.getValue()));
            mediaPlayer.setVolume((newValue.doubleValue() * musicVolumeSlider.getValue()));
            masterPlayer.setVolume(newValue.doubleValue());
        });

        // Bind the SFX volume slider to the volume property of the SFX MediaPlayer
        sfxVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            soundPlayer.setVolume((masterVolumeSlider.getValue() * newValue.doubleValue()));
        });

        // Bind the music volume slider to the volume property of the music MediaPlayer
        musicVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            mediaPlayer.setVolume((masterVolumeSlider.getValue() * newValue.doubleValue()));
        });
    }

    @FXML
    private void playSound(ActionEvent e) throws IOException{
        soundPlayer.play();
    }

    @FXML
    private void switchMain(ActionEvent e) throws IOException {
        soundPlayer.seek(Duration.ZERO);
        soundPlayer.play();
        FXMLLoader loader = new FXMLLoader(SudokuSolver.class.getResource("index.fxml"));
        root = loader.load();

        stage = (Stage) (((Node)e.getSource()).getScene().getWindow());
        scene = stage.getScene();
        scene.setRoot(root);
    }


    public static void setMediaPlayer(MediaPlayer mediaPlayer, MediaPlayer soundPlayer, MediaPlayer masterPlayer, Media rosedoku, Media click) {
        SettingsController.mediaPlayer = mediaPlayer;
        SettingsController.soundPlayer = soundPlayer;
        SettingsController.rosedoku = rosedoku;
        SettingsController.click = click;
        SettingsController.masterPlayer = masterPlayer;
    }
}
