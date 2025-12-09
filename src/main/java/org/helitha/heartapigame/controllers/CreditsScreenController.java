package org.helitha.heartapigame.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.helitha.heartapigame.managers.ScreenManager;
import org.helitha.heartapigame.managers.SoundManager;

public class CreditsScreenController {
    
    @FXML
    private Button muteButton;

    @FXML
    public void initialize() {
        System.out.println("Credits screen loaded");
        SoundManager.getInstance().setupMuteButton(muteButton);
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
