package com.example.rewards.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link RewardCalculator} validating the basic reward rules.
 */
@DisplayName("RewardCalculator basic behavior")
public class RewardCalculatorTest {

    /**
     * Amounts at or below $50 should yield zero points.
     */
    @Test
    @DisplayName("0 points for amounts <= $50")
    void zeroForBelowOrEqual50() {
        assertEquals(0, RewardCalculator.pointsForAmount(50));
        assertEquals(0, RewardCalculator.pointsForAmount(30));
    }

    /** Verify 1-point-per-dollar behavior between $50 and $100. */
    @Test
    @DisplayName("1 point per dollar between 50 and 100")
    void calculatesBetween50And100() {
        assertEquals(25, RewardCalculator.pointsForAmount(75));
    }

    /** Verify 2-points-per-dollar above $100 plus the 50..100 block. */
    @Test
    @DisplayName("2 points per dollar above 100 + base 50")
    void calculatesAbove100() {
        // 50 points for 50..100 + 2*(amount-100)
        assertEquals(90, RewardCalculator.pointsForAmount(120));
    }
}
