package org.helitha.heartapigame.managers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.helitha.heartapigame.Main;

import java.io.IOException;

/**
 * ScreenManager - Centralized navigation manager for all screens
 *
 * LOW COUPLING:
 * This class demonstrates low coupling by decoupling screen navigation from controllers:
 * - Controllers don't need to know about FXML loading or Scene management
 * - Controllers don't directly reference other controllers
 * - They just call ScreenManager.switchScene("SomeScreen.fxml")
 * - This makes controllers independent and easier to test/modify
 *
 * HIGH COHESION:
 * Single responsibility: Manage screen navigation and FXML loading
 * All navigation logic is centralized in one place
 *
 * DESIGN PRINCIPLE: Singleton Pattern
 * Provides a global access point for screen navigation throughout the app
 */
public class ScreenManager {
    private final Stage stage;
    private static ScreenManager instance;

    public ScreenManager(Stage stage) {
        this.stage = stage;
        instance = this;
    }

    public static ScreenManager getInstance() {
        return instance;
    }

    /**
     * Switch to a different screen by loading its FXML file
     *
     * LOW COUPLING:
     * Controllers call this method without knowing:
     * - How FXML files are loaded
     * - How scenes are created
     * - How the stage is managed
     */
    public void switchScene(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlFile));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load FXML file: " + fxmlFile);
        }
    }

    public Stage getStage() {
        return stage;
    }
}
