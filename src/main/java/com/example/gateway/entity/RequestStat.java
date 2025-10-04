package com.example.gateway.entity;

import com.example.gateway.util.ServiceName;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "request_statistics")
public class RequestStat {
    @Id
    private String requestId;

    private String clientID;

    @Enumerated(value = EnumType.STRING)
    private ServiceName serviceName;

    private Instant timestamp;
}
