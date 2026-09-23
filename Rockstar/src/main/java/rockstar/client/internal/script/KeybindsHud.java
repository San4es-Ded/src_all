package rockstar.client.internal.script;












import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.event.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.screen.ChatScreen;
import pyrock.events.client.ModuleToggledEvent;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.ui.Insets;
import rockstar.client.render.SizedFont;
import rockstar.modules.visual.MenuModule;
import rockstar.client.render.Fonts;
import rockstar.client.ui.TextAlignment;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.render.CornerRadii;
import rockstar.client.internal.config.SharedBooleanSetting;
import rockstar.client.event.EventListener;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.internal.ui.AbstractHudWidget;
import rockstar.client.module.ModuleEntry;
import rockstar.client.ui.VerticalColorGradient;
import rockstar.client.internal.event.ModuleToggleEvent;
import rockstar.client.RockstarClient;
import rockstar.client.ui.UiTransition;
import rockstar.client.ui.UiElement;
import rockstar.client.ui.ThemeColors;
import rockstar.client.util.TextUtils;
import rockstar.client.ui.ScreenMetricsAccess;
import rockstar.client.ui.UiNode;
import rockstar.client.ui.UiContainer;

public class KeybindsHud
extends AbstractHudWidget {
    private final BooleanSetting internalField0650 = new BooleanSetting(this, "hud.always_display");
    private final SharedBooleanSetting internalField0381 = new SharedBooleanSetting(this, "hud.uniform_width");
    private boolean internalField0277;
    private UiContainer internalField0634;
    private UiNode internalField0633;
    private final Map<ModuleEntry, UiNode> internalField0543 = new HashMap<ModuleEntry, UiNode>();
    private final List<ModuleEntry> internalField0416 = new ArrayList<ModuleEntry>();
    private List<ModuleEntry> internalField0417 = List.of();
    private final UiTransition internalField0918 = (f, typedValue004, nestedValue2009) -> {
        nestedValue2009.internalField0205 = f;
        nestedValue2009.internalField0206 = (this.internalField0277 ? 6.0f : -6.0f) * (1.0f - f);
    };
    private final EventListener<ModuleToggledEvent> internalField0157 = moduleToggledEvent -> this.internalMethod00123(moduleToggledEvent.getModule().getModule(), false);
    private final EventListener<ModuleToggleEvent> internalField0158 = typedValue147 -> this.internalMethod00123(typedValue147.internalMethod05415(), true);

    public KeybindsHud() {
        super("hud.keybinds", "keyboard");
        this.internalMethod04560();
        RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
    }

    @Override
    public UiContainer build() {
        this.internalField0543.clear();
        this.internalField0417 = List.of();
        this.internalField0634 = new UiContainer().internalMethod01863().internalMethod01855(this.internalMethod04984()).internalMethod03062(1.0f);
        this.internalField0633 = new UiContainer().internalMethod05895().internalMethod01855(TextAlignment.internalField0621).internalMethod03062(3.0f).internalMethod09266(11.0f).internalMethod03514(Insets.internalMethod02086(3.0f)).internalMethod07178((iII, typedValue006) -> {
            iII.drawClientRect(typedValue006.x(), typedValue006.y(), typedValue006.w(), typedValue006.h(), this.animation.internalMethod02881(), this.dragAnim.internalMethod02881(), 3.0f, 3.0f);
            iII.drawSquircle(typedValue006.x(), typedValue006.y(), 23.0f, typedValue006.h(), 3.0f, CornerRadii.internalMethod03908(3.0f), new VerticalColorGradient(ThemeColors.internalField1610.mulAlpha(0.1f), ThemeColors.internalField1610.mulAlpha(0.0f)));
        }).internalMethod03907(new UiElement().size(7.0f, 7.0f).interactive(false).icon("keyboard", 7.0f, ThemeColors.internalField1310)).internalMethod03907(new UiElement().interactive(false).text(Fonts.internalField0449.internalMethod01432(6.0f), () -> LanguageManager.internalMethod07214(this.name), typedParameter1002 -> ThemeColors.internalField1613));
        this.internalField0634.internalMethod03907(this.internalField0633);
        this.internalMethod04560();
        this.internalMethod06183(true);
        return this.internalField0634;
    }

    private UiNode internalMethod01111(ModuleEntry typedValue146) {
        UiNode typedValue004 = this.internalField0543.computeIfAbsent(typedValue146, typedValue145 -> new UiContainer().internalMethod05895().internalMethod01855(TextAlignment.internalField0621).internalMethod03062(1.0f).internalMethod07914(this.internalField0918).internalMethod03907(new UiElement().interactive(false).fillWidth().height(10.0f).radius(2.0f).padding(Insets.internalMethod02086(3.0f)).background(typedParameter1002 -> ThemeColors.internalField1612.mulAlpha(0.889f)).text(Fonts.internalField1154.internalMethod01432(6.0f), typedValue145::getName, typedParameter1002 -> ThemeColors.internalField1613)).internalMethod03907(new UiElement().interactive(false).height(10.0f).radius(2.0f).padding(Insets.internalMethod02086(3.0f)).background(typedParameter1002 -> ThemeColors.internalField1612.mulAlpha(0.94f)).textAlign(TextAlignment.internalField0621).text(Fonts.internalField1154.internalMethod01432(6.0f), () -> TextUtils.internalMethod04982(typedValue145.getKeybind()), typedParameter1002 -> ThemeColors.internalField1613.mulAlpha(0.75f))));
        if (typedValue004.phase() == UiNode.InternalType0146.internalField1091 || typedValue004.phase() == UiNode.InternalType0146.internalField1090 || typedValue004.phase() == UiNode.InternalType0146.internalField1089) {
            typedValue004.beginEnter(0.0f);
        }
        return typedValue004;
    }

    private void internalMethod06183(boolean bl) {
        if (!bl && this.internalField0416.equals(this.internalField0417)) {
            return;
        }
        this.internalField0417 = List.copyOf(this.internalField0416);
        SizedFont typedValue020 = Fonts.internalField1154.internalMethod01432(6.0f);
        HashMap<ModuleEntry, Float> hashMap = new HashMap<ModuleEntry, Float>();
        for (ModuleEntry typedValue146 : this.internalField0416) {
            hashMap.put(typedValue146, Float.valueOf(typedValue020.internalMethod00965(typedValue146.getName()) + typedValue020.internalMethod00965(TextUtils.internalMethod04982(typedValue146.getKeybind()))));
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.internalField0633);
        this.internalField0416.stream().sorted(Comparator.comparingDouble(typedValue145 -> ((Float)hashMap.get(typedValue145)).floatValue()).reversed()).forEach(typedValue145 -> arrayList.add(this.internalMethod01111((ModuleEntry)typedValue145)));
        this.internalField0634.internalMethod07849(arrayList);
    }

    private void internalMethod04560() {
        this.internalField0416.clear();
        for (ModuleEntry typedValue145 : RockstarClient.getInstance().getModuleManager().getModules()) {
            if (!this.internalMethod04867(typedValue145)) continue;
            this.internalField0416.add(typedValue145);
        }
    }

    private void internalMethod00123(ModuleEntry typedValue145, boolean bl) {
        boolean bl2;
        boolean bl3 = this.internalMethod04867(typedValue145);
        if (!(bl3 != (bl2 = this.internalField0416.contains(typedValue145)) || bl && bl3)) {
            return;
        }
        if (bl3 && !bl2) {
            this.internalField0416.add(typedValue145);
        } else if (!bl3) {
            this.internalField0416.remove(typedValue145);
        }
        if (this.internalField0634 != null) {
            this.internalMethod06183(bl);
        }
    }

    private boolean internalMethod04867(ModuleEntry typedValue145) {
        return !(typedValue145 instanceof MenuModule) && typedValue145.isEnabled() && typedValue145.getKeybind() != -1;
    }

    @Override
    public void update(UiRenderContext iII) {
        boolean bl = this.internalField0277 = this.x + this.width / 2.0f >= ScreenMetricsAccess.internalField0389.internalMethod03585() / 2.0f;
        if (this.internalField0634 != null) {
            this.internalField0634.internalMethod01855(this.internalMethod04984());
        }
        super.update(iII);
    }

    private TextAlignment internalMethod04984() {
        return this.internalField0381.internalMethod04496() ? TextAlignment.internalField1243 : (this.internalField0277 ? TextAlignment.internalField1242 : TextAlignment.internalField0622);
    }

    @Override
    public boolean show() {
        return this.internalField0650.internalMethod04496() || KeybindsHud.internalField0149.currentScreen instanceof ChatScreen || !this.internalField0416.isEmpty();
    }
}
