package org.helitha.heartapigame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.helitha.heartapigame.managers.ScreenManager;
import org.helitha.heartapigame.services.FirebaseService;

import java.io.IOException;

public class Main extends Application {
    // Window size constants used across the app
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;

    @Override
    public void start(Stage stage) throws IOException {
        // Initialize Firebase
        FirebaseService.getInstance().initialize();

        // Initialize ScreenManager with the primary stage
        new ScreenManager(stage);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LoadingScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setTitle("Heart API Game");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
