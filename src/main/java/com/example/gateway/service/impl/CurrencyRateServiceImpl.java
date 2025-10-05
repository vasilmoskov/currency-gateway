package com.example.gateway.service.impl;

import com.example.gateway.client.FixerClient;
import com.example.gateway.dto.response.CurrencyRatesAtGivenTimestamp;
import com.example.gateway.dto.response.CurrentCurrencyRatesResponse;
import com.example.gateway.dto.response.FixerResponse;
import com.example.gateway.dto.response.HistoryCurrencyRatesResponse;
import com.example.gateway.dto.response.TargetCurrencyPrice;
import com.example.gateway.entity.CurrencyRate;
import com.example.gateway.exception.ResourceNotFoundException;
import com.example.gateway.repository.CurrencyRateRepository;
import com.example.gateway.service.CurrencyRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
        } catch (DataIntegrityViolationException _) {
            LOGGER.warn("Currency rates for {} as of {} already exist.", baseCurrency, timestampOfCurrencyRate);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public CurrentCurrencyRatesResponse getCurrentRatesForCurrency(String baseCurrency) {
        LOGGER.debug("Fetching latest currency rates for {}.", baseCurrency);

        List<CurrencyRate> currencyRates = currencyRateRepository.findLatestRatesByBaseCurrency(baseCurrency);

        if (currencyRates.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Currency %s does not exist.", baseCurrency));
        }

        Set<TargetCurrencyPrice> rates = new TreeSet<>(Comparator.comparing(TargetCurrencyPrice::getTargetCurrency));
        currencyRates.forEach(cr -> rates.add(new TargetCurrencyPrice(cr.getTargetCurrency(), cr.getRate())));

        return CurrentCurrencyRatesResponse.builder()
                .baseCurrency(baseCurrency)
                .timestamp(currencyRates.getFirst().getTimestamp())
                .rates(rates)
                .build();
    }

    @Override
    public HistoryCurrencyRatesResponse getHistoryRatesForCurrency(String baseCurrency, Integer period) {
        LOGGER.debug("Validating whether {} exist as base currency.", baseCurrency);

        if (!currencyRateRepository.existsCurrencyRateByBaseCurrency(baseCurrency)) {
            throw new ResourceNotFoundException(String.format("Currency %s does not exist.", baseCurrency));
        }

        LOGGER.debug("Fetching history currency rates for {} for the past {} hours.", baseCurrency, period);

        Instant to = Instant.now();
        Instant from = to.minus(period, ChronoUnit.HOURS);

        List<CurrencyRate> currencyRates = currencyRateRepository.findByBaseCurrencyAndTimestampBetweenOrderByTimestampDesc(baseCurrency, from, to);

        Map<Instant, Set<TargetCurrencyPrice>> ratesByTimestamp = new LinkedHashMap<>();

        for (CurrencyRate currencyRate : currencyRates) {
            ratesByTimestamp.putIfAbsent(
                    currencyRate.getTimestamp(),
                    new TreeSet<>(Comparator.comparing(TargetCurrencyPrice::getTargetCurrency))
            );

            ratesByTimestamp.get(currencyRate.getTimestamp())
                    .add(new TargetCurrencyPrice(currencyRate.getTargetCurrency(), currencyRate.getRate()));
        }

        List<CurrencyRatesAtGivenTimestamp> ratesAt = ratesByTimestamp.entrySet().stream()
                .map(e -> CurrencyRatesAtGivenTimestamp
                        .builder()
                        .timestamp(e.getKey())
                        .rates(e.getValue())
                        .build())
                .toList();

        return HistoryCurrencyRatesResponse.builder()
                .baseCurrency(baseCurrency)
                .history(ratesAt)
                .build();
    }
}
