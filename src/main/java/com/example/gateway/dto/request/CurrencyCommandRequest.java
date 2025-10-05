package com.example.gateway.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
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
@XmlRootElement(name = "command")
@XmlAccessorType(XmlAccessType.FIELD)
public class CurrencyCommandRequest {

    @NotBlank(message = "ID must not be null or empty.")
    @XmlAttribute
    private String id;

    @Valid
    @XmlElement
    private GetRequest get;

    @Valid
    @XmlElement
    private HistoryRequest history;
}
