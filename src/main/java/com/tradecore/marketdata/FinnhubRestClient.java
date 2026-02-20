package com.tradecore.marketdata;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

/**
 * Finnhub REST market data client.
 *
 * API key resolution order:
 * 1. Environment variable: FINNHUB_API_KEY
 * 2. application.properties -> finnhub.api.key
 */
public class FinnhubRestClient implements MarketDataProvider {

    private static final String BASE_URL =
            "https://finnhub.io/api/v1/quote";

    private final String apiKey;
    private final HttpClient httpClient;
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Default constructor (recommended).
     * Resolves API key automatically.
     */
    public FinnhubRestClient() {
        this.apiKey = resolveApiKey();
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Explicit constructor (advanced / testing use).
     */
    public FinnhubRestClient(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API key must not be null/blank");
        }
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public MarketPrice fetchPrice(String symbol) throws Exception {

        String url = BASE_URL +
                "?symbol=" + symbol +
                "&token=" + apiKey;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IllegalStateException(
                    "Failed to fetch price, HTTP " + response.statusCode()
            );
        }

        JsonNode root = mapper.readTree(response.body());

        // Finnhub: "c" = current price
        double currentPrice = root.path("c").asDouble();

        if (currentPrice <= 0) {
            throw new IllegalStateException(
                    "Invalid price received for symbol " + symbol
            );
        }

        return new MarketPrice(symbol, currentPrice);
    }

    /* ===================== API KEY RESOLUTION ===================== */

    private String resolveApiKey() {

        // 1. Environment variable
        String envKey = System.getenv("FINNHUB_API_KEY");
        if (envKey != null && !envKey.isBlank()) {
            return envKey;
        }

        // 2. application.properties
        Properties props = new Properties();
        try (InputStream in =
                     getClass()
                             .getClassLoader()
                             .getResourceAsStream("application.properties")) {

            if (in == null) {
                throw new IllegalStateException(
                        "No FINNHUB_API_KEY env var and application.properties not found"
                );
            }

            props.load(in);
            String fileKey = props.getProperty("finnhub.api.key");

            if (fileKey == null || fileKey.isBlank()) {
                throw new IllegalStateException(
                        "finnhub.api.key missing in application.properties"
                );
            }

            return fileKey;

        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve Finnhub API key", e);
        }
    }
}