package com.bluntsoftware.saasy_service.service;

import com.bluntsoftware.saasy_service.dto.PaymentMethodDto;
import com.bluntsoftware.saasy_service.dto.TransactionDto;
import com.bluntsoftware.saasy_service.model.SaasySubscription;
import com.braintreegateway.Result;
import com.braintreegateway.Subscription;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PaymentService {
    String                  generateClientToken();
    Optional<Subscription>  createSubscription(SaasySubscription subscription);
    Optional<Subscription>  updateSubscription(String tenantId,SaasySubscription subscription);
    Optional<Subscription>  getSubscriptionByTenantId(String tenantId);
    Result<Subscription>    cancelSubscription(String tenantId);
    List<PaymentMethodDto>  setDefaultPaymentMethod(String tenantId, String paymentMethodToken);
    List<PaymentMethodDto>  getPaymentMethods(String tenantId);
    List<PaymentMethodDto>  removePaymentMethod(String tenantId,String paymentMethodToken);
    Map<String,Object>      getSubscriptionStatus(String tenantId);
    List<TransactionDto>    getTransactionHistory(String tenantId);
}
