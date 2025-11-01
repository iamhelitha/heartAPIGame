package org.helitha.heartapigame.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.helitha.heartapigame.managers.GameManager;
import org.helitha.heartapigame.managers.ScreenManager;
import org.helitha.heartapigame.managers.SoundManager;

public class DifficultyScreenController {

    @FXML
    private Button easyButton;

    @FXML
    private Button mediumButton;

    @FXML
    private Button hardButton;

    @FXML
    private Button backButton;

    @FXML
    private Button muteButton;

    @FXML
    public void initialize() {
        // Initialize controller
        System.out.println("Difficulty screen loaded");
        updateMuteButton();
    }

    private void updateMuteButton() {
        if (muteButton != null) {
            muteButton.setText(SoundManager.getInstance().isMuted() ? "🔇" : "🔊");
        }
    }

    @FXML
    private void handleMute() {
        SoundManager.getInstance().toggleMute();
        updateMuteButton();
    }

    @FXML
    private void handleEasy() {
        SoundManager.getInstance().playClickSound();
        System.out.println("Easy difficulty selected");
        GameManager.getInstance().setDifficulty(GameManager.EASY);
        GameManager.getInstance().resetGame();

        // Switch to play screen
        ScreenManager.getInstance().switchScene("PlayScreen.fxml");
    }

    @FXML
    private void handleMedium() {
        SoundManager.getInstance().playClickSound();
        System.out.println("Medium difficulty selected");
        GameManager.getInstance().setDifficulty(GameManager.MEDIUM);
        GameManager.getInstance().resetGame();

        // Switch to play screen
        ScreenManager.getInstance().switchScene("PlayScreen.fxml");
    }

    @FXML
    private void handleHard() {
        SoundManager.getInstance().playClickSound();
        System.out.println("Hard difficulty selected");
        GameManager.getInstance().setDifficulty(GameManager.HARD);
        GameManager.getInstance().resetGame();

        // Switch to play screen
        ScreenManager.getInstance().switchScene("PlayScreen.fxml");
    }

    @FXML
    private void handleBack() {
        SoundManager.getInstance().playClickSound();
        // Return to home screen
        ScreenManager.getInstance().switchScene("HomeScreen.fxml");
    }

    @FXML
    private void handleHomeButton(ActionEvent event) {
        SoundManager.getInstance().playClickSound();
        ScreenManager.getInstance().switchScene("HomeScreen.fxml");
    }
}
