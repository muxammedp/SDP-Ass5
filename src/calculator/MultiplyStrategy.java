package calculator;

import java.math.BigDecimal;

public final class MultiplyStrategy implements OperationStrategy {
    @Override
    public BigDecimal execute(BigDecimal left, BigDecimal right) {
        return left.multiply(right);
    }
}
