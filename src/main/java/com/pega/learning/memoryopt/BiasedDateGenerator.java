package com.pega.learning.memoryopt;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;

public class BiasedDateGenerator {

    private static final LocalDate RANGE_START = LocalDate.of(1950, 1, 1);
    private static final LocalDate RANGE_END = LocalDate.of(2025, 1, 1);
    private static final long TOTAL_DAYS = ChronoUnit.DAYS.between(RANGE_START, RANGE_END);
    private static final long MEAN_OFFSET = ChronoUnit.DAYS.between(
            RANGE_START, LocalDate.of(1975, 1, 1));

    private static final double STD_DEV_DAYS = ChronoUnit.DAYS.between(
            LocalDate.of(1950, 1, 1), LocalDate.of(2000, 1, 1)) / 3.0;

    public static LocalDate dateBetween1950And2025() {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        long offset;
        do {
            double gaussian = rnd.nextGaussian() * STD_DEV_DAYS + MEAN_OFFSET;
            offset = Math.round(gaussian);
        } while (offset < 0 || offset > TOTAL_DAYS);
        return RANGE_START.plusDays(offset);
    }

}
