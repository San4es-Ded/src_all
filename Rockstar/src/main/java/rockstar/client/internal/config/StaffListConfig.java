package rockstar.client.internal.config;




import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import rockstar.client.internal.script.StaffListStore;
import rockstar.client.internal.config.AbstractClientConfig;
import rockstar.client.internal.script.ConfigManager;
import rockstar.client.internal.core.ConfigFileAnnotation;
import rockstar.client.RockstarClient;

@ConfigFileAnnotation(internalMethod03654="staff", internalMethod00190="txt")
public final class StaffListConfig
extends AbstractClientConfig {
    @Override
    public void internalMethod07509() {
        try {
            List<String> list = RockstarClient.getInstance().internalMethod04407().internalMethod07279().stream().map(nestedValue0077 -> "[" + nestedValue0077.internalMethod04906() + "] " + nestedValue0077.internalMethod00138()).toList();
            ConfigManager.internalMethod04682(this.internalMethod05023(), String.join((CharSequence)System.lineSeparator(), list));
        }
        catch (Exception exception) {
            RockstarClient.internalField0572.error("Failed to save staff list", (Throwable)exception);
        }
    }

    @Override
    public void internalMethod07512() {
        try {
            ArrayList<StaffListStore.InternalType0181> arrayList = new ArrayList<StaffListStore.InternalType0181>();
            for (String string : Files.readAllLines(this.internalMethod05023().toPath(), StandardCharsets.UTF_8)) {
                StaffListStore.InternalType0181 nestedValue0077 = this.internalMethod03288(string);
                if (nestedValue0077 == null) continue;
                arrayList.add(nestedValue0077);
            }
            RockstarClient.getInstance().internalMethod04407().internalMethod03199(arrayList);
        }
        catch (Exception exception) {
            RockstarClient.internalField0572.error("Failed to load staff list", (Throwable)exception);
        }
    }

    private StaffListStore.InternalType0181 internalMethod03288(String string) {
        int n;
        if (string == null || string.isBlank()) {
            return null;
        }
        String string2 = string.trim();
        if (string2.startsWith("[") && (n = string2.indexOf(93)) > 1 && n + 1 < string2.length()) {
            return new StaffListStore.InternalType0181(string2.substring(n + 1).trim(), string2.substring(1, n).trim());
        }
        n = string2.indexOf(58);
        if (n > 0 && n + 1 < string2.length()) {
            return new StaffListStore.InternalType0181(string2.substring(n + 1).trim(), string2.substring(0, n).trim());
        }
        return new StaffListStore.InternalType0181(string2, "MODER");
    }
}

