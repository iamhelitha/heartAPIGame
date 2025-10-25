package org.helitha.heartapigame.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.helitha.heartapigame.models.GameData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * ApiService - Handles communication with the Heart Game API
 *
 * INTEROPERABILITY:
 * This class demonstrates interoperability by enabling communication between:
 * - The Java client application and a PHP-based web server
 * - The "contract" for this communication is HTTP protocol and JSON data format
 * - JSON structure: {"question": "image_url", "solution": hearts_count, "carrots": carrots_count}
 *
 * HIGH COHESION:
 * All API communication logic is encapsulated in this single class
 * Responsibility: Fetch game data from external API and parse JSON response
 *
 * LOW COUPLING:
 * Controllers don't need to know HOW data is fetched (HTTP, JSON parsing)
 * They just call fetchGameData() and receive a GameData object
 */
public class ApiService {

    private static final String API_URL = "https://marcconrad.com/uob/heart/api.php";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ApiService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Fetches game data from the Heart API
     *
     * INTEROPERABILITY EXAMPLE:
     * 1. Java client sends HTTP GET request to PHP server
     * 2. Server responds with JSON data
     * 3. Jackson library parses JSON into Java object
     *
     * @return GameData object containing question (image URL), solution (hearts count), and carrots count
     * @throws IOException if network error occurs
     * @throws InterruptedException if request is interrupted
     */
    public GameData fetchGameData() throws IOException, InterruptedException {
        // Create HTTP GET request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .GET()
                .build();

        // Send request and get response
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Check if request was successful
        if (response.statusCode() == 200) {
            String jsonResponse = response.body();
            System.out.println("API Response: " + jsonResponse);

            // Parse JSON to GameData object (INTEROPERABILITY: JSON data exchange)
            GameData gameData = objectMapper.readValue(jsonResponse, GameData.class);
            return gameData;
        } else {
            throw new IOException("API request failed with status code: " + response.statusCode());
        }
    }

    /**
     * Singleton instance for easy access
     */
    private static ApiService instance;

    public static ApiService getInstance() {
        if (instance == null) {
            instance = new ApiService();
        }
        return instance;
    }
}
