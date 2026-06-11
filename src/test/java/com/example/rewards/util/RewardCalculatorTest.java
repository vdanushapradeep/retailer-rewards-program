package com.example.rewards.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link RewardCalculator} validating the basic reward rules.
 */
@DisplayName("RewardCalculator behavior")
public class RewardCalculatorTest {

    /**
     * Amounts at or below $50 should yield zero points.
     */
    @Test
    @DisplayName("0 points for amounts <= $50")
    void zeroForBelowOrEqual50() {
        var calc = new RewardCalculator();
        assertEquals(0, calc.pointsForAmount(50));
        assertEquals(0, calc.pointsForAmount(30));
    }

    /** Verify 1-point-per-dollar behavior between $50 and $100. */
    @Test
    @DisplayName("1 point per dollar between 50 and 100")
    void calculatesBetween50And100() {
        var calc = new RewardCalculator();
        assertEquals(25, calc.pointsForAmount(75));
    }

    /** Verify 2-points-per-dollar above $100 plus the 50..100 block. */
    @Test
    @DisplayName("2 points per dollar above 100 + base 50")
    void calculatesAbove100() {
        // 50 points for 50..100 + 2*(amount-100)
        var calc = new RewardCalculator();
        assertEquals(90, calc.pointsForAmount(120));
    }

    /** Ensure fractional amounts are rounded to the nearest integer when computing points. */
    @Test
    @DisplayName("fractional amounts round to nearest")
    void fractionalAmountsRoundNearest() {
        var calc = new RewardCalculator();
        // 75.4 -> 25 points (75.4-50 = 25.4 -> round 25)
        assertEquals(25, calc.pointsForAmount(75.4));
        // 75.6 -> 26 points
        assertEquals(26, calc.pointsForAmount(75.6));
    }

    /** Large amounts scale linearly according to the rules. */
    @Test
    @DisplayName("large amounts scale correctly")
    void largeAmountsScaleCorrectly() {
        // For 1000 dollars: 50 points for 50..100 + 2*(900) = 1800 -> total 1850
        var calc = new RewardCalculator();
        assertEquals(1850, calc.pointsForAmount(1000));
    }

    /** Negative or zero amounts should return zero points. */
    @Test
    @DisplayName("negative and zero amounts return zero")
    void negativeOrZeroAmountsReturnZero() {
        var calc = new RewardCalculator();
        assertEquals(0, calc.pointsForAmount(0));
        assertEquals(0, calc.pointsForAmount(-10));
    }

    @Test
    @DisplayName("null BigDecimal amount returns zero")
    void nullBigDecimalReturnsZero() {
        var calc = new RewardCalculator();
        assertEquals(0, calc.pointsForAmount((java.math.BigDecimal) null));
    }
}
