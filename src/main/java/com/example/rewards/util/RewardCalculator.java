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
    private static final BigDecimal TWO = new BigDecimal("2");
    private static final BigDecimal ZERO = new BigDecimal("0.00");

    /**
     * Calculate reward points for a monetary amount.
     * <p>
     * Rules:
     * - amounts &le; 50 -> 0.00 points
     * - amounts between 50 and 100 -> 1 point per dollar above 50
     * - amounts above 100 -> 2 points per dollar above 100 plus 50
     *
     * @param amount monetary amount (may be null)
     * @return points with a scale of 2
     */
    public BigDecimal pointsForAmount(BigDecimal amount) {
        if (amount == null) {
            return ZERO;
        }

        BigDecimal normalized = amount.setScale(2, RoundingMode.HALF_UP);
        if (normalized.compareTo(FIFTY) <= 0) {
            return ZERO;
        }

        BigDecimal points;
        if (normalized.compareTo(ONE_HUNDRED) > 0) {
            BigDecimal above100 = normalized.subtract(ONE_HUNDRED);
            points = above100.multiply(TWO).add(new BigDecimal("50"));
        } else {
            points = normalized.subtract(FIFTY);
        }

        return points.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Convenience overload accepting a primitive {@code double} amount.
     *
     * @param amount value in decimal dollars
     * @return computed points
     */
    public BigDecimal pointsForAmount(double amount) {
        return pointsForAmount(BigDecimal.valueOf(amount));
    }
}
