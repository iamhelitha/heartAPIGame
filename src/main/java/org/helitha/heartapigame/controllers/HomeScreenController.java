package org.helitha.heartapigame.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.helitha.heartapigame.managers.GameSession;
import org.helitha.heartapigame.managers.ScreenManager;

import java.net.URL;

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

    private MediaPlayer backgroundMusicPlayer;
    private AudioClip clickSound;
    private AudioClip hoverSound;

    @FXML
    public void initialize() {
        // Display the current user's name
        String displayName = GameSession.getInstance().getDisplayName();
        if (displayName != null) {
            userNameLabel.setText("Welcome, " + displayName + "!");
        }

        // Load and play background music
        loadBackgroundMusic();

        // Load sound effects
        loadSoundEffects();

        // Add hover and click sound effects to all buttons
        setupButtonSounds(playButton);
        setupButtonSounds(leaderboardButton);
        setupButtonSounds(creditsButton);
        setupButtonSounds(logoutButton);
    }

    private void loadBackgroundMusic() {
        try {
            URL musicResource = getClass().getResource("/background_music.mp3");
            if (musicResource != null) {
                Media media = new Media(musicResource.toString());
                backgroundMusicPlayer = new MediaPlayer(media);
                backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop forever
                backgroundMusicPlayer.setVolume(0.3); // Set volume to 30%
                backgroundMusicPlayer.play();
                System.out.println("Background music started");
            } else {
                System.out.println("Background music file not found");
            }
        } catch (Exception e) {
            System.err.println("Error loading background music: " + e.getMessage());
        }
    }

    private void loadSoundEffects() {
        try {
            URL clickResource = getClass().getResource("/click.wav");
            if (clickResource != null) {
                clickSound = new AudioClip(clickResource.toString());
                clickSound.setVolume(0.5);
            } else {
                System.out.println("click.wav not found");
            }

            URL hoverResource = getClass().getResource("/hover.wav");
            if (hoverResource != null) {
                hoverSound = new AudioClip(hoverResource.toString());
                hoverSound.setVolume(0.3);
            } else {
                System.out.println("hover.wav not found");
            }
        } catch (Exception e) {
            System.err.println("Error loading sound effects: " + e.getMessage());
        }
    }

    private void setupButtonSounds(Button button) {
        // Play hover sound when mouse enters
        button.setOnMouseEntered(event -> {
            if (hoverSound != null) {
                hoverSound.play();
            }
        });

        // Play click sound when mouse is pressed
        button.setOnMousePressed(event -> {
            if (clickSound != null) {
                clickSound.play();
            }
        });
    }

    @FXML
    private void handlePlay() {
        System.out.println("Starting game for " + GameSession.getInstance().getDisplayName());
        // Navigate to difficulty selection screen
        ScreenManager.getInstance().switchScene("DifficultyScreen.fxml");
    }

    @FXML
    private void handleLeaderboard() {
        System.out.println("Opening leaderboard");
        // Navigate to leaderboard screen
        ScreenManager.getInstance().switchScene("LeaderboardScreen.fxml");
    }

    @FXML
    private void handleCredits() {
        System.out.println("Opening credits");
        // Navigate to credits screen
        ScreenManager.getInstance().switchScene("CreditsScreen.fxml");
    }

    @FXML
    private void handleLogout() {
        // Stop background music
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }

        // Clear the session
        GameSession.getInstance().clearSession();
        System.out.println("Logged out");

        // Return to login screen
        ScreenManager.getInstance().switchScene("LoginScreen.fxml");
    }
}
