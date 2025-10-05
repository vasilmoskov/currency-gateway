package com.example.gateway.service;

import com.example.gateway.util.ServiceName;

public interface RequestStatService {
    boolean isRequestExisting(String id);

    void saveRequestStat(String requestId, String clientId, Long timestamp, ServiceName serviceName);
}
