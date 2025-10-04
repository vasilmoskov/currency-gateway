package com.example.gateway.service;

import com.example.gateway.dto.request.CurrencyRatesRequest;
import com.example.gateway.util.ServiceName;

public interface RequestStatService {
    boolean isRequestExisting(String id);

    void saveRequestStat(CurrencyRatesRequest request, ServiceName serviceName);
}
