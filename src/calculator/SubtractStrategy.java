package calculator;

import java.math.BigDecimal;

public final class SubtractStrategy implements OperationStrategy {
    @Override
    public BigDecimal execute(BigDecimal left, BigDecimal right) {
        return left.subtract(right);
    }
}
