package org.kodekittu.executor;

import org.kodekittu.model.SIP;
import org.kodekittu.repo.SIPRepository;
import org.kodekittu.service.PaymentCallback;
import org.kodekittu.service.PaymentService;
import org.kodekittu.service.PricingService;
import org.kodekittu.utils.DateProvider;

import java.time.LocalDate;
import java.util.List;

public class ExecutionEngine {

    private final SIPRepository repo;
    private final PricingService pricingService;
    private final PaymentService paymentService;
    private final DateProvider dateProvider;

    public ExecutionEngine(SIPRepository repo,
                           PricingService pricingService,
                           PaymentService paymentService,
                           DateProvider dateProvider) {
        this.repo = repo;
        this.pricingService = pricingService;
        this.paymentService = paymentService;
        this.dateProvider = dateProvider;
    }

    /**
     * Entry point for execution using system/current date
     */
    public void processOnce() {
        LocalDate today = dateProvider.today();
        processOnce(today);
    }

    /**
     * Overloaded method for test/demo where date can be controlled
     */
    public void processOnce(LocalDate today) {
        List<SIP> sips = repo.getAll();

        for (SIP sip : sips) {
            if (!sip.isActive()) {
                continue;
            }

            if (shouldExecute(sip, today)) {
                execute(sip);
            }
        }
    }

    /**
     * Decides whether SIP should run on a given date
     */
    public boolean shouldExecute(SIP sip, LocalDate today) {
        LocalDate start = sip.getStartDate();

        switch (sip.getFrequency()) {
            case WEEKLY:
                return today.getDayOfWeek() == start.getDayOfWeek();

            case MONTHLY:
                return today.getDayOfMonth() == start.getDayOfMonth();

            case QUARTERLY:
                return today.getDayOfMonth() == start.getDayOfMonth()
                        && (today.getMonthValue() - start.getMonthValue()) % 3 == 0;

            default:
                return false;
        }
    }

    /**
     * Executes SIP installment
     */
    private void execute(SIP sip) {
        double nav = pricingService.getLatestNAV(sip.getFund().id());
        double amount = sip.getAmount();
        double units = amount / nav;

        System.out.println("Executing SIP: " + sip.getSipId()
                + " | Amount: " + amount
                + " | NAV: " + nav
                + " | Units: " + units);

        paymentService.processPayment(
                sip.getSipId(),
                amount,
                new PaymentCallback() {
                    @Override
                    public void onSuccess(String sipId, double amt) {
                        System.out.println("Payment SUCCESS for SIP: " + sipId);

                        // Apply step-up after successful installment
                        sip.applyStepUp();
                    }

                    @Override
                    public void onFailure(String sipId, double amt) {
                        System.out.println("Payment FAILED for SIP: " + sipId);
                    }
                }
        );
    }
}