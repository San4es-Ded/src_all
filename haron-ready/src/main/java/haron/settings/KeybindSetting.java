package haron.settings;

import haron.settings.Setting;
import java.util.Locale;
import org.lwjgl.glfw.GLFW;

public class KeybindSetting
extends Setting<Integer> {
    public boolean isBound() {
        return this.key() != -1;
    }

    public KeybindSetting(String string, String string2, int n) {
        super(string, string2, n);
    }

    public KeybindSetting(String string, int n) {
        this(string, "", n);
    }

    public KeybindSetting(String string) {
        this(string, "", -1);
    }

    public void clear() {
        this.setKey(-1);
    }

    public boolean b() {
        return this.isBound();
    }

    public void c() {
        this.clear();
    }

    public String d() {
        return this.displayName();
    }

    public int a() {
        return this.key();
    }

    public void a(int n) {
        this.setKey(n);
    }

    public int key() {
        return (Integer)this.k();
    }

    public String displayName() {
        int n = this.key();
        if (n == -1) {
            return "";
        }
        String string = GLFW.glfwGetKeyName((int)n, (int)0);
        return string != null ? string.toUpperCase(Locale.ROOT) : KeybindSetting.$sf$0(n);
    }

    public void setKey(int n) {
        super.a(n);
    }

    private static /* synthetic */ String $sf$0(int n) {
        return "KEY " + n;
    }
}
