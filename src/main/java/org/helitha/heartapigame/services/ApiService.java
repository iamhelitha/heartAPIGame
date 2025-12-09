package org.helitha.heartapigame.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.helitha.heartapigame.models.GameData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ApiService {

    private static final String API_URL = "https://marcconrad.com/uob/heart/api.php";
    private static final int MAX_RETRIES = 3;
    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(15);
    
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ApiService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(CONNECT_TIMEOUT)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public GameData fetchGameData() throws IOException, InterruptedException {
        IOException lastException = null;
        
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                return attemptFetch();
            } catch (IOException e) {
                lastException = e;
                System.err.println("API fetch attempt " + attempt + " failed: " + e.getMessage());
                
                if (attempt < MAX_RETRIES) {
                    long backoffMs = (long) Math.pow(2, attempt) * 500; // Exponential backoff: 1s, 2s, 4s
                    System.out.println("Retrying in " + backoffMs + "ms...");
                    Thread.sleep(backoffMs);
                }
            }
        }
        
        throw new IOException("Failed after " + MAX_RETRIES + " attempts", lastException);
    }

    private GameData attemptFetch() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .timeout(REQUEST_TIMEOUT)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String jsonResponse = response.body();
            System.out.println("API Response: " + jsonResponse);
            return objectMapper.readValue(jsonResponse, GameData.class);
        } else {
            throw new IOException("API request failed with status code: " + response.statusCode());
        }
    }

    private static ApiService instance;

    public static ApiService getInstance() {
        if (instance == null) {
            instance = new ApiService();
        }
        return instance;
    }
}
