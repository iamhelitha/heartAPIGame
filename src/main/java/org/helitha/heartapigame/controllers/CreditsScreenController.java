package org.helitha.heartapigame.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.helitha.heartapigame.managers.ScreenManager;

public class CreditsScreenController {
    
    @FXML
    public void initialize() {
        System.out.println("Credits screen loaded");
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
