package com.beacepl.ipo_service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ipoApplication")
public class IpoApplication {
    @Id
    private String id;
    private String ipoId;
    private String companyName;
    private int quantityApplied;
    private int quantityAllocated;
    private int refundAmount;
    private double fineAmount;
    private double amount;
    private double appliedAmount;
    private double faceValue;
    private double premium;
    private double rate;
    private String initiatedOn;
    private String status;
    private String executedOn;
    private String investorCode;
    private String instrumentId;
    private String accountId;
    private String offerId;
}
