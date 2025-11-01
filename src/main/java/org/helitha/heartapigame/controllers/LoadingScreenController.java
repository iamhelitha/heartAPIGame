package org.helitha.heartapigame.controllers;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import org.helitha.heartapigame.managers.ScreenManager;

import java.net.URL;
import java.util.ResourceBundle;

public class LoadingScreenController implements Initializable {

    @FXML private ProgressBar progressBar;
    @FXML private Label statusLabel;

    private Task<Void> loadingTask;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadingTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Total target duration ~1360ms (increased by 1 second)
                updateMessage("Loading game logic...");
                updateProgress(0, 100);
                Thread.sleep(260);

                updateMessage("Initializing services...");
                updateProgress(30, 100);
                Thread.sleep(390);

                updateMessage("Loading assets...");
                updateProgress(60, 100);
                Thread.sleep(390);

                updateMessage("Finalizing setup...");
                updateProgress(90, 100);
                Thread.sleep(290);

                updateMessage("Done!");
                updateProgress(100, 100);
                Thread.sleep(30);
                return null;
            }
        };

        // Bind properties
        progressBar.progressProperty().bind(loadingTask.progressProperty());
        statusLabel.textProperty().bind(loadingTask.messageProperty());

        // Switch screen on success
        loadingTask.setOnSucceeded(e -> {
            ScreenManager.getInstance().switchScene("LoginScreen.fxml");
        });

        // Start the task on a background daemon thread
        Thread t = new Thread(loadingTask);
        t.setDaemon(true);
        t.start();
    }
}
