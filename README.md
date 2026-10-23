# Assignment 5: Strategy Calculator

**Student:** Zhuman Mukhammed  
**Group:** SE-2522  
**Course:** Software Design Patterns, 2026-2027  
**Topic:** Option B - Calculator

A Java 17 console application with interchangeable addition, subtraction, and multiplication algorithms. One `Calculator` object changes its strategy at runtime. A separate division example demonstrates extension without modifying any original source file.

## Requirements

JDK 17 or newer. No Maven, Gradle, database, or external Java libraries are needed. Java 8 cannot run this project. Set `JAVA_HOME` to an installed JDK and use its matching `java` and `javac` executables.

## Windows PowerShell

From this folder, with `JAVA_HOME` configured:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\run.ps1 demo
powershell -NoProfile -ExecutionPolicy Bypass -File .\run.ps1 run
powershell -NoProfile -ExecutionPolicy Bypass -File .\run.ps1 test
powershell -NoProfile -ExecutionPolicy Bypass -File .\run.ps1 extension
```

The policy override applies only to the launched PowerShell process. Every action compiles all sources with `--release 17 -Xlint:all -Werror`. Use `build` to compile without running.

For this computer, the existing JDK used during verification is:

```powershell
$env:JAVA_HOME = 'C:\Users\muxammedp\SDP-ass4\.tools\jdk-17.0.20.1+1'
```

On another computer, use that computer's JDK location instead. The JDK is not bundled.

## Manual build (any terminal with JDK on PATH)

```text
mkdir out
javac --release 17 -encoding UTF-8 -Xlint:all -Werror -d out src/calculator/*.java src/app/*.java examples/example/*.java tests/tests/*.java
java -cp out app.Main --demo
java -cp out app.Main
java -cp out tests.CalculatorTest
java -cp out example.DivisionDemo
```

If `out` already exists, skip `mkdir out`. In IntelliJ IDEA, select JDK 17 and mark `src`, `examples`, and `tests` as source roots, then run `app.Main`.

## Try it

The interactive commands are `add`, `subtract`, `multiply`, and `exit`. Enter two numbers after choosing an operation. Commands are case-insensitive. Invalid commands and malformed numbers produce a message and return to the menu. End-of-input exits cleanly.

```text
add(12, 4) = 16
subtract(12, 4) = 8
multiply(12, 4) = 48
```

Numbers are parsed from strings into `BigDecimal`; the three core operations use exact decimal arithmetic. The extension uses `MathContext.DECIMAL64` for division (16 significant digits, HALF_EVEN rounding); division by zero throws `ArithmeticException`. Division is intentionally demonstrated by a separate client rather than added to the original menu.

## Structure

```text
src/app/Main.java                    Client, console input, runtime selection
src/calculator/OperationStrategy.java Strategy interface
src/calculator/Calculator.java        Context
src/calculator/*Strategy.java         Three concrete strategies
examples/example/DivideStrategy.java  Fourth algorithm, extension example
examples/example/DivisionDemo.java    New client for the extension
tests/tests/CalculatorTest.java       Standalone regression suite
tests/verify_cli.py                   Optional Python 3 CLI verification
docs/strategy.png                     UML diagram image
docs/strategy.mmd                     Editable Mermaid UML source
docs/DEFENSE.md                       Design walkthrough and practice questions
REPORT.md                            Required Markdown reflection and UML
REPORT.docx                          Word report with embedded UML image
VERIFICATION.txt                     Actual local verification output
run.ps1                              Windows build/run helper
```

## Add a new strategy

1. Add a class implementing `OperationStrategy` with `execute(BigDecimal left, BigDecimal right)`.
2. Instantiate the new class in a client and call `calculator.setStrategy(new YourStrategy())`.
3. Call `calculate` on the same context. Do not change `Calculator`, the interface, or existing strategies.

`DivideStrategy` and `DivisionDemo` are the working example. To expose a new strategy in the existing menu, add one entry to `Main.createStrategies()`; that changes client configuration, not the context. The separate example requires no changes even to the original client.

## Verification and Git history

The Java suite covers arithmetic, same-object switching, extension, null rejection, and delegation to an unknown strategy. Optional CLI tests require Python 3:

```powershell
python tests/verify_cli.py --java "$env:JAVA_HOME\bin\java.exe"
```

Inspect the incremental local commits with `git log --oneline`. History is stored in the project's `.git` folder. No submission ZIP is generated.

## Submission status

The local project and Markdown report are prepared. The GitHub repository has not been published, and no repository URL is invented. After publication, replace the pending status in section 6 of `REPORT.md` with the actual repository link and update both report formats. Review the implementation and reflection before submission and practice the defense using `docs/DEFENSE.md`.
