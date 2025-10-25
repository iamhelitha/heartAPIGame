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
    public void initialize() {
        // Initialize controller
    }

    @FXML
    private void handleLogin() {
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
        // Switch to registration screen
        ScreenManager.getInstance().switchScene("RegisterScreen.fxml");
    }

    @FXML
    private void handlePlayAsGuest() {
        // Create guest user in GameSession
        GameSession.getInstance().createGuestUser();

        System.out.println("Playing as guest: " + GameSession.getInstance().getDisplayName());

        // Switch to HomeScreen
        ScreenManager.getInstance().switchScene("HomeScreen.fxml");
    }
}
