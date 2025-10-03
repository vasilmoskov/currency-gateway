package com.example.gateway.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "currency_rates")
public class CurrencyRate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String baseCurrency;

    private String targetCurrency;

    private BigDecimal rate;

    private Instant timestamp;

    public CurrencyRate setId(Long id) {
        this.id = id;
        return this;
    }

    public CurrencyRate setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
        return this;
    }

    public CurrencyRate setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
        return this;
    }

    public CurrencyRate setRate(BigDecimal rate) {
        this.rate = rate;
        return this;
    }

    public CurrencyRate setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
