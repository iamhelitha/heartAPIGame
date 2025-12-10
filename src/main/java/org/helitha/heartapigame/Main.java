package org.helitha.heartapigame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.helitha.heartapigame.managers.ScreenManager;
import org.helitha.heartapigame.services.FirebaseService;

import java.io.IOException;

public class Main extends Application {
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            Font customFont = Font.loadFont(
                getClass().getResourceAsStream("fonts/PressStart2P-Regular.ttf"), 
                12
            );
            if (customFont != null) {
                System.out.println("Custom font loaded successfully: " + customFont.getFamily());
            } else {
                System.err.println("Failed to load custom font!");
            }
        } catch (Exception e) {
            System.err.println("Error loading custom font: " + e.getMessage());
            e.printStackTrace();
        }

        FirebaseService.getInstance().initialize();

        new ScreenManager(stage);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LoadingScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("css/global-styles.css").toExternalForm());
        stage.setTitle("Heart API Game");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
