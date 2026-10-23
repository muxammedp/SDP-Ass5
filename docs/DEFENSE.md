# Strategy Calculator - defense preparation

**Zhuman Mukhammed | SE-2522**

## Short introduction

"My project demonstrates the Strategy pattern using a calculator. `OperationStrategy` defines a common execution method. Addition, subtraction, and multiplication implement it. `Calculator` stores the interface and delegates calculation. The client selects a strategy and can replace it at runtime on the same calculator object."

## Live demonstration

Set `JAVA_HOME` to your installed JDK 17, open a terminal in the project folder, then run:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\run.ps1 demo
powershell -NoProfile -ExecutionPolicy Bypass -File .\run.ps1 run
```

Enter `add`, `12`, `4`; then `subtract`, `12`, `4`; then `multiply`, `12`, `4`. Results are 16, 8, and 48. Enter `exit` to finish. Explain that `runInteractive` creates its `Calculator` before the loop, so each operation uses the same context object.

Show these files in order:

1. `src/calculator/OperationStrategy.java`: one shared method signature.
2. `src/calculator/AddStrategy.java`: one concrete algorithm.
3. `src/calculator/Calculator.java`: interface field, setter, delegated execution.
4. `src/app/Main.java`: strategy map and client selection.
5. `examples/example/DivideStrategy.java` and `DivisionDemo.java`: extension without editing original classes.

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\run.ps1 extension
powershell -NoProfile -ExecutionPolicy Bypass -File .\run.ps1 test
```

The extension prints 16 before switching and 3 after switching. Division of 1 by 3 prints 0.3333333333333333.

## Object creation example

```java
Calculator calculator = new Calculator(new AddStrategy());
BigDecimal left = new BigDecimal("12");
BigDecimal right = new BigDecimal("4");
System.out.println(calculator.calculate(left, right));
calculator.setStrategy(new MultiplyStrategy());
System.out.println(calculator.calculate(left, right));
```

This prints 16 and 48. The setter replaces the strategy reference, not the calculator object.

## Likely questions

| Question | Answer grounded in this project |
| --- | --- |
| What problem does Strategy solve? | It separates a family of replaceable algorithms from the object that uses them. |
| Where is polymorphism? | `strategy.execute(left, right)` invokes the implementation belonging to the referenced object. |
| Does the context know `AddStrategy`? | No. It only knows `OperationStrategy`. The client creates concrete implementations. |
| Why keep `setStrategy`? | It allows the client to change behavior after the context has been created. |
| Why not subclass Calculator? | Composition allows one existing calculator to change behavior without replacing its class or instance. |
| Are all conditionals forbidden? | No. Validation and CLI control flow may branch. The context must not branch on strategy type. |
| How is Strategy different from State? | Strategy selects a calculation policy. State changes behavior according to lifecycle state and transitions. |
| How do you add an algorithm? | Implement the interface and supply the new object from a client. `DivideStrategy` demonstrates this. |
| Is changing the menu a violation of Open/Closed? | Registering a strategy is client configuration. Existing calculation logic and context remain unchanged. |
| Why BigDecimal? | It represents input decimal values directly. For example, 0.1 plus 0.2 produces 0.3. |
| Why build BigDecimal from strings? | Constructing it from a binary floating-point value may preserve that value's approximation. |
| Why DECIMAL64 for division? | A repeating quotient needs a rounding policy. Here it is 16 significant digits with HALF_EVEN rounding. |
| What happens with a null strategy? | `Objects.requireNonNull` throws before the field is replaced, preserving the previous valid strategy. |
| What does the custom strategy test prove? | An implementation unknown to the context is called once, with the original operands, and its result is returned unchanged. |
| What is the tradeoff? | More small classes and configuration in exchange for independent, interchangeable algorithms. |

## Practice instructor tasks

1. Write a `MaximumStrategy` using `left.max(right)` and test it with a new client.
2. Switch from multiplication back to addition on the same calculator.
3. Explain why subtraction must preserve operand order.
4. Add a new strategy to the client map and explain why the context stays unchanged.
5. Predict what happens when division receives zero as its right operand.

Russian reminders: Context хранит ссылку на интерфейс; Client выбирает реализацию; Strategy выполняет алгоритм. Смена стратегии меняет поведение того же объекта Calculator. State нужен для состояний жизненного цикла и переходов между ними.

Review the code and rewrite the reflection in your own words before submission. The report records the assistance used; do not describe generated work as independently authored.
