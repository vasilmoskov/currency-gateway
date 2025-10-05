package com.example.gateway.dto.response;

import com.example.gateway.util.InstantAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class CurrencyRatesAtGivenTimestamp {
    @XmlJavaTypeAdapter(InstantAdapter.class)
    private Instant timestamp;

    @XmlElementWrapper(name = "rates")
    @XmlElement(name = "rate")
    private Set<TargetCurrencyPrice> rates;
}
