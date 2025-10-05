package com.example.gateway.dto.response;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class TargetCurrencyPrice implements Comparable<TargetCurrencyPrice>{

    @XmlElement
    private String targetCurrency;

    @XmlElement
    private BigDecimal price;

    @Override
    public int compareTo(TargetCurrencyPrice o) {
        return this.targetCurrency.compareTo(o.getTargetCurrency());
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
