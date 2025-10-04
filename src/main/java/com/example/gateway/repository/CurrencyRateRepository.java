package com.example.gateway.repository;

import com.example.gateway.entity.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {

    @Query("""
            SELECT cr
            FROM CurrencyRate cr
            WHERE cr.baseCurrency = :baseCurrency
            AND cr.timestamp = (
                SELECT MAX(cr2.timestamp)
                FROM CurrencyRate cr2
                WHERE cr.baseCurrency = :baseCurrency
            )
            """)
    List<CurrencyRate> findLatestRatesByBaseCurrency(@Param("baseCurrency") String baseCurrency);
}
