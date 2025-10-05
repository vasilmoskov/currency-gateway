package com.example.gateway.dto.response;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "historyRates")
@XmlAccessorType(XmlAccessType.FIELD)
public class HistoryCurrencyRatesResponse implements CurrencyRatesResponse {

    @XmlElement
    private String baseCurrency;

    @XmlElementWrapper(name = "history")
    @XmlElement(name = "rateAt")
    private List<CurrencyRatesAtGivenTimestamp> history;
}
