package barnard;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Brackets {
    public interface Result {
        boolean isOk();
    }

    public static class OK implements Result {
        public boolean isOk() { return true; }
        public String toString() { return "OK"; }
    }

    public static class FAIL implements Result {
        FAIL(String actual, String expected) {
            this._actual = actual;
            this._expected = expected;
        }

        public boolean isOk() { return false; }
        public String actual() { return this._actual; }
        public String expected() { return this._expected; }
        public String toString() { return "Expected " + this.expected() + " but got " + this.actual(); }

        public String _actual;
        public String _expected;
    }

    static Map<String, String> PAIRS = Map.of(
            "{", "}",
            "[", "]",
            "<", ">"
    );

    static Set<String> tokensFrom(Map<String, String> pairs) {
        return pairs.entrySet().stream()
                .flatMap((Map.Entry<String, String> entry) -> Stream.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toSet());
    }

    static Result check(Iterator<String> input, Map<String, String> pairs) {
        Set<String> tokens = Brackets.tokensFrom(pairs);
        Stack<String> stack = new Stack<String>();
        while (input.hasNext()) {
            String token = input.next();

            // Ignore non-brackets
            if (!tokens.contains(token)) continue;

            // If this token is a key in pairs, that means it's an opening bracket.
            // Push its closing bracket to the stack.
            String closing = pairs.get(token);
            if (closing != null) {
                stack.push(closing);
                continue;
            }

            // Otherwise, we must be looking at a closing bracket. First, check to see if
            // we have anything on the stack. If we don't, that's a problem.
            if (stack.isEmpty()) {
                return new FAIL(token, "open brace, non-brace character, or end of input");
            }

            // If the token matches the top of the stack, it's an appropriately nested
            // closing bracket. Pop the stack and continue.
            if (token.equals(stack.peek())) {
                stack.pop();
                continue;
            }

            // Otherwise, we have a problem. Throw.
            return new FAIL(token, stack.peek());
        }
        return stack.isEmpty() ? new OK() : new FAIL("end of input", stack.peek());
    }

    static Result check(Iterator<String> input) {
        return Brackets.check(input, Brackets.PAIRS);
    }

    static Result check(String input) {
        return Brackets.check(Arrays.stream(input.split("")).iterator());
    }

    static void test(String input, boolean expected) {
        Result res = Brackets.check(input);
        System.out.printf(" '%s' ->\t%b\t\t[%s]\n",
            input, res, expected == res.isOk() ? "PASS" : "FAIL"
        );
    }

    public static void main(String... args) {
        Brackets.test("<{[]}>", true);
        Brackets.test("<{[]}>>", false);
        Brackets.test("[][(){}", false);
        Brackets.test("text ( is allowed ){rwwrwrrww [] ()}", true);

        // Read input from stdin and process it.
        System.out.println(
                Brackets
                        .check(new Scanner(System.in, "utf-8")
                                .useDelimiter(""))
                        .toString());

    }
}
