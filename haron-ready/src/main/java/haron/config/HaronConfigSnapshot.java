package haron.config;

import haron.config.ConfigMetadata;
import haron.config.ClientColorConfig;
import haron.config.ModuleConfig;
import haron.config.FriendConfig;
import haron.config.MarkerConfig;
import haron.config.HudPositionConfig;
import haron.config.NotificationVisibilityConfig;
import haron.config.MarkerSettingsConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HaronConfigSnapshot {
    public static final int a = 1;
    private int d = 1;
    private ConfigMetadata e;
    private Map<String, ModuleConfig> f = new HashMap<String, ModuleConfig>();
    private List<FriendConfig> g = new ArrayList<FriendConfig>();
    private List<MarkerConfig> h = new ArrayList<MarkerConfig>();
    private MarkerSettingsConfig i = new MarkerSettingsConfig();
    private Map<String, HudPositionConfig> j = new HashMap<String, HudPositionConfig>();
    private String k = "BOTTOM";
    private NotificationVisibilityConfig l = new NotificationVisibilityConfig();
    private ClientColorConfig m = new ClientColorConfig();
    public static int b;
    public static boolean c;

    public HaronConfigSnapshot() {
    }

    public HaronConfigSnapshot(ConfigMetadata au05792) {
        this.e = au05792;
    }

    public List<MarkerConfig> e() {
        return this.h;
    }

    public NotificationVisibilityConfig i() {
        return this.l;
    }

    public void b(Map<String, HudPositionConfig> map) {
        this.j = map;
    }

    public void b(List<MarkerConfig> list) {
        this.h = list;
    }

    public ConfigMetadata b() {
        return this.e;
    }

    public Map<String, ModuleConfig> c() {
        return this.f;
    }

    public String h() {
        return this.k;
    }

    public MarkerSettingsConfig f() {
        return this.i;
    }

    public List<FriendConfig> d() {
        return this.g;
    }

    public int a() {
        return this.d;
    }

    public void a(NotificationVisibilityConfig sewxud2) {
        this.l = sewxud2;
    }

    public void a(ConfigMetadata au05792) {
        this.e = au05792;
    }

    public void a(ClientColorConfig b617vo2) {
        this.m = b617vo2;
    }

    public void a(int n) {
        this.d = n;
    }

    public void a(MarkerSettingsConfig srqyen2) {
        this.i = srqyen2;
    }

    public void a(List<FriendConfig> list) {
        this.g = list;
    }

    public void a(String string) {
        int n = 752;
        this.k = string;
    }

    public void a(Map<String, ModuleConfig> map) {
        this.f = map;
    }

    public Map<String, HudPositionConfig> g() {
        return this.j;
    }

    public ClientColorConfig j() {
        return this.m;
    }
}

