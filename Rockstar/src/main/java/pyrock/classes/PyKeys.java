package pyrock.classes;



import rockstar.client.util.*;
import rockstar.client.internal.ui.*;
import java.util.List;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import rockstar.client.internal.ui.KeybindParser;
import rockstar.client.util.TextUtils;
import rockstar.client.MinecraftClientAccess;

public class PyKeys
implements MinecraftClientAccess {
    public int code(String string) {
        return KeybindParser.internalMethod06543(string);
    }

    public String name(int n) {
        return TextUtils.internalMethod04982(n);
    }

    public boolean down(Object object) {
        int n = this.toCode(object);
        if (n < 0) {
            return false;
        }
        long l = internalField0149.getWindow().getHandle();
        if (n <= 7) {
            return GLFW.glfwGetMouseButton((long)l, (int)n) == 1;
        }
        return InputUtil.isKeyPressed(MinecraftClientAccess.internalField0149.getWindow(), n);
    }

    public boolean mouse(int n) {
        return GLFW.glfwGetMouseButton((long)internalField0149.getWindow().getHandle(), (int)n) == 1;
    }

    public float x() {
        return (float)(PyKeys.internalField0149.mouse.getX() / internalField0149.getWindow().getScaleFactor());
    }

    public float y() {
        return (float)(PyKeys.internalField0149.mouse.getY() / internalField0149.getWindow().getScaleFactor());
    }

    public List<String> names() {
        return KeybindParser.internalMethod07034();
    }

    private int toCode(Object object) {
        if (object instanceof Number) {
            Number number = (Number)object;
            return number.intValue();
        }
        if (object instanceof String) {
            String string = (String)object;
            return KeybindParser.internalMethod06543(string);
        }
        return -1;
    }
}
