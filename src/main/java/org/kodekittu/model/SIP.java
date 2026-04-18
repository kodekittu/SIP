package org.kodekittu.model;

import org.kodekittu.enums.SIPFrequency;
import org.kodekittu.enums.SIPStatus;

import java.time.LocalDate;

public class SIP {
    private final String sipId;
    private final String userId;
    private final MutualFund fund;
    private double amount;
    private final SIPFrequency frequency;
    private SIPStatus status;
    private final LocalDate startDate;

    // Bonus: step-up %
    private final double stepUpPercent;

    public SIP(String sipId, String userId, MutualFund fund,
               double amount, SIPFrequency frequency,
               LocalDate startDate, double stepUpPercent) {
        this.sipId = sipId;
        this.userId = userId;
        this.fund = fund;
        this.amount = amount;
        this.frequency = frequency;
        this.startDate = startDate;
        this.status = SIPStatus.ACTIVE;
        this.stepUpPercent = stepUpPercent;
    }

    public boolean isActive() {
        return status == SIPStatus.ACTIVE;
    }

    public void pause() { this.status = SIPStatus.PAUSED; }
    public void resume() { this.status = SIPStatus.ACTIVE; }
    public  void stop() { this.status = SIPStatus.STOPPED; }

    public void applyStepUp() {
        if (stepUpPercent > 0) {
            amount += amount * stepUpPercent / 100;
        }
    }

    // getters
    public String getSipId() { return sipId; }
    public double getAmount() { return amount; }
    public SIPFrequency getFrequency() { return frequency; }
    public LocalDate getStartDate() { return startDate; }
    public MutualFund getFund() { return fund; }
}
