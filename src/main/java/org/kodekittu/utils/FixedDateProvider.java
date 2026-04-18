package org.kodekittu.utils;

import java.time.LocalDate;

public class FixedDateProvider implements DateProvider {
    private final LocalDate date;

    public FixedDateProvider(LocalDate date) {
        this.date = date;
    }

    public LocalDate today() {
        return date;
    }
}