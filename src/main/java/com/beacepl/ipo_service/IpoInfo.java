package com.beacepl.ipo_service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class IpoInfo {
    @JsonProperty("INSTRUMENT_NAME")
    private String instrumentName;
    @JsonProperty("FACE_VALUE")
    private String faceValue;
    @JsonProperty("PREMIUM")
    private String premium;
    @JsonProperty("LAST_APPLICATION_DATE")
    private String lastApplicationDate;
    @JsonProperty("RECORD_DATE")
    private String recordDate;
    @JsonProperty("MINIMUM_AMOUNT")
    private String minumumAmount;
    @JsonProperty("MINIMUM_MARKET_VALUE")
    private String minimumMarketValue;
    @JsonProperty("MULTIPLY_BY")
    private BigDecimal multiplyBy;
    @JsonProperty("MATURED_VAL_RDT")
    private BigDecimal maturedValueRdt;
    @JsonProperty("ELIGIBILITY")
    private BigDecimal eligibility;
    @JsonProperty("LEDGER_BALANCE")
    private BigDecimal ledgerBalance;
    @JsonProperty("INSTRUMENT_ID")
    private int instrumentId;
    @JsonProperty("PUBLIC_OFFER_ID")
    private int publicOfferId;
    @JsonProperty("ACCOUNT_ID")
    private int accountId;
}
