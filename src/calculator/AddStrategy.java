package calculator;

import java.math.BigDecimal;

public final class AddStrategy implements OperationStrategy {
    @Override
    public BigDecimal execute(BigDecimal left, BigDecimal right) {
        return left.add(right);
    }
}
