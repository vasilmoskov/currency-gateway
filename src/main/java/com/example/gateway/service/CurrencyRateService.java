package com.example.gateway.service;

import com.example.gateway.dto.response.CurrentCurrencyRatesResponse;
import com.example.gateway.dto.response.HistoryCurrencyRatesResponse;

public interface CurrencyRateService {
    void updateCurrencyRates();

    CurrentCurrencyRatesResponse getCurrentRatesForCurrency(String baseCurrency);

    HistoryCurrencyRatesResponse getHistoryRatesForCurrency(String baseCurrency, Integer period);
}
