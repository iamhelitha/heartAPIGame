package org.helitha.heartapigame.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import javafx.scene.media.AudioClip;
import org.helitha.heartapigame.managers.AsyncManager;
import org.helitha.heartapigame.managers.GameLogicManager;
import org.helitha.heartapigame.managers.GameManager;
import org.helitha.heartapigame.managers.GameSession;
import org.helitha.heartapigame.managers.ScreenManager;
import org.helitha.heartapigame.managers.SoundManager;
import org.helitha.heartapigame.models.GameData;
import org.helitha.heartapigame.services.ApiService;
import org.helitha.heartapigame.services.FirebaseService;

import java.util.List;
import java.util.Optional;
public class PlayScreenController {

    @FXML
    private Label scoreLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label questionLabel;

    @FXML
    private ImageView imageView;

    @FXML
    private Button button1;

    @FXML
    private Button button2;

    @FXML
    private Button button3;

    @FXML
    private Button button4;

    @FXML
    private Button muteButton;

    @FXML
    private AnchorPane rootPane;

    private Timeline countdown;
    private int timeRemaining;
    private final GameLogicManager gameLogic = GameLogicManager.getInstance();

    private AudioClip correctSound;
    private AudioClip incorrectSound;

    @FXML
    public void initialize() {
        timeRemaining = GameManager.getInstance().getTimerValue();
        updateTimeLabel();
        updateScoreLabel();
        loadSoundEffects();
        SoundManager.getInstance().setupMuteButton(muteButton);

        System.out.println("========== GAME STARTED ==========");
        System.out.println("Difficulty: " + GameManager.getInstance().getDifficulty());
        System.out.println("Timer: " + timeRemaining + "s, Points/answer: " + GameManager.getInstance().getPointsForDifficulty());
        System.out.println("==================================");

        initializeCountdownTimer();
        loadNewRound();
    }

    private void loadSoundEffects() {
        try {
            var correctResource = getClass().getResource("/correct.wav");
            if (correctResource != null) {
                correctSound = new AudioClip(correctResource.toString());
                correctSound.setVolume(0.6);
            }

            var incorrectResource = getClass().getResource("/incorrect.wav");
            if (incorrectResource != null) {
                incorrectSound = new AudioClip(incorrectResource.toString());
                incorrectSound.setVolume(0.5);
            }
        } catch (Exception e) {
            System.err.println("Error loading sound effects: " + e.getMessage());
        }
    }

    private void initializeCountdownTimer() {
        countdown = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining--;
            updateTimeLabel();

            if (timeRemaining <= 0) {
                handleGameOver();
            }
        }));
        countdown.setCycleCount(Timeline.INDEFINITE);
    }

    private void startCountdownTimer() {
        if (countdown != null) {
            countdown.play();
        }
    }

    private void pauseCountdownTimer() {
        if (countdown != null) {
            countdown.pause();
        }
    }

    private void updateTimeLabel() {
        timeLabel.setText("Time: " + timeRemaining);
        if (timeRemaining <= 10) {
            timeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #f44336;");
        } else if (timeRemaining <= 20) {
            timeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #FF9800;");
        } else {
            timeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #4CAF50;");
        }
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + GameManager.getInstance().getScore());
    }

    private void loadNewRound() {
        setButtonsEnabled(false);
        pauseCountdownTimer();

        AsyncManager.getInstance().runAsync(
            () -> ApiService.getInstance().fetchGameData(),
            gameData -> {
                System.out.println("Loaded game data: " + gameData);
                displayRound(gameData);
            },
            error -> {
                System.err.println("Error loading game data: " + error.getMessage());
                countdown.stop();
                Notifications.create()
                    .title("Connection Error")
                    .text("Failed to connect to game API. Please try again.")
                    .showError();
                ScreenManager.getInstance().switchScene("DifficultyScreen.fxml");
            }
        );
    }

    private void displayRound(GameData gameData) {
        try {
            Image image = new Image(gameData.question(), true);
            imageView.setImage(image);

            image.progressProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal.doubleValue() >= 1.0) {
                    startCountdownTimer();
                }
            });

            image.errorProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    System.err.println("Error loading image");
                    startCountdownTimer();
                }
            });
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
            startCountdownTimer();
        }
        String questionText = gameLogic.processGameData(gameData);
        questionLabel.setText(questionText);
        updateBackgroundForQuestionType(gameLogic.isAskingForHearts());

        System.out.println("Question: " + (gameLogic.isAskingForHearts() ? "HEARTS" : "CARROTS"));
        System.out.println("Correct answer: " + gameLogic.getCorrectAnswer());

        List<Integer> answers = gameLogic.generateAnswerOptions();

        button1.setText(String.valueOf(answers.get(0)));
        button2.setText(String.valueOf(answers.get(1)));
        button3.setText(String.valueOf(answers.get(2)));
        button4.setText(String.valueOf(answers.get(3)));

        setButtonsEnabled(true);
    }

    private void setButtonsEnabled(boolean enabled) {
        button1.setDisable(!enabled);
        button2.setDisable(!enabled);
        button3.setDisable(!enabled);
        button4.setDisable(!enabled);
    }

    private void updateBackgroundForQuestionType(boolean isHearts) {
        rootPane.getStyleClass().removeAll("hearts-mode", "carrots-mode", "main-background");
        if (isHearts) {
            rootPane.getStyleClass().add("hearts-mode");
        } else {
            rootPane.getStyleClass().add("carrots-mode");
        }
    }

    @FXML
    private void handleAnswer1() {
        checkAnswer(Integer.parseInt(button1.getText()));
    }

    @FXML
    private void handleAnswer2() {
        checkAnswer(Integer.parseInt(button2.getText()));
    }

    @FXML
    private void handleAnswer3() {
        checkAnswer(Integer.parseInt(button3.getText()));
    }

    @FXML
    private void handleAnswer4() {
        checkAnswer(Integer.parseInt(button4.getText()));
    }

    private void checkAnswer(int selectedAnswer) {
        if (gameLogic.checkAnswer(selectedAnswer)) {
            if (correctSound != null) correctSound.play();
            
            gameLogic.applyCorrectAnswer();
            updateScoreLabel();
            
            int points = gameLogic.calculatePoints();
            System.out.println("✓ Correct! +" + points + " pts. Total: " + GameManager.getInstance().getScore());

            Notifications.create()
                .title("Correct! ✓")
                .text("+" + points + " points")
                .hideAfter(Duration.seconds(1.5))
                .showInformation();

            loadNewRound();
        } else {
            if (incorrectSound != null) incorrectSound.play();
            
            int penalty = gameLogic.calculatePenalty();
            gameLogic.applyWrongAnswer();
            updateScoreLabel();
            
            int correctAns = gameLogic.getCorrectAnswer();
            System.out.println("✗ Wrong! Correct: " + correctAns + ". Total: " + GameManager.getInstance().getScore());

            String notificationText = penalty > 0 
                ? "Correct answer: " + correctAns + " (-" + penalty + " point)"
                : "Correct answer: " + correctAns;
                
            Notifications.create()
                .title("Incorrect ✗")
                .text(notificationText)
                .hideAfter(Duration.seconds(2))
                .showWarning();

            loadNewRound();
        }
    }

    private void handleGameOver() {
        countdown.stop();

        System.out.println("Game Over!");
        System.out.println("Final Score: " + GameManager.getInstance().getScore());

        saveScoreToFirebase();

        ScreenManager.getInstance().switchScene("LeaderboardScreen.fxml");
    }

    private void saveScoreToFirebase() {
        String playerName = GameSession.getInstance().getDisplayName();
        int finalScore = GameManager.getInstance().getScore();
        String difficulty = GameManager.getInstance().getDifficulty();

        AsyncManager.getInstance().runAsync(() -> {
            System.out.println("Saving score - Player: " + playerName + ", Score: " + finalScore + ", Difficulty: " + difficulty);
            FirebaseService.getInstance().saveScore(playerName, finalScore);
        });
    }

    @FXML
    private void handleExit() {
        if (countdown != null) {
            countdown.stop();
        }

        System.out.println("Exiting game. Final score: " + GameManager.getInstance().getScore());

        ScreenManager.getInstance().switchScene("HomeScreen.fxml");
    }

    @FXML
    private void handleHomeButton() {
        if (countdown != null) {
            countdown.stop();
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit Game");
        alert.setHeaderText("Are you sure you want to quit?");
        alert.setContentText("Your current score and progress will be lost.");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            ScreenManager.getInstance().switchScene("HomeScreen.fxml");
        } else {
            if (countdown != null) {
                countdown.play();
            }
        }
    }
}
