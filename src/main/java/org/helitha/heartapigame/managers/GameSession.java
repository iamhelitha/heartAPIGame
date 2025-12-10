package org.helitha.heartapigame.managers;

import java.util.Random;
import java.util.prefs.Preferences;
public class GameSession {

    private static GameSession instance;
    private static final String PREF_USER_ID = "userId";
    private static final String PREF_DISPLAY_NAME = "displayName";
    private static final String PREF_IS_GUEST = "isGuest";

    private final Preferences prefs;
    private String userId;
    private String displayName;
    private boolean isGuest;

    private GameSession() {
        prefs = Preferences.userNodeForPackage(GameSession.class);
        loadSavedSession();
    }

    public static GameSession getInstance() {
        if (instance == null) {
            instance = new GameSession();
        }
        return instance;
    }

    private void loadSavedSession() {
        String savedUserId = prefs.get(PREF_USER_ID, null);
        String savedDisplayName = prefs.get(PREF_DISPLAY_NAME, null);
        boolean savedIsGuest = prefs.getBoolean(PREF_IS_GUEST, false);

        if (savedUserId != null && savedDisplayName != null && !savedIsGuest) {
            this.userId = savedUserId;
            this.displayName = savedDisplayName;
            this.isGuest = false;
            System.out.println("Restored session for: " + displayName);
        }
    }

    public void setUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
        this.isGuest = false;
        saveSession();
    }

    private void saveSession() {
        if (!isGuest && userId != null) {
            prefs.put(PREF_USER_ID, userId);
            prefs.put(PREF_DISPLAY_NAME, displayName);
            prefs.putBoolean(PREF_IS_GUEST, false);
        }
    }

    public void createGuestUser() {
        Random random = new Random();
        int guestNumber = random.nextInt(9999) + 1;
        this.displayName = "Guest" + guestNumber;
        this.userId = "guest_" + System.currentTimeMillis();
        this.isGuest = true;
    }

    public void clearSession() {
        this.userId = null;
        this.displayName = null;
        this.isGuest = false;
        
        prefs.remove(PREF_USER_ID);
        prefs.remove(PREF_DISPLAY_NAME);
        prefs.remove(PREF_IS_GUEST);
    }

    public boolean hasSavedSession() {
        return prefs.get(PREF_USER_ID, null) != null && !prefs.getBoolean(PREF_IS_GUEST, true);
    }

    public String getUserId() { return userId; }
    public String getDisplayName() { return displayName; }
    public boolean isGuest() { return isGuest; }
    public boolean isLoggedIn() { return userId != null && displayName != null; }
}
