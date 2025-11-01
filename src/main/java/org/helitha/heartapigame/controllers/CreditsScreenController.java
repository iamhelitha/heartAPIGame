package org.helitha.heartapigame.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.helitha.heartapigame.managers.ScreenManager;
import org.helitha.heartapigame.managers.SoundManager;

public class CreditsScreenController {
    
    @FXML
    private Button muteButton;

    @FXML
    public void initialize() {
        System.out.println("Credits screen loaded");
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
