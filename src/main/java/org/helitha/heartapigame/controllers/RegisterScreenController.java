package org.helitha.heartapigame.controllers;

import com.google.firebase.auth.UserRecord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.helitha.heartapigame.services.FirebaseService;
import org.helitha.heartapigame.managers.ScreenManager;

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
    public void initialize() {
        // Initialize controller
    }

    @FXML
    private void handleRegister() {
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
        // Switch back to login screen
        ScreenManager.getInstance().switchScene("LoginScreen.fxml");
    }

    @FXML
    private void handleHomeButton(ActionEvent event) {
        ScreenManager sm = new ScreenManager((Stage) ((Node) event.getSource()).getScene().getWindow());
        sm.switchScene("HomeScreen.fxml");
    }
}
