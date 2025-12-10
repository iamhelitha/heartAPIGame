package org.helitha.heartapigame.managers;

import javafx.scene.control.Button;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
public class SoundManager {
    private static SoundManager instance;

    private MediaPlayer backgroundMusicPlayer;
    private AudioClip clickSound;

    private boolean musicEnabled = true;
    private boolean soundEffectsEnabled = true;

    private SoundManager() {
        loadSounds();
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    private void loadSounds() {
        loadBackgroundMusic();
        loadClickSound();
    }

    private void loadBackgroundMusic() {
        try {
            URL musicResource = getClass().getResource("/org/helitha/heartapigame/sounds/Jeremy Blake - Powerup!  NO COPYRIGHT 8-bit Music.mp3");
            if (musicResource != null) {
                Media media = new Media(musicResource.toString());
                backgroundMusicPlayer = new MediaPlayer(media);
                backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                backgroundMusicPlayer.setVolume(0.3);
                System.out.println("Background music loaded successfully");
            } else {
                System.err.println("Background music file not found");
            }
        } catch (Exception e) {
            System.err.println("Error loading background music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadClickSound() {
        try {
            URL clickResource = getClass().getResource("/org/helitha/heartapigame/sounds/Mouse Click Sound Effect.wav");
            if (clickResource != null) {
                clickSound = new AudioClip(clickResource.toString());
                clickSound.setVolume(0.5);
                System.out.println("Click sound loaded successfully (WAV format)");
            } else {
                System.err.println("Click sound file not found");
            }
        } catch (Exception e) {
            System.err.println("Error loading click sound: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void playBackgroundMusic() {
        if (backgroundMusicPlayer != null && musicEnabled) {
            backgroundMusicPlayer.play();
            System.out.println("Background music started");
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
            System.out.println("Background music stopped");
        }
    }

    public void pauseBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.pause();
        }
    }

    public void playClickSound() {
        if (clickSound != null && soundEffectsEnabled) {
            clickSound.play();
        }
    }

    public void setMusicVolume(double volume) {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.setVolume(Math.max(0.0, Math.min(1.0, volume)));
        }
    }

    public void setSoundEffectsVolume(double volume) {
        if (clickSound != null) {
            clickSound.setVolume(Math.max(0.0, Math.min(1.0, volume)));
        }
    }

    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        if (!enabled && backgroundMusicPlayer != null) {
            backgroundMusicPlayer.pause();
        } else if (enabled && backgroundMusicPlayer != null) {
            backgroundMusicPlayer.play();
        }
    }

    public void setSoundEffectsEnabled(boolean enabled) {
        this.soundEffectsEnabled = enabled;
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public boolean isSoundEffectsEnabled() {
        return soundEffectsEnabled;
    }

    public boolean toggleMute() {
        boolean shouldMute = musicEnabled || soundEffectsEnabled;

        if (shouldMute) {
            setMusicEnabled(false);
            setSoundEffectsEnabled(false);
        } else {
            setMusicEnabled(true);
            setSoundEffectsEnabled(true);
        }

        return !musicEnabled;
    }

    public boolean isMuted() {
        return !musicEnabled && !soundEffectsEnabled;
    }

    public void setupMuteButton(Button muteButton) {
        if (muteButton == null) return;
        
        updateMuteButtonIcon(muteButton);
        muteButton.setOnAction(e -> {
            toggleMute();
            updateMuteButtonIcon(muteButton);
        });
    }

    public void updateMuteButtonIcon(Button muteButton) {
        if (muteButton != null) {
            muteButton.setText(isMuted() ? "🔇" : "🔊");
        }
    }
}