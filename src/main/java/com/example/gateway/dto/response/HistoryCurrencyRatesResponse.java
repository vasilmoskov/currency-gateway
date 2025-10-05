package com.example.gateway.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryCurrencyRatesResponse {
    private String baseCurrency;
    private List<CurrencyRatesAtGivenTimestamp> history;
}
