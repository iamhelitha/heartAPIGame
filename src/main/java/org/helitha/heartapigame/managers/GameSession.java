package org.helitha.heartapigame.managers;

import java.util.Random;
import java.util.prefs.Preferences;
public class GameSession {

    private static GameSession instance;
    private static final String PREF_USER_ID = "userId";
    private static final String PREF_DISPLAY_NAME = "displayName";
    private static final String PREF_IS_GUEST = "isGuest";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_REFRESH_TOKEN = "refreshToken";
    private static final String PREF_ID_TOKEN = "idToken";

    private final Preferences prefs;
    private String userId;
    private String displayName;
    private String email;
    private String idToken;
    private String refreshToken;
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
        String savedEmail = prefs.get(PREF_EMAIL, null);
        String savedRefreshToken = prefs.get(PREF_REFRESH_TOKEN, null);
        String savedIdToken = prefs.get(PREF_ID_TOKEN, null);
        boolean savedIsGuest = prefs.getBoolean(PREF_IS_GUEST, false);

        if (savedUserId != null && savedDisplayName != null && !savedIsGuest) {
            this.userId = savedUserId;
            this.displayName = savedDisplayName;
            this.email = savedEmail;
            this.refreshToken = savedRefreshToken;
            this.idToken = savedIdToken;
            this.isGuest = false;
            System.out.println("Restored session for: " + displayName);
            System.out.println("Session has refresh token: " + (savedRefreshToken != null));
        }
    }

    public void setUser(String userId, String displayName, String email, String idToken, String refreshToken) {
        this.userId = userId;
        this.displayName = displayName;
        this.email = email;
        this.idToken = idToken;
        this.refreshToken = refreshToken;
        this.isGuest = false;
        saveSession();
    }

    private void saveSession() {
        if (!isGuest && userId != null) {
            prefs.put(PREF_USER_ID, userId);
            prefs.put(PREF_DISPLAY_NAME, displayName);
            prefs.putBoolean(PREF_IS_GUEST, false);
            
            if (email != null) {
                prefs.put(PREF_EMAIL, email);
            }
            if (idToken != null) {
                prefs.put(PREF_ID_TOKEN, idToken);
            }
            if (refreshToken != null) {
                prefs.put(PREF_REFRESH_TOKEN, refreshToken);
                System.out.println("Refresh token saved for future auto-login");
            }
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
        this.email = null;
        this.idToken = null;
        this.refreshToken = null;
        this.isGuest = false;
        
        prefs.remove(PREF_USER_ID);
        prefs.remove(PREF_DISPLAY_NAME);
        prefs.remove(PREF_EMAIL);
        prefs.remove(PREF_ID_TOKEN);
        prefs.remove(PREF_REFRESH_TOKEN);
        prefs.remove(PREF_IS_GUEST);
        System.out.println("Session cleared - all tokens removed");
    }

    public boolean hasSavedSession() {
        return prefs.get(PREF_USER_ID, null) != null && !prefs.getBoolean(PREF_IS_GUEST, true);
    }

    public String getUserId() { return userId; }
    public String getDisplayName() { return displayName; }
    public String getEmail() { return email; }
    public String getIdToken() { return idToken; }
    public String getRefreshToken() { return refreshToken; }
    public boolean isGuest() { return isGuest; }
    public boolean isLoggedIn() { return userId != null && displayName != null; }
}
