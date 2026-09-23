package rockstar.client.internal.core;


import rockstar.client.*;
import java.util.Map;
import java.util.Set;
import rockstar.client.internal.core.ExpressionFunction;
import rockstar.client.internal.core.ExpressionFunctions;
import rockstar.client.internal.core.ExpressionOperator;
import rockstar.client.internal.core.ExpressionOperators;
import rockstar.client.internal.core.FunctionSeparatorToken;
import rockstar.client.internal.core.CloseParenthesesToken;
import rockstar.client.internal.core.FunctionToken;
import rockstar.client.internal.core.NumberToken;
import rockstar.client.internal.core.OpenParenthesesToken;
import rockstar.client.internal.core.OperatorToken;
import rockstar.client.internal.core.ExpressionToken;
import rockstar.client.internal.core.UnknownFunctionOrVariableException;
import rockstar.client.internal.core.VariableToken;

public class ExpressionTokenizer {
    private final char[] internalField0611;
    private final int internalField0227;
    private final Map<String, ExpressionFunction> internalField0543;
    private final Map<String, ExpressionOperator> internalField0544;
    private final Set<String> internalField0546;
    private final boolean internalField0277;
    private int internalField0228 = 0;
    private ExpressionToken internalField0552;

    public ExpressionTokenizer(String string, Map<String, ExpressionFunction> map, Map<String, ExpressionOperator> map2, Set<String> set, boolean bl) {
        this.internalField0611 = string.trim().toCharArray();
        this.internalField0227 = this.internalField0611.length;
        this.internalField0543 = map;
        this.internalField0544 = map2;
        this.internalField0546 = set;
        this.internalField0277 = bl;
    }

    public ExpressionTokenizer(String string, Map<String, ExpressionFunction> map, Map<String, ExpressionOperator> map2, Set<String> set) {
        this.internalField0611 = string.trim().toCharArray();
        this.internalField0227 = this.internalField0611.length;
        this.internalField0543 = map;
        this.internalField0544 = map2;
        this.internalField0546 = set;
        this.internalField0277 = true;
    }

    public boolean internalMethod02671() {
        return this.internalField0611.length > this.internalField0228;
    }

    public ExpressionToken internalMethod03330() {
        char c = this.internalField0611[this.internalField0228];
        while (Character.isWhitespace(c)) {
            c = this.internalField0611[++this.internalField0228];
        }
        if (Character.isDigit(c) || c == '.') {
            if (this.internalField0552 != null) {
                if (this.internalField0552.internalMethod01431() == 1) {
                    throw new IllegalArgumentException("Unable to parse char '" + c + "' (Code:" + c + ") at [" + this.internalField0228 + "]");
                }
                if (this.internalField0277 && this.internalField0552.internalMethod01431() != 2 && this.internalField0552.internalMethod01431() != 4 && this.internalField0552.internalMethod01431() != 3 && this.internalField0552.internalMethod01431() != 7) {
                    this.internalField0552 = new OperatorToken(ExpressionOperators.internalMethod00317('*', 2));
                    return this.internalField0552;
                }
            }
            return this.internalMethod03688(c);
        }
        if (this.internalMethod07474(c)) {
            return this.internalMethod04057();
        }
        if (this.internalMethod07529(c)) {
            if (this.internalField0552 != null && this.internalField0277 && this.internalField0552.internalMethod01431() != 2 && this.internalField0552.internalMethod01431() != 4 && this.internalField0552.internalMethod01431() != 3 && this.internalField0552.internalMethod01431() != 7) {
                this.internalField0552 = new OperatorToken(ExpressionOperators.internalMethod00317('*', 2));
                return this.internalField0552;
            }
            return this.internalMethod04518(true);
        }
        if (this.internalMethod08109(c)) {
            return this.internalMethod04518(false);
        }
        if (ExpressionOperator.internalMethod02426(c)) {
            return this.internalMethod06827(c);
        }
        if (ExpressionTokenizer.internalMethod07475(c) || c == '_') {
            if (this.internalField0552 != null && this.internalField0277 && this.internalField0552.internalMethod01431() != 2 && this.internalField0552.internalMethod01431() != 4 && this.internalField0552.internalMethod01431() != 3 && this.internalField0552.internalMethod01431() != 7) {
                this.internalField0552 = new OperatorToken(ExpressionOperators.internalMethod00317('*', 2));
                return this.internalField0552;
            }
            return this.internalMethod08571();
        }
        throw new IllegalArgumentException("Unable to parse char '" + c + "' (Code:" + c + ") at [" + this.internalField0228 + "]");
    }

    private ExpressionToken internalMethod04057() {
        ++this.internalField0228;
        this.internalField0552 = new FunctionSeparatorToken();
        return this.internalField0552;
    }

    private boolean internalMethod07474(char c) {
        return c == ',';
    }

    private ExpressionToken internalMethod04518(boolean bl) {
        this.internalField0552 = bl ? new OpenParenthesesToken() : new CloseParenthesesToken();
        ++this.internalField0228;
        return this.internalField0552;
    }

    private boolean internalMethod07529(char c) {
        return c == '(' || c == '{' || c == '[';
    }

    private boolean internalMethod08109(char c) {
        return c == ')' || c == '}' || c == ']';
    }

    private ExpressionToken internalMethod08571() {
        int n = this.internalField0228++;
        int n2 = 1;
        ExpressionToken typedValue243 = null;
        int n3 = 1;
        if (this.internalMethod08110(n)) {
            // empty if block
        }
        int n4 = n + n3 - 1;
        while (!this.internalMethod08110(n4) && ExpressionTokenizer.internalMethod07530(this.internalField0611[n4])) {
            String string = new String(this.internalField0611, n, n3);
            if (this.internalField0546 != null && this.internalField0546.contains(string)) {
                n2 = n3;
                typedValue243 = new VariableToken(string);
            } else {
                ExpressionFunction typedValue238 = this.internalMethod05110(string);
                if (typedValue238 != null) {
                    n2 = n3;
                    typedValue243 = new FunctionToken(typedValue238);
                }
            }
            n4 = n + ++n3 - 1;
        }
        if (typedValue243 == null) {
            throw new UnknownFunctionOrVariableException(new String(this.internalField0611), this.internalField0228, n3);
        }
        this.internalField0228 += n2;
        this.internalField0552 = typedValue243;
        return this.internalField0552;
    }

    private ExpressionFunction internalMethod05110(String string) {
        ExpressionFunction typedValue238 = null;
        if (this.internalField0543 != null) {
            typedValue238 = this.internalField0543.get(string);
        }
        if (typedValue238 == null) {
            typedValue238 = ExpressionFunctions.internalMethod03979(string);
        }
        return typedValue238;
    }

    private ExpressionToken internalMethod06827(char c) {
        int n = this.internalField0228;
        int n2 = 1;
        StringBuilder stringBuilder = new StringBuilder();
        ExpressionOperator typedValue239 = null;
        stringBuilder.append(c);
        while (!this.internalMethod08110(n + n2) && ExpressionOperator.internalMethod02426(this.internalField0611[n + n2])) {
            stringBuilder.append(this.internalField0611[n + n2++]);
        }
        while (!stringBuilder.isEmpty()) {
            ExpressionOperator typedValue240 = this.internalMethod05111(stringBuilder.toString());
            if (typedValue240 == null) {
                stringBuilder.setLength(stringBuilder.length() - 1);
                continue;
            }
            typedValue239 = typedValue240;
            break;
        }
        this.internalField0228 += stringBuilder.length();
        this.internalField0552 = new OperatorToken(typedValue239);
        return this.internalField0552;
    }

    private ExpressionOperator internalMethod05111(String string) {
        ExpressionOperator typedValue239 = null;
        if (this.internalField0544 != null) {
            typedValue239 = this.internalField0544.get(string);
        }
        if (typedValue239 == null && string.length() == 1) {
            int n = 2;
            if (this.internalField0552 == null) {
                n = 1;
            } else {
                ExpressionOperator typedValue240;
                int n2 = this.internalField0552.internalMethod01431();
                if (n2 == 4 || n2 == 7) {
                    n = 1;
                } else if (n2 == 2 && ((typedValue240 = ((OperatorToken)this.internalField0552).internalMethod02443()).internalMethod01042() == 2 || typedValue240.internalMethod01042() == 1 && !typedValue240.internalMethod01043())) {
                    n = 1;
                }
            }
            typedValue239 = ExpressionOperators.internalMethod00317(string.charAt(0), n);
        }
        return typedValue239;
    }

    private ExpressionToken internalMethod03688(char c) {
        int n;
        int n2 = 1;
        if (this.internalMethod08110((n = this.internalField0228++) + n2)) {
            this.internalField0552 = new NumberToken(Double.parseDouble(String.valueOf(c)));
            return this.internalField0552;
        }
        while (!this.internalMethod08110(n + n2) && ExpressionTokenizer.internalMethod02150(this.internalField0611[n + n2], this.internalField0611[n + n2 - 1] == 'e' || this.internalField0611[n + n2 - 1] == 'E')) {
            ++n2;
            ++this.internalField0228;
        }
        if (this.internalField0611[n + n2 - 1] == 'e' || this.internalField0611[n + n2 - 1] == 'E') {
            --n2;
            --this.internalField0228;
        }
        this.internalField0552 = new NumberToken(this.internalField0611, n, n2);
        return this.internalField0552;
    }

    private static boolean internalMethod02150(char c, boolean bl) {
        return Character.isDigit(c) || c == '.' || c == 'e' || c == 'E' || bl && (c == '-' || c == '+');
    }

    public static boolean internalMethod07475(int n) {
        return Character.isLetter(n);
    }

    public static boolean internalMethod07530(int n) {
        return ExpressionTokenizer.internalMethod07475(n) || Character.isDigit(n) || n == 95 || n == 46;
    }

    private boolean internalMethod08110(int n) {
        return this.internalField0227 <= n;
    }
}

