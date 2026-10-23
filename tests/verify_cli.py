import argparse
import pathlib
import subprocess

parser = argparse.ArgumentParser()
parser.add_argument("--java", default="java")
args = parser.parse_args()
root = pathlib.Path(__file__).resolve().parents[1]
checks = 0


def check(label, user_input="", arguments=(), expected=(), code=0, forbidden=()):
    global checks
    result = subprocess.run(
        [args.java, "-cp", "out", "app.Main", *arguments],
        input=user_input, text=True, capture_output=True, cwd=root, timeout=10,
    )
    output = result.stdout + result.stderr
    assert result.returncode == code, (label, result.returncode, output)
    for fragment in expected:
        assert fragment in output, (label, fragment, output)
    for fragment in forbidden:
        assert fragment not in output, (label, fragment, output)
    checks += 1
    print("PASS:", label)


check("demo", arguments=("--demo",), expected=("add(12, 4) = 16", "subtract(12, 4) = 8", "multiply(12, 4) = 48"))
check("interactive runtime switching", "add\n12\n4\nsubtract\n12\n4\nmultiply\n12\n4\nexit\n", expected=("Result: 16", "Result: 8", "Result: 48"))
check("unknown command recovery", "divide\nadd\n1\n2\nexit\n", expected=("Unknown operation", "Result: 3"))
check("malformed first operand recovery", "add\nwrong\n2\nadd\n3\n4\nexit\n", expected=("Invalid number", "Result: 7"))
check("malformed second operand recovery", "add\n1\nwrong\nadd\n3\n4\nexit\n", expected=("Invalid number", "Result: 7"))
check("whitespace and case", " ADD \n 0.1 \n 0.2 \n EXIT \n", expected=("Result: 0.3",))
check("blank operand", "add\n\n2\nexit\n", expected=("Invalid number",))
check("non-finite number", "add\nNaN\n2\nexit\n", expected=("Invalid number",))
check("negative decimals", "multiply\n-2.5\n4\nexit\n", expected=("Result: -10.0",))
check("EOF at command")
check("EOF at first operand", "add\n", forbidden=("Result:",))
check("EOF at second operand", "add\n1\n", forbidden=("Result:",))
check("help", arguments=("--help",), expected=("Usage:",))
check("unknown argument", arguments=("--invalid",), code=2, expected=("Unknown arguments",))
print(f"CLI SUMMARY: {checks}/{checks} PASS")
