package org.helitha.heartapigame.managers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
public class GameManager {

    private static GameManager instance;

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final StringProperty difficulty = new SimpleStringProperty(EASY);
    private final IntegerProperty timerValue = new SimpleIntegerProperty(EASY_TIMER);

    public static final String EASY = "Easy";
    public static final String MEDIUM = "Medium";
    public static final String HARD = "Hard";

    public static final int EASY_TIMER = 45;
    public static final int MEDIUM_TIMER = 30;
    public static final int HARD_TIMER = 20;

    public static final int EASY_POINTS = 1;
    public static final int MEDIUM_POINTS = 3;
    public static final int HARD_POINTS = 5;

    private GameManager() {
        resetGame();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void resetGame() {
        this.score.set(0);
        this.difficulty.set(EASY);
        this.timerValue.set(EASY_TIMER);
    }

    public void setDifficulty(String diff) {
        this.difficulty.set(diff);
        switch (diff) {
            case EASY -> this.timerValue.set(EASY_TIMER);
            case MEDIUM -> this.timerValue.set(MEDIUM_TIMER);
            case HARD -> this.timerValue.set(HARD_TIMER);
            default -> this.timerValue.set(EASY_TIMER);
        }
    }

    public void addScore(int points) {
        this.score.set(this.score.get() + points);
    }

    public void incrementScore() {
        this.score.set(this.score.get() + 1);
    }

    public int getPointsForDifficulty() {
        return switch (difficulty.get()) {
            case EASY -> EASY_POINTS;
            case MEDIUM -> MEDIUM_POINTS;
            case HARD -> HARD_POINTS;
            default -> EASY_POINTS;
        };
    }

    public IntegerProperty scoreProperty() { return score; }
    public StringProperty difficultyProperty() { return difficulty; }
    public IntegerProperty timerValueProperty() { return timerValue; }

    public int getScore() { return score.get(); }
    public String getDifficulty() { return difficulty.get(); }
    public int getTimerValue() { return timerValue.get(); }

    public void setScore(int value) { score.set(value); }
    public void setTimerValue(int value) { timerValue.set(value); }
}
