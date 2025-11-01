package org.helitha.heartapigame.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.helitha.heartapigame.managers.GameSession;
import org.helitha.heartapigame.managers.ScreenManager;
import org.helitha.heartapigame.managers.SoundManager;

public class HomeScreenController {

    @FXML
    private Label userNameLabel;

    @FXML
    private Button playButton;

    @FXML
    private Button leaderboardButton;

    @FXML
    private Button creditsButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button muteButton;

    @FXML
    public void initialize() {
        // Display the current user's name
        String displayName = GameSession.getInstance().getDisplayName();
        if (displayName != null) {
            userNameLabel.setText("Welcome, " + displayName + "!");
        }

        // Start background music using SoundManager
        SoundManager.getInstance().playBackgroundMusic();

        // Update mute button text
        updateMuteButton();

        // Add click sound effects to all buttons
        setupButtonSounds(playButton);
        setupButtonSounds(leaderboardButton);
        setupButtonSounds(creditsButton);
        setupButtonSounds(logoutButton);
    }

    private void updateMuteButton() {
        if (muteButton != null) {
            muteButton.setText(SoundManager.getInstance().isMuted() ? "🔇" : "🔊");
        }
    }

    private void setupButtonSounds(Button button) {
        // Play click sound when mouse is pressed
        button.setOnMousePressed(event -> {
            SoundManager.getInstance().playClickSound();
        });
    }

    @FXML
    private void handlePlay() {
        SoundManager.getInstance().playClickSound();
        System.out.println("Starting game for " + GameSession.getInstance().getDisplayName());
        // Navigate to difficulty selection screen
        ScreenManager.getInstance().switchScene("DifficultyScreen.fxml");
    }

    @FXML
    private void handleLeaderboard() {
        SoundManager.getInstance().playClickSound();
        System.out.println("Opening leaderboard");
        // Navigate to leaderboard screen
        ScreenManager.getInstance().switchScene("LeaderboardScreen.fxml");
    }

    @FXML
    private void handleCredits() {
        SoundManager.getInstance().playClickSound();
        System.out.println("Opening credits");
        // Navigate to credits screen
        ScreenManager.getInstance().switchScene("CreditsScreen.fxml");
    }

    @FXML
    private void handleLogout() {
        SoundManager.getInstance().playClickSound();

        // Stop background music
        SoundManager.getInstance().stopBackgroundMusic();

        // Clear the session
        GameSession.getInstance().clearSession();
        System.out.println("Logged out");

        // Return to login screen
        ScreenManager.getInstance().switchScene("LoginScreen.fxml");
    }

    @FXML
    private void handleMute() {
        SoundManager.getInstance().toggleMute();
        updateMuteButton();
    }
}
