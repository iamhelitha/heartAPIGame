package org.helitha.heartapigame.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.helitha.heartapigame.models.AuthResult;
import org.helitha.heartapigame.services.FirebaseService;
import org.helitha.heartapigame.managers.GameSession;
import org.helitha.heartapigame.managers.ScreenManager;
import org.helitha.heartapigame.managers.SoundManager;

public class LoginScreenController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Hyperlink registerLink;
    @FXML private Button guestButton;
    @FXML private Button muteButton;

    @FXML
    public void initialize() {
        SoundManager.getInstance().playBackgroundMusic();
        SoundManager.getInstance().setupMuteButton(muteButton);

        if (GameSession.getInstance().hasSavedSession() && GameSession.getInstance().isLoggedIn()) {
            System.out.println("Auto-login: Welcome back " + GameSession.getInstance().getDisplayName());
            ScreenManager.getInstance().switchScene("HomeScreen.fxml");
        }
    }

    @FXML
    private void handleLogin() {
        SoundManager.getInstance().playClickSound();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Please fill in all fields");
            return;
        }

        AuthResult authResult = FirebaseService.getInstance().loginUser(email, password);

        if (authResult != null) {
            GameSession.getInstance().setUser(
                authResult.getLocalId(),
                authResult.getDisplayName() != null ? authResult.getDisplayName() : email
            );
            System.out.println("Login successful! Welcome " + GameSession.getInstance().getDisplayName());
            ScreenManager.getInstance().switchScene("HomeScreen.fxml");
        } else {
            System.out.println("Login failed. Please check your credentials.");
        }
    }
    
    @FXML
    private void handleRegister() {
        SoundManager.getInstance().playClickSound();
        ScreenManager.getInstance().switchScene("RegisterScreen.fxml");
    }

    @FXML
    private void handlePlayAsGuest() {
        SoundManager.getInstance().playClickSound();
        GameSession.getInstance().createGuestUser();
        System.out.println("Playing as guest: " + GameSession.getInstance().getDisplayName());
        ScreenManager.getInstance().switchScene("HomeScreen.fxml");
    }
}
