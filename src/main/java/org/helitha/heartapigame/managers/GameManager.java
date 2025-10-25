package org.helitha.heartapigame.managers;

/**
 * GameManager - Manages the core game state and configuration.
 *
 * DESIGN PRINCIPLE: High Cohesion
 * This class has a single, well-defined responsibility: managing game state (score, difficulty, timer).
 * All game-state-related logic is encapsulated here, making the class highly cohesive.
 *
 * DESIGN PRINCIPLE: Singleton Pattern
 * Ensures only one instance of game state exists throughout the application,
 * providing a global access point for all controllers.
 */
public class GameManager {

    private static GameManager instance;

    private int score;
    private String difficulty;
    private int timerValue;

    // Difficulty constants
    public static final String EASY = "Easy";
    public static final String MEDIUM = "Medium";
    public static final String HARD = "Hard";

    // Timer values for each difficulty (in seconds) - aligned with project requirements
    public static final int EASY_TIMER = 45;      // 45 seconds for Easy
    public static final int MEDIUM_TIMER = 30;    // 30 seconds for Medium
    public static final int HARD_TIMER = 20;      // 20 seconds for Hard

    // Points awarded for correct answers based on difficulty
    public static final int EASY_POINTS = 1;
    public static final int MEDIUM_POINTS = 3;
    public static final int HARD_POINTS = 5;

    private GameManager() {
        // Private constructor for singleton
        resetGame();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /**
     * Reset game to initial state
     */
    public void resetGame() {
        this.score = 0;
        this.difficulty = EASY;
        this.timerValue = EASY_TIMER;
    }

    /**
     * Set difficulty and corresponding timer value
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;

        // Set timer based on difficulty
        switch (difficulty) {
            case EASY:
                this.timerValue = EASY_TIMER;
                break;
            case MEDIUM:
                this.timerValue = MEDIUM_TIMER;
                break;
            case HARD:
                this.timerValue = HARD_TIMER;
                break;
            default:
                this.timerValue = EASY_TIMER;
        }
    }

    /**
     * Add points to score
     */
    public void addScore(int points) {
        this.score += points;
    }

    /**
     * Increment score by 1
     */
    public void incrementScore() {
        this.score++;
    }

    /**
     * Get points for current difficulty level
     */
    public int getPointsForDifficulty() {
        switch (difficulty) {
            case EASY:
                return EASY_POINTS;
            case MEDIUM:
                return MEDIUM_POINTS;
            case HARD:
                return HARD_POINTS;
            default:
                return EASY_POINTS;
        }
    }

    // Getters
    public int getScore() {
        return score;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getTimerValue() {
        return timerValue;
    }

    // Setters
    public void setScore(int score) {
        this.score = score;
    }

    public void setTimerValue(int timerValue) {
        this.timerValue = timerValue;
    }
}
