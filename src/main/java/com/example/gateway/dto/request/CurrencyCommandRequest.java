package com.example.gateway.dto.request;

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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "command")
@XmlAccessorType(XmlAccessType.FIELD)
public class CurrencyCommandRequest {

    @NotBlank
    @XmlAttribute
    private String id;

    @XmlElement
    private GetRequest get;

    @XmlElement
    private HistoryRequest history;
}
