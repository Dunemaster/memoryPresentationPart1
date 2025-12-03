package com.pega.learning.memoryopt;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 *  A unicorn is a company that reached a valuation of
 *  1 billion US dollars. For some reason we want to
 */
public final class UnicornInfo {

    final boolean isUnicorn;

    /**
     * The year when the company reached unicorn status.
     * Can be {@code null} if the company is not a unicorn or the year is not available.
     */
    @Nullable
    final Integer unicornStatusReachedYear;

    /**
     *  Market capitalization in billions USD.
     *  Can be {@code null} if the company is not a unicorn or the year is not available.
     */
    @Nullable
    final Integer marketCapInBillions;

    public UnicornInfo(boolean isUnicorn, Integer unicornStatusReachedYear,
                       Integer marketCapInBillions) {
        this.isUnicorn = isUnicorn;
        this.marketCapInBillions = marketCapInBillions;
        if (unicornStatusReachedYear != null) {
            if (unicornStatusReachedYear < 0) {
                throw new IllegalArgumentException("unicornStatusReachedYear cannot be negative");
            }
            if (marketCapInBillions < 0) {
                throw new IllegalArgumentException("marketCapInBillions cannot be negative");
            }
        }

        this.unicornStatusReachedYear = unicornStatusReachedYear;
    }

    public static UnicornInfo createUnicornInfo(boolean isUnicorn, Integer unicornStatusReachedYear,
                                                Integer marketCapInBillions) {
        if (!isUnicorn) {
            return NOT_A_UNICORN;
        }
        return new UnicornInfo(true, unicornStatusReachedYear, marketCapInBillions);
    }

    private static final UnicornInfo NOT_A_UNICORN =
            new UnicornInfo(false, null, null);
}
