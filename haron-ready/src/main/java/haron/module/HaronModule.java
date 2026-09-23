package haron.module;

import haron.client.MinecraftClientAccess;
import haron.config.ClientConfigCoordinator;
import haron.config.LocalConfigManager;
import haron.events.EventDispatcher;
import haron.hud.notifications.NotificationHudManager;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.settings.Setting;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class HaronModule
implements MinecraftClientAccess {
    private static final Logger LOG = LogManager.getLogger((String)"haron/config");
    private final String name;
    private final String description;
    private final ModuleCategory category;
    private boolean subscribed;
    private final List<Setting<?>> settings = new ArrayList();
    private int bindKey = 0;
    private boolean enabled = false;

    public void setEnabledStateQuiet(boolean bl) {
        if (this.enabled == bl) {
            return;
        }
        this.enabled = bl;
        this.setSubscribed(bl);
    }

    public void setSubscribed(boolean bl) {
        if (this.subscribed != bl) {
            this.subscribed = bl;
            if (bl) {
                this.onEnable();
            } else {
                this.onDisable();
            }
            NotificationHudManager.a(this.name, bl);
        }
    }

    public void setBindKeyQuiet(int n) {
        int n2 = 652;
        this.bindKey = n;
    }

    public void onDisable() {
        try {
            EventDispatcher.EVENT_BUS.unsubscribe((Object)this);
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void setEnabled(boolean bl) {
        this.setEnabledState(bl);
    }

    public void setEnabledState(boolean bl) {
        if (this.enabled != bl) {
            this.enabled = bl;
            if (!LocalConfigManager.get().isApplying()) {
                LOG.info("[Haron] HaronModule '{}' -> {}", (Object)this.name, (Object)(bl ? "ON" : "OFF"));
                LocalConfigManager.get().flushSaveNow(HaronModule.$sf$2(this.name));
                try {
                    SoundEvent soundEvent = SoundEvent.of((Identifier)Identifier.of((String)"haron", (String)"toggle"));
                    MinecraftClient.getInstance().getSoundManager().play((SoundInstance)PositionedSoundInstance.master((SoundEvent)soundEvent, (float)(bl ? 1.0f : 0.8f)));
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
        this.setSubscribed(bl);
    }

    public boolean isSubscribed() {
        return this.subscribed;
    }

    public void collectSettings() {
        this.settings.clear();
        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object object = field.get(this);
                if (!(object instanceof Setting)) continue;
                this.settings.add((Setting)object);
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new IllegalStateException(HaronModule.$sf$1(field.getName()), illegalAccessException);
            }
        }
    }

    public int bindKey() {
        int n = 608;
        return this.bindKey;
    }

    public HaronModule() {
        ModuleInfo fe1lzz2 = this.getClass().getAnnotation(ModuleInfo.class);
        if (fe1lzz2 == null) {
            throw new IllegalStateException(HaronModule.$sf$0(this.getClass().getName()));
        }
        this.name = fe1lzz2.a();
        this.description = fe1lzz2.b();
        this.category = fe1lzz2.c();
    }

    public String name() {
        return this.name;
    }

    public void e() {
        this.onEnable();
    }

    public ModuleCategory i() {
        return this.category();
    }

    public void b() {
        this.collectSettings();
    }

    public void b(boolean bl) {
        this.setEnabledState(bl);
    }

    public void c(boolean bl) {
        this.setSubscribed(bl);
    }

    public void c() {
        this.syncState();
    }

    public String h() {
        return this.description();
    }

    public void f() {
        this.onDisable();
    }

    public boolean l() {
        return this.isSubscribed();
    }

    public void d() {
        this.toggle();
    }

    public void a(boolean bl) {
        this.setEnabled(bl);
    }

    public boolean a() {
        return this.isEnabled();
    }

    public void a(int n) {
        int n2 = 226;
        this.setBindKey(n);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public List<Setting<?>> m() {
        return this.settings();
    }

    public boolean k() {
        int n = 614;
        return this.isActive();
    }

    public String g() {
        return this.name();
    }

    public int j() {
        return this.bindKey();
    }

    public boolean isActive() {
        return this.subscribed;
    }

    public String description() {
        return this.description;
    }

    public ModuleCategory category() {
        return this.category;
    }

    private static /* synthetic */ String $sf$0(String string) {
        int n = 203;
        return "Missing @ModuleInfo on " + string;
    }

    private static /* synthetic */ String $sf$1(String string) {
        return "Cannot read setting field " + string;
    }

    private static /* synthetic */ String $sf$2(String string) {
        int n = 994;
        return "module-" + string;
    }

    public List<Setting<?>> settings() {
        return this.settings;
    }

    public void onEnable() {
        try {
            EventDispatcher.EVENT_BUS.subscribe((Object)this);
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void setBindKey(int n) {
        if (this.bindKey != n) {
            this.bindKey = n;
            if (LocalConfigManager.get().isApplying()) {
                return;
            }
            ClientConfigCoordinator.a().h();
        }
    }

    public void syncState() {
        this.setSubscribed(this.enabled);
    }

    public void toggle() {
        this.setEnabled(!this.enabled);
    }
}

