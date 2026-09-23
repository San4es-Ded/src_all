package rockstar.client.setting;







import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.internal.core.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.BooleanSupplier;
import lombok.Generated;
import org.jetbrains.annotations.NotNull;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.Insets;
import rockstar.client.ui.LayoutAlignment;
import rockstar.client.render.Fonts;
import rockstar.client.ui.TextAlignment;
import rockstar.client.animation.Motion;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.AbstractSetting;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.ui.UiElement;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.ui.ThemeColors;
import rockstar.client.core.CursorType;
import rockstar.client.ui.FlexDirection;
import rockstar.client.internal.core.SpriteAnimationPlayer;
import rockstar.client.ui.TextLabel;
import rockstar.client.ui.UiNode;
import rockstar.client.ui.UiContainer;

public class ModeSetting
extends AbstractSetting {
    final List<InternalType0088> internalField0416 = new ArrayList<InternalType0088>();
    private InternalType0088 internalField0237;

    public ModeSetting(@NotNull SettingOwner typedValue159, String string, String string2, @NotNull BooleanSupplier booleanSupplier) {
        super(typedValue159, string, booleanSupplier);
    }

    public ModeSetting(@NotNull SettingOwner typedValue159, String string, @NotNull BooleanSupplier booleanSupplier) {
        super(typedValue159, string, booleanSupplier);
    }

    public ModeSetting(@NotNull SettingOwner typedValue159, String string, String string2) {
        super(typedValue159, string);
    }

    public ModeSetting(@NotNull SettingOwner typedValue159, String string) {
        super(typedValue159, string);
    }

    public void internalMethod06102(InternalType0088 nestedValue2011) {
        this.internalField0416.add(nestedValue2011);
        if (this.internalField0237 == null) {
            this.internalField0237 = nestedValue2011;
        }
    }

    public void internalMethod00219(String ... stringArray) {
        String string = this.internalField0237 == null ? null : this.internalField0237.getName();
        this.internalField0416.clear();
        this.internalField0237 = null;
        if (stringArray == null) {
            return;
        }
        for (String string2 : stringArray) {
            new InternalType0088(this, string2);
        }
        if (string != null) {
            for (InternalType0088 nestedValue2011 : this.internalField0416) {
                if (!nestedValue2011.getName().equalsIgnoreCase(string)) continue;
                this.internalField0237 = nestedValue2011;
                return;
            }
        }
        if (!this.internalField0416.isEmpty()) {
            this.internalField0237 = this.internalField0416.getFirst();
        }
    }

    public boolean internalMethod00197(String string) {
        return this.internalField0237 != null && this.internalField0237.getName().equalsIgnoreCase(string);
    }

    public boolean internalMethod06103(InternalType0088 nestedValue2011) {
        return this.internalField0237 == nestedValue2011;
    }

    public void internalMethod03917(InternalType0088 nestedValue2011) {
        if (this.internalField0237 == nestedValue2011) {
            return;
        }
        this.notifyChanged();
        this.internalField0237 = nestedValue2011;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(this.internalField0237 == null ? "" : this.internalField0237.getName());
    }

    public InternalType0088 internalMethod07213() {
        List<InternalType0088> list = this.internalField0416.stream().filter(InternalType0088::isSelected).toList();
        if (!list.isEmpty()) {
            Random random = new Random();
            return list.get(random.nextInt(list.size()));
        }
        return null;
    }

    @Override
    public void fromJson(JsonElement jsonElement) {
        if (jsonElement == null || !jsonElement.isJsonPrimitive() || !jsonElement.getAsJsonPrimitive().isString()) {
            return;
        }
        String string = jsonElement.getAsString();
        for (InternalType0088 nestedValue2011 : this.internalField0416) {
            if (!nestedValue2011.getName().equalsIgnoreCase(string)) continue;
            this.internalField0237 = nestedValue2011;
            break;
        }
    }

    @Override
    public boolean isValidJson(JsonElement jsonElement) {
        if (jsonElement == null || !jsonElement.isJsonPrimitive() || !jsonElement.getAsJsonPrimitive().isString()) {
            return false;
        }
        String string = jsonElement.getAsString();
        return this.internalField0416.stream().anyMatch(nestedValue2011 -> nestedValue2011.getName().equalsIgnoreCase(string));
    }

    @Override
    public UiContainer createComponent() {
        UiContainer typedValue006 = new UiContainer().internalMethod03907(new TextLabel(Fonts.internalField1154.internalMethod01432(8.0f), () -> LanguageManager.internalMethod07214(this.internalField0248)).internalMethod02959(typedValue011 -> ThemeColors.internalField1613.mulAlpha(0.75f + 0.25f * typedValue011.hover())).internalMethod02902().fill()).internalMethod03062(6.0f).internalMethod01192(FlexDirection.internalField1246).internalMethod07607(LayoutAlignment.internalField1377).internalMethod01855(TextAlignment.internalField0621).internalMethod03514(Insets.internalMethod00105(6.0f, 0.0f, 0.0f, 0.0f)).internalMethod09609();
        UiContainer typedValue007 = new UiContainer(){
            private final IdentityHashMap<InternalType0088, UiNode> internalField0594 = new IdentityHashMap();
            private List<InternalType0088> internalField0416;
            {
                for (InternalType0088 nestedValue2011 : ModeSetting.this.internalField0416) {
                    UiElement typedParameter1002 = nestedValue2011.buildComponent();
                    this.internalField0594.put(nestedValue2011, typedParameter1002);
                    this.internalMethod03907(typedParameter1002);
                }
                this.internalField0416 = new ArrayList<InternalType0088>(ModeSetting.this.internalField0416);
            }

            @Override
            protected void onTick(float f, float f2, float f3) {
                if (!ModeSetting.internalMethod01641(this.internalField0416, ModeSetting.this.internalField0416)) {
                    ArrayList<UiNode> arrayList = new ArrayList<UiNode>(ModeSetting.this.internalField0416.size());
                    for (InternalType0088 nestedValue2012 : ModeSetting.this.internalField0416) {
                        arrayList.add(this.internalField0594.computeIfAbsent(nestedValue2012, InternalType0088::buildComponent));
                    }
                    this.internalField0594.keySet().removeIf(nestedValue2011 -> !ModeSetting.this.internalField0416.contains(nestedValue2011));
                    this.internalMethod07849(arrayList);
                    this.internalField0416 = new ArrayList<InternalType0088>(ModeSetting.this.internalField0416);
                }
                super.onTick(f, f2, f3);
            }
        }.internalMethod01192(FlexDirection.internalField1246).internalMethod03062(2.0f).internalMethod09609().internalMethod03514(Insets.internalMethod00105(0.0f, 0.0f, 5.0f, 0.0f)).internalMethod07971();
        return new UiContainer().internalMethod01192(FlexDirection.internalField0629).internalMethod03062(5.0f).internalMethod03907(typedValue006).internalMethod03907(typedValue007);
    }

    static boolean internalMethod01641(List<?> list, List<?> list2) {
        if (list.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) == list2.get(i)) continue;
            return false;
        }
        return true;
    }

    @Generated
    public List<InternalType0088> internalMethod06723() {
        return this.internalField0416;
    }

    @Generated
    public InternalType0088 internalMethod07418() {
        return this.internalField0237;
    }

    public static class InternalType0088 {
        private final ModeSetting parent;
        private final String name;
        private final String description;
        private final AnimatedValue hoverAnimation = new AnimatedValue(300L, Easing.internalField1626);
        private final AnimatedValue activeAnimation = new AnimatedValue(300L, Easing.internalField1626);
        private final BooleanSupplier hideCondition;
        private SpriteAnimationPlayer enableAnimation;
        private SpriteAnimationPlayer disableAnimation;
        private SpriteAnimationPlayer currentAnimation;
        private boolean lastState;

        public InternalType0088(ModeSetting typedValue170, String string) {
            this(typedValue170, string, "", () -> false);
        }

        public InternalType0088(ModeSetting typedValue170, String string, String string2) {
            this(typedValue170, string, string2, () -> false);
        }

        public InternalType0088(ModeSetting typedValue170, String string, String string2, BooleanSupplier booleanSupplier) {
            this.parent = typedValue170;
            this.name = string;
            this.description = string2;
            this.hideCondition = booleanSupplier;
            typedValue170.internalMethod06102(this);
        }

        public boolean isHidden() {
            return this.hideCondition != null && this.hideCondition.getAsBoolean();
        }

        public InternalType0088 select() {
            this.parent.internalMethod03917(this);
            return this;
        }

        public boolean isSelected() {
            return this.parent.internalMethod07418() == this;
        }

        public UiElement buildComponent() {
            return new UiElement().bind("selected", this::isSelected, Motion.internalMethod01328(220L, Easing.internalField1626)).text(Fonts.internalField0449.internalMethod01432(7.0f), () -> LanguageManager.internalMethod07214(this.name), typedParameter1002 -> ThemeColors.internalMethod01303(InternalType0088.background(typedParameter1002)).mulAlpha(0.75f + 0.25f * typedParameter1002.sig("selected"))).textAlign(TextAlignment.internalField0621).background(InternalType0088::background).radius(2.5f).padding(Insets.internalMethod00172(3.0f)).cursor(CursorType.internalField0567).onClick(this::select);
        }

        private static ColorRGBA background(UiElement typedParameter1002) {
            return ThemeColors.internalField1614.mix(ThemeColors.internalField1310, 0.2f * typedParameter1002.hover()).mix(ThemeColors.internalField1310.mix(ThemeColors.internalField1614, 0.2f * typedParameter1002.hover()), typedParameter1002.sig("selected"));
        }

        public String toString() {
            return this.name;
        }

        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (object == null || object.getClass() != this.getClass()) {
                return false;
            }
            InternalType0088 nestedValue2011 = (InternalType0088)object;
            return Objects.equals(this.parent, nestedValue2011.parent) && Objects.equals(this.name, nestedValue2011.name) && Objects.equals(this.description, nestedValue2011.description);
        }

        public int hashCode() {
            return Objects.hash(this.parent, this.name, this.description);
        }

        @Generated
        public void setEnableAnimation(SpriteAnimationPlayer typedValue260) {
            this.enableAnimation = typedValue260;
        }

        @Generated
        public void setDisableAnimation(SpriteAnimationPlayer typedValue260) {
            this.disableAnimation = typedValue260;
        }

        @Generated
        public void setCurrentAnimation(SpriteAnimationPlayer typedValue260) {
            this.currentAnimation = typedValue260;
        }

        @Generated
        public void setLastState(boolean bl) {
            this.lastState = bl;
        }

        @Generated
        public ModeSetting getParent() {
            return this.parent;
        }

        @Generated
        public String getName() {
            return this.name;
        }

        @Generated
        public String getDescription() {
            return this.description;
        }

        @Generated
        public AnimatedValue getHoverAnimation() {
            return this.hoverAnimation;
        }

        @Generated
        public AnimatedValue getActiveAnimation() {
            return this.activeAnimation;
        }

        @Generated
        public BooleanSupplier getHideCondition() {
            return this.hideCondition;
        }

        @Generated
        public SpriteAnimationPlayer getEnableAnimation() {
            return this.enableAnimation;
        }

        @Generated
        public SpriteAnimationPlayer getDisableAnimation() {
            return this.disableAnimation;
        }

        @Generated
        public SpriteAnimationPlayer getCurrentAnimation() {
            return this.currentAnimation;
        }

        @Generated
        public boolean isLastState() {
            return this.lastState;
        }
    }
}
