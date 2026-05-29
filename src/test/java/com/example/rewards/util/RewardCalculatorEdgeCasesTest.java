package com.example.rewards.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Edge-case tests for {@link RewardCalculator}.
 */
@DisplayName("RewardCalculator edge cases")
public class RewardCalculatorEdgeCasesTest {

    /** Ensure fractional amounts are rounded to the nearest integer when computing points. */
    @Test
    @DisplayName("fractional amounts round to nearest")
    void fractionalAmountsRoundNearest() {
        // 75.4 -> 25 points (75.4-50 = 25.4 -> round 25)
        assertEquals(25, RewardCalculator.pointsForAmount(75.4));
        // 75.6 -> 26 points
        assertEquals(26, RewardCalculator.pointsForAmount(75.6));
    }

    /** Large amounts scale linearly according to the rules. */
    @Test
    @DisplayName("large amounts scale correctly")
    void largeAmountsScaleCorrectly() {
        // For 1000 dollars: 50 points for 50..100 + 2*(900) = 1800 -> total 1850
        assertEquals(1850, RewardCalculator.pointsForAmount(1000));
    }

    /** Negative or zero amounts should return zero points. */
    @Test
    @DisplayName("negative and zero amounts return zero")
    void negativeOrZeroAmountsReturnZero() {
        assertEquals(0, RewardCalculator.pointsForAmount(0));
        assertEquals(0, RewardCalculator.pointsForAmount(-10));
    }
}
