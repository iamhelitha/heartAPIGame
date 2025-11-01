package org.helitha.heartapigame.managers;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

/**
 * Manages all sound effects and background music for the game
 */
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

    /**
     * Load all sound resources
     */
    private void loadSounds() {
        loadBackgroundMusic();
        loadClickSound();
    }

    /**
     * Load background music
     */
    private void loadBackgroundMusic() {
        try {
            URL musicResource = getClass().getResource("/org/helitha/heartapigame/sounds/Jeremy Blake - Powerup!  NO COPYRIGHT 8-bit Music.mp3");
            if (musicResource != null) {
                Media media = new Media(musicResource.toString());
                backgroundMusicPlayer = new MediaPlayer(media);
                backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop forever
                backgroundMusicPlayer.setVolume(0.3); // Set volume to 30%
                System.out.println("Background music loaded successfully");
            } else {
                System.err.println("Background music file not found");
            }
        } catch (Exception e) {
            System.err.println("Error loading background music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load click sound effect (WAV format for low latency)
     */
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

    /**
     * Start playing background music
     */
    public void playBackgroundMusic() {
        if (backgroundMusicPlayer != null && musicEnabled) {
            backgroundMusicPlayer.play();
            System.out.println("Background music started");
        }
    }

    /**
     * Stop background music
     */
    public void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
            System.out.println("Background music stopped");
        }
    }

    /**
     * Pause background music
     */
    public void pauseBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.pause();
        }
    }

    /**
     * Play click sound effect
     */
    public void playClickSound() {
        if (clickSound != null && soundEffectsEnabled) {
            clickSound.play();
        }
    }

    /**
     * Set background music volume (0.0 to 1.0)
     */
    public void setMusicVolume(double volume) {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.setVolume(Math.max(0.0, Math.min(1.0, volume)));
        }
    }

    /**
     * Set sound effects volume (0.0 to 1.0)
     */
    public void setSoundEffectsVolume(double volume) {
        if (clickSound != null) {
            clickSound.setVolume(Math.max(0.0, Math.min(1.0, volume)));
        }
    }

    /**
     * Enable or disable background music
     */
    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        if (!enabled && backgroundMusicPlayer != null) {
            backgroundMusicPlayer.pause();
        } else if (enabled && backgroundMusicPlayer != null) {
            backgroundMusicPlayer.play();
        }
    }

    /**
     * Enable or disable sound effects
     */
    public void setSoundEffectsEnabled(boolean enabled) {
        this.soundEffectsEnabled = enabled;
    }

    /**
     * Check if music is enabled
     */
    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    /**
     * Check if sound effects are enabled
     */
    public boolean isSoundEffectsEnabled() {
        return soundEffectsEnabled;
    }

    /**
     * Toggle mute state for all sounds
     * @return true if now muted, false if unmuted
     */
    public boolean toggleMute() {
        boolean shouldMute = musicEnabled || soundEffectsEnabled;

        if (shouldMute) {
            // Mute everything
            setMusicEnabled(false);
            setSoundEffectsEnabled(false);
        } else {
            // Unmute everything
            setMusicEnabled(true);
            setSoundEffectsEnabled(true);
        }

        return !musicEnabled; // Return true if muted
    }

    /**
     * Check if currently muted
     */
    public boolean isMuted() {
        return !musicEnabled && !soundEffectsEnabled;
    }
}

