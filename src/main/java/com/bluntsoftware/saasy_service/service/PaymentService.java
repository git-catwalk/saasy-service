package com.bluntsoftware.saasy_service.service;

import com.bluntsoftware.saasy_service.model.SaasySubscription;
import com.braintreegateway.PaymentMethod;
import com.braintreegateway.Result;
import com.braintreegateway.Subscription;
import com.braintreegateway.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PaymentService {
    String generateClientToken();
    Optional<Subscription> createSubscription(SaasySubscription subscription);
    Optional<Subscription> updateSubscription(String tenantId,SaasySubscription subscription);
    Optional<Subscription> getSubscriptionByTenantId(String tenantId);
    Result<Subscription>   cancelSubscription(String tenantId);
    List<PaymentMethod>    setDefaultPaymentMethod(String tenantId,String paymentMethodToken);
    List<PaymentMethod>    getPaymentMethods(String tenantId);
    List<PaymentMethod>    removePaymentMethod(String tenantId,String paymentMethodToken);
    Map<String,Object>     getSubscriptionStatus(String tenantId);
    List<Transaction>      getTransactionHistory(String tenantId);
}
