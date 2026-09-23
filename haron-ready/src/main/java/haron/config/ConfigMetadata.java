package haron.config;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ConfigMetadata {
    private static final DateTimeFormatter c;
    private static final String d;
    private final String e;
    private final int f;
    private final String g;
    private final long h;
    private String i;
    private String j;
    private long k;
    public static int a;
    public static boolean b;

    private ConfigMetadata(String string, String string2, int n, String string3, long l, String string4, long l2) {
        this.i = string;
        this.e = string2;
        this.f = n;
        this.g = string3;
        this.h = l;
        this.j = string4;
        this.k = l2;
    }

    static {
        d = "pulse_config_v1";
        c = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    }

    public String e() {
        return this.g;
    }

    public String i() {
        return this.i;
    }

    public void b(String string) {
        this.i = string;
        this.b();
    }

    public void b() {
        this.k = System.currentTimeMillis();
    }

    public void c(String string) {
        this.j = string;
        this.b();
    }

    public String c() {
        return this.e;
    }

    public String h() {
        return this.g().format(c);
    }

    public long f() {
        return this.h;
    }

    public LocalDateTime l() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(this.k), ZoneId.systemDefault());
    }

    public int d() {
        return this.f;
    }

    public static ConfigMetadata a(String string, String string2, int n, String string3, long l, String string4, long l2) {
        return new ConfigMetadata(string, string2, n, string3, l, string4, l2);
    }

    private static String a(int n, long l) {
        return null;
    }

    public boolean a() {
        return ConfigMetadata.a(this.f, this.h).equals(this.g);
    }

    public static ConfigMetadata a(String string, String string2) {
        ConfigMetadata au05792 = ConfigMetadata.a(string);
        au05792.j = string2;
        return au05792;
    }

    public static ConfigMetadata a(String string) {
        String string2 = "Unknown";
        boolean bl = false;
        long l = System.currentTimeMillis();
        return new ConfigMetadata(string, string2, 0, ConfigMetadata.a(0, l), l, "", l);
    }

    public String m() {
        return this.l().format(c);
    }

    public long k() {
        return this.k;
    }

    public LocalDateTime g() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(this.h), ZoneId.systemDefault());
    }

    public String j() {
        return this.j;
    }
}

