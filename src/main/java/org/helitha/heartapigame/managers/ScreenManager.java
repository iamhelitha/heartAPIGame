package org.helitha.heartapigame.managers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.helitha.heartapigame.Main;

import java.io.IOException;
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

    public void switchScene(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlFile));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);

            scene.getStylesheets().add(
                Main.class.getResource("css/global-styles.css").toExternalForm()
            );
            
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
