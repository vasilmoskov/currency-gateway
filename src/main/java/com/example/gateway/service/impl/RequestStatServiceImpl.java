package com.example.gateway.service.impl;

import com.example.gateway.entity.RequestStat;
import com.example.gateway.repository.RequestStatRepository;
import com.example.gateway.service.RequestStatService;
import com.example.gateway.util.ServiceName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class RequestStatServiceImpl implements RequestStatService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestStatServiceImpl.class);

    private static final String REQUEST_RECEIVED_ROUTING_KEY = "request.received";

    private final RequestStatRepository requestStatRepository;
    private final AmqpTemplate amqpTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchangeName;

    public RequestStatServiceImpl(RequestStatRepository requestStatRepository, AmqpTemplate amqpTemplate) {
        this.requestStatRepository = requestStatRepository;
        this.amqpTemplate = amqpTemplate;
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
    public void saveRequestStat(String requestId, String clientId, Long timestamp, ServiceName serviceName) {
        RequestStat stat = RequestStat.builder()
                .requestId(requestId)
                .clientID(clientId)
                .timestamp(timestamp != null ? Instant.ofEpochMilli(timestamp) : Instant.now())
                .serviceName(serviceName).build();

        LOGGER.debug("Saving request stat: {}.", stat);

        requestStatRepository.save(stat);

        LOGGER.debug("Publishing message {} to RabbitMQ.", stat);

        try {
            amqpTemplate.convertAndSend(exchangeName, REQUEST_RECEIVED_ROUTING_KEY, stat);
            LOGGER.info("Published request stat to RabbitMQ: {}.", stat);
        } catch (Exception e) {
            LOGGER.error("Failed to publish message to RabbitMQ.", e);
        }
    }
}
