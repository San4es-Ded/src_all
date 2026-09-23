package rockstar.client.internal.script;












import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.i18n.*;
import rockstar.client.animation.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.framework.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec2f;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.modules.player.AutoFarmModule;
import rockstar.client.internal.inventory.AutoFarmModeState;
import rockstar.client.internal.script.FarmStatsTracker;
import rockstar.client.internal.core.FarmStatsMetric;
import rockstar.client.internal.core.AutoFarmUnit;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.animation.Motion;
import rockstar.client.render.CornerRadii;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.animation.AnimatedFloat;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.internal.script.DynamicIslandHud;
import rockstar.client.internal.framework.IslandGroupStatus;
import rockstar.client.module.ModuleManager;
import rockstar.client.RockstarClient;
import rockstar.client.animation.Easing;
import rockstar.client.ui.ThemeColors;
import rockstar.client.util.GameUtils;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.util.MathUtils;
import rockstar.client.ui.UiNode;
import rockstar.client.ui.UiContainer;

public class AutoFarmStatusWidget
extends IslandGroupStatus
implements MinecraftClientAccess {
    private static final float internalField0205 = 138.0f;
    private static final String internalField0248 = "  ";
    private static final int internalField0227 = 16;
    private UiContainer internalField0634;

    public AutoFarmStatusWidget(MultiSelectSetting typedValue173) {
        super(typedValue173, "auto_farm");
    }

    public UiContainer internalMethod00198(DynamicIslandHud typedValue201) {
        if (this.internalField0634 == null) {
            this.internalField0634 = new InternalType0095(typedValue201);
        }
        return this.internalField0634;
    }

    @Override
    public boolean canShow() {
        AutoFarmModule typedValue221 = this.internalMethod02333();
        return typedValue221 != null && typedValue221.isEnabled() && GameUtils.internalMethod00471();
    }

    public void internalMethod00943(UiRenderContext iII, ItemStack itemStack, float f, float f2, float f3, float f4) {
        if (itemStack == null || itemStack.isEmpty() || f4 <= 0.02f) {
            return;
        }
        float[] fArray = (float[])RenderSystem.getShaderColor().clone();
        RenderSystem.setShaderColor((float)fArray[0], (float)fArray[1], (float)fArray[2], (float)(fArray[3] * f4));
        iII.drawItem(itemStack, f, f2, f3 / 16.0f);
        RenderSystem.setShaderColor((float)fArray[0], (float)fArray[1], (float)fArray[2], (float)fArray[3]);
    }

    public void internalMethod00238(UiRenderContext iII, SizedFont typedValue020, String string, float f, float f2, float f3, ColorRGBA colorRGBA) {
        if (string == null || string.isBlank() || f3 <= 1.0f) {
            return;
        }
        if (typedValue020.internalMethod00965(string) <= f3 + 0.5f) {
            iII.drawText(typedValue020, string, f, f2, colorRGBA);
            return;
        }
        iII.drawFadeText(typedValue020, string, f, f2, colorRGBA, 0.0f, Math.min(7.0f, f3), f3);
    }

    public static float internalMethod03638(SizedFont typedValue020, String string) {
        if (string == null || string.isEmpty()) {
            return 0.0f;
        }
        float f = 0.0f;
        for (int i = 0; i < string.length(); ++i) {
            f += AutoFarmStatusWidget.internalMethod06992(typedValue020, string.charAt(i));
        }
        return f;
    }

    public static float internalMethod06992(SizedFont typedValue020, char c) {
        return c >= '0' && c <= '9' ? AutoFarmStatusWidget.internalMethod05113(typedValue020) : typedValue020.internalMethod00667(c);
    }

    private static float internalMethod05113(SizedFont typedValue020) {
        float f = 0.0f;
        for (char c = '0'; c <= '9'; c = (char)(c + '\u0001')) {
            f = Math.max(f, typedValue020.internalMethod00667(c));
        }
        return f;
    }

    public float internalMethod04995(float f) {
        double d = internalField0149.getWindow().getScaleFactor();
        return d <= 0.0 ? f : (float)((double)Math.round((double)f * d) / d);
    }

    public String internalMethod05612() {
        AutoFarmModeState typedValue313 = this.internalMethod07278();
        if (typedValue313 == null) {
            return "";
        }
        String string = typedValue313.internalMethod02315().internalMethod00107();
        String string2 = typedValue313.internalMethod06901();
        return string2 == null || string2.isBlank() ? string : string + " " + string2;
    }

    public ItemStack internalMethod07200() {
        AutoFarmModeState typedValue313 = this.internalMethod07278();
        ItemStack itemStack = typedValue313 == null ? ItemStack.EMPTY : typedValue313.internalMethod04126();
        return itemStack == null || itemStack.isEmpty() ? new ItemStack((ItemConvertible)Items.WHEAT) : itemStack;
    }

    public AutoFarmUnit internalMethod00435() {
        AutoFarmModeState typedValue313 = this.internalMethod07278();
        return typedValue313 == null ? AutoFarmUnit.internalField0853 : typedValue313.internalMethod02317();
    }

    public FarmStatsMetric internalMethod00432() {
        AutoFarmModeState typedValue313 = this.internalMethod07278();
        return typedValue313 == null ? FarmStatsMetric.internalField0850 : typedValue313.internalMethod00321();
    }

    public FarmStatsTracker internalMethod00431() {
        AutoFarmModule typedValue221 = this.internalMethod02333();
        return typedValue221 == null ? null : typedValue221.internalMethod03987();
    }

    public AutoFarmModeState internalMethod07278() {
        AutoFarmModule typedValue221 = this.internalMethod02333();
        return typedValue221 == null ? null : typedValue221.internalMethod02711();
    }

    private AutoFarmModule internalMethod02333() {
        ModuleManager typedValue148 = RockstarClient.getInstance().getModuleManager();
        return typedValue148 == null ? null : typedValue148.getModule(AutoFarmModule.class);
    }

    final class InternalType0095
    extends UiContainer {
        private final DynamicIslandHud internalField0209;
        private final InternalType0096 internalField0309 = new InternalType0096();

        InternalType0095(DynamicIslandHud typedValue201) {
            this.internalField0209 = typedValue201;
            this.internalMethod03995(48.0f, 15.0f);
            this.internalMethod07853(false);
            this.internalMethod09801();
            this.snapSize();
        }

        @Override
        protected void measure() {
            this.prefW = this.internalField0209.internalMethod08516() ? 138.0f : this.internalMethod09246();
            this.prefH = this.internalField0209.internalMethod08516() ? 78.0f : 15.0f;
        }

        @Override
        protected void onTick(float f, float f2, float f3) {
            super.onTick(f, f2, f3);
            this.internalField0309.internalMethod03975(f);
        }

        @Override
        protected void drawSelf(UiRenderContext iII, float f) {
            FarmStatsTracker typedValue314 = AutoFarmStatusWidget.this.internalMethod00431();
            if (typedValue314 == null) {
                return;
            }
            float f2 = this.internalField0209.internalMethod05767().internalMethod02881();
            float f3 = this.x();
            float f4 = this.y();
            float f5 = this.w();
            this.internalField0309.internalMethod07455(FarmStatsTracker.internalMethod01822(typedValue314.internalMethod01588()));
            this.internalMethod06471(iII, f3, f4, f2, f);
            if (f2 < 0.999f) {
                this.internalMethod02383(iII, typedValue314, f3, f4, f5, f2, f);
            }
            if (f2 > 0.001f) {
                this.internalMethod05493(iII, typedValue314, f3, f4, f5, f2, f);
            }
        }

        @Override
        protected void drawChildren(UiRenderContext iII, float f) {
        }

        private void internalMethod06471(UiRenderContext iII, float f, float f2, float f3, float f4) {
            float f5 = MathUtils.internalMethod02587(8.0, 10.0, f3);
            float f6 = f + MathUtils.internalMethod02587(7.0, 10.0, f3);
            float f7 = f2 + MathUtils.internalMethod02587(this.internalMethod07100(8.0f), 9.0, f3);
            if (this.internalMethod07101(f3)) {
                f6 = AutoFarmStatusWidget.this.internalMethod04995(f6);
                f7 = AutoFarmStatusWidget.this.internalMethod04995(f7);
            }
            AutoFarmStatusWidget.this.internalMethod00943(iII, AutoFarmStatusWidget.this.internalMethod07200(), f6, f7, f5, f4);
        }

        private boolean internalMethod07101(float f) {
            return f <= 0.002f && Math.abs(this.w() - this.prefW) <= 0.05f;
        }

        private float internalMethod07100(float f) {
            return (15.0f - f) / 2.0f;
        }

        private float internalMethod01861(SizedFont typedValue020) {
            return 9.0f + (10.0f - typedValue020.internalMethod04890()) / 2.0f;
        }

        private void internalMethod02383(UiRenderContext iII, FarmStatsTracker typedValue314, float f, float f2, float f3, float f4, float f5) {
            float f6 = f5 * (1.0f - f4);
            if (f6 <= 0.004f) {
                return;
            }
            SizedFont typedValue020 = Fonts.internalField0449.internalMethod01432(7.0f);
            ColorRGBA colorRGBA = ThemeColors.internalMethod08459().withAlpha(255.0f * f6);
            float f7 = f + 7.0f + 8.0f + 4.0f;
            float f8 = f2 + this.internalMethod07100(typedValue020.internalMethod04890());
            String string = this.internalMethod05059();
            float f9 = AutoFarmStatusWidget.internalMethod03638(typedValue020, string);
            float f10 = AutoFarmStatusWidget.internalMethod03638(typedValue020, this.internalField0309.internalMethod07205());
            float f11 = f + f3 - 7.0f - f10;
            AutoFarmStatusWidget.this.internalMethod00238(iII, typedValue020, string, f7, f8, f11 - f7, colorRGBA);
            this.internalField0309.internalMethod06805(iII, typedValue020, Math.min(f7 + f9, f11), f8, colorRGBA);
        }

        private String internalMethod05059() {
            String string = AutoFarmStatusWidget.this.internalMethod05612();
            return string.isBlank() ? "" : string + AutoFarmStatusWidget.internalField0248;
        }

        private void internalMethod05493(UiRenderContext iII, FarmStatsTracker typedValue314, float f, float f2, float f3, float f4, float f5) {
            float f6 = f5 * f4;
            if (f6 <= 0.004f) {
                return;
            }
            ColorRGBA colorRGBA = ThemeColors.internalMethod08459();
            SizedFont typedValue020 = Fonts.internalField0449.internalMethod01432(7.0f);
            this.internalMethod04285(iII, typedValue314, f, f2, f3, f6);
            String string = LanguageManager.internalMethod07214("hud.dynamic_island.statuses.auto_farm");
            float f7 = AutoFarmStatusWidget.internalMethod03638(typedValue020, this.internalField0309.internalMethod07205());
            float f8 = f + 10.0f + 10.0f + 4.0f;
            this.internalField0309.internalMethod06805(iII, typedValue020, f + f3 - 10.0f - f7, f2 + this.internalMethod01861(typedValue020), colorRGBA.withAlpha(255.0f * f6));
            AutoFarmStatusWidget.this.internalMethod00238(iII, typedValue020, string, f8, f2 + this.internalMethod01861(typedValue020), f + f3 - 10.0f - f7 - 6.0f - f8, colorRGBA.withAlpha(255.0f * f6));
            this.internalMethod02077(iII, f, f2, f3, f6);
            this.internalMethod02989(iII, typedValue314, f, f2, f3, f6);
        }

        private void internalMethod02077(UiRenderContext iII, float f, float f2, float f3, float f4) {
            float f5;
            AutoFarmModeState typedValue313 = AutoFarmStatusWidget.this.internalMethod07278();
            if (typedValue313 == null) {
                return;
            }
            SizedFont typedValue020 = Fonts.internalField1154.internalMethod01432(6.0f);
            ColorRGBA colorRGBA = ThemeColors.internalMethod02531();
            float f6 = f2 + 24.0f;
            float f7 = f + f3 - 10.0f;
            String string = AutoFarmStatusWidget.this.internalMethod05612();
            float f8 = f5 = string.isBlank() ? 0.0f : typedValue020.internalMethod00965(string);
            if (f5 > 0.0f) {
                iII.drawText(typedValue020, string, f7 - f5, f6, colorRGBA.withAlpha(153.0f * f4));
            }
            float f9 = f + 10.0f;
            float f10 = f7 - f5 - (f5 > 0.0f ? 6.0f : 0.0f) - f9;
            AutoFarmStatusWidget.this.internalMethod00238(iII, typedValue020, LanguageManager.internalMethod07214(typedValue313.getName()), f9, f6, f10, colorRGBA.withAlpha(255.0f * f4));
        }

        private void internalMethod02989(UiRenderContext iII, FarmStatsTracker typedValue314, float f, float f2, float f3, float f4) {
            AutoFarmUnit typedValue316 = AutoFarmStatusWidget.this.internalMethod00435();
            float f5 = f + 10.0f;
            float f6 = f + f3 / 2.0f + 2.0f;
            this.internalMethod03915(iII, f5, f2, typedValue316.internalMethod02188(), FarmStatsTracker.internalMethod03854(typedValue314.internalMethod08500()), ThemeColors.internalMethod08459(), f4);
            long l = typedValue314.internalMethod08508();
            this.internalMethod03915(iII, f6, f2, LanguageManager.internalMethod07214("modules.auto_farm.stats.income"), FarmStatsTracker.internalMethod03244(l), l < 0L ? ThemeColors.internalField0777 : ThemeColors.internalMethod08459(), f4);
        }

        private void internalMethod03915(UiRenderContext iII, float f, float f2, String string, String string2, ColorRGBA colorRGBA, float f3) {
            ColorRGBA colorRGBA2 = ThemeColors.internalMethod08459().withAlpha(158.1f * f3);
            iII.drawText(Fonts.internalField0449.internalMethod01432(6.0f), string, f, f2 + 38.0f, colorRGBA2);
            iII.drawText(Fonts.internalField0449.internalMethod01432(9.0f), string2, f, f2 + 46.0f, colorRGBA.withAlpha(255.0f * f3));
        }

        private void internalMethod04285(UiRenderContext iII, FarmStatsTracker typedValue314, float f, float f2, float f3, float f4) {
            float f5 = f + 6.0f;
            float f6 = Math.max(1.0f, f3 - 12.0f);
            float f7 = f2 + 71.0f;
            float f8 = 3.0f;
            iII.drawRoundedRect(f5 + f8, f7 - 0.25f, Math.max(1.0f, f6 - 6.0f), 0.5f, CornerRadii.internalMethod03908(0.25f), ThemeColors.internalMethod08459().withAlpha(30.599998f * f4));
            float[] fArray = this.internalMethod02880(typedValue314);
            if (fArray.length < 2) {
                return;
            }
            float f9 = 0.0f;
            for (float f10 : fArray) {
                f9 = Math.max(f9, f10);
            }
            if (f9 <= 0.0f) {
                f9 = 1.0f;
            }
            float f11 = 17.0f;
            float f12 = (f6 - 6.0f) / (float)(fArray.length - 1);
            Vec2f[] vec2fArray = new Vec2f[fArray.length];
            for (int i = 0; i < fArray.length; ++i) {
                vec2fArray[i] = new Vec2f(f5 + f8 + f12 * (float)i, f7 - f11 * (fArray[i] / f9));
            }
            ColorRGBA colorRGBA = ThemeColors.internalMethod02531();
            this.internalMethod04887(iII, vec2fArray, f7, colorRGBA, f4);
            this.internalMethod05857(iII, vec2fArray, f5, f2 + 54.0f - 2.0f, f6, f11 + 4.0f, colorRGBA.withAlpha(191.25f * f4));
            Vec2f vec2f = vec2fArray[vec2fArray.length - 1];
            iII.drawRoundedRect(vec2f.x - 1.5f, vec2f.y - 1.5f, 3.0f, 3.0f, CornerRadii.internalMethod03908(1.5f), colorRGBA.withAlpha(255.0f * f4));
        }

        private void internalMethod04887(UiRenderContext iII, Vec2f[] vec2fArray, float f, ColorRGBA colorRGBA, float f2) {
            int n = (vec2fArray.length - 1) * 6 + 1;
            float[] fArray = new float[n];
            float[] fArray2 = new float[n];
            int n2 = 0;
            for (int i = 0; i < vec2fArray.length - 1; ++i) {
                Vec2f[] vec2fArray2 = this.internalMethod04201(vec2fArray, i);
                for (int j = 0; j < 6; ++j) {
                    float f3 = (float)j / 6.0f;
                    fArray[n2] = (float)MathUtils.internalMethod03549(f3, vec2fArray[i].x, vec2fArray2[0].x, vec2fArray2[1].x, vec2fArray[i + 1].x);
                    fArray2[n2] = (float)MathUtils.internalMethod03549(f3, vec2fArray[i].y, vec2fArray2[0].y, vec2fArray2[1].y, vec2fArray[i + 1].y);
                    ++n2;
                }
            }
            fArray[n2] = vec2fArray[vec2fArray.length - 1].x;
            fArray2[n2] = vec2fArray[vec2fArray.length - 1].y;
            iII.drawAreaGradient(fArray, fArray2, f, colorRGBA.withAlpha(63.75f * f2), colorRGBA.withAlpha(0.0f));
        }

        private Vec2f[] internalMethod04201(Vec2f[] vec2fArray, int n) {
            Vec2f vec2f = vec2fArray[Math.max(0, n - 1)];
            Vec2f vec2f2 = vec2fArray[n];
            Vec2f vec2f3 = vec2fArray[n + 1];
            Vec2f vec2f4 = vec2fArray[Math.min(vec2fArray.length - 1, n + 2)];
            return new Vec2f[]{new Vec2f(vec2f2.x + (vec2f3.x - vec2f.x) / 6.0f, vec2f2.y + (vec2f3.y - vec2f.y) / 6.0f), new Vec2f(vec2f3.x - (vec2f4.x - vec2f2.x) / 6.0f, vec2f3.y - (vec2f4.y - vec2f2.y) / 6.0f)};
        }

        private void internalMethod05857(UiRenderContext iII, Vec2f[] vec2fArray, float f, float f2, float f3, float f4, ColorRGBA colorRGBA) {
            for (int i = 0; i < vec2fArray.length - 1; ++i) {
                Vec2f vec2f = vec2fArray[i];
                Vec2f vec2f2 = vec2fArray[i + 1];
                Vec2f[] vec2fArray2 = this.internalMethod04201(vec2fArray, i);
                Vec2f vec2f3 = vec2fArray2[0];
                Vec2f vec2f4 = vec2fArray2[1];
                float f5 = Math.max(f, Math.min(Math.min(vec2f.x, vec2f2.x), Math.min(vec2f3.x, vec2f4.x)) - 2.0f);
                float f6 = Math.min(f + f3, Math.max(Math.max(vec2f.x, vec2f2.x), Math.max(vec2f3.x, vec2f4.x)) + 2.0f);
                float f7 = Math.max(f2, Math.min(Math.min(vec2f.y, vec2f2.y), Math.min(vec2f3.y, vec2f4.y)) - 2.0f);
                float f8 = Math.min(f2 + f4, Math.max(Math.max(vec2f.y, vec2f2.y), Math.max(vec2f3.y, vec2f4.y)) + 2.0f);
                iII.drawSmoothBezier(f5, f7, f6 - f5, f8 - f7, vec2f, vec2f3, vec2f4, vec2f2, 1.0f, colorRGBA);
            }
        }

        private float[] internalMethod02880(FarmStatsTracker typedValue314) {
            int n;
            int n2;
            int n3;
            long[] lArray = typedValue314.internalMethod02394(AutoFarmStatusWidget.this.internalMethod00432());
            if (lArray.length < 2) {
                return new float[0];
            }
            int n4 = Math.min(16, lArray.length);
            float[] fArray = new float[n4];
            for (int i = 0; i < n4; ++i) {
                n3 = (int)((long)(i + 1) * (long)lArray.length / (long)n4);
                n2 = (int)((long)i * (long)lArray.length / (long)n4);
                if (n3 <= n2) {
                    n3 = n2 + 1;
                }
                long l = 0L;
                for (n = n2; n < n3 && n < lArray.length; ++n) {
                    l += lArray[n];
                }
                fArray[i] = (float)l / (float)(n3 - n2);
            }
            float[] fArray2 = new float[n4];
            n2 = Math.max(0, 1);
            for (n3 = 0; n3 < n4; ++n3) {
                float f = 0.0f;
                int n5 = 0;
                for (n = n3 - n2; n <= n3 + n2; ++n) {
                    if (n < 0 || n >= n4) continue;
                    f += fArray[n];
                    ++n5;
                }
                fArray2[n3] = n5 == 0 ? fArray[n3] : f / (float)n5;
            }
            return fArray2;
        }

        private float internalMethod09246() {
            FarmStatsTracker typedValue314 = AutoFarmStatusWidget.this.internalMethod00431();
            if (typedValue314 == null) {
                return 22.0f;
            }
            SizedFont typedValue020 = Fonts.internalField0449.internalMethod01432(7.0f);
            float f = 19.0f + AutoFarmStatusWidget.internalMethod03638(typedValue020, this.internalMethod05059()) + AutoFarmStatusWidget.internalMethod03638(typedValue020, FarmStatsTracker.internalMethod01822(typedValue314.internalMethod01588())) + 7.0f;
            return Math.min(f, 128.0f);
        }
    }

    static final class InternalType0096 {
        private static final float internalField0205 = 4.0f;
        private static final int internalField0227 = 400;
        private final List<InternalType0526> internalField0416 = new ArrayList<InternalType0526>();
        private String internalField0248 = "";

        InternalType0096() {
        }

        String internalMethod07205() {
            return this.internalField0248;
        }

        void internalMethod07455(String string) {
            if (string == null) {
                string = "";
            }
            if (string.equals(this.internalField0248)) {
                return;
            }
            boolean bl = string.length() != this.internalField0248.length();
            this.internalField0248 = string;
            while (this.internalField0416.size() < string.length()) {
                this.internalField0416.add(new InternalType0526());
            }
            while (this.internalField0416.size() > string.length()) {
                this.internalField0416.remove(this.internalField0416.size() - 1);
            }
            for (int i = 0; i < string.length(); ++i) {
                InternalType0526 nestedValue2069 = this.internalField0416.get(i);
                char c = string.charAt(i);
                if (c == nestedValue2069.internalField0180) continue;
                nestedValue2069.internalField0181 = bl ? (char)'\u0000' : nestedValue2069.internalField0180;
                nestedValue2069.internalField0180 = c;
                nestedValue2069.internalField0623.internalMethod03759(bl ? 1.0f : 0.0f);
                if (bl) continue;
                nestedValue2069.internalField0623.internalMethod03690(1.0f);
            }
        }

        void internalMethod03975(float f) {
            for (InternalType0526 nestedValue2069 : this.internalField0416) {
                nestedValue2069.internalField0623.internalMethod08946(f);
            }
        }

        void internalMethod06805(UiRenderContext iII, SizedFont typedValue020, float f, float f2, ColorRGBA colorRGBA) {
            float f3 = f;
            for (InternalType0526 nestedValue2069 : this.internalField0416) {
                float f4 = nestedValue2069.internalField0623.internalMethod02046();
                this.internalMethod04210(iII, typedValue020, nestedValue2069.internalField0181, f3, f2, colorRGBA, 1.0f - f4, 4.0f * f4);
                this.internalMethod04210(iII, typedValue020, nestedValue2069.internalField0180, f3, f2, colorRGBA, f4, 4.0f * (f4 - 1.0f));
                f3 += AutoFarmStatusWidget.internalMethod06992(typedValue020, nestedValue2069.internalField0180);
            }
        }

        private void internalMethod04210(UiRenderContext iII, SizedFont typedValue020, char c, float f, float f2, ColorRGBA colorRGBA, float f3, float f4) {
            if (c == '\u0000' || f3 <= 0.004f) {
                return;
            }
            iII.drawText(typedValue020, String.valueOf(c), f, f2 + f4, colorRGBA.withAlpha(colorRGBA.getAlpha() * f3));
        }

        static final class InternalType0526 {
            char internalField0181;
            char internalField0180;
            final AnimatedFloat internalField0623 = new AnimatedFloat(1.0f, Motion.internalMethod01328(400L, Easing.internalField0812));

            InternalType0526() {
            }
        }
    }
}
