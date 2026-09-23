package haron.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConfigProfileEntry {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM HH:mm");
    private String name;
    private final String data;
    private final LocalDateTime createdAt;
    private boolean selected;
    private final boolean shared;
    private final int remoteId;
    private final String owner;

    public ConfigProfileEntry(String string, String string2, LocalDateTime localDateTime) {
        this.name = string;
        this.data = string2;
        this.createdAt = localDateTime;
        this.selected = false;
        this.shared = false;
        this.remoteId = 0;
        this.owner = null;
    }

    public ConfigProfileEntry(int n, String string, String string2, String string3, LocalDateTime localDateTime) {
        this.name = string;
        this.data = string2;
        this.createdAt = localDateTime;
        this.selected = false;
        this.shared = true;
        this.remoteId = n;
        this.owner = string3;
    }

    public ConfigProfileEntry(String string, String string2) {
        this(string, string2, LocalDateTime.now());
    }

    public boolean e() {
        int n = 759;
        return this.selected;
    }

    public String b() {
        return this.owner;
    }

    public LocalDateTime c() {
        return this.createdAt;
    }

    public String h() {
        return this.owner;
    }

    public boolean f() {
        return this.shared;
    }

    public String d() {
        return this.createdAt.format(DATE_FORMAT);
    }

    public void a(String string) {
        this.name = string;
    }

    public String a() {
        return this.name;
    }

    public void a(boolean bl) {
        this.selected = bl;
    }

    public int g() {
        return this.remoteId;
    }

    public String getData() {
        return this.data;
    }
}

