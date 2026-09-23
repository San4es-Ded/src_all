package rockstar.client.internal.network;



import rockstar.client.module.*;
import rockstar.client.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Set;
import rockstar.client.RockstarClient;

public final class ScriptSecurityGuard {
    private static final String[] internalField0359 = new String[]{"sun.", "com.sun.", "jdk.", "java.lang.reflect.", "java.lang.invoke.", "java.lang.foreign.", "java.lang.instrument.", "java.lang.module.", "java.lang.classfile.", "java.security.", "javax.script.", "javax.tools.", "org.lwjgl.system.", "io.netty.util.internal.", "org.objectweb.asm", "net.bytebuddy", "javassist", "jep.", "moscow.rockstar.systems.python."};
    private static final Set<String> internalField0546 = Set.of("java.lang.Runtime", "java.lang.ProcessBuilder", "java.lang.Process", "java.lang.ProcessHandle", "java.lang.System", "java.lang.Class", "java.lang.ClassLoader", "java.lang.Thread", "java.lang.ThreadGroup", "java.lang.Module", "java.lang.ModuleLayer", "java.lang.SecurityManager", "java.net.URLClassLoader", "java.awt.Robot", "java.awt.Desktop");

    private ScriptSecurityGuard() {
    }

    public static void internalMethod03423(String string) {
        if (string == null || string.isEmpty()) {
            return;
        }
        String string2 = string.replace('/', '.');
        if (internalField0546.contains(string2)) {
            ScriptSecurityGuard.internalMethod01975(string2);
        }
        for (String string3 : internalField0359) {
            if (!string2.startsWith(string3)) continue;
            ScriptSecurityGuard.internalMethod01975(string2);
        }
    }

    public static void internalMethod02572(Class<?> clazz) {
        if (clazz == null) {
            return;
        }
        Class<?> clazz2 = clazz;
        while (clazz2.isArray()) {
            clazz2 = clazz2.getComponentType();
        }
        if (clazz2.isPrimitive()) {
            return;
        }
        ScriptSecurityGuard.internalMethod03423(clazz2.getName());
    }

    public static void internalMethod05218(Method method) {
        if (method == null) {
            return;
        }
        ScriptSecurityGuard.internalMethod02572(method.getDeclaringClass());
        ScriptSecurityGuard.internalMethod02572(method.getReturnType());
    }

    public static void internalMethod01473(Field field) {
        if (field == null) {
            return;
        }
        ScriptSecurityGuard.internalMethod02572(field.getDeclaringClass());
        ScriptSecurityGuard.internalMethod02572(field.getType());
    }

    public static Object internalMethod04158(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Class) {
            Class clazz = (Class)object;
            ScriptSecurityGuard.internalMethod01975(clazz.getName());
        }
        if (object instanceof ClassLoader || object instanceof Member || object instanceof MethodHandle || object instanceof MethodHandles.Lookup || object instanceof VarHandle || object instanceof Runtime || object instanceof Process || object instanceof ProcessBuilder || object instanceof ProcessHandle || object instanceof Thread || object instanceof ThreadGroup || object instanceof java.lang.Module) {
            ScriptSecurityGuard.internalMethod01975(object.getClass().getName());
        }
        return object;
    }

    private static void internalMethod01975(String string) {
        RockstarClient.internalField0572.warn("[Scripts] \u0441\u043a\u0440\u0438\u043f\u0442\u0443 \u043e\u0442\u043a\u0430\u0437\u0430\u043d\u043e \u0432 \u0434\u043e\u0441\u0442\u0443\u043f\u0435 \u043a {}", (Object)string);
        throw new InternalType0290("\u043a\u043b\u0430\u0441\u0441 " + string + " \u0437\u0430\u043a\u0440\u044b\u0442 \u0434\u043b\u044f \u0441\u043a\u0440\u0438\u043f\u0442\u043e\u0432");
    }

    public static final class InternalType0290
    extends RuntimeException {
        InternalType0290(String string) {
            super(string);
        }
    }
}

