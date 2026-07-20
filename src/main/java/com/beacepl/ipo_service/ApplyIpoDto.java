package com.beacepl.ipo_service;

import lombok.Data;

@Data
public class ApplyIpoDto {
    private String accountId;
    private String instrumentId;
    private double rate;
    private String amount;
    private String offerId;
    private String date;
}
