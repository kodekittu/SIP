package org.kodekittu.service;

import org.kodekittu.enums.SIPFrequency;
import org.kodekittu.model.MutualFund;
import org.kodekittu.model.SIP;
import org.kodekittu.repo.SIPRepository;

import java.time.LocalDate;
import java.util.UUID;

public class SIPService {
    private final SIPRepository repo;

    public SIPService(SIPRepository repo) {
        this.repo = repo;
    }

    public SIP createSIP(String userId, MutualFund fund,
                         double amount, SIPFrequency frequency,
                         double stepUpPercent) {

        SIP sip = new SIP(
                UUID.randomUUID().toString(),
                userId,
                fund,
                amount,
                frequency,
                LocalDate.now(),
                stepUpPercent
        );

        repo.save(sip);
        return sip;
    }

    public void pause(String sipId) {
        repo.get(sipId).pause();
    }

    public void resume(String sipId) {
        repo.get(sipId).resume();
    }

    public void stop(String sipId) {
        repo.get(sipId).stop();
    }
}
