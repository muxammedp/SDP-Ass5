package app;

import calculator.AddStrategy;
import calculator.Calculator;
import calculator.MultiplyStrategy;
import calculator.OperationStrategy;
import calculator.SubtractStrategy;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            runInteractive();
        } else if (args.length == 1 && args[0].equals("--demo")) {
            runDemo();
        } else if (args.length == 1 && args[0].equals("--help")) {
            System.out.println("Usage: java -cp out app.Main [--demo | --help]");
        } else {
            System.err.println("Unknown arguments. Use --help.");
            System.exit(2);
        }
    }

    private static Map<String, OperationStrategy> createStrategies() {
        Map<String, OperationStrategy> strategies = new LinkedHashMap<>();
        strategies.put("add", new AddStrategy());
        strategies.put("subtract", new SubtractStrategy());
        strategies.put("multiply", new MultiplyStrategy());
        return strategies;
    }

    private static void runDemo() {
        Calculator calculator = new Calculator(new AddStrategy());
        BigDecimal left = new BigDecimal("12");
        BigDecimal right = new BigDecimal("4");
        System.out.println("Strategy Calculator | Zhuman Mukhammed | SE-2522");
        System.out.println("One Calculator object; strategies switch at runtime.");
        for (Map.Entry<String, OperationStrategy> entry : createStrategies().entrySet()) {
            calculator.setStrategy(entry.getValue());
            System.out.printf("%s(%s, %s) = %s%n", entry.getKey(), left, right,
                    calculator.calculate(left, right).toPlainString());
        }
    }

    private static void runInteractive() {
        Map<String, OperationStrategy> strategies = createStrategies();
        Calculator calculator = new Calculator(new AddStrategy());
        System.out.println("Strategy Calculator | Zhuman Mukhammed | SE-2522");
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Operations: " + String.join(", ", strategies.keySet()) + ", exit");
                String command = readLine(scanner, "Operation: ");
                if (command == null || command.equalsIgnoreCase("exit")) {
                    return;
                }
                OperationStrategy selected = strategies.get(command.toLowerCase(Locale.ROOT));
                if (selected == null) {
                    System.out.println("Unknown operation. Choose a listed operation.");
                    continue;
                }
                String left = readLine(scanner, "First number: ");
                if (left == null) {
                    return;
                }
                String right = readLine(scanner, "Second number: ");
                if (right == null) {
                    return;
                }
                try {
                    BigDecimal first = new BigDecimal(left);
                    BigDecimal second = new BigDecimal(right);
                    calculator.setStrategy(selected);
                    System.out.println("Result: " + calculator.calculate(first, second).toPlainString());
                } catch (NumberFormatException exception) {
                    System.out.println("Invalid number. Use decimal notation such as -12.5.");
                }
            }
        }
    }

    private static String readLine(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.hasNextLine() ? scanner.nextLine().trim() : null;
    }
}
