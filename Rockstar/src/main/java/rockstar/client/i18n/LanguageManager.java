package rockstar.client.i18n;


import rockstar.client.*;
import rockstar.client.internal.core.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import lombok.Generated;
import pyrock.events.client.LanguageChangedEvent;
import rockstar.client.i18n.Language;
import rockstar.client.internal.core.TranslationOverrideStore;
import rockstar.client.RockstarClient;

public final class LanguageManager {
    private static final Language internalField0165;
    private static Language internalField0164;
    private static final Map<String, String> internalField0543;
    private static boolean internalField0277;
    private static final Map<String, String> internalField0544;

    public static void internalMethod05353() {
        String string = "/assets/" + RockstarClient.internalField1077 + "/lang/" + internalField0164.internalMethod03875() + ".lang";
        try {
            String string2;
            try (InputStream inputStream = LanguageManager.class.getResourceAsStream(string);){
                if (inputStream == null) {
                    throw new RuntimeException("Language file not found: " + string);
                }
                string2 = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }
            string2 = string2.trim();
            internalField0277 = false;
            internalField0543.clear();
            int n = 0;
            for (String string3 : string2.split("\r?\n")) {
                ++n;
                String string4 = LanguageManager.internalMethod08289(string3).trim();
                if (string4.isEmpty()) continue;
                LanguageManager.internalMethod02385(string4, n, string);
            }
            internalField0277 = true;
        }
        catch (IOException iOException) {
            throw new RuntimeException("Failed to load translations for language: " + internalField0164.internalMethod03875(), iOException);
        }
    }

    public static void internalMethod04491(@Nonnull Language typedValue141) {
        internalField0164 = typedValue141;
        internalField0544.clear();
        LanguageManager.internalMethod05353();
        if (RockstarClient.getInstance() != null && RockstarClient.getInstance().internalMethod03317() != null) {
            RockstarClient.getInstance().internalMethod03317().internalMethod06883(new LanguageChangedEvent(typedValue141.internalMethod03875()));
        }
    }

    public static void internalMethod05357() {
        internalField0544.clear();
    }

    public static String internalMethod07214(String string) {
        LanguageManager.internalMethod09046();
        return internalField0544.computeIfAbsent(string, LanguageManager::internalMethod08087);
    }

    public static String internalMethod00160(String string, Object ... objectArray) {
        LanguageManager.internalMethod09046();
        String string2 = internalField0544.computeIfAbsent(string, LanguageManager::internalMethod08087);
        return String.format(string2, objectArray);
    }

    public static String internalMethod00095(String string) {
        LanguageManager.internalMethod09046();
        String string2 = internalField0543.get(string);
        if (string2 != null) {
            return string2;
        }
        String string3 = TranslationOverrideStore.internalMethod03810(string);
        return string3 != null ? string3 : " ";
    }

    private static String internalMethod08087(String string) {
        String string2 = internalField0543.get(string);
        if (string2 != null) {
            return string2;
        }
        String string3 = TranslationOverrideStore.internalMethod03810(string);
        return string3 != null ? string3 : string;
    }

    private static void internalMethod02385(String string, int n, String string2) {
        int n2 = string.indexOf(61);
        if (n2 == -1) {
            RockstarClient.internalField0572.warn("Warning: Invalid line format at line {} in {}: {}", new Object[]{n, string2, string});
            return;
        }
        String string3 = string.substring(0, n2).trim();
        String string4 = string.substring(n2 + 1).trim();
        if (string3.isEmpty()) {
            RockstarClient.internalField0572.warn("Warning: Empty key at line {} in {}", (Object)n, (Object)string2);
            return;
        }
        internalField0543.put(string3, string4);
    }

    private static String internalMethod08289(String string) {
        int n = string.indexOf("#");
        if (n != -1) {
            return string.substring(0, n);
        }
        return string;
    }

    private static void internalMethod09046() {
        if (!internalField0277) {
            LanguageManager.internalMethod05353();
        }
    }

    @Generated
    private LanguageManager() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Generated
    public static Language internalMethod00625() {
        return internalField0164;
    }

    static {
        internalField0164 = internalField0165 = Language.internalField0164;
        internalField0543 = new HashMap<String, String>();
        internalField0544 = new HashMap<String, String>();
    }
}
