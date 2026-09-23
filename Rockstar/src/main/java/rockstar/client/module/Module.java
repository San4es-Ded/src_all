package rockstar.client.module;







import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.notification.*;
import rockstar.client.i18n.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.internal.event.*;
import rockstar.client.internal.core.*;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import pyrock.classes.PyModule;
import pyrock.events.client.ModuleToggledEvent;
import rockstar.modules.other.GlobalsMenuModule;
import rockstar.modules.other.SoundsModule;
import rockstar.modules.visual.MenuModule;
import rockstar.client.setting.Setting;
import rockstar.client.i18n.Language;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.module.ModuleEntry;
import rockstar.client.internal.event.ModuleToggleEvent;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.util.TextUtils;
import rockstar.client.internal.core.ClientSoundEvents;
import rockstar.client.notification.NotificationType;
import rockstar.profile.Profile;
import rockstar.profile.Role;

public abstract class Module
implements ModuleEntry {
    private int internalField0227;
    private ModuleCategory internalField0405;
    private boolean internalField0277;
    private boolean internalField0276;
    private String internalField0248;
    private boolean internalField1100;
    private boolean internalField1102;
    private List<Setting> internalField0416 = new ArrayList<Setting>();
    private int internalField0228;
    private final Map<String, JsonElement> internalField0543 = new HashMap<String, JsonElement>();
    private final AnimatedValue internalField0808 = new AnimatedValue(300L, 0.0f, Easing.internalField1626);

    public Module() {
        ModuleInfo typedValue151 = this.getClass().getAnnotation(ModuleInfo.class);
        this.internalField1100 = typedValue151.internalMethod08049();
        this.internalField1102 = typedValue151.internalMethod08050();
        this.internalField0248 = typedValue151.name();
        this.internalField0405 = typedValue151.category();
        this.internalField0228 = this.internalField0227 = typedValue151.defaultKey();
    }

    public Module(String string, ModuleCategory typedValue150, int n) {
        this.internalField0248 = string;
        this.internalField0405 = typedValue150;
        this.internalField0227 = n;
        this.internalField0228 = n;
    }

    @Override
    public final void toggle() {
        this.setEnabled(!this.internalField0277, false);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    public static boolean internalMethod08663() {
        Role role = Profile.getRole();
        return role == Role.ADMIN || role == Role.OWNER;
    }

    public static boolean internalMethod08664() {
        return !Profile.getUsername().isEmpty();
    }

    @Override
    public final void setKeybind(int n) {
        if (this.internalField0227 == n) {
            return;
        }
        this.internalField0227 = n;
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new ModuleToggleEvent(this));
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void internalMethod08229() {
    }

    @Override
    public final void disable() {
        this.setEnabled(false, false);
    }

    @Override
    public final void enable() {
        this.setEnabled(true, false);
    }

    @Override
    public final void setEnabled(boolean bl, boolean bl2) {
        if (!bl && this.internalField1102) {
            return;
        }
        if (this.internalField0277 == bl) {
            return;
        }
        this.internalField0277 = bl;
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new ModuleToggledEvent(new PyModule(this), this.internalField0277));
        if (!(this instanceof MenuModule) && !(this instanceof GlobalsMenuModule) && RockstarClient.getInstance().getModuleManager().getModule(SoundsModule.class).isEnabled() && !bl2) {
            ClientSoundEvents.internalField0131.internalMethod03132(RockstarClient.getInstance().getModuleManager().getModule(SoundsModule.class).internalMethod01798(), this.internalField0277 ? 1.1f : 1.0f);
        }
        if (this.internalField0277) {
            RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
            if (!(bl2 || this instanceof MenuModule || this instanceof GlobalsMenuModule)) {
                RockstarClient.getInstance().internalMethod02503().internalMethod04075(NotificationType.internalField0704, this.internalField0248.replace(" ", "") + " " + LanguageManager.internalMethod07214("enabled") + (LanguageManager.internalMethod00625() == Language.internalField0164 ? TextUtils.internalMethod05852(this.internalField0248) : ""));
            }
            this.onEnable();
        } else {
            RockstarClient.getInstance().internalMethod03317().internalMethod07237(this);
            if (!(bl2 || this instanceof MenuModule || this instanceof GlobalsMenuModule)) {
                RockstarClient.getInstance().internalMethod02503().internalMethod04075(NotificationType.internalField0705, this.internalField0248.replace(" ", "") + " " + LanguageManager.internalMethod07214("disabled") + (LanguageManager.internalMethod00625() == Language.internalField0164 ? TextUtils.internalMethod05852(this.internalField0248) : ""));
            }
            this.onDisable();
        }
    }

    public String internalMethod05374(String string) {
        return "modules.settings." + this.getName().toLowerCase().replace(" ", "_") + "." + string;
    }

    public final void internalMethod09922() {
        this.internalField0228 = this.internalField0227;
        this.internalField0543.clear();
        for (Setting typedValue157 : this.internalField0416) {
            this.internalField0543.put(typedValue157.getName(), typedValue157.toJson());
        }
    }

    public final void internalMethod09923() {
        this.setKeybind(this.internalField0228);
        for (Setting typedValue157 : this.internalField0416) {
            JsonElement jsonElement = this.internalField0543.get(typedValue157.getName());
            if (jsonElement == null) continue;
            typedValue157.fromJson(jsonElement);
        }
    }

    @Override
    @Generated
    public int getKeybind() {
        return this.internalField0227;
    }

    @Override
    @Generated
    public ModuleCategory getCategory() {
        return this.internalField0405;
    }

    @Override
    @Generated
    public boolean isEnabled() {
        return this.internalField0277;
    }

    @Override
    @Generated
    public boolean internalMethod08983() {
        return this.internalField0276;
    }

    @Override
    @Generated
    public String getName() {
        return this.internalField0248;
    }

    @Override
    @Generated
    public boolean isEnabledByDefault() {
        return this.internalField1100;
    }

    @Generated
    public boolean internalMethod09924() {
        return this.internalField1102;
    }

    @Override
    @Generated
    public List<Setting> getSettings() {
        return this.internalField0416;
    }

    @Generated
    public int internalMethod02361() {
        return this.internalField0228;
    }

    @Generated
    public Map<String, JsonElement> internalMethod03454() {
        return this.internalField0543;
    }

    @Override
    @Generated
    public AnimatedValue internalMethod00056() {
        return this.internalField0808;
    }

    @Generated
    public void internalMethod05979(ModuleCategory typedValue150) {
        this.internalField0405 = typedValue150;
    }

    @Generated
    public void internalMethod05084(boolean bl) {
        this.internalField0277 = bl;
    }

    @Generated
    public void internalMethod05149(boolean bl) {
        this.internalField0276 = bl;
    }

    @Generated
    public void internalMethod02189(String string) {
        this.internalField0248 = string;
    }

    @Generated
    public void internalMethod09144(boolean bl) {
        this.internalField1100 = bl;
    }

    @Generated
    public void internalMethod07912(boolean bl) {
        this.internalField1102 = bl;
    }

    @Generated
    public void internalMethod01563(List<Setting> list) {
        this.internalField0416 = list;
    }

    @Generated
    public void internalMethod05148(int n) {
        this.internalField0228 = n;
    }
}
