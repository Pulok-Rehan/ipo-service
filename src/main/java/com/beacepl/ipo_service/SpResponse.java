package com.beacepl.ipo_service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpResponse {
    @JsonProperty("P_IS_SUCCESS")
    private Integer is_success;
}
