package com.example.gateway.repository;

import com.example.gateway.entity.RequestStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestStatRepository extends JpaRepository<RequestStat, Long> {
    Optional<RequestStat> findByRequestId(String requestId);
}
