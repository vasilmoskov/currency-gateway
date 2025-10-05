package com.example.gateway.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRatesRequest {
    private String requestId;
    private long timestamp;
    private String client;
    private String currency;

    /**
     * The period represents the number of hours of historical currency rate data to retrieve.
     * A maximum of 168 hours (7 days) is allowed to prevent excessive database load
     * in case a user provides very large value.
     */
    @Max(168)
    @Positive
    private Integer period;
}
