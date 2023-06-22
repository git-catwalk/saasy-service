package com.bluntsoftware.saasy_service.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaasySubscription {
    private String id;
    private String appId;
    private String firstName;
    private String lastName;
    private String email;
    private String companyName;
    private String nonce;
    private BigDecimal chargeAmount;
    private String planId;
}
