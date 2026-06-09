package com.example.rewards.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Injectable reward points calculator. Uses BigDecimal for currency safety.
 */
@Component
public class RewardCalculator {

    private static final BigDecimal FIFTY = new BigDecimal("50");
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    public long pointsForAmount(BigDecimal amount) {
        /**
         * Calculate reward points for a monetary amount.
         * <p>
         * Rules:
         * - amounts &le; 50 -> 0 points
         * - amounts between 50 and 100 -> 1 point per dollar above 50
         * - amounts above 100 -> 2 points per dollar above 100 plus 50
         *
         * @param amount monetary amount (may be null)
         * @return points as a long value
         */
        if (amount == null)
            return 0L;
        BigDecimal rounded = amount.setScale(0, RoundingMode.HALF_UP);
        if (rounded.compareTo(FIFTY) <= 0)
            return 0L;

        long points = 0L;
        if (rounded.compareTo(ONE_HUNDRED) > 0) {
            BigDecimal above100 = rounded.subtract(ONE_HUNDRED);
            points += above100.longValueExact() * 2L;
            points += 50L;
        } else {
            BigDecimal above50 = rounded.subtract(FIFTY);
            points += above50.longValueExact();
        }
        return points;
    }

    /**
     * Convenience overload accepting a primitive {@code double} amount.
     *
     * @param amount value in decimal dollars
     * @return computed points
     */
    public long pointsForAmount(double amount) {
        return pointsForAmount(BigDecimal.valueOf(amount));
    }
}
