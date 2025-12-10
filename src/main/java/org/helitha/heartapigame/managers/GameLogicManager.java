package org.helitha.heartapigame.managers;

import org.helitha.heartapigame.models.GameData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
public class GameLogicManager {

    private static GameLogicManager instance;
    private final Random random;
    
    private int correctAnswer;
    private boolean askingForHearts;

    private GameLogicManager() {
        this.random = new Random();
    }

    public static GameLogicManager getInstance() {
        if (instance == null) {
            instance = new GameLogicManager();
        }
        return instance;
    }

    public String processGameData(GameData gameData) {
        askingForHearts = random.nextBoolean();
        
        if (askingForHearts) {
            correctAnswer = gameData.solution();
            return "How many HEARTS ❤?";
        } else {
            correctAnswer = gameData.carrots();
            return "How many CARROTS 🥕?";
        }
    }

    public List<Integer> generateAnswerOptions() {
        List<Integer> options = new ArrayList<>();
        options.add(correctAnswer);

        while (options.size() < 4) {
            int wrongAnswer = random.nextInt(15);
            if (!options.contains(wrongAnswer)) {
                options.add(wrongAnswer);
            }
        }

        Collections.shuffle(options);
        return options;
    }

    public boolean checkAnswer(int selectedAnswer) {
        return selectedAnswer == correctAnswer;
    }

    public int calculatePoints() {
        return GameManager.getInstance().getPointsForDifficulty();
    }

    public int calculatePenalty() {
        String difficulty = GameManager.getInstance().getDifficulty();
        return GameManager.EASY.equals(difficulty) ? 0 : 1;
    }

    public void applyCorrectAnswer() {
        int points = calculatePoints();
        GameManager.getInstance().addScore(points);
    }

    public void applyWrongAnswer() {
        int penalty = calculatePenalty();
        if (penalty > 0) {
            int currentScore = GameManager.getInstance().getScore();
            GameManager.getInstance().setScore(Math.max(0, currentScore - penalty));
        }
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public boolean isAskingForHearts() {
        return askingForHearts;
    }
}
