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
        String displayName = GameSession.getInstance().getDisplayName();
        if (displayName != null) {
            userNameLabel.setText("Welcome, " + displayName + "!");
        }

        SoundManager.getInstance().playBackgroundMusic();
        SoundManager.getInstance().setupMuteButton(muteButton);

        setupButtonSounds(playButton);
        setupButtonSounds(leaderboardButton);
        setupButtonSounds(creditsButton);
        setupButtonSounds(logoutButton);
    }

    private void setupButtonSounds(Button button) {
        button.setOnMousePressed(event -> SoundManager.getInstance().playClickSound());
    }

    @FXML
    private void handlePlay() {
        SoundManager.getInstance().playClickSound();
        System.out.println("Starting game for " + GameSession.getInstance().getDisplayName());
        ScreenManager.getInstance().switchScene("DifficultyScreen.fxml");
    }

    @FXML
    private void handleLeaderboard() {
        SoundManager.getInstance().playClickSound();
        System.out.println("Opening leaderboard");
        ScreenManager.getInstance().switchScene("LeaderboardScreen.fxml");
    }

    @FXML
    private void handleCredits() {
        SoundManager.getInstance().playClickSound();
        System.out.println("Opening credits");
        ScreenManager.getInstance().switchScene("CreditsScreen.fxml");
    }

    @FXML
    private void handleLogout() {
        SoundManager.getInstance().playClickSound();
        SoundManager.getInstance().stopBackgroundMusic();
        GameSession.getInstance().clearSession();
        System.out.println("Logged out");
        ScreenManager.getInstance().switchScene("LoginScreen.fxml");
    }
}
