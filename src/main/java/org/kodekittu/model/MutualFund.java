package org.kodekittu.model;

public class MutualFund {
    private final String id;
    private final String name;

    public MutualFund(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }
}
