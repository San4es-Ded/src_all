package rockstar.client.internal.script;


import rockstar.client.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import rockstar.client.internal.script.EmbeddedPythonRuntime;
import rockstar.client.RockstarClient;

public final class ScriptLibraryInstaller {
    private static final Set<String> internalField0546 = ConcurrentHashMap.newKeySet();
    private static final ExecutorService internalField0124 = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "Script-Pip");
        thread.setDaemon(true);
        return thread;
    });

    private ScriptLibraryInstaller() {
    }

    public static void internalMethod03301(List<String> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        File file = new File(EmbeddedPythonRuntime.internalMethod00088(), "python.exe");
        if (!file.exists()) {
            return;
        }
        for (String string : list) {
            String string2;
            if (string == null || (string2 = string.trim()).isEmpty() || string2.startsWith("-") || !internalField0546.add(string2.toLowerCase(Locale.ROOT))) continue;
            internalField0124.submit(() -> ScriptLibraryInstaller.internalMethod04370(file, string2));
        }
    }

    private static void internalMethod04370(File file, String string) {
        try {
            Process process = new ProcessBuilder(file.getAbsolutePath(), "-m", "pip", "install", string, "--no-warn-script-location").redirectErrorStream(true).start();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));){
                String string2;
                while ((string2 = bufferedReader.readLine()) != null) {
                    RockstarClient.internalField0572.info("[pip] {}", (Object)string2);
                }
            }
            int n = process.waitFor();
            if (n == 0) {
                RockstarClient.internalField0572.info("[Scripts] \u0431\u0438\u0431\u043b\u0438\u043e\u0442\u0435\u043a\u0430 '{}' \u0433\u043e\u0442\u043e\u0432\u0430", (Object)string);
            } else {
                RockstarClient.internalField0572.warn("[Scripts] \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u0438\u0442\u044c '{}' (pip \u043a\u043e\u0434 {})", (Object)string, (Object)n);
                internalField0546.remove(string.toLowerCase(Locale.ROOT));
            }
        }
        catch (Exception exception) {
            RockstarClient.internalField0572.error("[Scripts] \u043e\u0448\u0438\u0431\u043a\u0430 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0438 '{}'", (Object)string, (Object)exception);
            internalField0546.remove(string.toLowerCase(Locale.ROOT));
        }
    }
}

