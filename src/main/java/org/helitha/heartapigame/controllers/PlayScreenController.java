package org.helitha.heartapigame.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import javafx.scene.media.AudioClip;
import org.helitha.heartapigame.managers.GameManager;
import org.helitha.heartapigame.managers.GameSession;
import org.helitha.heartapigame.managers.ScreenManager;
import org.helitha.heartapigame.managers.SoundManager;
import org.helitha.heartapigame.models.GameData;
import org.helitha.heartapigame.services.ApiService;
import org.helitha.heartapigame.services.FirebaseService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * PlayScreenController - Main game screen controller
 *
 * EVENT-DRIVEN PROGRAMMING:
 * - User events: Button clicks (handleAnswer1-4), timer events (countdown)
 * - Time-driven events: Timeline fires every second to update timer
 * - Asynchronous events: API responses trigger UI updates via Platform.runLater()
 *
 * INTEROPERABILITY:
 * - Communicates with Heart Game API (PHP server) via HTTP GET requests
 * - Parses JSON responses and displays images/data from external service
 * - The "contract" is the JSON format: {"question": "url", "solution": hearts, "carrots": count}
 *
 * LOW COUPLING:
 * - Doesn't directly depend on other controllers
 * - Uses ApiService for API communication (abstraction)
 * - Uses GameManager for state management
 * - Uses ScreenManager for navigation
 */
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

    private Timeline countdown;
    private int timeRemaining;
    private int correctAnswer;
    private Random random;
    private boolean isAskingForHearts;

    // Audio feedback
    private AudioClip correctSound;
    private AudioClip incorrectSound;

    @FXML
    public void initialize() {
        random = new Random();

        // Set initial timer value based on difficulty
        timeRemaining = GameManager.getInstance().getTimerValue();
        updateTimeLabel();
        updateScoreLabel();

        // Load sound effects
        loadSoundEffects();

        // Update mute button
        updateMuteButton();

        System.out.println("========== GAME STARTED ==========");
        System.out.println("Play screen loaded");
        System.out.println("Difficulty: " + GameManager.getInstance().getDifficulty());
        System.out.println("Timer: " + timeRemaining + " seconds");
        System.out.println("Points per correct answer: " + GameManager.getInstance().getPointsForDifficulty());
        System.out.println("==================================");

        // Initialize countdown timer (EVENT-DRIVEN: Time-based events)
        // Timer will start when first image is loaded
        initializeCountdownTimer();

        // Load first round
        loadNewRound();
    }

    /**
     * Load audio feedback sounds for correct/incorrect answers
     */
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

    /**
     * EVENT-DRIVEN: Initialize the countdown timer (but don't start it yet)
     */
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

    /**
     * Start or resume the countdown timer
     */
    private void startCountdownTimer() {
        if (countdown != null) {
            countdown.play();
        }
    }

    /**
     * Pause the countdown timer
     */
    private void pauseCountdownTimer() {
        if (countdown != null) {
            countdown.pause();
        }
    }

    private void updateTimeLabel() {
        timeLabel.setText("Time: " + timeRemaining);

        // Change color based on remaining time
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

    /**
     * INTEROPERABILITY: Asynchronous API call to external PHP-based Heart Game API
     * Load new round by fetching data from API
     */
    private void loadNewRound() {
        // Disable buttons while loading
        setButtonsEnabled(false);

        // Pause timer while loading
        pauseCountdownTimer();

        // Fetch data in background thread (EVENT-DRIVEN: Asynchronous event)
        new Thread(() -> {
            try {
                // Call API to get game data (INTEROPERABILITY)
                GameData gameData = ApiService.getInstance().fetchGameData();

                System.out.println("Loaded game data: " + gameData);

                // Update UI on JavaFX thread (EVENT-DRIVEN: Response event)
                Platform.runLater(() -> {
                    displayRound(gameData);
                });

            } catch (Exception e) {
                System.err.println("Error loading game data: " + e.getMessage());
                e.printStackTrace();

                // Show error and return to difficulty screen
                Platform.runLater(() -> {
                    countdown.stop();
                    Notifications.create()
                        .title("Connection Error")
                        .text("Failed to connect to game API. Please try again.")
                        .showError();
                    ScreenManager.getInstance().switchScene("DifficultyScreen.fxml");
                });
            }
        }).start();
    }

    private void displayRound(GameData gameData) {
        // Load image from URL
        try {
            Image image = new Image(gameData.question(), true); // true for background loading
            imageView.setImage(image);

            // Add listener to start timer only when image is loaded
            image.progressProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.doubleValue() >= 1.0) {
                    // Image fully loaded, start the timer
                    System.out.println("Image loaded successfully, starting timer");
                    startCountdownTimer();
                }
            });

            // Handle image loading errors
            image.errorProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    System.err.println("Error loading image from: " + gameData.question());
                    // Start timer anyway to avoid blocking gameplay
                    startCountdownTimer();
                }
            });

        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
            // Start timer anyway to avoid blocking gameplay
            startCountdownTimer();
        }

        // Randomly decide to ask for HEARTS or CARROTS
        // This is the core challenge: players must read the prompt carefully!
        isAskingForHearts = random.nextBoolean();

        if (isAskingForHearts) {
            questionLabel.setText("How many HEARTS ❤?");
            correctAnswer = gameData.solution();
        } else {
            questionLabel.setText("How many CARROTS 🥕?");
            correctAnswer = gameData.carrots();
        }

        System.out.println("Question: " + (isAskingForHearts ? "HEARTS" : "CARROTS"));
        System.out.println("Correct answer: " + correctAnswer);

        // Generate answer options
        List<Integer> answers = generateAnswerOptions(correctAnswer);

        // Set button texts
        button1.setText(String.valueOf(answers.get(0)));
        button2.setText(String.valueOf(answers.get(1)));
        button3.setText(String.valueOf(answers.get(2)));
        button4.setText(String.valueOf(answers.get(3)));

        // Enable buttons
        setButtonsEnabled(true);
    }

    private List<Integer> generateAnswerOptions(int correct) {
        List<Integer> options = new ArrayList<>();
        options.add(correct);

        // Generate 3 random wrong answers
        while (options.size() < 4) {
            int wrongAnswer = random.nextInt(15); // Random number 0-14
            if (!options.contains(wrongAnswer)) {
                options.add(wrongAnswer);
            }
        }

        // Shuffle the options
        Collections.shuffle(options);

        return options;
    }

    private void setButtonsEnabled(boolean enabled) {
        button1.setDisable(!enabled);
        button2.setDisable(!enabled);
        button3.setDisable(!enabled);
        button4.setDisable(!enabled);
    }

    // EVENT-DRIVEN: User click events
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
        if (selectedAnswer == correctAnswer) {
            // Correct answer!
            System.out.println("Correct answer!");

            // Play correct sound
            if (correctSound != null) {
                correctSound.play();
            }

            // Calculate points based on difficulty
            int points = GameManager.getInstance().getPointsForDifficulty();
            GameManager.getInstance().addScore(points);
            updateScoreLabel();

            System.out.println("✓ Correct! Added " + points + " points (" + GameManager.getInstance().getDifficulty() + " mode). Total score: " + GameManager.getInstance().getScore());

            // Show visual feedback
            Notifications.create()
                .title("Correct! ✓")
                .text("+" + points + " points")
                .hideAfter(Duration.seconds(1.5))
                .showInformation();

            // Load next round
            loadNewRound();

        } else {
            // Wrong answer
            System.out.println("✗ Wrong answer! Correct was: " + correctAnswer);

            // Play incorrect sound
            if (incorrectSound != null) {
                incorrectSound.play();
            }

            // Subtract points (optional - only for Medium and Hard)
            if (!GameManager.EASY.equals(GameManager.getInstance().getDifficulty())) {
                int penalty = 1;
                int currentScore = GameManager.getInstance().getScore();
                GameManager.getInstance().setScore(Math.max(0, currentScore - penalty));
                updateScoreLabel();
                System.out.println("✗ Lost " + penalty + " point (" + GameManager.getInstance().getDifficulty() + " mode). Total score: " + GameManager.getInstance().getScore());

                // Show visual feedback with penalty
                Notifications.create()
                    .title("Incorrect ✗")
                    .text("Correct answer: " + correctAnswer + " (-" + penalty + " point)")
                    .hideAfter(Duration.seconds(2))
                    .showWarning();
            } else {
                // Easy mode - no penalty, just show correct answer
                System.out.println("No penalty in Easy mode. Total score: " + GameManager.getInstance().getScore());
                Notifications.create()
                    .title("Incorrect ✗")
                    .text("Correct answer: " + correctAnswer)
                    .hideAfter(Duration.seconds(2))
                    .showWarning();
            }

            // Still load next round (player continues)
            loadNewRound();
        }
    }

    private void handleGameOver() {
        // Stop the timer
        countdown.stop();

        System.out.println("Game Over!");
        System.out.println("Final Score: " + GameManager.getInstance().getScore());

        // Save score to Firebase (in background) - INTEROPERABILITY
        saveScoreToFirebase();

        // Switch to leaderboard screen
        ScreenManager.getInstance().switchScene("LeaderboardScreen.fxml");
    }

    /**
     * INTEROPERABILITY: Save score to Firebase Firestore (Google's cloud database)
     */
    private void saveScoreToFirebase() {
        // Save score in background thread
        new Thread(() -> {
            try {
                String playerName = GameSession.getInstance().getDisplayName();
                int finalScore = GameManager.getInstance().getScore();
                String difficulty = GameManager.getInstance().getDifficulty();

                System.out.println("Saving score to Firebase:");
                System.out.println("Player: " + playerName);
                System.out.println("Score: " + finalScore);
                System.out.println("Difficulty: " + difficulty);

                // Save score to Firebase Firestore
                FirebaseService.getInstance().saveScore(playerName, finalScore);

            } catch (Exception e) {
                System.err.println("Error saving score to Firebase: " + e.getMessage());
            }
        }).start();
    }

    @FXML
    private void handleExit() {
        // Stop the timer
        if (countdown != null) {
            countdown.stop();
        }

        System.out.println("Exiting game. Final score: " + GameManager.getInstance().getScore());

        // Return to home screen
        ScreenManager.getInstance().switchScene("HomeScreen.fxml");
    }

    @FXML
    private void handleHomeButton(ActionEvent event) {
        // First, stop the timer
        if (countdown != null) {
            countdown.stop();
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit Game");
        alert.setHeaderText("Are you sure you want to quit?");
        alert.setContentText("Your current score and progress will be lost.");

        // Show the alert and wait for a response
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User clicked OK. Go home.
            ScreenManager sm = new ScreenManager((Stage) ((Node) event.getSource()).getScene().getWindow());
            sm.switchScene("HomeScreen.fxml");
        } else {
            // User clicked Cancel or closed the dialog. Resume the game.
            if (countdown != null) {
                countdown.play();
            }
        }
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
}
