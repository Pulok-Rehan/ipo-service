package com.beacepl.ipo_service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IpoResponse {
    @JsonProperty("P_RESULT")
    private Integer result;
}
