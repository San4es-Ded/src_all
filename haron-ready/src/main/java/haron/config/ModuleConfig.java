package haron.config;

import haron.config.SettingConfig;
import java.util.HashMap;
import java.util.Map;

public class ModuleConfig {
    private boolean c;
    private int d;
    private Map<String, SettingConfig> e = new HashMap<String, SettingConfig>();
    public static int a;
    public static boolean b;

    public ModuleConfig() {
    }

    public ModuleConfig(boolean bl, int n) {
        this.c = bl;
        this.d = n;
    }

    public int b() {
        return this.d;
    }

    public Map<String, SettingConfig> c() {
        return this.e;
    }

    public void a(boolean bl) {
        this.c = bl;
    }

    public void a(Map<String, SettingConfig> map) {
        this.e = map;
    }

    public void a(int n) {
        this.d = n;
    }

    public boolean a() {
        return this.c;
    }
}

