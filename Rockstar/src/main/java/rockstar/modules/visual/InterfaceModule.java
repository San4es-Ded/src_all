package rockstar.modules.visual;






import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.internal.script.HudManager;
import rockstar.client.internal.ui.AbstractHudWidget;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.ui.FlexDirection;
import rockstar.client.ui.ScreenMetricsAccess;
import rockstar.client.module.Module;
import rockstar.client.ui.UiContainer;

@ModuleInfo(name="Interface", category=ModuleCategory.VISUALS, internalMethod08049=true, internalMethod08050=true)
public class InterfaceModule
extends Module {
    private final Map<AbstractHudWidget, MultiSelectSetting.InternalType0091> internalField0543 = new IdentityHashMap<AbstractHudWidget, MultiSelectSetting.InternalType0091>();
    private MultiSelectSetting internalField0675;

    public InterfaceModule() {
        this.internalMethod09716();
    }

    private void internalMethod09716() {
        this.internalField0675 = new MultiSelectSetting(this, "modules.settings.interface.elements"){

            @Override
            public JsonElement toJson() {
                return new JsonObject();
            }

            @Override
            public void fromJson(JsonElement jsonElement) {
            }

            @Override
            public boolean isValidJson(JsonElement jsonElement) {
                return jsonElement != null && jsonElement.isJsonObject();
            }

            @Override
            public UiContainer createComponent() {
                InterfaceModule.this.internalMethod09718();
                UiContainer typedValue006 = super.createComponent().internalMethod09609();
                return new UiContainer(){

                    @Override
                    protected void onTick(float f, float f2, float f3) {
                        InterfaceModule.this.internalMethod09718();
                        super.onTick(f, f2, f3);
                    }
                }.internalMethod01192(FlexDirection.internalField0629).internalMethod03907(typedValue006);
            }
        };
        this.internalMethod09718();
    }

    void internalMethod09718() {
        List<MultiSelectSetting.InternalType0091> list;
        HudManager typedValue196 = RockstarClient.getInstance().internalMethod01271();
        if (typedValue196 == null || this.internalField0675 == null) {
            return;
        }
        List<AbstractHudWidget> list2 = typedValue196.internalMethod09520();
        List<MultiSelectSetting.InternalType0091> list3 = this.internalField0675.internalMethod01792();
        if (!this.internalMethod00069(list3, list2)) {
            list = new ArrayList<MultiSelectSetting.InternalType0091>(list2.size());
            for (AbstractHudWidget object : list2) {
                list.add(this.internalField0543.computeIfAbsent(object, typedValue197 -> new InternalType0307(this.internalField0675, (AbstractHudWidget)typedValue197)));
            }
            this.internalField0543.keySet().removeIf(typedValue197 -> !list2.contains(typedValue197));
            list3.clear();
            list3.addAll(list);
        }
        list = this.internalField0675.internalMethod07492();
        list.removeIf(nestedValue2013 -> !list3.contains(nestedValue2013));
        for (MultiSelectSetting.InternalType0091 nestedValue2014 : list3) {
            boolean bl = nestedValue2014.isSelected();
            if (bl == list.contains(nestedValue2014)) continue;
            if (bl) {
                list.add(nestedValue2014);
                continue;
            }
            list.remove(nestedValue2014);
        }
    }

    private boolean internalMethod00069(List<MultiSelectSetting.InternalType0091> list, List<AbstractHudWidget> list2) {
        if (list.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list2.size(); ++i) {
            if (this.internalField0543.get(list2.get(i)) == list.get(i)) continue;
            return false;
        }
        return true;
    }

    public static boolean internalMethod09717() {
        return false;
    }

    public static float internalMethod07584() {
        return 0.0f;
    }

    public static float internalMethod07585() {
        return 1.0f;
    }

    public static float internalMethod08761() {
        return 0.79f;
    }

    public static float internalMethod08764() {
        return 12.0f;
    }

    public static float internalMethod08774() {
        return 2.0f;
    }

    public static float internalMethod08775() {
        return 1.0f;
    }

    public static boolean internalMethod09719() {
        return false;
    }

    public static boolean internalMethod09917() {
        return true;
    }

    @Generated
    public Map<AbstractHudWidget, MultiSelectSetting.InternalType0091> internalMethod03499() {
        return this.internalField0543;
    }

    @Generated
    public MultiSelectSetting internalMethod00420() {
        return this.internalField0675;
    }

    static final class InternalType0307
    extends MultiSelectSetting.InternalType0091 {
        private final AbstractHudWidget internalField0947;

        InternalType0307(MultiSelectSetting typedValue173, AbstractHudWidget typedValue197) {
            super(typedValue173, typedValue197.getName());
            this.internalField0947 = typedValue197;
        }

        @Override
        public boolean isSelected() {
            return this.internalField0947.isShowing();
        }

        @Override
        public MultiSelectSetting.InternalType0091 toggle() {
            boolean bl;
            boolean bl2 = bl = !this.internalField0947.isShowing();
            if (bl && this.internalField0947.getX() == 0.0f && this.internalField0947.getY() == 0.0f) {
                this.internalField0947.pos(ScreenMetricsAccess.internalField0389.internalMethod03585() / 2.0f, ScreenMetricsAccess.internalField0389.internalMethod03589() / 2.0f);
            }
            this.internalField0947.setShowing(bl);
            if (bl) {
                this.select();
            } else {
                this.deselect();
            }
            RockstarClient.getInstance().internalMethod03371().internalMethod05165("client");
            return this;
        }
    }
}
