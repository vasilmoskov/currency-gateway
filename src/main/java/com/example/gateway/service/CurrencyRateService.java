package com.example.gateway.service;

import com.example.gateway.dto.response.CurrentCurrencyRatesResponse;

public interface CurrencyRateService {
    void updateCurrencyRates();

    CurrentCurrencyRatesResponse getCurrentRatesForCurrency(String baseCurrency);

}
