package com.example.gateway.service.impl;

import com.example.gateway.client.FixerClient;
import com.example.gateway.dto.response.CurrentCurrencyRatesResponse;
import com.example.gateway.dto.response.FixerResponse;
import com.example.gateway.entity.CurrencyRate;
import com.example.gateway.exception.ResourceNotFoundException;
import com.example.gateway.repository.CurrencyRateRepository;
import com.example.gateway.service.CurrencyRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
                .map(e -> CurrencyRate
                        .builder()
                        .baseCurrency(baseCurrency)
                        .targetCurrency(e.getKey())
                        .rate(e.getValue())
                        .timestamp(timestampOfCurrencyRate)
                        .build())
                .toList();

        LOGGER.debug("Saving currency rates for {} as of {}.", baseCurrency, timestampOfCurrencyRate);

        try {
            currencyRateRepository.saveAll(currencyRates);
            LOGGER.debug("Successfully saved currency rates for {} as of {}.", baseCurrency, timestampOfCurrencyRate);
        } catch (DataIntegrityViolationException _) { // todo: handle this exception in Global Exception Handler
            LOGGER.error("Currency rates for {} as of {} already exist.", baseCurrency, timestampOfCurrencyRate);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public CurrentCurrencyRatesResponse getCurrentRatesForCurrency(String baseCurrency) {
        LOGGER.debug("Fetching latest currency rates for {}.", baseCurrency);

        List<CurrencyRate> currencyRates = currencyRateRepository.findLatestRatesByBaseCurrency(baseCurrency);

        if (currencyRates.isEmpty()) {
            // todo: handle in Global Exception Handler
            throw new ResourceNotFoundException(String.format("Currency %s does not exist.", baseCurrency));
        }

        Map<String, BigDecimal> targetCurrenciesRates = new LinkedHashMap<>();
        currencyRates.forEach(cr -> targetCurrenciesRates.put(cr.getTargetCurrency(), cr.getRate()));

        return CurrentCurrencyRatesResponse.builder()
                .baseCurrency(baseCurrency)
                .timestamp(currencyRates.getFirst().getTimestamp())
                .rates(targetCurrenciesRates)
                .build();
    }
}
