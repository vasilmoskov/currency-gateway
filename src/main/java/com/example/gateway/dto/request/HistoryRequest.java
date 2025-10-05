package com.example.gateway.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class HistoryRequest {

    @NotBlank(message = "Consumer must not be null or empty.")
    @XmlAttribute
    private String consumer;

    @NotBlank(message = "Currency must not be null or empty.")
    @XmlAttribute
    private String currency;

    /**
     * The period represents the number of hours of historical currency rate data to retrieve.
     * A maximum of 168 hours (7 days) is allowed to prevent excessive database load
     * in case a user provides very large value.
     */
    @Max(value = 168, message = "Period must be at most 168 hours.")
    @Positive(message = "Period must be a positive number.")
    @XmlAttribute
    private Integer period;
}
