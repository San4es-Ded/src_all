package rockstar.client.internal.script;



import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import jep.ClassEnquirer;
import jep.JepConfig;
import jep.MainInterpreter;
import jep.PyConfig;
import jep.SharedInterpreter;
import pyrock.McBridge;
import rockstar.client.internal.script.PyrockClassEnquirer;
import rockstar.client.internal.core.ClassIntegrityLoader;
import rockstar.client.internal.script.ConfigManager;
import rockstar.client.RockstarClient;
import rockstar.client.MinecraftClientAccess;

public final class EmbeddedPythonRuntime {
    private static volatile Thread internalField0380;
    private static volatile boolean internalField0277;
    private static volatile boolean internalField0276;
    private static volatile String internalField0248;
    private static SharedInterpreter internalField0527;
    private static volatile boolean internalField1099;

    private EmbeddedPythonRuntime() {
    }

    public static String internalMethod02793() {
        return internalField0248;
    }

    public static File internalMethod00088() {
        return new File(ConfigManager.internalField0148, "runtime/python");
    }

    public static void internalMethod07576() {
        if (internalField0380 == null) {
            internalField0380 = Thread.currentThread();
        }
    }

    public static boolean internalMethod07577() {
        File file = EmbeddedPythonRuntime.internalMethod06225(EmbeddedPythonRuntime.internalMethod00088());
        return file != null && file.exists();
    }

    public static boolean internalMethod07579() {
        return internalField0276;
    }

    public static boolean internalMethod08721() {
        return internalField1099;
    }

    public static boolean internalMethod08724() {
        return Thread.currentThread() == internalField0380;
    }

    public static void internalMethod01721(Runnable runnable) {
        if (internalField0380 == null || EmbeddedPythonRuntime.internalMethod08724()) {
            runnable.run();
        } else {
            MinecraftClientAccess.internalField0149.execute(runnable);
        }
    }

    public static SharedInterpreter internalMethod07274() {
        if (!internalField0277) {
            EmbeddedPythonRuntime.internalMethod07578();
        }
        return internalField0527;
    }

    private static synchronized void internalMethod07578() {
        if (internalField0277) {
            return;
        }
        File file = EmbeddedPythonRuntime.internalMethod00088();
        File file2 = EmbeddedPythonRuntime.internalMethod06225(file);
        if (file2 == null || !file2.exists()) {
            RockstarClient.internalField0572.warn("[Python] jep native \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d \u0432 {} \u2014 \u0436\u0434\u0451\u043c \u0437\u0430\u0433\u0440\u0443\u0437\u043a\u0438 \u0440\u0430\u043d\u0442\u0430\u0439\u043c\u0430", (Object)file.getAbsolutePath());
            return;
        }
        internalField0277 = true;
        if (internalField0380 == null) {
            internalField0380 = Thread.currentThread();
        }
        try {
            internalField1099 = true;
            File file3 = EmbeddedPythonRuntime.internalMethod01314(file);
            if (file3 != null) {
                System.load(file3.getAbsolutePath());
            }
            MainInterpreter.setJepLibraryPath((String)file2.getAbsolutePath());
            PyConfig pyConfig = new PyConfig();
            pyConfig.setPythonHome(file.getAbsolutePath());
            MainInterpreter.setInitParams((PyConfig)pyConfig);
            JepConfig jepConfig = new JepConfig();
            jepConfig.addIncludePaths(new String[]{new File(file, "Lib/site-packages").getAbsolutePath(), new File(file, "scripts-lib").getAbsolutePath()});
            jepConfig.redirectStdout((OutputStream)System.out);
            jepConfig.redirectStdErr((OutputStream)System.err);
            jepConfig.setClassLoader((ClassLoader)new ClassIntegrityLoader(McBridge.class.getClassLoader()));
            jepConfig.setClassEnquirer((ClassEnquirer)new PyrockClassEnquirer());
            SharedInterpreter.setConfig((JepConfig)jepConfig);
            internalField0527 = new SharedInterpreter();
            internalField0527.exec(EmbeddedPythonRuntime.internalMethod07502());
            internalField0276 = true;
            RockstarClient.internalField0572.info("[Python] CPython \u0438\u043d\u0438\u0446\u0438\u0430\u043b\u0438\u0437\u0438\u0440\u043e\u0432\u0430\u043d ({})", (Object)file.getAbsolutePath());
        }
        catch (Throwable throwable) {
            internalField0276 = false;
            internalField0248 = throwable.getMessage() != null ? throwable.getMessage() : throwable.getClass().getSimpleName();
            RockstarClient.internalField0572.error("[Python] \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0438\u043d\u0438\u0446\u0438\u0430\u043b\u0438\u0437\u0438\u0440\u043e\u0432\u0430\u0442\u044c CPython", throwable);
        }
    }

    private static File internalMethod01314(File file2) {
        File[] fileArray = file2.listFiles((file, string) -> string.matches("python3\\d+\\.dll"));
        return fileArray != null && fileArray.length > 0 ? fileArray[0] : null;
    }

    private static File internalMethod06225(File file) {
        String string = System.getProperty("os.name", "").toLowerCase();
        String string2 = string.contains("win") ? "jep.dll" : (string.contains("mac") ? "libjep.jnilib" : "libjep.so");
        File file2 = new File(file, string2);
        if (file2.exists()) {
            return file2;
        }
        return new File(file, "Lib/site-packages/jep/" + string2);
    }

    private static String internalMethod07502() throws Exception {
        try (InputStream inputStream = McBridge.class.getResourceAsStream("/rockstar/prelude.py");){
            if (inputStream == null) {
                throw new IllegalStateException("/rockstar/prelude.py \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d \u0432 \u0440\u0435\u0441\u0443\u0440\u0441\u0430\u0445");
            }
            String string = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return string;
        }
    }
}

