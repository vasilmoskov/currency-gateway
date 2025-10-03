package com.example.gateway.scheduler;

import com.example.gateway.service.CurrencyRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RatesCollectorScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RatesCollectorScheduler.class);

    private final CurrencyRateService ratesCollectorService;

    public RatesCollectorScheduler(CurrencyRateService ratesCollectorService) {
        this.ratesCollectorService = ratesCollectorService;
    }

    @Scheduled(fixedRateString = "${rates-collector.interval:900000}")
    public void executeCollectRates() {
        LOGGER.debug("Task for collecting latest currency rates has started.");

        ratesCollectorService.updateCurrencyRates();

        LOGGER.debug("Task for collecting latest currency rates has finished.");
    }
}
