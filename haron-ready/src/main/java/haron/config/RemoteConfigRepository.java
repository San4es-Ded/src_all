package haron.config;

import haron.config.RemoteProfileSummary;
import haron.config.RemoteProfileEntry;
import haron.config.HaronConfigSnapshot;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import net.minecraft.client.MinecraftClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class RemoteConfigRepository {
    private static int a = 22;
    private static int b = 23;
    private static int c = 24;
    private static int d = 25;
    private static int e = 26;
    private static int f = 27;
    private static int g = 28;
    private final Map<String, RemoteProfileSummary> i = new ConcurrentHashMap<String, RemoteProfileSummary>();
    private final List<RemoteProfileEntry> j = new ArrayList<RemoteProfileEntry>();
    private volatile HaronConfigSnapshot k;
    private volatile String l;
    private volatile int m;
    private volatile boolean n;
    private volatile boolean o;
    private volatile Runnable p;

    public List<String> e() {
        return Collections.emptyList();
    }

    public boolean i() {
        return false;
    }

    public HaronConfigSnapshot b() {
        int n = 575;
        return null;
    }

    public void b(String string, Runnable runnable, Consumer<String> consumer) {
    }

    public boolean b(HaronConfigSnapshot xc39mk2) {
        return false;
    }

    private void b(Runnable runnable) {
        MinecraftClient.getInstance().execute(runnable);
    }

    public String c() {
        return "";
    }

    public void c(String string, Runnable runnable, Consumer<String> consumer) {
    }

    public void h() {
    }

    public Collection<RemoteProfileSummary> f() {
        try {
            Field field = RemoteConfigRepository.class.getDeclaredField("i");
            field.setAccessible(true);
            return new ArrayList<RemoteProfileSummary>(((Map)field.get(this)).values());
        }
        catch (Throwable throwable) {
            return Collections.emptyList();
        }
    }

    public int d() {
        return 0;
    }

    public void d(String string, Runnable runnable, Consumer<String> consumer) {
    }

    public void a(HaronConfigSnapshot xc39mk2, String string, Runnable runnable, Consumer<String> consumer) {
    }

    public void a(HaronConfigSnapshot xc39mk2) {
    }

    public void a(String string, int n, Integer n2, Integer n3, Consumer<String> consumer, Consumer<String> consumer2) {
    }

    public void a(String string, String string2, Runnable runnable, Consumer<String> consumer) {
    }

    public void a(HaronConfigSnapshot xc39mk2, Runnable runnable, Consumer<String> consumer) {
    }

    public void a(String string, Runnable runnable, Consumer<String> consumer) {
    }

    public void a(int n) {
    }

    public void a(JSONObject jSONObject) {
    }

    public void a(Runnable runnable) {
    }

    public void a(int n, Consumer<HaronConfigSnapshot> consumer, Consumer<String> consumer2) {
    }

    public List<RemoteProfileEntry> a() {
        return Collections.emptyList();
    }

    public RemoteProfileSummary a(String string) {
        return null;
    }

    public void a(JSONArray jSONArray) {
    }

    public boolean g() {
        return false;
    }

    private boolean j() {
        return false;
    }
}

