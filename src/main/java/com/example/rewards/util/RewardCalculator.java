package com.example.rewards.util;

/**
 * Utility implementing the retailer reward points calculation.
 *
 * Rules implemented here:
 * - 0 points for amounts <= 50
 * - 1 point per dollar for each whole dollar spent above 50 and up to 100
 * - 2 points per dollar for each whole dollar spent above 100
 *
 * Rounding policy: fractional dollars are rounded to the nearest whole number
 * when converting to points. This is a deliberate simple policy; if a more
 * conservative (floor) or generous (ceiling) strategy is required it can be
 * changed in a single place.
 */
public class RewardCalculator {
    /**
     * Calculate the reward points for a single transaction amount.
     *
     * @param amount transaction amount in dollars (may be fractional)
     * @return number of points earned (non-negative)
     */
    public static long pointsForAmount(double amount) {
        if (amount <= 50) return 0;
        long points = 0;
        if (amount > 100) {
            // points for amount above 100
            points += Math.round((amount - 100) * 2);
            // points for the 50..100 range is always 50 whole points
            points += 50;
        } else {
            // amount is between 50 and 100 (exclusive of <=50, inclusive of 100)
            points += Math.round(amount - 50);
        }
        return points;
    }
}
