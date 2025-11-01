package org.helitha.heartapigame.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.helitha.heartapigame.managers.GameManager;
import org.helitha.heartapigame.managers.GameSession;
import org.helitha.heartapigame.managers.ScreenManager;
import org.helitha.heartapigame.managers.SoundManager;
import org.helitha.heartapigame.models.LeaderboardEntry;
import org.helitha.heartapigame.services.FirebaseService;

import java.util.Comparator;
import java.util.List;

public class LeaderboardScreenController {

    @FXML
    private Label finalScoreLabel;

    @FXML
    private TableView<LeaderboardRow> leaderboardTable;

    @FXML
    private TableColumn<LeaderboardRow, Integer> rankColumn;

    @FXML
    private TableColumn<LeaderboardRow, String> nameColumn;

    @FXML
    private TableColumn<LeaderboardRow, Integer> scoreColumn;

    @FXML
    private Button muteButton;

    @FXML
    public void initialize() {
        // Display the final score
        int finalScore = GameManager.getInstance().getScore();
        String difficulty = GameManager.getInstance().getDifficulty();
        String playerName = GameSession.getInstance().getDisplayName();

        finalScoreLabel.setText(playerName + "'s Score: " + finalScore + " (" + difficulty + ")");

        System.out.println("Leaderboard screen loaded");
        System.out.println("Player: " + playerName + ", Score: " + finalScore + ", Difficulty: " + difficulty);

        // Set up table columns
        rankColumn.setCellValueFactory(cellData -> cellData.getValue().rankProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        scoreColumn.setCellValueFactory(cellData -> cellData.getValue().scoreProperty().asObject());

        // Update mute button
        updateMuteButton();

        // Load all scores from Firebase and sort on the application side
        loadAllScoresSorted();
    }

    private void updateMuteButton() {
        if (muteButton != null) {
            muteButton.setText(SoundManager.getInstance().isMuted() ? "🔇" : "🔊");
        }
    }

    @FXML
    private void handleMute() {
        SoundManager.getInstance().toggleMute();
        updateMuteButton();
    }

    private void loadAllScoresSorted() {
        // Load scores in background thread
        new Thread(() -> {
            try {
                List<LeaderboardEntry> allScores = FirebaseService.getInstance().getAllScores();

                // Sort client-side by score descending
                allScores.sort(Comparator.comparingInt(LeaderboardEntry::getScore).reversed());

                // Convert to table rows with rank
                ObservableList<LeaderboardRow> rows = FXCollections.observableArrayList();
                for (int i = 0; i < allScores.size(); i++) {
                    LeaderboardEntry entry = allScores.get(i);
                    rows.add(new LeaderboardRow(i + 1, entry.getUsername(), entry.getScore()));
                }

                // Update UI on JavaFX thread
                Platform.runLater(() -> leaderboardTable.setItems(rows));

            } catch (Exception e) {
                System.err.println("Error loading leaderboard: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    private void handleBackToHome() {
        // Return to home screen
        ScreenManager.getInstance().switchScene("HomeScreen.fxml");
    }

    @FXML
    private void handleHomeButton(ActionEvent event) {
        SoundManager.getInstance().playClickSound();
        ScreenManager.getInstance().switchScene("HomeScreen.fxml");
    }

    /**
     * Inner class to represent a row in the leaderboard table
     */
    public static class LeaderboardRow {
        private final SimpleIntegerProperty rank;
        private final SimpleStringProperty name;
        private final SimpleIntegerProperty score;

        public LeaderboardRow(int rank, String name, int score) {
            this.rank = new SimpleIntegerProperty(rank);
            this.name = new SimpleStringProperty(name);
            this.score = new SimpleIntegerProperty(score);
        }

        public SimpleIntegerProperty rankProperty() {
            return rank;
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public SimpleIntegerProperty scoreProperty() {
            return score;
        }
    }
}
