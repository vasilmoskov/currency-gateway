package com.example.gateway.controller;

import com.example.gateway.dto.response.CurrentCurrencyRatesResponse;
import com.example.gateway.dto.request.CurrencyRatesRequest;
import com.example.gateway.exception.ResourceAlreadyExistsException;
import com.example.gateway.service.CurrencyRateService;
import com.example.gateway.service.RequestStatService;
import com.example.gateway.util.ServiceName;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(path = "/json_api",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE
)
/**
 * EXT_SERVICE_1
 */
public class JsonCurrencyRateController {

    private final RequestStatService requestStatService;
    private final CurrencyRateService currencyRateService;

    public JsonCurrencyRateController(RequestStatService requestStatService, CurrencyRateService currencyRateService) {
        this.requestStatService = requestStatService;
        this.currencyRateService = currencyRateService;
    }

    @PostMapping(value = "/current")
    public CurrentCurrencyRatesResponse current(@RequestBody CurrencyRatesRequest request) {
        if (requestStatService.isRequestExisting(request.getRequestId())) {
            // todo: handle in Global Exception Handler
            throw new ResourceAlreadyExistsException(
                    String.format("Request with id %s already exists.", request.getRequestId()));
        }

        CurrentCurrencyRatesResponse response = currencyRateService.getCurrentRatesForCurrency(request.getCurrency());

        requestStatService.saveRequestStat(request, ServiceName.EXT_SERVICE_1);

        return response;
    }
}
