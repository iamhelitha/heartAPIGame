package org.helitha.heartapigame.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.helitha.heartapigame.managers.GameManager;
import org.helitha.heartapigame.managers.ScreenManager;

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
    public void initialize() {
        // Initialize controller
        System.out.println("Difficulty screen loaded");
    }

    @FXML
    private void handleEasy() {
        System.out.println("Easy difficulty selected");
        GameManager.getInstance().setDifficulty(GameManager.EASY);
        GameManager.getInstance().resetGame();

        // Switch to play screen
        ScreenManager.getInstance().switchScene("PlayScreen.fxml");
    }

    @FXML
    private void handleMedium() {
        System.out.println("Medium difficulty selected");
        GameManager.getInstance().setDifficulty(GameManager.MEDIUM);
        GameManager.getInstance().resetGame();

        // Switch to play screen
        ScreenManager.getInstance().switchScene("PlayScreen.fxml");
    }

    @FXML
    private void handleHard() {
        System.out.println("Hard difficulty selected");
        GameManager.getInstance().setDifficulty(GameManager.HARD);
        GameManager.getInstance().resetGame();

        // Switch to play screen
        ScreenManager.getInstance().switchScene("PlayScreen.fxml");
    }

    @FXML
    private void handleBack() {
        // Return to home screen
        ScreenManager.getInstance().switchScene("HomeScreen.fxml");
    }

    @FXML
    private void handleHomeButton(ActionEvent event) {
        ScreenManager sm = new ScreenManager((Stage) ((Node) event.getSource()).getScene().getWindow());
        sm.switchScene("HomeScreen.fxml");
    }
}
