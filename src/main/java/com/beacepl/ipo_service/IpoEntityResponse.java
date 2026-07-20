package com.beacepl.ipo_service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IpoEntityResponse {
//    private String ipoId;
    private String companyName;
    private String validity;
    private String recordDate;
    private int faceValue;
    private double premium;
    private double offerPrice;
    private double amountRemarks;
    private boolean eligibility;
    private BigDecimal maturedShareValue;
    private String remarksForNonEligibility;
    private String ledgerBalance;
    private String instrumentId;
    private String publicOfferId;
    private String accountId;
}
