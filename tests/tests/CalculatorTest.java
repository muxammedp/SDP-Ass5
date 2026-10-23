package tests;

import calculator.AddStrategy;
import calculator.Calculator;
import calculator.MultiplyStrategy;
import calculator.OperationStrategy;
import calculator.SubtractStrategy;
import example.DivideStrategy;
import java.math.BigDecimal;

public final class CalculatorTest {
    private static int checks;

    private CalculatorTest() {
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator(new AddStrategy());
        equal("addition", "16", calculator.calculate(number("12"), number("4")));
        equal("exact decimal addition", "0.3", calculator.calculate(number("0.1"), number("0.2")));
        equal("mixed signs", "-3", calculator.calculate(number("-8"), number("5")));
        equal("beyond long range", "9223372036854775808",
                calculator.calculate(number("9223372036854775807"), BigDecimal.ONE));
        calculator.setStrategy(new SubtractStrategy());
        equal("runtime switch to subtraction", "8", calculator.calculate(number("12"), number("4")));
        equal("operand order", "-8", calculator.calculate(number("4"), number("12")));
        equal("decimal subtraction", "0.7", calculator.calculate(number("1"), number("0.3")));
        calculator.setStrategy(new MultiplyStrategy());
        equal("runtime switch to multiplication", "48", calculator.calculate(number("12"), number("4")));
        equal("zero", "0", calculator.calculate(number("12"), BigDecimal.ZERO));
        equal("negative multiplication", "-10", calculator.calculate(number("-2.5"), number("4")));
        equal("two negatives", "10", calculator.calculate(number("-2.5"), number("-4")));
        calculator.setStrategy(new AddStrategy());
        equal("switch back", "16", calculator.calculate(number("12"), number("4")));
        throwsType("null constructor", NullPointerException.class, () -> new Calculator(null));
        throwsType("null replacement", NullPointerException.class, () -> calculator.setStrategy(null));
        equal("failed replacement preserves strategy", "5", calculator.calculate(number("2"), number("3")));
        throwsType("null left operand", NullPointerException.class,
                () -> calculator.calculate(null, BigDecimal.ONE));
        throwsType("null right operand", NullPointerException.class,
                () -> calculator.calculate(BigDecimal.ONE, null));
        verifyDelegation();
        calculator.setStrategy(new DivideStrategy());
        equal("extension division", "3", calculator.calculate(number("12"), number("4")));
        equal("repeating division", "0.3333333333333333",
                calculator.calculate(BigDecimal.ONE, number("3")));
        equal("negative division", "-2.5", calculator.calculate(number("-10"), number("4")));
        throwsType("division by zero", ArithmeticException.class,
                () -> calculator.calculate(BigDecimal.ONE, BigDecimal.ZERO));
        equal("usable after failed division", "2", calculator.calculate(number("8"), number("4")));
        System.out.printf("SUMMARY: %d/%d PASS%n", checks, checks);
    }

    private static void verifyDelegation() {
        BigDecimal left = number("2");
        BigDecimal right = number("7");
        BigDecimal sentinel = number("123.456");
        int[] calls = {0};
        OperationStrategy custom = (first, second) -> {
            if (first != left || second != right) {
                throw new AssertionError("Context changed or reordered operands.");
            }
            calls[0]++;
            return sentinel;
        };
        Calculator calculator = new Calculator(custom);
        if (calculator.calculate(left, right) != sentinel || calls[0] != 1) {
            throw new AssertionError("Context must delegate exactly once and return the result.");
        }
        pass("unknown strategy delegates once with original operands");
    }

    private static BigDecimal number(String value) {
        return new BigDecimal(value);
    }

    private static void equal(String label, String expected, BigDecimal actual) {
        if (number(expected).compareTo(actual) != 0) {
            throw new AssertionError(label + ": expected " + expected + ", got " + actual);
        }
        pass(label);
    }

    private static void throwsType(String label, Class<? extends Throwable> type, Runnable action) {
        try {
            action.run();
        } catch (Throwable exception) {
            if (!type.isInstance(exception)) {
                throw new AssertionError(label + ": wrong exception", exception);
            }
            pass(label);
            return;
        }
        throw new AssertionError(label + ": expected " + type.getSimpleName());
    }

    private static void pass(String label) {
        checks++;
        System.out.println("PASS: " + label);
    }
}
