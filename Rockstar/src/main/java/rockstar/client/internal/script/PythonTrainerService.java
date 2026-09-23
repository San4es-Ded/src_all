package rockstar.client.internal.script;





import rockstar.client.rotation.*;
import rockstar.client.internal.core.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import lombok.SneakyThrows;
import net.minecraft.client.MinecraftClient;
import rockstar.client.internal.script.EmbeddedPythonRuntime;
import rockstar.client.RockstarClient;
import rockstar.client.internal.config.NeuroModelConfig;

public final class PythonTrainerService {
    private static final String internalField0248 = "assets/rockstar/neuro/trainer/";
    private static final String internalField0247 = "common.py";
    private static final String internalField1077 = "train_aura.py";
    private static final String internalField1076 = "https://download.pytorch.org/whl/cpu";
    private static final String internalField1079 = "https://pypi.org/simple";
    private static volatile boolean internalField0277;
    private static volatile boolean internalField0276;
    private static volatile String internalField1078;

    private PythonTrainerService() {
    }

    public static void internalMethod07293() {
        if (internalField0277) {
            return;
        }
        internalField0277 = true;
        Thread thread = new Thread(PythonTrainerService::internalMethod07296, "Neuro-Trainer-Setup");
        thread.setDaemon(true);
        thread.start();
    }

    private static void internalMethod07296() {
        File file = PythonTrainerService.internalMethod04801();
        if (file == null) {
            return;
        }
        if (PythonTrainerService.internalMethod07297()) {
            RockstarClient.internalField0572.info("[Neuro] \u0442\u0440\u0435\u043d\u0435\u0440 \u0433\u043e\u0442\u043e\u0432: numpy \u0438 torch \u043d\u0430 \u043c\u0435\u0441\u0442\u0435");
            return;
        }
        PythonTrainerService.internalMethod07030(file);
    }

    private static void internalMethod07030(File file) {
        internalField0276 = true;
        try {
            if (!PythonTrainerService.internalMethod08585() && !PythonTrainerService.internalMethod03588(file, "numpy")) {
                return;
            }
            if (!PythonTrainerService.internalMethod08586()) {
                RockstarClient.internalField0572.info("[Neuro] \u043a\u0430\u0447\u0430\u044e torch \u0434\u043b\u044f \u0442\u0440\u0435\u043d\u0435\u0440\u0430 (~200 \u041c\u0411, \u0440\u0430\u0437\u043e\u0432\u043e)");
                if (!PythonTrainerService.internalMethod03588(file, "torch", "--index-url", internalField1076, "--extra-index-url", internalField1079)) {
                    return;
                }
            }
            if (PythonTrainerService.internalMethod07297()) {
                RockstarClient.internalField0572.info("[Neuro] \u0437\u0430\u0432\u0438\u0441\u0438\u043c\u043e\u0441\u0442\u0438 \u0442\u0440\u0435\u043d\u0435\u0440\u0430 \u043d\u0430 \u043c\u0435\u0441\u0442\u0435, \u043e\u0431\u0443\u0447\u0435\u043d\u0438\u0435 \u0434\u043e\u0441\u0442\u0443\u043f\u043d\u043e");
                internalField1078 = null;
            }
        }
        finally {
            internalField0276 = false;
        }
    }

    private static boolean internalMethod03588(File file, String ... stringArray) {
        ArrayList<String> arrayList = new ArrayList<String>(List.of(file.getAbsolutePath(), "-m", "pip", "install"));
        arrayList.addAll(List.of(stringArray));
        arrayList.add("--no-warn-script-location");
        arrayList.add("--disable-pip-version-check");
        try {
            Process process = new ProcessBuilder(arrayList).redirectErrorStream(true).start();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));){
                String string;
                while ((string = bufferedReader.readLine()) != null) {
                    if (string.isBlank()) continue;
                    RockstarClient.internalField0572.info("[Neuro][pip] {}", (Object)string.strip());
                }
            }
            int n = process.waitFor();
            if (n == 0) {
                return true;
            }
            internalField1078 = "pip " + stringArray[0] + " \u0432\u0435\u0440\u043d\u0443\u043b " + n;
            RockstarClient.internalField0572.warn("[Neuro] {} \u043d\u0435 \u043f\u043e\u0441\u0442\u0430\u0432\u0438\u043b\u0441\u044f (\u043a\u043e\u0434 {})", (Object)stringArray[0], (Object)n);
        }
        catch (Exception exception) {
            internalField1078 = exception.getMessage();
            RockstarClient.internalField0572.error("[Neuro] \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0430 {} \u0441\u043e\u0440\u0432\u0430\u043b\u0430\u0441\u044c", (Object)stringArray[0], (Object)exception);
        }
        return false;
    }

    public static Process internalMethod02657(File file, Path path, Path path2, String string) throws Exception {
        String string2 = PythonTrainerService.internalMethod05807();
        ArrayList<String> arrayList = new ArrayList<String>(List.of(file.getAbsolutePath(), "-u", "-", "--data", path.toString(), "--out", path2.toString()));
        if (string != null) {
            arrayList.add("--epochs");
            arrayList.add(string);
        }
        ProcessBuilder processBuilder = new ProcessBuilder(arrayList).redirectErrorStream(true);
        processBuilder.environment().put("PYTHONIOENCODING", "utf-8");
        processBuilder.environment().put("PYTHONDONTWRITEBYTECODE", "1");
        Process process = processBuilder.start();
        try (OutputStream outputStream = process.getOutputStream();){
            outputStream.write(string2.getBytes(StandardCharsets.UTF_8));
        }
        return process;
    }

    private static String internalMethod05807() {
        String string = PythonTrainerService.internalMethod02032(PythonTrainerService.internalMethod06799(internalField0247));
        String string2 = PythonTrainerService.internalMethod02032(PythonTrainerService.internalMethod06799(internalField1077));
        String string3 = PythonTrainerService.internalMethod03131().toString().replace("\\", "\\\\");
        return "import base64, sys, types\n__rs_home = \"%s\"\n__rs_common = types.ModuleType(\"common\")\n__rs_common.__file__ = __rs_home + \"/common.py\"\nexec(compile(base64.b64decode(\"%s\").decode(\"utf-8\"), \"common.py\", \"exec\"),\n     __rs_common.__dict__)\nsys.modules[\"common\"] = __rs_common\n__rs_globals = {\"__name__\": \"__main__\", \"__file__\": __rs_home + \"/train_aura.py\"}\nexec(compile(base64.b64decode(\"%s\").decode(\"utf-8\"), \"train_aura.py\", \"exec\"),\n     __rs_globals)\n".formatted(string3, string, string2);
    }

    @SneakyThrows(IOException.class)
    private static String internalMethod06799(String string) {
        String string2;
        block12: {
            Path path = MinecraftClient.getInstance().runDirectory.toPath().resolve("../tools/rotation-model/" + string).normalize();
            try {
                if (Files.isRegularFile(path, new LinkOption[0])) {
                    return Files.readString(path, StandardCharsets.UTF_8);
                }
            }
            catch (Exception exception) {
                RockstarClient.internalField0572.warn("[Neuro] \u043d\u0435 \u043f\u0440\u043e\u0447\u0438\u0442\u0430\u0442\u044c \u0442\u0440\u0435\u043d\u0435\u0440 \u0438\u0437 \u0440\u0435\u043f\u043e\u0437\u0438\u0442\u043e\u0440\u0438\u044f: {}", (Object)exception.getMessage());
            }
            InputStream inputStream = PythonTrainerService.class.getClassLoader().getResourceAsStream(internalField0248 + string);
            try {
                if (inputStream == null) {
                    throw new IllegalStateException("\u0432 jar \u043d\u0435\u0442 " + string);
                }
                string2 = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                if (inputStream == null) break block12;
            }
            catch (Throwable throwable) {
                try {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        }
                        catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    }
                    throw throwable;
                }
                catch (Exception exception) {
                    throw new IllegalStateException("\u0442\u0440\u0435\u043d\u0435\u0440 \u043d\u0435 \u0447\u0438\u0442\u0430\u0435\u0442\u0441\u044f: " + exception.getMessage(), exception);
                }
            }
            inputStream.close();
        }
        return string2;
    }

    private static String internalMethod02032(String string) {
        return Base64.getEncoder().encodeToString(string.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean internalMethod07294() {
        return internalField0276;
    }

    public static String internalMethod01147() {
        return internalField1078;
    }

    public static boolean internalMethod07297() {
        return PythonTrainerService.internalMethod08585() && PythonTrainerService.internalMethod08586();
    }

    public static boolean internalMethod08585() {
        return PythonTrainerService.internalMethod05058("numpy");
    }

    public static boolean internalMethod08586() {
        return PythonTrainerService.internalMethod05058("torch");
    }

    private static boolean internalMethod05058(String string) {
        File file = EmbeddedPythonRuntime.internalMethod00088();
        return new File(file, "Lib/site-packages/" + string + "/__init__.py").isFile() || new File(file, "lib/python3.11/site-packages/" + string + "/__init__.py").isFile();
    }

    public static Path internalMethod03131() {
        return NeuroModelConfig.internalMethod06488().resolve("trainer");
    }

    public static File internalMethod04801() {
        File file = EmbeddedPythonRuntime.internalMethod00088();
        File file2 = new File(file, "python.exe");
        if (file2.isFile()) {
            return file2;
        }
        File file3 = new File(file, "bin/python3");
        return file3.isFile() ? file3 : null;
    }
}
