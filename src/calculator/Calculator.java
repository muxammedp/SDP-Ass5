package calculator;

import java.math.BigDecimal;
import java.util.Objects;

public final class Calculator {
    private OperationStrategy strategy;

    public Calculator(OperationStrategy strategy) {
        setStrategy(strategy);
    }

    public void setStrategy(OperationStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy, "Strategy must not be null.");
    }

    public BigDecimal calculate(BigDecimal left, BigDecimal right) {
        Objects.requireNonNull(left, "Left operand must not be null.");
        Objects.requireNonNull(right, "Right operand must not be null.");
        return strategy.execute(left, right);
    }
}
