package haron.hud.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.hud.core.HudElement;
import haron.hud.elements.CustomPotionRowState;
import haron.hud.elements.StatusEffectSnapshot;
import haron.hud.elements.StatusEffectRowState;
import haron.hud.elements.CustomPotionSnapshot;
import haron.hud.elements.PlaceholderEffect;
import haron.module.ModuleManager;
import haron.modules.hud.Potions;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class PotionsHudElement
extends HudElement {
    private static final float A = 8.5f;
    private static final float B = 8.0f;
    private static final float C = 24.0f;
    private static final float D = 16.0f;
    private static final float E = 5.0f;
    private static final float F = 8.0f;
    private static final float G = 2.5f;
    private static final float H = 12.0f;
    private static final float I = 2.0f;
    private static final float J = 12.0f;
    private static final float K = 6.0f;
    private static final float L = 6.5f;
    private static final float M = 0.5f;
    private static final float N = 100.0f;
    private static final int O = 200;
    private static final double P = 0.2;
    private static final long Q = 1000L;
    private static final float R = 6.0f;
    private static final float S = 1.0f;
    private static final String HUD_TITLE = "Potions";
    private static final String HEADER_SYMBOL = "✦";
    private static final float HEADER_DROP = 6.0f;
    private static final float ROW_TEXT_DROP = 5.0f;
    private final Map<RegistryEntry<StatusEffect>, StatusEffectRowState> T = new LinkedHashMap<RegistryEntry<StatusEffect>, StatusEffectRowState>();
    private final List<PlaceholderEffect> U = new ArrayList<PlaceholderEffect>();
    private final AnimatedValue V = new AnimatedValue();
    private final AnimatedValue W = new AnimatedValue();
    private final AnimatedValue X = new AnimatedValue();
    private final Map<String, CustomPotionRowState> Y = new LinkedHashMap<String, CustomPotionRowState>();
    private final AnimatedValue Z = new AnimatedValue();
    private long aa = 0L;
    private int FriendCard = 0;
    private boolean CosmeticModelRenderer = false;
    private boolean CosmeticModelLoader = false;
    private boolean ModelPart = false;
    public static boolean n;
    private static final Color WHITE;
    private static final Color TEXT;
    private static final Color ORANGE_WARN;
    private static Color DIVIDER_CENTER;
    private static Color DIVIDER_EDGE;
    private static Color ROW_DIVIDER;
    private static Color ROW_DIVIDER_CENTER;

    private static int fontIndex(float f) {
        return Math.max(10, Math.min(48, Math.round(f)));
    }

    private static void updateAccentColors() {
        Color color = pryrvd.ACCENT;
        int n = color.getRed();
        int n2 = color.getGreen();
        int n3 = color.getBlue();
        DIVIDER_CENTER = new Color(n, n2, n3, 78);
        DIVIDER_EDGE = new Color(n, n2, n3, 33);
        ROW_DIVIDER = new Color(n, n2, n3, 33);
        ROW_DIVIDER_CENTER = new Color(n, n2, n3, 78);
    }

    private void drawGlowSegment(ShapeRenderer s7swsm2, MatrixStack matrixStack, float f, float f2, float f3, float f4, float f5) {
        float f6 = 2.0f * f4;
        float f7 = f6 / 2.0f;
        float f8 = f3 / 2.0f;
        float f9 = f + f8;
        float f10 = f6 + 2.0f * f4;
        Color color = this.a(DIVIDER_EDGE, f5);
        Color color2 = this.a(DIVIDER_CENTER, f5);
        s7swsm2.a(f - 1.0f * f4, f2 - 1.0f * f4, f8 + 1.0f * f4, f10, f10 / 2.0f, color, color2, color, color2, matrixStack);
        s7swsm2.a(f9, f2 - 1.0f * f4, f8 + 1.0f * f4, f10, f10 / 2.0f, color2, color, color2, color, matrixStack);
        Color color3 = this.a(DIVIDER_EDGE, f5);
        Color color4 = this.a(DIVIDER_CENTER, f5);
        s7swsm2.a(f, f2, f8, f6, f7, color3, color4, color3, color4, matrixStack);
        s7swsm2.a(f9, f2, f8, f6, f7, color4, color3, color4, color3, matrixStack);
    }

    private void drawDivider(ShapeRenderer s7swsm2, MatrixStack matrixStack, float f, float f2, float f3, float f4, float f5) {
        float f6 = 0.8f * f5;
        float f7 = f3 / 2.0f;
        float f8 = f + f7;
        float f9 = f6 / 2.0f;
        s7swsm2.a(f, f2, f7, f6, f9, this.a(ROW_DIVIDER, f4), this.a(ROW_DIVIDER_CENTER, f4), this.a(ROW_DIVIDER, f4), this.a(ROW_DIVIDER_CENTER, f4), matrixStack);
        s7swsm2.a(f8, f2, f7, f6, f9, this.a(ROW_DIVIDER_CENTER, f4), this.a(ROW_DIVIDER, f4), this.a(ROW_DIVIDER_CENTER, f4), this.a(ROW_DIVIDER, f4), matrixStack);
    }

    private String effectDisplayName(RegistryEntry<StatusEffect> registryEntry) {
        int n = 522;
        return Text.translatable((String)((StatusEffect)registryEntry.value()).getTranslationKey()).getString();
    }

    private String trimToWidth(FontRenderer v6hnga2, String string, float f) {
        if (string == null) {
            return "";
        }
        if (f <= 0.0f) {
            return "";
        }
        if (v6hnga2.a(string) <= f) {
            return string;
        }
        String string2 = "...";
        if (v6hnga2.a(string2) > f) {
            return "";
        }
        String string3 = string;
        while (!string3.isEmpty()) {
            if (!(v6hnga2.a(PotionsHudElement.$sf$3(string3, string2)) > f)) break;
            string3 = string3.substring(0, string3.length() - 1);
        }
        return string3.isEmpty() ? "" : PotionsHudElement.$sf$3(string3, string2);
    }

    private void drawEffectRow(MatrixStack matrixStack, ShapeRenderer s7swsm2, FontRenderer v6hnga2, FontRenderer v6hnga3, float f, float f2, RegistryEntry<StatusEffect> registryEntry, String string, String string2, int n, float f3, float f4) {
        float f5;
        if (f3 < 0.01f) {
            return;
        }
        float f6 = 8.0f * f4;
        float f7 = 2.5f * f4;
        float f8 = f2 + 12.0f * f4 / 2.0f;
        float f9 = 1.6f * f4;
        float f10 = 8.0f * f4;
        s7swsm2.a(f, f8 - f10 / 2.0f, f9, f10, f9 / 2.0f, this.a(pryrvd.ACCENT, f3), matrixStack);
        float f11 = f + f9 + 4.0f * f4;
        float f12 = f8 - f6 / 2.0f;
        Identifier identifier = this.b(registryEntry);
        if (identifier != null) {
            this.drawEffectIcon(s7swsm2, matrixStack, identifier, f11, f12, f6, this.a(Color.WHITE, f3));
        } else {
            s7swsm2.a(f11, f12, f6, f6, 2.0f * f4, this.a(pryrvd.ACCENT, f3), matrixStack);
        }
        float f13 = v6hnga3.a(string2);
        float f14 = this.b + this.d - 8.5f * f4 - f13;
        float f15 = f11 + f6 + f7;
        float f16 = f14 - f15 - 8.0f * f4;
        String string3 = this.trimToWidth(v6hnga2, string, f16);
        float f17 = 5.0f * f4;
        float f18 = f8 - v6hnga2.b(string3) / 2.0f + f17;
        v6hnga2.a(string3, f15, (double)f18, this.a(TEXT, f3), matrixStack);
        Color color = pryrvd.ACCENT_SOFT;
        if (n > 0 && n <= 200) {
            f5 = (float)(Math.sin((double)System.currentTimeMillis() / 150.0) * 0.5 + 0.5);
            color = this.a(pryrvd.ACCENT_SOFT, ORANGE_WARN, f5);
        }
        f5 = f8 - v6hnga3.b(string2) / 2.0f + f17;
        v6hnga3.a(string2, f14, (double)f5, this.a(color, f3), matrixStack);
    }

    private void drawPotionRow(MatrixStack matrixStack, ShapeRenderer s7swsm2, FontRenderer v6hnga2, FontRenderer v6hnga3, float f, float f2, CustomPotionRowState ax5lt52, float f3, float f4) {
        float f5;
        if (f3 < 0.01f) {
            return;
        }
        float f6 = 8.0f * f4;
        float f7 = 2.5f * f4;
        float f8 = f2 + 12.0f * f4 / 2.0f;
        float f9 = 1.6f * f4;
        float f10 = 8.0f * f4;
        s7swsm2.a(f, f8 - f10 / 2.0f, f9, f10, f9 / 2.0f, this.a(pryrvd.ACCENT, f3), matrixStack);
        float f11 = f + f9 + 4.0f * f4;
        float f12 = f8 - f6 / 2.0f;
        if (ax5lt52.f && ax5lt52.d != null) {
            Identifier identifier = this.b(ax5lt52.d);
            if (identifier != null) {
                this.drawEffectIcon(s7swsm2, matrixStack, identifier, f11, f12, f6, this.a(Color.WHITE, f3));
            } else {
                s7swsm2.a(f11, f12, f6, f6, 2.0f * f4, this.a(pryrvd.ACCENT, f3), matrixStack);
            }
        } else if (ax5lt52.e != null) {
            this.a(matrixStack, new ItemStack((ItemConvertible)ax5lt52.e), f11, f12, f6, f3);
        } else {
            s7swsm2.a(f11, f12, f6, f6, 2.0f * f4, this.a(pryrvd.ACCENT, f3), matrixStack);
        }
        float f13 = v6hnga3.a(ax5lt52.b);
        float f14 = this.b + this.d - 8.5f * f4 - f13;
        float f15 = f11 + f6 + f7;
        float f16 = f14 - f15 - 8.0f * f4;
        String string = this.trimToWidth(v6hnga2, ax5lt52.a, f16);
        float f17 = 5.0f * f4;
        float f18 = f8 - v6hnga2.b(string) / 2.0f + f17;
        v6hnga2.a(string, f15, (double)f18, this.a(TEXT, f3), matrixStack);
        Color color = pryrvd.ACCENT_SOFT;
        if (ax5lt52.c > 0 && ax5lt52.c <= 200) {
            f5 = (float)(Math.sin((double)System.currentTimeMillis() / 150.0) * 0.5 + 0.5);
            color = this.a(pryrvd.ACCENT_SOFT, ORANGE_WARN, f5);
        }
        f5 = f8 - v6hnga3.b(ax5lt52.b) / 2.0f + f17;
        v6hnga3.a(ax5lt52.b, f14, (double)f5, this.a(color, f3), matrixStack);
    }

    private void drawEffectIcon(ShapeRenderer s7swsm2, MatrixStack matrixStack, Identifier identifier, float f, float f2, float f3, Color color) {
        if (identifier == null || color.getAlpha() < 1) {
            return;
        }
        s7swsm2.a(identifier, f, f2, f3, f3, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, color, matrixStack);
    }

    private void drawSymbol(ShapeRenderer s7swsm2, MatrixStack matrixStack, FontRenderer v6hnga2, String string, float f, float f2, Color color, float f3) {
        Color color2 = this.a(color, f3);
        if (color2.getAlpha() < 1) {
            return;
        }
        v6hnga2.a(string, f, (double)f2, color2, matrixStack);
    }

    private String formatAmplifierSuffix(int n) {
        return n <= 0 ? "" : PotionsHudElement.$sf$1(Text.translatable((String)PotionsHudElement.$sf$0(n)).getString());
    }

    private void addPlaceholderDefinition(RegistryEntry<StatusEffect> registryEntry, String string) {
        this.U.add(new PlaceholderEffect(registryEntry, this.effectDisplayName(registryEntry), string));
    }

    private String formatEffectDuration(int n) {
        if (n <= 0) {
            return "--:--";
        }
        int n2 = Math.max(0, n / 20);
        int n3 = n2 / 60;
        return String.format("%d:%02d", n3, n2 % 60);
    }

    public PotionsHudElement(float f, float f2) {
        super(f, f2);
        this.X.d(0.0);
        this.v();
        this.a();
    }

    static {
        WHITE = new Color(255, 255, 255, 255);
        TEXT = new Color(243, 230, 215, 255);
        ORANGE_WARN = new Color(255, 100, 100, 255);
    }

    private Identifier b(RegistryEntry<StatusEffect> registryEntry) {
        Identifier identifier = Registries.STATUS_EFFECT.getId(registryEntry.value());
        if (identifier != null) {
            return Identifier.of((String)identifier.getNamespace(), (String)PotionsHudElement.$sf$2(identifier.getPath()));
        }
        return null;
    }

    private List<CustomPotionSnapshot> x() {
        return new ArrayList<CustomPotionSnapshot>();
    }

    private void a(MatrixStack matrixStack, ItemStack itemStack, float f, float f2, float f3, float f4) {
        if (this.a.player == null || itemStack.isEmpty() || f4 < 0.01f) {
            return;
        }
        DrawContext drawContext = new DrawContext(this.a, this.a.getBufferBuilders().getEntityVertexConsumers());
        MatrixStack matrixStack2 = drawContext.getMatrices();
        matrixStack2.push();
        matrixStack2.translate(f, f2, 100.0f);
        float f5 = f3 / 16.0f;
        matrixStack2.scale(f5, f5, 1.0f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)f4);
        drawContext.drawItem(itemStack, 0, 0);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.disableBlend();
        matrixStack2.pop();
    }

    private Color a(Color color, float f) {
        int n = (int)((float)color.getAlpha() * Math.max(0.0f, Math.min(1.0f, f)));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, n)));
    }

    private Color a(Color color, Color color2, float f) {
        f = Math.max(0.0f, Math.min(1.0f, f));
        int n = (int)((float)color.getRed() + (float)(color2.getRed() - color.getRed()) * f);
        int n2 = (int)((float)color.getGreen() + (float)(color2.getGreen() - color.getGreen()) * f);
        int n3 = (int)((float)color.getBlue() + (float)(color2.getBlue() - color.getBlue()) * f);
        int n4 = (int)((float)color.getAlpha() + (float)(color2.getAlpha() - color.getAlpha()) * f);
        return new Color(n, n2, n3, n4);
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2) {
        if (this.a.player == null) {
            return;
        }
        PotionsHudElement.updateAccentColors();
        this.a();
        float f3 = (float)this.X.j();
        if (f3 < 0.01f) {
            return;
        }
        boolean bl = this.w();
        float f4 = this.g();
        FontRenderer v6hnga2 = ClientFonts.b[PotionsHudElement.fontIndex(15.0f * f4)];
        FontRenderer v6hnga3 = ClientFonts.a[PotionsHudElement.fontIndex(16.0f * f4)];
        FontRenderer v6hnga4 = ClientFonts.a[PotionsHudElement.fontIndex(16.0f * f4)];
        FontRenderer v6hnga5 = ClientFonts.a[PotionsHudElement.fontIndex(14.0f * f4)];
        float f5 = 8.5f * f4;
        float f6 = 24.0f * f4;
        float f7 = 12.0f * f4;
        float f8 = 2.0f * f4;
        float f9 = 6.5f * f4;
        Color color = this.a(pryrvd.PANEL_BG_TOP, f3);
        Color color2 = this.a(pryrvd.PANEL_BG_BOT, f3);
        s7swsm2.a(this.b, this.c, this.d, this.e, f9, color, color, color2, color2, matrixStack);
        float f10 = this.b + f5 + 2.0f * f4;
        float f11 = v6hnga2.b("Potions");
        float f12 = this.c + f6 / 2.0f - f11 / 2.0f + 6.0f * f4;
        v6hnga2.a("Potions", f10, (double)f12, this.a(WHITE, f3), matrixStack);
        float f13 = v6hnga2.a("Potions");
        float f14 = f12 + f11 / 2.0f;
        float f15 = v6hnga3.a(HEADER_SYMBOL);
        float f16 = v6hnga3.b(HEADER_SYMBOL);
        float f17 = this.b + this.d - f5 - f15 - 1.0f * f4;
        float f18 = f14 - f16 / 2.0f;
        this.drawSymbol(s7swsm2, matrixStack, v6hnga3, HEADER_SYMBOL, f17, f18, pryrvd.ACCENT_SOFT, f3);
        float f19 = this.c + f6;
        this.drawGlowSegment(s7swsm2, matrixStack, f10, f19, f13, f4, f3);
        float f20 = f17 - 3.0f * f4;
        float f21 = f15 + 6.0f * f4;
        this.drawGlowSegment(s7swsm2, matrixStack, f20, f19, f21, f4, f3);
        float f22 = this.b + f5;
        float f23 = this.c + f6 + 6.0f * f4;
        if (bl && !this.U.isEmpty()) {
            PlaceholderEffect v46i032 = this.U.get(this.FriendCard % this.U.size());
            this.drawEffectRow(matrixStack, s7swsm2, v6hnga4, v6hnga5, f22, f23, v46i032.a, v46i032.b, v46i032.c, -1, f3, f4);
        } else {
            boolean bl2 = true;
            for (StatusEffectRowState e74vis2 : this.T.values()) {
                float f24 = (float)e74vis2.e.j() * f3;
                if (f24 < 0.01f) continue;
                if (!bl2) {
                    this.drawDivider(s7swsm2, matrixStack, f22, f23, this.d - f5 * 2.0f, f24, f4);
                    f23 += f8;
                }
                this.drawEffectRow(matrixStack, s7swsm2, v6hnga4, v6hnga5, f22, f23, e74vis2.a, e74vis2.b, e74vis2.c, e74vis2.d, f24, f4);
                f23 += f7;
                bl2 = false;
            }
            float f25 = (float)this.Z.j();
            if (f25 > 0.01f) {
                boolean bl3;
                boolean bl4 = bl3 = !bl2;
                if (bl3) {
                    this.drawDivider(s7swsm2, matrixStack, f22, f23 += 6.0f * f4, this.d - f5 * 2.0f, f25 * f3, f4);
                    f23 += 7.0f * f4;
                }
                boolean bl5 = true;
                for (CustomPotionRowState ax5lt52 : this.Y.values()) {
                    float f26 = (float)ax5lt52.h.j() * f25 * f3;
                    if (f26 < 0.01f) continue;
                    if (!bl5) {
                        this.drawDivider(s7swsm2, matrixStack, f22, f23, this.d - f5 * 2.0f, f26, f4);
                        f23 += f8;
                    }
                    this.drawPotionRow(matrixStack, s7swsm2, v6hnga4, v6hnga5, f22, f23, ax5lt52, f26, f4);
                    f23 += f7;
                    bl5 = false;
                }
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    @Override
    protected void a() {
        int n;
        boolean bl;
        int extraCount = 0;
        long l;
        this.u();
        boolean bl2 = this.w();
        List<StatusEffectSnapshot> list = this.y();
        float f = this.g();
        FontRenderer v6hnga2 = ClientFonts.b[PotionsHudElement.fontIndex(15.0f * f)];
        if (bl2 && !this.U.isEmpty() && (l = System.currentTimeMillis()) - this.aa >= 1000L) {
            this.FriendCard = (this.FriendCard + 1) % this.U.size();
            this.aa = l;
        }
        HashSet<RegistryEntry<StatusEffect>> hashSet2 = new HashSet<RegistryEntry<StatusEffect>>();
        for (StatusEffectSnapshot object22 : list) {
            hashSet2.add(object22.a);
        }
        for (StatusEffectRowState e74vis2 : this.T.values()) {
            if (hashSet2.contains(e74vis2.a) || e74vis2.g) continue;
            e74vis2.g = true;
            e74vis2.e.a(0.0, 0.2, Easings.h);
        }
        int n2 = 0;
        for (StatusEffectSnapshot bdbltv2 : list) {
            StatusEffectRowState e74vis3 = this.T.get(bdbltv2.a);
            if (e74vis3 == null) {
                StatusEffectRowState e74vis4 = new StatusEffectRowState(bdbltv2.a, bdbltv2.b, bdbltv2.c, bdbltv2.d);
                e74vis4.e.d(0.0);
                e74vis4.e.a(1.0, 0.2, Easings.h);
                e74vis4.f.d(n2);
                this.T.put(bdbltv2.a, e74vis4);
            } else {
                e74vis3.b = bdbltv2.b;
                e74vis3.c = bdbltv2.c;
                e74vis3.d = bdbltv2.d;
                e74vis3.g = false;
                if (e74vis3.e.i() < 1.0) {
                    e74vis3.e.a(1.0, 0.2, Easings.h);
                }
                if (Math.abs(e74vis3.f.i() - (double)n2) > 0.01) {
                    e74vis3.f.a((double)n2, 0.2, Easings.h);
                }
            }
            ++n2;
        }
        this.T.entrySet().removeIf(entry -> ((StatusEffectRowState)entry.getValue()).g && ((StatusEffectRowState)entry.getValue()).e.j() < 0.01);
        for (StatusEffectRowState e74vis5 : this.T.values()) {
            e74vis5.e.a();
            e74vis5.f.a();
        }
        List<CustomPotionSnapshot> list2 = (Boolean)ModuleManager.POTIONS_HUD.n().k() == false ? Collections.emptyList() : this.x();
        HashSet<String> hashSet = new HashSet<String>();
        for (CustomPotionSnapshot shbwav2 : list2) {
            hashSet.add(shbwav2.a);
        }
        for (CustomPotionRowState ax5lt52 : this.Y.values()) {
            if (hashSet.contains(ax5lt52.a) || ax5lt52.j) continue;
            ax5lt52.j = true;
            ax5lt52.h.a(0.0, 0.2, Easings.h);
        }
        int n3 = 0;
        for (CustomPotionSnapshot shbwav3 : list2) {
            CustomPotionRowState ax5lt53 = this.Y.get(shbwav3.a);
            if (ax5lt53 == null) {
                CustomPotionRowState ax5lt54 = new CustomPotionRowState(shbwav3.a, shbwav3.b, shbwav3.c, shbwav3.d, shbwav3.e, shbwav3.f, shbwav3.g);
                ax5lt54.h.d(0.0);
                ax5lt54.h.a(1.0, 0.2, Easings.h);
                ax5lt54.i.d(n3);
                this.Y.put(shbwav3.a, ax5lt54);
            } else {
                ax5lt53.b = shbwav3.b;
                ax5lt53.c = shbwav3.c;
                ax5lt53.d = shbwav3.d;
                ax5lt53.e = shbwav3.e;
                ax5lt53.f = shbwav3.f;
                ax5lt53.g = shbwav3.g;
                ax5lt53.j = false;
                if (ax5lt53.h.i() < 1.0) {
                    ax5lt53.h.a(1.0, 0.2, Easings.h);
                }
                if (Math.abs(ax5lt53.i.i() - (double)n3) > 0.01) {
                    ax5lt53.i.a((double)n3, 0.2, Easings.h);
                }
            }
            ++n3;
        }
        this.Y.entrySet().removeIf(entry -> ((CustomPotionRowState)entry.getValue()).j && ((CustomPotionRowState)entry.getValue()).h.j() < 0.01);
        for (CustomPotionRowState ax5lt55 : this.Y.values()) {
            ax5lt55.h.a();
            ax5lt55.i.a();
        }
        boolean bl3 = false;
        for (CustomPotionRowState ax5lt53 : this.Y.values()) {
            if (ax5lt53.j) continue;
            ++extraCount;
        }
        boolean bl4 = bl = extraCount > 0;
        if (bl && this.Z.i() < 1.0) {
            this.Z.a(1.0, 0.2, Easings.h);
        } else if (!bl && this.Z.i() > 0.0) {
            this.Z.a(0.0, 0.2, Easings.h);
        }
        this.Z.a();
        int n4 = 0;
        for (StatusEffectRowState e74vis6 : this.T.values()) {
            if (e74vis6.g) continue;
            ++n4;
        }
        boolean bl5 = bl2 || n4 > 0 || extraCount > 0;
        boolean bl6 = this.CosmeticModelLoader;
        this.CosmeticModelLoader = bl5;
        if (bl5 && !bl6) {
            this.X.a(1.0, 0.2, Easings.h);
        } else if (!bl5 && bl6) {
            this.X.a(0.0, 0.2, Easings.h);
        } else if (bl5 && this.X.i() < 1.0) {
            this.X.a(1.0, 0.2, Easings.h);
        }
        this.X.a();
        float f2 = 8.5f * f;
        float f3 = 24.0f * f;
        float f4 = 12.0f * f;
        float f5 = 2.0f * f;
        float f6 = 100.0f * f;
        float f7 = v6hnga2.a("Potions") + 42.0f * f;
        f6 = Math.max(f6, f7);
        int n5 = n = bl2 ? 1 : n4;
        if (bl2 && this.U.isEmpty()) {
            n = 0;
        }
        float f8 = 0.0f;
        if (n > 0) {
            f8 = 0.0f + (float)n * f4;
            f8 += (float)Math.max(0, n - 1) * f5;
        }
        if (extraCount > 0) {
            if (n > 0) {
                f8 += 13.0f * f;
            }
            f8 += (float)extraCount * f4;
            f8 += (float)Math.max(0, extraCount - 1) * f5;
        }
        float f9 = f6 + f2 * 2.0f;
        float f10 = f3 + 6.0f * f + f8 + 10.0f * f;
        if (this.CosmeticModelRenderer) {
            if (Math.abs(this.W.i() - (double)f9) > 0.5) {
                this.W.a((double)f9, 0.2, Easings.h);
            }
            if (Math.abs(this.V.i() - (double)f10) > 0.5) {
                this.V.a((double)f10, 0.2, Easings.h);
            }
        } else {
            this.W.d(f9);
            this.V.d(f10);
            this.CosmeticModelRenderer = true;
        }
        this.W.a();
        this.V.a();
        this.d = (float)this.W.j();
        this.e = (float)this.V.j();
    }

    private void v() {
        this.U.clear();
        this.addPlaceholderDefinition((RegistryEntry<StatusEffect>)StatusEffects.SPEED, "0:30");
        this.addPlaceholderDefinition((RegistryEntry<StatusEffect>)StatusEffects.STRENGTH, "0:45");
        this.addPlaceholderDefinition((RegistryEntry<StatusEffect>)StatusEffects.FIRE_RESISTANCE, "2:00");
    }

    private boolean w() {
        return this.a.currentScreen instanceof ChatScreen && this.y().isEmpty();
    }

    private void u() {
        if (this.ModelPart) {
            return;
        }
        Potions uarhs42 = ModuleManager.POTIONS_HUD;
        this.f().a(uarhs42);
        this.ModelPart = true;
    }

    private List<StatusEffectSnapshot> y() {
        ArrayList<StatusEffectSnapshot> arrayList = new ArrayList<StatusEffectSnapshot>();
        if (this.a.player != null) {
            for (StatusEffectInstance statusEffectInstance : this.a.player.getStatusEffects()) {
                RegistryEntry registryEntry = statusEffectInstance.getEffectType();
                String string = PotionsHudElement.$sf$3(this.effectDisplayName((RegistryEntry<StatusEffect>)registryEntry), this.formatAmplifierSuffix(statusEffectInstance.getAmplifier()));
                String string2 = this.formatEffectDuration(statusEffectInstance.getDuration());
                arrayList.add(new StatusEffectSnapshot((RegistryEntry<StatusEffect>)registryEntry, string, string2, statusEffectInstance.getDuration()));
            }
        }
        return arrayList;
    }

    private static /* synthetic */ String $sf$0(int n) {
        return "potion.potency." + n;
    }

    private static /* synthetic */ String $sf$3(String string, String string2) {
        return string + string2;
    }

    private static /* synthetic */ String $sf$1(String string) {
        return " " + string;
    }

    private static /* synthetic */ String $sf$2(String string) {
        return "textures/mob_effect/" + string + ".png";
    }
}
