package org.helitha.heartapigame.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
        System.out.println("Difficulty screen loaded");
        SoundManager.getInstance().setupMuteButton(muteButton);
    }

    @FXML
    private void handleEasy() {
        SoundManager.getInstance().playClickSound();
        System.out.println("Easy difficulty selected");
        GameManager.getInstance().setDifficulty(GameManager.EASY);
        GameManager.getInstance().setScore(0);
        ScreenManager.getInstance().switchScene("PlayScreen.fxml");
    }

    @FXML
    private void handleMedium() {
        SoundManager.getInstance().playClickSound();
        System.out.println("Medium difficulty selected");
        GameManager.getInstance().setDifficulty(GameManager.MEDIUM);
        GameManager.getInstance().setScore(0);
        ScreenManager.getInstance().switchScene("PlayScreen.fxml");
    }

    @FXML
    private void handleHard() {
        SoundManager.getInstance().playClickSound();
        System.out.println("Hard difficulty selected");
        GameManager.getInstance().setDifficulty(GameManager.HARD);
        GameManager.getInstance().setScore(0);
        ScreenManager.getInstance().switchScene("PlayScreen.fxml");
    }

    @FXML
    private void handleBack() {
        SoundManager.getInstance().playClickSound();
        ScreenManager.getInstance().switchScene("HomeScreen.fxml");
    }

    @FXML
    private void handleHomeButton() {
        SoundManager.getInstance().playClickSound();
        ScreenManager.getInstance().switchScene("HomeScreen.fxml");
    }
}
