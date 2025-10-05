package com.example.gateway.controller;

import com.example.gateway.dto.request.CurrencyCommandRequest;
import com.example.gateway.dto.request.GetRequest;
import com.example.gateway.dto.request.HistoryRequest;
import com.example.gateway.dto.response.CurrencyRatesResponse;
import com.example.gateway.dto.response.CurrentCurrencyRatesResponse;
import com.example.gateway.dto.response.HistoryCurrencyRatesResponse;
import com.example.gateway.exception.ResourceAlreadyExistsException;
import com.example.gateway.service.CurrencyRateService;
import com.example.gateway.service.RequestStatService;
import com.example.gateway.util.ServiceName;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/xml_api",
        consumes = MediaType.APPLICATION_XML_VALUE,
        produces = MediaType.APPLICATION_XML_VALUE)
/*
 * EXT_SERVICE_2
 */
public class XmlCurrencyRateController {

    private final RequestStatService requestStatService;
    private final CurrencyRateService currencyRateService;

    public XmlCurrencyRateController(RequestStatService requestStatService, CurrencyRateService currencyRateService) {
        this.requestStatService = requestStatService;
        this.currencyRateService = currencyRateService;
    }

    @PostMapping(value = "/command")
    public ResponseEntity<CurrencyRatesResponse> command(@Valid @RequestBody CurrencyCommandRequest requestBody) {

        if (requestStatService.isRequestExisting(requestBody.getId())) {
            throw new ResourceAlreadyExistsException(
                    String.format("Request with id %s already exists.", requestBody.getId()));
        }

        String client;
        ResponseEntity<CurrencyRatesResponse> response;

        if (requestBody.getGet() != null) {
            GetRequest getRequest = requestBody.getGet();

            client = getRequest.getConsumer();
            CurrentCurrencyRatesResponse currentCurrencyRatesResponse = currencyRateService.getCurrentRatesForCurrency(getRequest.getCurrency());
            response = ResponseEntity.ok(currentCurrencyRatesResponse);

        } else if (requestBody.getHistory() != null) {
            HistoryRequest historyRequest = requestBody.getHistory();

            client = historyRequest.getConsumer();
            HistoryCurrencyRatesResponse historyRatesForCurrency = currencyRateService.getHistoryRatesForCurrency(historyRequest.getCurrency(), historyRequest.getPeriod());

            if (historyRatesForCurrency.getHistory().isEmpty()) {
                response = ResponseEntity.noContent().build();
            } else {
                response = ResponseEntity.ok(historyRatesForCurrency);
            }

        } else {
            return ResponseEntity.badRequest().build();
        }

        requestStatService.saveRequestStat(requestBody.getId(), client, null, ServiceName.EXT_SERVICE_2);

        return response;
    }
}
