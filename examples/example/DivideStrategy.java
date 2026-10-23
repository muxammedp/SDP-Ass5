package example;

import calculator.OperationStrategy;
import java.math.BigDecimal;
import java.math.MathContext;

public final class DivideStrategy implements OperationStrategy {
    @Override
    public BigDecimal execute(BigDecimal left, BigDecimal right) {
        return left.divide(right, MathContext.DECIMAL64);
    }
}
