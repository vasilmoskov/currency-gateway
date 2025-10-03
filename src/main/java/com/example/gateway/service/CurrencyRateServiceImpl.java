package com.example.gateway.service;

import com.example.gateway.client.FixerClient;
import com.example.gateway.dto.FixerResponse;
import com.example.gateway.entity.CurrencyRate;
import com.example.gateway.repository.CurrencyRateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class CurrencyRateServiceImpl implements CurrencyRateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyRateServiceImpl.class);

    private final CurrencyRateRepository currencyRateRepository;
    private final FixerClient fixerClient;

    public CurrencyRateServiceImpl(CurrencyRateRepository currencyRateRepository, FixerClient fixerClient) {
        this.currencyRateRepository = currencyRateRepository;
        this.fixerClient = fixerClient;
    }

    public void updateCurrencyRates() {
        FixerResponse dto = fixerClient.fetchLatestCurrencyRates();
        String baseCurrency = dto.getBase();
        Instant timestampOfCurrencyRate = Instant.ofEpochSecond(dto.getTimestamp());

        LOGGER.debug("Fetched currency rates for {} as of {}.", baseCurrency, timestampOfCurrencyRate);

        List<CurrencyRate> currencyRates = dto.getRates()
                .entrySet()
                .stream()
                .map(e -> new CurrencyRate()
                        .setBaseCurrency(baseCurrency)
                        .setTargetCurrency(e.getKey())
                        .setRate(e.getValue())
                        .setTimestamp(timestampOfCurrencyRate))
                .toList();

        LOGGER.debug("Save currency rates for {} as of {}.", baseCurrency, timestampOfCurrencyRate);

        currencyRateRepository.saveAll(currencyRates);
    }
}
