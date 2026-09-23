package rockstar.client.command;



import rockstar.client.core.*;
import rockstar.client.*;
import java.util.Collections;
import java.util.List;
import rockstar.client.core.OperationResult;

@FunctionalInterface
public interface CommandValidator {
    public OperationResult validate(String localValue1);

    default public List<String> suggestions(String string) {
        return Collections.emptyList();
    }
}

