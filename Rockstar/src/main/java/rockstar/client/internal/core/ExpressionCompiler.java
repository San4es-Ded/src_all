package rockstar.client.internal.core;


import rockstar.client.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import rockstar.client.internal.core.ExpressionFunction;
import rockstar.client.internal.core.ExpressionOperator;
import rockstar.client.internal.core.OperatorToken;
import rockstar.client.internal.core.ExpressionToken;
import rockstar.client.internal.core.ExpressionTokenizer;

public class ExpressionCompiler {
    public static ExpressionToken[] internalMethod07162(String string, Map<String, ExpressionFunction> map, Map<String, ExpressionOperator> map2, Set<String> set, boolean bl) {
        ExpressionToken typedValue243;
        Stack<ExpressionToken> stack = new Stack<ExpressionToken>();
        ArrayList<ExpressionToken> arrayList = new ArrayList<ExpressionToken>();
        ExpressionTokenizer typedValue244 = new ExpressionTokenizer(string, map, map2, set, bl);
        block8: while (typedValue244.internalMethod02671()) {
            typedValue243 = typedValue244.internalMethod03330();
            switch (typedValue243.internalMethod01431()) {
                case 1: 
                case 6: {
                    arrayList.add(typedValue243);
                    continue block8;
                }
                case 3: {
                    stack.add(typedValue243);
                    continue block8;
                }
                case 7: {
                    while (!stack.empty() && ((ExpressionToken)stack.peek()).internalMethod01431() != 4) {
                        arrayList.add((ExpressionToken)stack.pop());
                    }
                    if (!stack.empty() && ((ExpressionToken)stack.peek()).internalMethod01431() == 4) continue block8;
                    throw new IllegalArgumentException("Misplaced function separator ',' or mismatched parentheses");
                }
                case 2: {
                    while (!stack.empty() && ((ExpressionToken)stack.peek()).internalMethod01431() == 2) {
                        OperatorToken typedValue241 = (OperatorToken)typedValue243;
                        OperatorToken typedValue242 = (OperatorToken)stack.peek();
                        if (typedValue241.internalMethod02443().internalMethod01042() == 1 && typedValue242.internalMethod02443().internalMethod01042() == 2 || (!typedValue241.internalMethod02443().internalMethod01043() || typedValue241.internalMethod02443().internalMethod01076() > typedValue242.internalMethod02443().internalMethod01076()) && typedValue241.internalMethod02443().internalMethod01076() >= typedValue242.internalMethod02443().internalMethod01076()) break;
                        arrayList.add((ExpressionToken)stack.pop());
                    }
                    stack.push(typedValue243);
                    continue block8;
                }
                case 4: {
                    stack.push(typedValue243);
                    continue block8;
                }
                case 5: {
                    while (((ExpressionToken)stack.peek()).internalMethod01431() != 4) {
                        arrayList.add((ExpressionToken)stack.pop());
                    }
                    stack.pop();
                    if (stack.isEmpty() || ((ExpressionToken)stack.peek()).internalMethod01431() != 3) continue block8;
                    arrayList.add((ExpressionToken)stack.pop());
                    continue block8;
                }
            }
            throw new IllegalArgumentException("Unknown Token type encountered. This should not happen");
        }
        while (!stack.empty()) {
            typedValue243 = (ExpressionToken)stack.pop();
            if (typedValue243.internalMethod01431() == 5 || typedValue243.internalMethod01431() == 4) {
                throw new IllegalArgumentException("Mismatched parentheses detected. Please check the expression");
            }
            arrayList.add(typedValue243);
        }
        return arrayList.toArray(new ExpressionToken[0]);
    }
}

