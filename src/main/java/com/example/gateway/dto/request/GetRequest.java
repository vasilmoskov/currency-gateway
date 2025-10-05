package com.example.gateway.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class GetRequest {

    @NotBlank(message = "Consumer must not be null or empty.")
    @XmlAttribute
    private String consumer;

    @NotBlank(message = "Currency must not be null or empty.")
    @XmlElement
    private String currency;
}