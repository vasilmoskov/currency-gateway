package com.example.gateway.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRatesRequest {
    @NotBlank(message = "Request ID must not be null or empty.")
    private String requestId;

    private Long timestamp;

    @NotBlank(message = "Client must not be null or empty.")
    private String client;

    @NotBlank(message = "Currency must not be null or empty.")
    private String currency;

    /**
     * The period represents the number of hours of historical currency rate data to retrieve.
     * A maximum of 168 hours (7 days) is allowed to prevent excessive database load
     * in case a user provides very large value.
     */
    @Max(value = 168, message = "Period must be at most 168 hours.")
    @Positive(message = "Period must be a positive number.")
    private Integer period;
}
