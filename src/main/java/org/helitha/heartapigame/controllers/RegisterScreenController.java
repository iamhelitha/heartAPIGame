package org.helitha.heartapigame.controllers;

import com.google.firebase.auth.UserRecord;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.helitha.heartapigame.services.FirebaseService;
import org.helitha.heartapigame.managers.ScreenManager;
import org.helitha.heartapigame.managers.SoundManager;

public class RegisterScreenController {

    @FXML
    private TextField displayNameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerButton;

    @FXML
    private Hyperlink loginLink;

    @FXML
    private Button muteButton;

    @FXML
    public void initialize() {
        // Start background music
        SoundManager.getInstance().playBackgroundMusic();
        
        // Update mute button text based on current state
        updateMuteButton();
    }
    
    @FXML
    private void handleMute() {
        SoundManager.getInstance().playClickSound();
        SoundManager.getInstance().toggleMute();
        updateMuteButton();
    }
    
    private void updateMuteButton() {
        if (muteButton != null) {
            muteButton.setText(SoundManager.getInstance().isMusicEnabled() ? "🔊" : "🔇");
        }
    }

    @FXML
    private void handleRegister() {
        SoundManager.getInstance().playClickSound();
        String displayName = displayNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        // Validate input
        if (displayName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println("Please fill in all fields");
            return;
        }

        // Call Firebase registration
        UserRecord userRecord = FirebaseService.getInstance().registerUser(email, password, displayName);

        if (userRecord != null) {
            System.out.println("Registration successful! User ID: " + userRecord.getUid());
            System.out.println("Please login with your credentials.");

            // Switch to login screen
            ScreenManager.getInstance().switchScene("LoginScreen.fxml");
        } else {
            System.out.println("Registration failed. Please try again.");
        }
    }

    @FXML
    private void handleBackToLogin() {
        SoundManager.getInstance().playClickSound();
        // Switch back to login screen
        ScreenManager.getInstance().switchScene("LoginScreen.fxml");
    }
}
