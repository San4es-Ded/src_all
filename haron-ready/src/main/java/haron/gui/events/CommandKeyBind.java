package haron.gui.events;

public class CommandKeyBind {
    public String command;
    public int key;
    public boolean binding;

    public CommandKeyBind(String string, int n) {
        this.command = string;
        this.key = n;
        this.binding = false;
    }
}

