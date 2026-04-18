package org.kodekittu.repo;

import org.kodekittu.model.SIP;

import java.util.concurrent.ConcurrentHashMap;
import java.util.*;

public class SIPRepository {
    private final Map<String, SIP> sipStore = new ConcurrentHashMap<>();

    public void save(SIP sip) {
        sipStore.put(sip.getSipId(), sip);
    }

    public SIP get(String id) {
        return sipStore.get(id);
    }

    public List<SIP> getAll() {
        return new ArrayList<>(sipStore.values());
    }
}