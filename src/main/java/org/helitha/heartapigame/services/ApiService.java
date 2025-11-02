package org.helitha.heartapigame.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.helitha.heartapigame.models.GameData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class ApiService {

    private static final String API_URL = "https://marcconrad.com/uob/heart/api.php";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ApiService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }


    public GameData fetchGameData() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String jsonResponse = response.body();
            System.out.println("API Response: " + jsonResponse);
            GameData gameData = objectMapper.readValue(jsonResponse, GameData.class);
            return gameData;
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
