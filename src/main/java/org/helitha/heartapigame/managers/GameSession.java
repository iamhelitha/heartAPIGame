package org.helitha.heartapigame.managers;

import java.util.Random;

/**
 * GameSession - Manages the current user session and virtual identity
 *
 * VIRTUAL IDENTITY:
 * This class is central to managing the user's virtual identity in the game:
 * - Authenticated users: Have a real Firebase UID and chosen display name
 * - Guest users: Get a temporary identity (e.g., "Guest4721") for anonymous play
 * - The virtual identity personalizes the experience (welcome message, leaderboard name)
 * - This identity persists throughout the game session
 *
 * DESIGN PRINCIPLE: Singleton Pattern
 * Ensures a single session exists across the entire application
 * All screens can access the current user's identity via getInstance()
 *
 * HIGH COHESION:
 * Single responsibility: Manage current user session data
 * All session-related data (userId, displayName, isGuest) is here
 */
public class GameSession {

    private static GameSession instance;

    private String userId;
    private String displayName;
    private boolean isGuest;

    private GameSession() {
        // Private constructor for singleton
    }

    public static GameSession getInstance() {
        if (instance == null) {
            instance = new GameSession();
        }
        return instance;
    }

    /**
     * Set user session for authenticated user
     *
     * VIRTUAL IDENTITY:
     * Establishes the user's authenticated virtual identity
     * - userId: Unique identifier from Firebase Authentication
     * - displayName: The name chosen by the user during registration
     */
    public void setUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
        this.isGuest = false;
    }

    /**
     * Create guest user session with random name
     *
     * VIRTUAL IDENTITY:
     * Creates a temporary virtual identity for users who don't want to register
     * - Assigns a random guest name (e.g., "Guest4721")
     * - Allows anonymous participation in the game and leaderboard
     * - No password required, no persistent account created
     */
    public void createGuestUser() {
        Random random = new Random();
        int guestNumber = random.nextInt(9999) + 1;
        this.displayName = "Guest" + guestNumber;
        this.userId = "guest_" + System.currentTimeMillis();
        this.isGuest = true;
    }

    /**
     * Clear current session (logout)
     *
     * VIRTUAL IDENTITY:
     * Removes the current virtual identity, requiring re-authentication
     */
    public void clearSession() {
        this.userId = null;
        this.displayName = null;
        this.isGuest = false;
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isGuest() {
        return isGuest;
    }

    public boolean isLoggedIn() {
        return userId != null && displayName != null;
    }
}
