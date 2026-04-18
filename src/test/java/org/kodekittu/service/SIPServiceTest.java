package org.kodekittu.service;

import org.junit.jupiter.api.Test;
import org.kodekittu.enums.SIPFrequency;
import org.kodekittu.enums.SIPStatus;
import org.kodekittu.executor.ExecutionEngine;
import org.kodekittu.model.MutualFund;
import org.kodekittu.model.SIP;
import org.kodekittu.repo.SIPRepository;
import org.kodekittu.utils.FixedDateProvider;

import java.time.LocalDate;

import static org.junit.Assert.*;

class SIPServiceTest {

    @Test
    void shouldCreateSIPSuccessfully() {
        SIPRepository repo = new SIPRepository();
        SIPService service = new SIPService(repo);

        MutualFund mf = new MutualFund("MF1", "Test Fund");

        SIP sip = service.createSIP("user1", mf, 1000,
                SIPFrequency.MONTHLY, 0);

        assertNotNull(sip);
        assertEquals(1000, sip.getAmount(), 0);
        assertEquals(SIPStatus.ACTIVE, sip.isActive() ? SIPStatus.ACTIVE : SIPStatus.PAUSED);
        assertEquals(1, repo.getAll().size());
    }

    @Test
    void shouldPauseAndUnpauseSIP() {
        SIPRepository repo = new SIPRepository();
        SIPService service = new SIPService(repo);

        MutualFund mf = new MutualFund("MF1", "Test Fund");

        SIP sip = service.createSIP("user1", mf, 1000,
                SIPFrequency.MONTHLY, 0);

        service.pause(sip.getSipId());
        assertFalse(sip.isActive());

        service.unpause(sip.getSipId());
        assertTrue(sip.isActive());
    }

    @Test
    void shouldApplyStepUpCorrectly() {
        MutualFund mf = new MutualFund("MF1", "Test Fund");

        SIP sip = new SIP("1", "user1", mf,
                1000, SIPFrequency.MONTHLY,
                LocalDate.now(), 10);

        sip.applyStepUp();

        assertEquals(1100, sip.getAmount(), 0.01);
    }

    @Test
    void shouldExecuteMonthlySIPOnSameDay() {

        SIPRepository repo = new SIPRepository();
        PricingService pricingService = new PricingService();
        PaymentService paymentService = new PaymentService();

        LocalDate startDate = LocalDate.of(2025, 1, 10);

        SIP sip = new SIP("1", "user1",
                new MutualFund("MF1", "Test"),
                1000,
                SIPFrequency.MONTHLY,
                startDate,
                0);

        repo.save(sip);

        ExecutionEngine engine = new ExecutionEngine(
                repo,
                pricingService,
                paymentService,
                new FixedDateProvider(LocalDate.of(2025, 2, 10))
        );

        // Instead of calling private method, expose for testing
        boolean result = engine.shouldExecute(sip, LocalDate.of(2025, 2, 10));

        assertTrue(result);
    }

    @Test
    void shouldExecuteWeeklySIPOnSameWeekday() {

        LocalDate startDate = LocalDate.of(2025, 1, 7); // Tuesday

        SIP sip = new SIP("1", "user1",
                new MutualFund("MF1", "Test"),
                1000,
                SIPFrequency.WEEKLY,
                startDate,
                0);

        ExecutionEngine engine = new ExecutionEngine(
                new SIPRepository(),
                new PricingService(),
                new PaymentService(),
                new FixedDateProvider(LocalDate.of(2025, 1, 14)) // next Tuesday
        );

        boolean result = engine.shouldExecute(sip, LocalDate.of(2025, 1, 14));

        assertTrue(result);
    }
}
