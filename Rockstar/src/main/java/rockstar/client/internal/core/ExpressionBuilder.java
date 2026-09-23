package rockstar.client.internal.core;



import rockstar.client.internal.script.*;
import rockstar.client.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import rockstar.client.internal.script.ExpressionEvaluator;
import rockstar.client.internal.core.ExpressionFunction;
import rockstar.client.internal.core.ExpressionFunctions;
import rockstar.client.internal.core.ExpressionOperator;
import rockstar.client.internal.core.ExpressionCompiler;

public class ExpressionBuilder {
    private final String internalField0248;
    private final Map<String, ExpressionFunction> internalField0543;
    private final Map<String, ExpressionOperator> internalField0544;
    private final Set<String> internalField0546;
    private boolean internalField0277 = true;

    public ExpressionBuilder(String string) {
        if (string == null || string.trim().isEmpty()) {
            throw new IllegalArgumentException("Expression can not be empty");
        }
        this.internalField0248 = string;
        this.internalField0544 = new HashMap<String, ExpressionOperator>(4);
        this.internalField0543 = new HashMap<String, ExpressionFunction>(4);
        this.internalField0546 = new HashSet<String>(4);
    }

    public ExpressionBuilder internalMethod03206(ExpressionFunction typedValue238) {
        this.internalField0543.put(typedValue238.internalMethod04838(), typedValue238);
        return this;
    }

    public ExpressionBuilder internalMethod01831(ExpressionFunction ... iIiIIiIII_Class329Array) {
        for (ExpressionFunction typedValue238 : iIiIIiIII_Class329Array) {
            this.internalField0543.put(typedValue238.internalMethod04838(), typedValue238);
        }
        return this;
    }

    public ExpressionBuilder internalMethod03859(List<ExpressionFunction> list) {
        for (ExpressionFunction typedValue238 : list) {
            this.internalField0543.put(typedValue238.internalMethod04838(), typedValue238);
        }
        return this;
    }

    public ExpressionBuilder internalMethod02756(Set<String> set) {
        this.internalField0546.addAll(set);
        return this;
    }

    public ExpressionBuilder internalMethod04828(String ... stringArray) {
        Collections.addAll(this.internalField0546, stringArray);
        return this;
    }

    public ExpressionBuilder internalMethod04588(String string) {
        this.internalField0546.add(string);
        return this;
    }

    public ExpressionBuilder internalMethod05045(boolean bl) {
        this.internalField0277 = bl;
        return this;
    }

    public void internalMethod05207(ExpressionOperator typedValue239) {
        this.internalMethod02027(typedValue239);
        this.internalField0544.put(typedValue239.internalMethod00746(), typedValue239);
    }

    private void internalMethod02027(ExpressionOperator typedValue239) {
        String string = typedValue239.internalMethod00746();
        for (char c : string.toCharArray()) {
            if (ExpressionOperator.internalMethod02426(c)) continue;
            throw new IllegalArgumentException("The operator symbol '" + string + "' is invalid");
        }
    }

    public ExpressionBuilder internalMethod06791(ExpressionOperator ... iIiIIiIiI_Class331Array) {
        for (ExpressionOperator typedValue239 : iIiIIiIiI_Class331Array) {
            this.internalMethod05207(typedValue239);
        }
        return this;
    }

    public ExpressionBuilder internalMethod05395(List<ExpressionOperator> list) {
        for (ExpressionOperator typedValue239 : list) {
            this.internalMethod05207(typedValue239);
        }
        return this;
    }

    public ExpressionEvaluator internalMethod05680() {
        if (this.internalField0248.isEmpty()) {
            throw new IllegalArgumentException("The expression can not be empty");
        }
        this.internalField0546.add("pi");
        this.internalField0546.add("\u03c0");
        this.internalField0546.add("e");
        this.internalField0546.add("\u03c6");
        for (String string : this.internalField0546) {
            if (ExpressionFunctions.internalMethod03979(string) == null && !this.internalField0543.containsKey(string)) continue;
            throw new IllegalArgumentException("A variable can not have the same name as a function [" + string + "]");
        }
        return new ExpressionEvaluator(ExpressionCompiler.internalMethod07162(this.internalField0248, this.internalField0543, this.internalField0544, this.internalField0546, this.internalField0277), this.internalField0543.keySet());
    }
}

