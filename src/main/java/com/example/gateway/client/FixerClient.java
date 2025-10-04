package com.example.gateway.client;

import com.example.gateway.dto.response.FixerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class FixerClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(FixerClient.class);

    private static final String FIXER_API_URL = "https://data.fixer.io/api";

    private final WebClient webClient;

    @Value("${fixer.api.key}")
    private String apiKey;

    public FixerClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl(FIXER_API_URL).build();
    }

    public FixerResponse fetchLatestCurrencyRates() {
        LOGGER.debug("Fetch latest currency rates from Fixer.");

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest")
                        .queryParam("access_key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(FixerResponse.class)
                .block();
    }
}
