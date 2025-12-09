package org.helitha.heartapigame.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import org.helitha.heartapigame.models.AuthResult;
import org.helitha.heartapigame.models.LeaderboardEntry;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * FirebaseService - Handles Firebase Authentication and Firestore database operations
 *
 * INTEROPERABILITY:
 * This class demonstrates interoperability with Google's Firebase cloud services:
 * - Firebase Authentication: User registration and login via Google's authentication service
 * - Cloud Firestore: NoSQL database for storing leaderboard data
 * - Communication happens over HTTPS using Firebase Admin SDK
 *
 * VIRTUAL IDENTITY:
 * Users establish a virtual identity by:
 * - Registering with email, password, and display name
 * - Firebase assigns a unique UID (User ID) to each user
 * - This identity is used to personalize the experience and track scores
 * - Guest users get a temporary identity (e.g., "Guest4721")
 *
 * HIGH COHESION:
 * All Firebase-related operations (auth + database) are in this class
 * Single responsibility: Manage user identity and persistent data storage
 *
 * LOW COUPLING:
 * Controllers don't need to know Firebase implementation details
 * They just call methods like registerUser(), saveScore(), getTopScores()
 */
public class FirebaseService {

    private static FirebaseService instance;
    private FirebaseAuth firebaseAuth;
    private Firestore firestore;
    private String firebaseApiKey;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    // Firebase REST API endpoint for password sign-in
    private static final String SIGN_IN_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=";

    private FirebaseService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public static FirebaseService getInstance() {
        if (instance == null) {
            instance = new FirebaseService();
        }
        return instance;
    }

    /**
     * Initialize Firebase Admin SDK with service account key
     */
    public void initialize() {
        try {
            String credentialsPath = "config/firebase-credentials.json";
            InputStream serviceAccount = new FileInputStream(credentialsPath);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            firebaseAuth = FirebaseAuth.getInstance();
            firestore = FirestoreClient.getFirestore();
            
            // Load API key from credentials file for REST API authentication
            loadApiKey(credentialsPath);
            
            System.out.println("Firebase initialized successfully");

        } catch (IOException e) {
            System.err.println("Error initializing Firebase: " + e.getMessage());
            System.err.println("Make sure the firebase-credentials.json file exists in the config/ directory");
        }
    }

    /**
     * Load the Firebase Web API key from environment or config
     */
    private void loadApiKey(String credentialsPath) {
        // Try to load from environment variable first
        firebaseApiKey = System.getenv("FIREBASE_API_KEY");
        
        if (firebaseApiKey == null || firebaseApiKey.isEmpty()) {
            // Try to load from a separate config file
            try (InputStream is = new FileInputStream("config/firebase-api-key.txt")) {
                firebaseApiKey = new String(is.readAllBytes()).trim();
            } catch (IOException e) {
                System.err.println("Warning: Firebase API key not found. Password verification will use fallback.");
                System.err.println("Create config/firebase-api-key.txt with your Firebase Web API key.");
            }
        }
    }

    /**
     * Register a new user with Firebase Authentication
     *
     * VIRTUAL IDENTITY:
     * Creates a new virtual identity for the user with:
     * - Unique UID assigned by Firebase
     * - Email and password for authentication
     * - Display name for personalization
     *
     * INTEROPERABILITY:
     * Communicates with Google's Firebase Authentication service over HTTPS
     */
    public UserRecord registerUser(String email, String password, String displayName) {
        if (firebaseAuth == null) {
            System.err.println("Firebase not initialized");
            return null;
        }

        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password)
                    .setDisplayName(displayName)
                    .setEmailVerified(false);

            UserRecord userRecord = firebaseAuth.createUser(request);
            System.out.println("Successfully created user: " + userRecord.getUid());
            return userRecord;

        } catch (FirebaseAuthException e) {
            System.err.println("Error creating user: " + e.getMessage());
            System.err.println("Error code: " + e.getAuthErrorCode());
            return null;
        }
    }

    /**
     * Login user by verifying credentials using Firebase REST API
     * This properly verifies the password against Firebase Authentication
     */
    public AuthResult loginUser(String email, String password) {
        if (firebaseApiKey == null || firebaseApiKey.isEmpty()) {
            System.err.println("Firebase API key not configured - cannot verify password");
            return null;
        }

        try {
            // Build JSON request body
            String requestBody = objectMapper.writeValueAsString(Map.of(
                    "email", email,
                    "password", password,
                    "returnSecureToken", true
            ));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SIGN_IN_URL + firebaseApiKey))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(10))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode json = objectMapper.readTree(response.body());
                String localId = json.get("localId").asText();
                String userEmail = json.get("email").asText();
                String displayName = json.has("displayName") ? json.get("displayName").asText() : userEmail;
                String idToken = json.get("idToken").asText();
                String refreshToken = json.get("refreshToken").asText();
                boolean registered = json.has("registered") && json.get("registered").asBoolean();

                System.out.println("Login successful for user: " + localId);
                return new AuthResult(localId, userEmail, displayName, idToken, refreshToken, registered);
            } else {
                // Parse error response
                JsonNode errorJson = objectMapper.readTree(response.body());
                String errorMessage = errorJson.path("error").path("message").asText("Unknown error");
                System.err.println("Login failed: " + errorMessage);
                return null;
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error during login: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get user by UID
     */
    public UserRecord getUserById(String uid) {
        if (firebaseAuth == null) {
            System.err.println("Firebase not initialized");
            return null;
        }

        try {
            return firebaseAuth.getUser(uid);
        } catch (FirebaseAuthException e) {
            System.err.println("Error getting user: " + e.getMessage());
            return null;
        }
    }

    /**
     * Save score to Firestore leaderboard collection
     *
     * INTEROPERABILITY:
     * Stores data in Google's Cloud Firestore (NoSQL database)
     * Data is synchronized across all clients in real-time
     *
     * VIRTUAL IDENTITY:
     * Associates the score with the player's display name (virtual identity)
     */
    public void saveScore(String username, int score) {
        if (firestore == null) {
            System.err.println("Firestore not initialized");
            return;
        }

        try {
            Map<String, Object> scoreData = new HashMap<>();
            scoreData.put("username", username);
            scoreData.put("score", score);
            scoreData.put("timestamp", System.currentTimeMillis());

            // Add document to leaderboard collection
            firestore.collection("leaderboard")
                    .add(scoreData)
                    .get(); // Wait for completion

            System.out.println("Score saved successfully: " + username + " - " + score);

        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error saving score: " + e.getMessage());
            System.err.println("Error type: " + e.getClass().getSimpleName());
        }
    }

    /**
     * Get top 10 scores from leaderboard, ordered by score descending
     *
     * INTEROPERABILITY:
     * Queries Cloud Firestore database and retrieves data
     */
    public List<LeaderboardEntry> getTopScores() {
        List<LeaderboardEntry> topScores = new ArrayList<>();

        if (firestore == null) {
            System.err.println("Firestore not initialized");
            return topScores;
        }

        try {
            // Query leaderboard collection, order by score descending, limit to 10
            List<QueryDocumentSnapshot> documents = firestore.collection("leaderboard")
                    .orderBy("score", Query.Direction.DESCENDING)
                    .limit(10)
                    .get()
                    .get()
                    .getDocuments();

            // Convert documents to LeaderboardEntry objects
            for (QueryDocumentSnapshot document : documents) {
                String username = document.getString("username");
                Long scoreLong = document.getLong("score");
                int score = scoreLong != null ? scoreLong.intValue() : 0;

                topScores.add(new LeaderboardEntry(username, score));
            }

            System.out.println("Retrieved " + topScores.size() + " top scores");

        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error retrieving top scores: " + e.getMessage());
            System.err.println("Error type: " + e.getClass().getSimpleName());
        }

        return topScores;
    }

    /**
     * Get ALL scores from leaderboard without server-side ordering or limiting.
     * Sorting and ranking should be handled on the application side.
     */
    public List<LeaderboardEntry> getAllScores() {
        List<LeaderboardEntry> scores = new ArrayList<>();

        if (firestore == null) {
            System.err.println("Firestore not initialized");
            return scores;
        }

        try {
            List<QueryDocumentSnapshot> documents = firestore.collection("leaderboard")
                    .get()
                    .get()
                    .getDocuments();

            for (QueryDocumentSnapshot document : documents) {
                String username = document.getString("username");
                Long scoreLong = document.getLong("score");
                int score = scoreLong != null ? scoreLong.intValue() : 0;

                // Only add valid entries
                if (username != null && !username.isEmpty()) {
                    scores.add(new LeaderboardEntry(username, score));
                }
            }

            System.out.println("Retrieved " + scores.size() + " scores (unsorted)");

        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error retrieving all scores: " + e.getMessage());
            System.err.println("Error type: " + e.getClass().getSimpleName());
        }

        return scores;
    }
}
