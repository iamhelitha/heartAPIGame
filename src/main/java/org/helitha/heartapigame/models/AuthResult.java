package org.helitha.heartapigame.models;

/**
 * AuthResult - Represents the result of Firebase authentication via REST API
 * Used to hold user data returned from signInWithPassword endpoint
 */
public class AuthResult {
    private final String localId;      // Firebase UID
    private final String email;
    private final String displayName;
    private final String idToken;
    private final String refreshToken;
    private final boolean registered;

    public AuthResult(String localId, String email, String displayName, 
                      String idToken, String refreshToken, boolean registered) {
        this.localId = localId;
        this.email = email;
        this.displayName = displayName;
        this.idToken = idToken;
        this.refreshToken = refreshToken;
        this.registered = registered;
    }

    public String getLocalId() { return localId; }
    public String getEmail() { return email; }
    public String getDisplayName() { return displayName; }
    public String getIdToken() { return idToken; }
    public String getRefreshToken() { return refreshToken; }
    public boolean isRegistered() { return registered; }
}
