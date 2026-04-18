package org.kodekittu;

import org.kodekittu.enums.SIPFrequency;
import org.kodekittu.executor.ExecutionEngine;
import org.kodekittu.model.MutualFund;
import org.kodekittu.model.SIP;
import org.kodekittu.repo.SIPRepository;
import org.kodekittu.service.PaymentService;
import org.kodekittu.service.PricingService;
import org.kodekittu.service.SIPService;
import org.kodekittu.utils.DateProvider;
import org.kodekittu.utils.FixedDateProvider;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        SIPRepository repo = new SIPRepository();
        SIPService sipService = new SIPService(repo);

        PricingService pricingService = new PricingService();
        PaymentService paymentService = new PaymentService();
        DateProvider dateProvider =
                new FixedDateProvider(LocalDate.of(2026, 3, 10));

        ExecutionEngine engine =
                new ExecutionEngine(repo, pricingService, paymentService, dateProvider);

        // Step 1: Create Fund
        MutualFund mf = new MutualFund("MF1", "Axis Bluechip");

        // Step 2: Create SIP
        SIP sip = sipService.createSIP(
                "user1", mf, 1000,
                SIPFrequency.MONTHLY, 10
        );

        System.out.println("Created SIP: " + sip.getSipId());

        // Step 3: View Portfolio
        System.out.println("All SIPs: " + repo.getAll().size());

        // Step 4: Simulate execution day
        LocalDate executionDate = sip.getStartDate().plusMonths(1);

        System.out.println("\n--- Executing SIP ---");
        engine.processOnce(executionDate);

        // Step 5: Pause SIP
        sipService.pause(sip.getSipId());
        System.out.println("\nSIP Paused");

        // Step 6: Try execution again (should NOT execute)
        System.out.println("\n--- Executing After Pause ---");
        engine.processOnce(executionDate.plusMonths(1));

        // Step 7: Unpause SIP
        sipService.unpause(sip.getSipId());
        System.out.println("\nSIP Unpaused");

        // Step 8: Execute again
        System.out.println("\n--- Executing After Unpause ---");
        engine.processOnce(executionDate.plusMonths(2));
    }
}