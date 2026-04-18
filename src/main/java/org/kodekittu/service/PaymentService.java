package org.kodekittu.service;

public class PaymentService {
    public void processPayment(String sipId, double amount, PaymentCallback callback) {
        // simulate async success
        new Thread(() -> {
            try {
                Thread.sleep(100);
                callback.onSuccess(sipId, amount);
            } catch (Exception e) {
                callback.onFailure(sipId, amount);
            }
        }).start();
    }
}
