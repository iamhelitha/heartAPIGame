package org.helitha.heartapigame.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.helitha.heartapigame.managers.AsyncManager;
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
        int finalScore = GameManager.getInstance().getScore();
        String difficulty = GameManager.getInstance().getDifficulty();
        String playerName = GameSession.getInstance().getDisplayName();

        finalScoreLabel.setText(playerName + "'s Score: " + finalScore + " (" + difficulty + ")");
        System.out.println("Leaderboard - Player: " + playerName + ", Score: " + finalScore);

        rankColumn.setCellValueFactory(cellData -> cellData.getValue().rankProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        scoreColumn.setCellValueFactory(cellData -> cellData.getValue().scoreProperty().asObject());

        SoundManager.getInstance().setupMuteButton(muteButton);
        loadAllScoresSorted();
    }

    private void loadAllScoresSorted() {
        AsyncManager.getInstance().runAsync(
            () -> {
                List<LeaderboardEntry> allScores = FirebaseService.getInstance().getAllScores();
                allScores.sort(Comparator.comparingInt(LeaderboardEntry::getScore).reversed());
                return allScores;
            },
            allScores -> {
                ObservableList<LeaderboardRow> rows = FXCollections.observableArrayList();
                for (int i = 0; i < allScores.size(); i++) {
                    LeaderboardEntry entry = allScores.get(i);
                    rows.add(new LeaderboardRow(i + 1, entry.getUsername(), entry.getScore()));
                }
                leaderboardTable.setItems(rows);
            },
            error -> System.err.println("Error loading leaderboard: " + error.getMessage())
        );
    }

    @FXML
    private void handleBackToHome() {
        ScreenManager.getInstance().switchScene("HomeScreen.fxml");
    }

    @FXML
    private void handleHomeButton() {
        SoundManager.getInstance().playClickSound();
        ScreenManager.getInstance().switchScene("HomeScreen.fxml");
    }

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
