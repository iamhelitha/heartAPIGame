package org.helitha.heartapigame.controllers;

import com.google.firebase.auth.UserRecord;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.helitha.heartapigame.services.FirebaseService;
import org.helitha.heartapigame.managers.GameSession;
import org.helitha.heartapigame.managers.ScreenManager;
import org.helitha.heartapigame.managers.SoundManager;

public class LoginScreenController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink registerLink;

    @FXML
    private Button guestButton;

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
    private void handleLogin() {
        SoundManager.getInstance().playClickSound();
        String email = emailField.getText();
        String password = passwordField.getText();

        // Validate input
        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Please fill in all fields");
            return;
        }

        // Call Firebase login
        UserRecord userRecord = FirebaseService.getInstance().loginUser(email, password);

        if (userRecord != null) {
            // Store user info in GameSession
            GameSession.getInstance().setUser(
                userRecord.getUid(),
                userRecord.getDisplayName() != null ? userRecord.getDisplayName() : email
            );

            System.out.println("Login successful! Welcome " + GameSession.getInstance().getDisplayName());

            // Switch to HomeScreen
            ScreenManager.getInstance().switchScene("HomeScreen.fxml");
        } else {
            System.out.println("Login failed. Please check your credentials.");
        }
    }

    @FXML
    private void handleRegister() {
        SoundManager.getInstance().playClickSound();
        // Switch to registration screen
        ScreenManager.getInstance().switchScene("RegisterScreen.fxml");
    }

    @FXML
    private void handlePlayAsGuest() {
        SoundManager.getInstance().playClickSound();
        // Create guest user in GameSession
        GameSession.getInstance().createGuestUser();

        System.out.println("Playing as guest: " + GameSession.getInstance().getDisplayName());

        // Switch to HomeScreen
        ScreenManager.getInstance().switchScene("HomeScreen.fxml");
    }
}
