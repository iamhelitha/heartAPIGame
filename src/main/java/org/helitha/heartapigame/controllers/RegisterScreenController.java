package org.helitha.heartapigame.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.helitha.heartapigame.services.FirebaseService;
import org.helitha.heartapigame.managers.ScreenManager;
import org.helitha.heartapigame.managers.SoundManager;

import java.util.regex.Pattern;

public class RegisterScreenController {

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

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
        SoundManager.getInstance().playBackgroundMusic();
        SoundManager.getInstance().setupMuteButton(muteButton);
    }

    @FXML
    private void handleRegister() {
        SoundManager.getInstance().playClickSound();
        String displayName = displayNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (displayName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println("Please fill in all fields");
            return;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            System.out.println("Please enter a valid email address");
            return;
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {
            System.out.println("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long");
            return;
        }

        var userRecord = FirebaseService.getInstance().registerUser(email, password, displayName);

        if (userRecord != null) {
            System.out.println("Registration successful! User ID: " + userRecord.getUid());
            System.out.println("Please login with your credentials.");
            ScreenManager.getInstance().switchScene("LoginScreen.fxml");
        } else {
            System.out.println("Registration failed. Please try again.");
        }
    }

    @FXML
    private void handleBackToLogin() {
        SoundManager.getInstance().playClickSound();
        ScreenManager.getInstance().switchScene("LoginScreen.fxml");
    }
}
