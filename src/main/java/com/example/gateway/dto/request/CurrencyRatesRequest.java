package com.example.gateway.dto.request;

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
    private Integer period;
}
