package com.example.gateway.service.impl;

import com.example.gateway.dto.request.CurrencyRatesRequest;
import com.example.gateway.entity.RequestStat;
import com.example.gateway.repository.RequestStatRepository;
import com.example.gateway.service.RequestStatService;
import com.example.gateway.util.ServiceName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class RequestStatServiceImpl implements RequestStatService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestStatServiceImpl.class);

    private final RequestStatRepository requestStatRepository;

    public RequestStatServiceImpl(RequestStatRepository requestStatRepository) {
        this.requestStatRepository = requestStatRepository;
    }

    @Override
    public boolean isRequestExisting(String id) {
        LOGGER.debug("Fetching request with id: {}.", id);

        return requestStatRepository
                .findByRequestId(id)
                .isPresent();
    }

    @Transactional
    @Override
    public void saveRequestStat(CurrencyRatesRequest request, ServiceName serviceName) {
        RequestStat stat = RequestStat.builder()
                .requestId(request.getRequestId())
                .clientID(request.getClient())
                .timestamp(Instant.ofEpochMilli(request.getTimestamp()))
                .serviceName(serviceName).build();

        LOGGER.debug("Saving request stat: {}.", stat);

        requestStatRepository.save(stat);
    }
}
