package pyrock.classes;



import rockstar.client.command.*;
import rockstar.client.internal.command.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import pyrock.classes.PyCommand;
import rockstar.client.command.CommandNode;
import rockstar.client.internal.command.CommandRegistry;
import rockstar.client.RockstarClient;

public class PyCommands {
    private final List<PyCommand> mine = new ArrayList<PyCommand>();

    public PyCommand create(String string) {
        PyCommand pyCommand = new PyCommand(string);
        this.mine.add(pyCommand);
        return pyCommand;
    }

    public boolean exists(String string) {
        if (string == null || string.isBlank()) {
            return false;
        }
        for (CommandNode internalValue0007 : PyCommands.registry().internalMethod05967()) {
            for (String string2 : internalValue0007.internalMethod03764()) {
                if (!string2.equalsIgnoreCase(string)) continue;
                return true;
            }
        }
        return false;
    }

    public List<String> names() {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (CommandNode internalValue0007 : PyCommands.registry().internalMethod05967()) {
            if (internalValue0007.internalMethod03764().isEmpty()) continue;
            arrayList.add(internalValue0007.internalMethod03764().getFirst());
        }
        return arrayList;
    }

    public boolean remove(String string) {
        if (string == null) {
            return false;
        }
        String string2 = string.toLowerCase(Locale.ROOT);
        for (PyCommand pyCommand : new ArrayList<PyCommand>(this.mine)) {
            if (!pyCommand.name().equals(string2)) continue;
            this.mine.remove(pyCommand);
            return pyCommand.remove();
        }
        return false;
    }

    public boolean run(String string) {
        if (string == null || string.isBlank()) {
            return false;
        }
        String string2 = PyCommands.registry().internalMethod03606();
        String string3 = string.startsWith(string2) ? string : string2 + string;
        try {
            return PyCommands.registry().internalMethod04610(string3);
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    public String prefix() {
        return PyCommands.registry().internalMethod03606();
    }

    private static CommandRegistry registry() {
        return RockstarClient.getInstance().internalMethod05348();
    }
}

