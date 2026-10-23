package example;

import calculator.AddStrategy;
import calculator.Calculator;
import java.math.BigDecimal;

public final class DivisionDemo {
    private DivisionDemo() {
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator(new AddStrategy());
        BigDecimal left = new BigDecimal("12");
        BigDecimal right = new BigDecimal("4");
        System.out.println("Before switching: " + calculator.calculate(left, right));
        calculator.setStrategy(new DivideStrategy());
        System.out.println("After switching to division: " + calculator.calculate(left, right));
        System.out.println("Same Calculator; no original class was modified.");
        System.out.println("1 / 3 (DECIMAL64): "
                + calculator.calculate(BigDecimal.ONE, new BigDecimal("3")));
    }
}
