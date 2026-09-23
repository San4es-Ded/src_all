package rockstar.client.internal.script;



import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import rockstar.client.internal.core.DoubleStack;
import rockstar.client.internal.core.ValidationResult;
import rockstar.client.internal.core.ExpressionFunction;
import rockstar.client.internal.core.ExpressionFunctions;
import rockstar.client.internal.core.ExpressionOperator;
import rockstar.client.internal.core.FunctionToken;
import rockstar.client.internal.core.NumberToken;
import rockstar.client.internal.core.OperatorToken;
import rockstar.client.internal.core.ExpressionToken;
import rockstar.client.internal.core.VariableToken;

public class ExpressionEvaluator {
    private final ExpressionToken[] internalField0167;
    private final Map<String, Double> internalField0543;
    private final Set<String> internalField0546;

    private static Map<String, Double> internalMethod04218() {
        HashMap<String, Double> hashMap = new HashMap<String, Double>(4);
        hashMap.put("pi", Math.PI);
        hashMap.put("\u03c0", Math.PI);
        hashMap.put("\u03c6", 1.61803398874);
        hashMap.put("e", Math.E);
        return hashMap;
    }

    public ExpressionEvaluator(ExpressionEvaluator typedValue236) {
        this.internalField0167 = Arrays.copyOf(typedValue236.internalField0167, typedValue236.internalField0167.length);
        this.internalField0543 = new HashMap<String, Double>();
        this.internalField0543.putAll(typedValue236.internalField0543);
        this.internalField0546 = new HashSet<String>(typedValue236.internalField0546);
    }

    public ExpressionEvaluator(ExpressionToken[] iIiIiIIii_Class340Array) {
        this.internalField0167 = iIiIiIIii_Class340Array;
        this.internalField0543 = ExpressionEvaluator.internalMethod04218();
        this.internalField0546 = Collections.emptySet();
    }

    public ExpressionEvaluator(ExpressionToken[] iIiIiIIii_Class340Array, Set<String> set) {
        this.internalField0167 = iIiIiIIii_Class340Array;
        this.internalField0543 = ExpressionEvaluator.internalMethod04218();
        this.internalField0546 = set;
    }

    public void internalMethod07284(String string, double d) {
        this.internalMethod05077(string);
        this.internalField0543.put(string, d);
    }

    private void internalMethod05077(String string) {
        if (this.internalField0546.contains(string) || ExpressionFunctions.internalMethod03979(string) != null) {
            throw new IllegalArgumentException("The variable name '" + string + "' is invalid. Since there exists a function with the same name");
        }
    }

    public ExpressionEvaluator internalMethod01084(Map<String, Double> map) {
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            this.internalMethod07284(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public ExpressionEvaluator internalMethod05521() {
        this.internalField0543.clear();
        return this;
    }

    public Set<String> internalMethod04219() {
        HashSet<String> hashSet = new HashSet<String>();
        for (ExpressionToken typedValue243 : this.internalField0167) {
            if (typedValue243.internalMethod01431() != 6) continue;
            hashSet.add(((VariableToken)typedValue243).internalMethod04449());
        }
        return hashSet;
    }

    public ValidationResult internalMethod07150(boolean bl) {
        ArrayList<String> arrayList = new ArrayList<String>(0);
        if (bl) {
            for (ExpressionToken typedValue243 : this.internalField0167) {
                String object;
                if (typedValue243.internalMethod01431() != 6 || this.internalField0543.containsKey(object = ((VariableToken)typedValue243).internalMethod04449())) continue;
                arrayList.add("The setVariable '" + object + "' has not been set");
            }
        }
        int n = 0;
        for (ExpressionToken typedValue243 : this.internalField0167) {
            switch (typedValue243.internalMethod01431()) {
                case 1: 
                case 6: {
                    ++n;
                    break;
                }
                case 3: {
                    ExpressionFunction typedValue238 = ((FunctionToken)typedValue243).internalMethod02363();
                    int n2 = typedValue238.internalMethod02745();
                    if (n2 > n) {
                        arrayList.add("Not enough arguments for '" + typedValue238.internalMethod04838() + "'");
                    }
                    if (n2 > 1) {
                        n -= n2 - 1;
                        break;
                    }
                    if (n2 != 0) break;
                    ++n;
                    break;
                }
                case 2: {
                    ExpressionOperator typedValue239 = ((OperatorToken)typedValue243).internalMethod02443();
                    if (typedValue239.internalMethod01042() != 2) break;
                    --n;
                }
            }
            if (n >= 1) continue;
            arrayList.add("Too many operators");
            return new ValidationResult(false, arrayList);
        }
        if (n > 1) {
            arrayList.add("Too many operands");
        }
        return arrayList.isEmpty() ? ValidationResult.internalField0369 : new ValidationResult(false, arrayList);
    }

    public ValidationResult internalMethod05522() {
        return this.internalMethod07150(true);
    }

    public Future<Double> internalMethod06566(ExecutorService executorService) {
        return executorService.submit(this::internalMethod06442);
    }

    public double internalMethod06442() {
        DoubleStack typedValue235 = new DoubleStack();
        for (ExpressionToken typedValue243 : this.internalField0167) {
            Object object;
            if (typedValue243.internalMethod01431() == 1) {
                typedValue235.internalMethod06182(((NumberToken)typedValue243).internalMethod02759());
                continue;
            }
            if (typedValue243.internalMethod01431() == 6) {
                object = ((VariableToken)typedValue243).internalMethod04449();
                Double d = this.internalField0543.get(object);
                if (d == null) {
                    throw new IllegalArgumentException("No value has been set for the setVariable '" + (String)object + "'.");
                }
                typedValue235.internalMethod06182(d);
                continue;
            }
            if (typedValue243.internalMethod01431() == 2) {
                object = (OperatorToken)typedValue243;
                if (typedValue235.internalMethod03377() < ((OperatorToken)object).internalMethod02443().internalMethod01042()) {
                    throw new IllegalArgumentException("Invalid number of operands available for '" + ((OperatorToken)object).internalMethod02443().internalMethod00746() + "' operator");
                }
                if (((OperatorToken)object).internalMethod02443().internalMethod01042() == 2) {
                    double d = typedValue235.internalMethod03379();
                    double d2 = typedValue235.internalMethod03379();
                    typedValue235.internalMethod06182(((OperatorToken)object).internalMethod02443().internalMethod07375(d2, d));
                    continue;
                }
                if (((OperatorToken)object).internalMethod02443().internalMethod01042() != 1) continue;
                double d = typedValue235.internalMethod03379();
                typedValue235.internalMethod06182(((OperatorToken)object).internalMethod02443().internalMethod07375(d));
                continue;
            }
            if (typedValue243.internalMethod01431() != 3) continue;
            object = (FunctionToken)typedValue243;
            int n = ((FunctionToken)object).internalMethod02363().internalMethod02745();
            if (typedValue235.internalMethod03377() < n) {
                throw new IllegalArgumentException("Invalid number of arguments available for '" + ((FunctionToken)object).internalMethod02363().internalMethod04838() + "' function");
            }
            double[] dArray = new double[n];
            for (int i = n - 1; i >= 0; --i) {
                dArray[i] = typedValue235.internalMethod03379();
            }
            typedValue235.internalMethod06182(((FunctionToken)object).internalMethod02363().internalMethod03000(dArray));
        }
        if (typedValue235.internalMethod03377() > 1) {
            throw new IllegalArgumentException("Invalid number of items on the output queue. Might be caused by an invalid number of arguments for a function.");
        }
        return typedValue235.internalMethod03379();
    }
}

