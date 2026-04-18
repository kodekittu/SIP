package org.kodekittu.service;

public interface PaymentCallback {
    void onSuccess(String sipId, double amount);
    void onFailure(String sipId, double amount);
}
