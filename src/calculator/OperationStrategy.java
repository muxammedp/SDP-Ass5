package calculator;

import java.math.BigDecimal;

@FunctionalInterface
public interface OperationStrategy {
    BigDecimal execute(BigDecimal left, BigDecimal right);
}
