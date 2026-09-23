package haron.config;

import haron.config.ClientColorConfig;
import haron.config.SettingConfig;
import haron.config.ModuleConfig;
import haron.config.FriendConfig;
import haron.config.MarkerConfig;
import haron.config.RemoteProfileSummary;
import haron.config.HudPositionConfig;
import haron.config.RemoteConfigRepository;
import haron.config.NotificationVisibilityConfig;
import haron.config.MarkerSettingsConfig;
import haron.config.RemoteProfileEntry;
import haron.config.HaronConfigSnapshot;
import haron.config.LocalConfigManager;
import haron.gui.core.DockPosition;
import haron.gui.core.ClickGuiScreen;
import haron.gui.friends.FriendsTab;
import haron.hud.core.HudElement;
import haron.hud.core.HudManager;
import haron.hud.notifications.NotificationSettingsPopup;
import haron.hud.notifications.eq8z6w;
import haron.hud.notifications.gv8nup;
import haron.hud.notifications.oo89jz;
import haron.hud.notifications.NotificationHudManager;
import haron.markers.MarkerDisplaySettings;
import haron.module.ModuleManager;
import haron.module.HaronModule;
import haron.modules.hud.ClientColor;
import haron.settings.NumberSetting;
import haron.settings.ColorSetting;
import haron.settings.KeybindSetting;
import haron.settings.Setting;
import haron.settings.SettingGroup;
import haron.settings.ModeSetting;
import haron.settings.ValidatedTextSetting;
import haron.settings.ItemHighlightSetting;
import haron.settings.BooleanSetting;
import java.awt.Color;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.apache.logging.log4j.LogManager;
import ru.haron.Haron;

public class ClientConfigCoordinator {
    private static final int b = 30;
    private RemoteConfigRepository d;
    private ScheduledExecutorService e;
    private volatile boolean f = false;
    private static final ClientConfigCoordinator a = new ClientConfigCoordinator();
    private static final DateTimeFormatter c = DateTimeFormatter.ISO_LOCAL_DATE;

    private ClientConfigCoordinator() {
    }

    public void e() {
    }

    public boolean i() {
        return true;
    }

    private void b(String string) {
        if (string != null) {
            try {
                DockPosition dockPosition = DockPosition.valueOf(string);
                Screen screen = Haron.getInstance().getClickGui();
                if (screen instanceof ClickGuiScreen) {
                    ClickGuiScreen xm1fpp2 = (ClickGuiScreen)screen;
                    xm1fpp2.setDock(dockPosition);
                }
            }
            catch (IllegalArgumentException illegalArgumentException) {
                Haron.getLOGGER().warn(ClientConfigCoordinator.$sf$0(string));
            }
            catch (Exception exception) {
                Haron.getLOGGER().error("crypt", (Throwable)exception);
            }
        }
    }

    private void b(List<FriendConfig> list) {
        if (list == null) {
            return;
        }
        try {
            FriendsTab.clearFriends();
            for (FriendConfig i8p39m2 : list) {
                if (i8p39m2 == null || i8p39m2.a() == null) continue;
                FriendsTab.addFriend(i8p39m2.a());
            }
        }
        catch (Exception exception) {
            Haron.getLOGGER().error("[Haron] friends load failed", (Throwable)exception);
        }
    }

    private void b(Map<String, HudPositionConfig> map) {
        if (map != null) {
            HudManager kxdl862 = HudManager.a();
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            float f = (float)((double)minecraftClient.getWindow().getFramebufferWidth() / 2.0);
            float f2 = (float)((double)minecraftClient.getWindow().getFramebufferHeight() / 2.0);
            this.a(kxdl862.i(), map.get("crypt"), f, f2);
            this.a(kxdl862.j(), map.get("crypt"), f, f2);
            this.a(kxdl862.k(), map.get("crypt"), f, f2);
            this.a(kxdl862.l(), map.get("crypt"), f, f2);
            kxdl862.d();
        }
    }

    public void b(String string, Runnable runnable, Consumer<String> consumer) {
        if (string != null) {
            RemoteConfigRepository rdqdh62 = this.j();
            if (rdqdh62 != null) {
                try {
                    Field field = RemoteConfigRepository.class.getDeclaredField("i");
                    field.setAccessible(true);
                    ((Map)field.get(rdqdh62)).remove(string);
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            }
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    public void b() {
    }

    private String s() {
        try {
            Screen screen = Haron.getInstance().getClickGui();
            if (screen instanceof ClickGuiScreen) {
                ClickGuiScreen xm1fpp2 = (ClickGuiScreen)screen;
                return xm1fpp2.getDock().name();
            }
        }
        catch (Exception exception) {
            Haron.getLOGGER().error("crypt", (Throwable)exception);
        }
        return "crypt";
    }

    private void c(List<MarkerConfig> list) {
        if (list != null) {
            gv8nup.g().forEach(gv8nup::b);
            for (MarkerConfig nafzlb2 : list) {
                try {
                    gv8nup.a(new eq8z6w(nafzlb2.a(), nafzlb2.b(), nafzlb2.c(), nafzlb2.d(), new Color(nafzlb2.e()), oo89jz.valueOf(nafzlb2.f())));
                }
                catch (Exception exception) {
                    Haron.getLOGGER().error(ClientConfigCoordinator.$sf$0(nafzlb2.a()), (Throwable)exception);
                }
            }
        }
    }

    public void c() {
    }

    public void c(String string, Runnable runnable, Consumer<String> consumer) {
        RemoteConfigRepository rdqdh62;
        if (string == null || string.trim().isEmpty() || (rdqdh62 = this.j()) == null) {
            return;
        }
        String string2 = string.trim();
        try {
            RemoteProfileSummary ozellm2 = new RemoteProfileSummary();
            ozellm2.b = string2;
            ozellm2.c = "";
            ozellm2.g = System.currentTimeMillis();
            Field field = RemoteConfigRepository.class.getDeclaredField("i");
            field.setAccessible(true);
            ((Map)field.get(rdqdh62)).put(string2, ozellm2);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    private Map<String, ModuleConfig> n() {
        HashMap<String, ModuleConfig> hashMap = new HashMap<String, ModuleConfig>();
        for (HaronModule jxs16t2 : ModuleManager.all()) {
            ModuleConfig hncvt12 = new ModuleConfig();
            hncvt12.a(jxs16t2.k());
            hncvt12.a(jxs16t2.j());
            hncvt12.a(this.a(jxs16t2.m()));
            hashMap.put(jxs16t2.g(), hncvt12);
        }
        return hashMap;
    }

    public void h() {
        try {
            LocalConfigManager.get().requestSave("state-changed");
        }
        catch (Exception exception) {
            LogManager.getLogger((String)"haron/config").warn("[Haron] Autosave request failed", (Throwable)exception);
        }
    }

    public void f() {
    }

    public HaronConfigSnapshot l() {
        return null;
    }

    public void d() {
    }

    private void a(List<Setting<?>> list, Map<String, SettingConfig> map) {
        if (map != null) {
            for (Setting<?> mmdapc2 : list) {
                SettingConfig bw3xj62 = map.get(mmdapc2.f());
                if (bw3xj62 == null) continue;
                try {
                    Object object;
                    if (mmdapc2 instanceof BooleanSetting && "crypt".equals(bw3xj62.a())) {
                        ((BooleanSetting)mmdapc2).a((Boolean)bw3xj62.b());
                    }
                    if (mmdapc2 instanceof NumberSetting && "crypt".equals(bw3xj62.a()) && (object = bw3xj62.b()) instanceof Number) {
                        ((NumberSetting)mmdapc2).a(((Number)object).floatValue());
                    }
                    if (mmdapc2 instanceof ColorSetting && "crypt".equals(bw3xj62.a())) {
                        ((ColorSetting)mmdapc2).a(bw3xj62.c().floatValue(), bw3xj62.d().floatValue(), bw3xj62.e().floatValue());
                    }
                    if (mmdapc2 instanceof KeybindSetting && "crypt".equals(bw3xj62.a()) && (object = bw3xj62.b()) instanceof Number) {
                        ((KeybindSetting)mmdapc2).a(((Number)object).intValue());
                    }
                    if (mmdapc2 instanceof ModeSetting && "crypt".equals(bw3xj62.a())) {
                        object = (ModeSetting)mmdapc2;
                        Object object2 = bw3xj62.b();
                        if (object2 instanceof Number) {
                            ((Setting)object).a(((Number)object2).intValue());
                        }
                        if (((ModeSetting)object).c() && bw3xj62.f() != null) {
                            HashSet<Integer> hashSet = new HashSet<Integer>();
                            for (int n : bw3xj62.f()) {
                                hashSet.add(n);
                            }
                            ((ModeSetting)object).a((Set<Integer>)hashSet);
                        }
                    }
                    if (mmdapc2 instanceof ValidatedTextSetting && "crypt".equals(bw3xj62.a())) {
                        ((ValidatedTextSetting)mmdapc2).a((String)bw3xj62.b());
                    }
                    if (!(mmdapc2 instanceof ItemHighlightSetting) || !"crypt".equals(bw3xj62.a())) continue;
                    object = (ItemHighlightSetting)mmdapc2;
                    if (bw3xj62.g() != null) {
                        ((Setting)object).a(bw3xj62.g());
                    }
                    if (bw3xj62.h() == null) continue;
                    ((ItemHighlightSetting)object).a(new Color(bw3xj62.h()));
                }
                catch (Exception exception) {
                    Haron.getLOGGER().error(ClientConfigCoordinator.$sf$0(mmdapc2.f()), (Throwable)exception);
                }
            }
        }
    }

    private void a(Map<String, ModuleConfig> map) {
        if (map != null) {
            for (HaronModule jxs16t2 : ModuleManager.all()) {
                ModuleConfig hncvt12 = map.get(jxs16t2.g());
                if (hncvt12 == null) continue;
                jxs16t2.a(hncvt12.b());
                jxs16t2.b(hncvt12.a());
                this.a(jxs16t2.m(), hncvt12.c());
            }
        }
    }

    public static ClientConfigCoordinator a() {
        return a;
    }

    private void a(NotificationVisibilityConfig sewxud2) {
        NotificationHudManager notificationHud;
        if (sewxud2 == null || (notificationHud = NotificationHudManager.getInstance()) == null) {
            return;
        }
        NotificationSettingsPopup settingsPopup = notificationHud.settingsPopup();
        settingsPopup.setNotificationsEnabled(sewxud2.areNotificationsEnabled());
        settingsPopup.setMusicNotificationsEnabled(sewxud2.isMusicEnabled());
        settingsPopup.setPerformanceNotificationsEnabled(sewxud2.isPerformanceEnabled());
        settingsPopup.setEventNotificationsEnabled(sewxud2.areEventsEnabled());
        settingsPopup.setModuleNotificationsEnabled(sewxud2.areModulesEnabled());
    }

    public void a(int n, Consumer<HaronConfigSnapshot> consumer, Consumer<String> consumer2) {
    }

    private void a(HudElement hylpge2, HudPositionConfig p8xsxi2, float f, float f2) {
        if (hylpge2 == null || p8xsxi2 == null) {
            return;
        }
        hylpge2.t().a(p8xsxi2.a());
        hylpge2.t().b(p8xsxi2.b());
        hylpge2.b();
        float f3 = hylpge2.t().a(hylpge2.n(), f);
        float f4 = hylpge2.t().b(hylpge2.o(), f2);
        hylpge2.a(f3);
        hylpge2.b(f4);
    }

    private void a(MarkerSettingsConfig srqyen2) {
        if (srqyen2 != null) {
            MarkerDisplaySettings.a(srqyen2.a());
            MarkerDisplaySettings.a(srqyen2.b());
            MarkerDisplaySettings.b(srqyen2.c());
            MarkerDisplaySettings.c(srqyen2.d());
        }
    }

    private Map<String, SettingConfig> a(List<Setting<?>> list) {
        HashMap<String, SettingConfig> hashMap = new HashMap<String, SettingConfig>();
        for (Setting<?> mmdapc2 : list) {
            if (mmdapc2 instanceof SettingGroup) continue;
            SettingConfig bw3xj62 = new SettingConfig();
            if (mmdapc2 instanceof BooleanSetting) {
                bw3xj62.a("crypt");
                bw3xj62.a(((BooleanSetting)mmdapc2).a());
            } else if (mmdapc2 instanceof NumberSetting) {
                bw3xj62.a("crypt");
                bw3xj62.a(Float.valueOf(((NumberSetting)mmdapc2).a()));
            } else if (mmdapc2 instanceof ColorSetting colorSetting) {
                bw3xj62.a("crypt");
                bw3xj62.a(Float.valueOf(colorSetting.c()));
                bw3xj62.b(Float.valueOf(colorSetting.d()));
                bw3xj62.c(Float.valueOf(colorSetting.e()));
            } else if (mmdapc2 instanceof KeybindSetting) {
                bw3xj62.a("crypt");
                bw3xj62.a(((KeybindSetting)mmdapc2).a());
            } else if (mmdapc2 instanceof ModeSetting modeSetting) {
                bw3xj62.a("crypt");
                bw3xj62.a((Integer)modeSetting.k());
                if (modeSetting.c()) {
                    bw3xj62.a(modeSetting.e().stream().mapToInt(n -> {
                        return n;
                    }).toArray());
                }
            } else if (mmdapc2 instanceof ValidatedTextSetting) {
                bw3xj62.a("crypt");
                bw3xj62.a((Object)((ValidatedTextSetting)mmdapc2).a());
            } else if (mmdapc2 instanceof ItemHighlightSetting rangeSetting) {
                bw3xj62.a("crypt");
                bw3xj62.a(rangeSetting.a());
                bw3xj62.a(rangeSetting.e());
            } else {
                Haron.getLOGGER().warn("crypt", (Object)mmdapc2.getClass().getSimpleName(), (Object)mmdapc2.f());
            }
            hashMap.put(mmdapc2.f(), bw3xj62);
        }
        return hashMap;
    }

    public void a(HaronConfigSnapshot xc39mk2) {
    }

    public void a(String string, int n, Integer n2, Integer n3, Consumer<String> consumer, Consumer<String> consumer2) {
    }

    public void a(String string, Runnable runnable, Consumer<String> consumer) {
        if (runnable != null) {
            runnable.run();
        }
    }

    public void a(String string, String string2, Runnable runnable, Consumer<String> consumer) {
    }

    private HudPositionConfig a(HudElement hylpge2) {
        return new HudPositionConfig(hylpge2.t().a(), hylpge2.t().b(), hylpge2.g());
    }

    private void a(ClientColorConfig b617vo2) {
        if (b617vo2 != null) {
            ClientColor byzlib2 = ModuleManager.CLIENT_COLOR;
            String[] stringArray = byzlib2.a.a();
            for (int i = 0; i < stringArray.length; ++i) {
                if (!stringArray[i].equals(b617vo2.a())) continue;
                byzlib2.a.a(Integer.valueOf(i));
                break;
            }
            byzlib2.b.a(b617vo2.b(), b617vo2.c(), b617vo2.d());
            byzlib2.e.a(b617vo2.e(), b617vo2.f(), b617vo2.g());
            byzlib2.f.a(b617vo2.h(), b617vo2.i(), b617vo2.j());
        }
    }

    public boolean a(String string) {
        return false;
    }

    private void m() {
        if (this.e != null) {
            this.e.shutdown();
        }
        this.e = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "crypt");
            thread.setDaemon(true);
            return thread;
        });
        this.e.scheduleAtFixedRate(() -> {
            if (this.d == null || !this.d.i()) {
                return;
            }
            try {
                this.d.a(this.l(), (Runnable)null, (Consumer<String>)null);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }, 30L, 30L, TimeUnit.SECONDS);
    }

    private List<FriendConfig> o() {
        ArrayList<FriendConfig> arrayList = new ArrayList<FriendConfig>();
        try {
            String string = LocalDate.now().format(c);
            for (String string2 : FriendsTab.allFriends()) {
                FriendConfig i8p39m2 = new FriendConfig();
                i8p39m2.a(string2);
                i8p39m2.b(string);
                arrayList.add(i8p39m2);
            }
        }
        catch (Exception exception) {
            Haron.getLOGGER().error("[Haron] friends save failed", (Throwable)exception);
        }
        return arrayList;
    }

    private List<MarkerConfig> p() {
        ArrayList<MarkerConfig> arrayList = new ArrayList<MarkerConfig>();
        for (eq8z6w eq8z6w2 : gv8nup.a()) {
            if (eq8z6w2.j()) continue;
            MarkerConfig nafzlb2 = new MarkerConfig();
            nafzlb2.a(eq8z6w2.a());
            nafzlb2.a(eq8z6w2.b());
            nafzlb2.b(eq8z6w2.c());
            nafzlb2.c(eq8z6w2.d());
            nafzlb2.d(eq8z6w2.e().getRGB());
            nafzlb2.b(eq8z6w2.f().name());
            nafzlb2.a(eq8z6w2.j());
            nafzlb2.a(eq8z6w2.k());
            nafzlb2.e(eq8z6w2.l());
            nafzlb2.b(eq8z6w2.n());
            arrayList.add(nafzlb2);
        }
        return arrayList;
    }

    public List<RemoteProfileEntry> k() {
        return Collections.emptyList();
    }

    private NotificationVisibilityConfig t() {
        NotificationVisibilityConfig sewxud2 = new NotificationVisibilityConfig();
        NotificationHudManager notificationHud = NotificationHudManager.getInstance();
        if (notificationHud != null) {
            NotificationSettingsPopup settingsPopup = notificationHud.settingsPopup();
            sewxud2.setNotificationsEnabled(settingsPopup.areNotificationsEnabled());
            sewxud2.setMusicEnabled(settingsPopup.areMusicNotificationsEnabled());
            sewxud2.setPerformanceEnabled(settingsPopup.arePerformanceNotificationsEnabled());
            sewxud2.setEventsEnabled(settingsPopup.areEventNotificationsEnabled());
            sewxud2.setModulesEnabled(settingsPopup.areModuleNotificationsEnabled());
        }
        return sewxud2;
    }

    public String g() {
        return "default";
    }

    public RemoteConfigRepository j() {
        if (this.d == null) {
            this.d = new RemoteConfigRepository();
            try {
                RemoteProfileSummary ozellm2 = new RemoteProfileSummary();
                ozellm2.b = "default";
                ozellm2.c = "";
                ozellm2.g = System.currentTimeMillis();
                Field field = RemoteConfigRepository.class.getDeclaredField("i");
                field.setAccessible(true);
                ((Map)field.get(this.d)).put("default", ozellm2);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        return this.d;
    }

    private MarkerSettingsConfig q() {
        MarkerSettingsConfig srqyen2 = new MarkerSettingsConfig();
        srqyen2.a(MarkerDisplaySettings.a());
        srqyen2.a(MarkerDisplaySettings.b());
        srqyen2.b(MarkerDisplaySettings.c());
        srqyen2.c(MarkerDisplaySettings.d());
        srqyen2.d(MarkerDisplaySettings.e());
        return srqyen2;
    }

    private ClientColorConfig u() {
        ClientColorConfig b617vo2 = new ClientColorConfig();
        ClientColor byzlib2 = ModuleManager.CLIENT_COLOR;
        b617vo2.a(byzlib2.a.d());
        b617vo2.a(byzlib2.b.c());
        b617vo2.b(byzlib2.b.d());
        b617vo2.c(byzlib2.b.e());
        b617vo2.d(byzlib2.e.c());
        b617vo2.e(byzlib2.e.d());
        b617vo2.f(byzlib2.e.e());
        b617vo2.g(byzlib2.f.c());
        b617vo2.h(byzlib2.f.d());
        b617vo2.i(byzlib2.f.e());
        return b617vo2;
    }

    private Map<String, HudPositionConfig> r() {
        HashMap<String, HudPositionConfig> hashMap = new HashMap<String, HudPositionConfig>();
        HudManager kxdl862 = HudManager.a();
        if (kxdl862.i() != null) {
            hashMap.put("crypt", this.a(kxdl862.i()));
        }
        if (kxdl862.j() != null) {
            hashMap.put("crypt", this.a(kxdl862.j()));
        }
        if (kxdl862.k() != null) {
            hashMap.put("crypt", this.a(kxdl862.k()));
        }
        if (kxdl862.l() != null) {
            hashMap.put("crypt", this.a(kxdl862.l()));
        }
        return hashMap;
    }

    private static /* synthetic */ String $sf$0(String string) {
        return "crypt" + string;
    }
}
