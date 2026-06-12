package com.example.rewards.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

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
        assertEquals(new BigDecimal("0.00"), calc.pointsForAmount(50));
        assertEquals(new BigDecimal("0.00"), calc.pointsForAmount(30));
    }

    /** Verify 1-point-per-dollar behavior between $50 and $100. */
    @Test
    @DisplayName("1 point per dollar between 50 and 100")
    void calculatesBetween50And100() {
        var calc = new RewardCalculator();
        assertEquals(new BigDecimal("25.00"), calc.pointsForAmount(75));
    }

    /** Verify 2-points-per-dollar above $100 plus the 50..100 block. */
    @Test
    @DisplayName("2 points per dollar above 100 + base 50")
    void calculatesAbove100() {
        var calc = new RewardCalculator();
        assertEquals(new BigDecimal("90.00"), calc.pointsForAmount(120));
    }

    /** Ensure fractional amounts preserve decimal point values in reward calculations. */
    @Test
    @DisplayName("fractional amounts preserve decimal reward points")
    void fractionalAmountsPreserveDecimals() {
        var calc = new RewardCalculator();
        assertEquals(new BigDecimal("25.40"), calc.pointsForAmount(75.4));
        assertEquals(new BigDecimal("25.60"), calc.pointsForAmount(75.6));
    }

    /** Large amounts scale linearly according to the rules. */
    @Test
    @DisplayName("large amounts scale correctly")
    void largeAmountsScaleCorrectly() {
        var calc = new RewardCalculator();
        assertEquals(new BigDecimal("1850.00"), calc.pointsForAmount(1000));
    }

    /** Negative or zero amounts should return zero points. */
    @Test
    @DisplayName("negative and zero amounts return zero")
    void negativeOrZeroAmountsReturnZero() {
        var calc = new RewardCalculator();
        assertEquals(new BigDecimal("0.00"), calc.pointsForAmount(0));
        assertEquals(new BigDecimal("0.00"), calc.pointsForAmount(-10));
    }

    @Test
    @DisplayName("null BigDecimal amount returns zero")
    void nullBigDecimalReturnsZero() {
        var calc = new RewardCalculator();
        assertEquals(new BigDecimal("0.00"), calc.pointsForAmount((BigDecimal) null));
    }
}
