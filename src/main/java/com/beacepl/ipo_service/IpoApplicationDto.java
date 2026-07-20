package com.beacepl.ipo_service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IpoApplicationDto {
    private String companyName;
    private double faceValue;
    private double premium;
    private double amount;
    private String date;
    private String ipoId;
    private String investorCode;
    private String accountId;
    private String instrumentId;
    private String offerId;
}
