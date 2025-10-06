package com.example.gateway.dto.response;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString
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
        if (this.targetCurrency == null || this.price == null || o.targetCurrency == null || o.price == null) {
            throw new IllegalArgumentException("Target currencies and prices should not be null.");
        }

        int currencyComparison = this.targetCurrency.compareTo(o.targetCurrency);

        if (currencyComparison != 0) {
            return currencyComparison;
        }

        return this.price.compareTo(o.price);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        TargetCurrencyPrice other = (TargetCurrencyPrice) obj;

        return Objects.equals(targetCurrency, other.targetCurrency) &&
                Objects.equals(price, other.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetCurrency, price);
    }
}
