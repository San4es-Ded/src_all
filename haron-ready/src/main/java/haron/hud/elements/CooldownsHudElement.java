package haron.hud.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.core.BooleanCoercion;
import haron.hud.core.HudElement;
import haron.hud.elements.CooldownSnapshot;
import haron.hud.elements.CooldownRowState;
import haron.hud.elements.CooldownPlaceholder;
import haron.inventory.fh7bgv;
import haron.inventory.nzsxbq;
import haron.inventory.welt51;
import haron.module.ModuleManager;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class CooldownsHudElement
extends HudElement {
    private static final String HUD_TITLE = "Cooldowns";
    private static final String HEADER_SYMBOL = "◔";
    private static final float HUD_HEADER_ICON_BASE = 10.0f;
    private static final float HUD_HEADER_ICON_GAP = 4.0f;
    private static final float x = 8.5f;
    private static final float y = 8.0f;
    private static final float z = 24.0f;
    private static final float A = 16.0f;
    private static final float B = 5.0f;
    private static final float C = 10.0f;
    private static final float D = 2.5f;
    private static final float E = 12.0f;
    private static final float F = 2.0f;
    private static final float G = 12.0f;
    private static final float H = 6.0f;
    private static final float I = 6.5f;
    private static final float J = 0.5f;
    private static final float K = 100.0f;
    private static final double L = 0.2;
    private static final long M = 1000L;
    private final Map<Item, CooldownRowState> O = new LinkedHashMap<Item, CooldownRowState>();
    private final List<CooldownPlaceholder> P = new ArrayList<CooldownPlaceholder>();
    private final AnimatedValue Q = new AnimatedValue();
    private final AnimatedValue R = new AnimatedValue();
    private final AnimatedValue S = new AnimatedValue();
    private long T = 0L;
    private int U = 0;
    private boolean V = false;
    private boolean W = false;
    private boolean X = false;
    private List<CooldownSnapshot> Y = List.of();
    private static final Color WHITE = new Color(232, 231, 236, 255);
    private static Color DIVIDER_CLR;
    private static Color DIVIDER_CENTER_CLR;
    private static final float HEADER_DROP = 3.5f;
    private static final float HEADER_SYMBOL_SIZE = 16.0f;
    private static final float HEADER_SYMBOL_Y_OFFSET = 0.0f;
    private static final ItemRenderState N;
    private static final Map<Item, String> ITEM_DISPLAY_NAMES;

    private static int fontIndex(float f) {
        return Math.max(6, Math.min(64, Math.round(f)));
    }

    private static void updateAccentColors() {
        Color color = pryrvd.ACCENT;
        int n = color.getRed();
        int n2 = color.getGreen();
        int n3 = color.getBlue();
        DIVIDER_CLR = new Color(n, n2, n3, 33);
        DIVIDER_CENTER_CLR = new Color(n, n2, n3, 78);
    }

    private void drawGlowSegment(ShapeRenderer s7swsm2, MatrixStack matrixStack, float f, float f2, float f3, float f4, float f5) {
        float f6 = 2.0f * f4;
        float f7 = f6 / 2.0f;
        float f8 = f3 / 2.0f;
        float f9 = f + f8;
        float f10 = f6 + 2.0f * f4;
        Color color = this.a(DIVIDER_CLR, f5);
        Color color2 = this.a(DIVIDER_CENTER_CLR, f5);
        s7swsm2.a(f - 1.0f * f4, f2 - 1.0f * f4, f8 + 1.0f * f4, f10, f10 / 2.0f, color, color2, color, color2, matrixStack);
        s7swsm2.a(f9, f2 - 1.0f * f4, f8 + 1.0f * f4, f10, f10 / 2.0f, color2, color, color2, color, matrixStack);
        Color color3 = this.a(DIVIDER_CLR, f5);
        Color color4 = this.a(DIVIDER_CENTER_CLR, f5);
        s7swsm2.a(f, f2, f8, f6, f7, color3, color4, color3, color4, matrixStack);
        s7swsm2.a(f9, f2, f8, f6, f7, color4, color3, color4, color3, matrixStack);
    }

    private void drawDivider(ShapeRenderer s7swsm2, MatrixStack matrixStack, float f, float f2, float f3, float f4, float f5) {
        float f6 = 0.8f * f5;
        float f7 = f3 / 2.0f;
        float f8 = f + f7;
        float f9 = f6 / 2.0f;
        s7swsm2.a(f, f2, f7, f6, f9, this.a(DIVIDER_CLR, f4), this.a(DIVIDER_CENTER_CLR, f4), this.a(DIVIDER_CLR, f4), this.a(DIVIDER_CENTER_CLR, f4), matrixStack);
        s7swsm2.a(f8, f2, f7, f6, f9, this.a(DIVIDER_CENTER_CLR, f4), this.a(DIVIDER_CLR, f4), this.a(DIVIDER_CENTER_CLR, f4), this.a(DIVIDER_CLR, f4), matrixStack);
    }

    private static String formatCooldownTime(int n) {
        if (n <= 0) {
            return "0:00";
        }
        float f = (float)n / 20.0f;
        int n2 = (int)f;
        int n3 = n2 / 60;
        return n3 > 0 ? String.format("%d:%02d", n3, n2 % 60) : (f < 10.0f ? String.format("%.1f", Float.valueOf(f)) : String.format("%d", n2));
    }

    private static float rowWidth(FontRenderer v6hnga2, FontRenderer v6hnga3, String string, String string2, float f) {
        float f2 = 2.5f * f;
        return 10.0f * f + f2 + v6hnga2.a(string) + 8.0f * f + (v6hnga3.a(string2) + 6.5f * f * 2.0f + 0.5f * f * 2.0f - 2.0f * f);
    }

    private void drawRow(MatrixStack matrixStack, DrawContext drawContext, ShapeRenderer s7swsm2, FontRenderer v6hnga2, FontRenderer v6hnga3, float f, float f2, ItemStack itemStack, String string, String string2, float f3, float f4) {
        if (f3 < 0.01f) {
            return;
        }
        float f5 = 10.0f * f4;
        float f6 = 2.5f * f4;
        float f7 = 8.5f * f4;
        float f8 = f2 + 12.0f * f4 / 2.0f;
        float f9 = 1.6f * f4;
        float f10 = 8.0f * f4;
        s7swsm2.a(f, f8 - f10 / 2.0f, f9, f10, f9 / 2.0f, this.a(pryrvd.ACCENT, f3), matrixStack);
        float f11 = f + f9 + 4.0f * f4;
        this.a(matrixStack, drawContext, itemStack, f11, f8 - f5 / 2.0f, f5, f3);
        float f12 = f11 + f5 + f6;
        v6hnga2.a(string, f12, (double)(f8 - v6hnga2.b(string) / 4.0f), this.a(WHITE, f3), matrixStack);
        float f13 = v6hnga3.a(string2);
        float f14 = this.b + this.d - f7 - f13;
        v6hnga3.a(string2, f14, (double)(f8 - v6hnga3.b(string2) / 4.0f), this.a(pryrvd.ACCENT_SOFT, f3), matrixStack);
    }

    private void tryAddCooldownStack(ItemCooldownManager itemCooldownManager, ItemStack itemStack, HashSet<Identifier> hashSet, List<CooldownSnapshot> list) {
        if (itemStack.isEmpty()) {
            return;
        }
        Identifier identifier = itemCooldownManager.getGroup(itemStack);
        if (hashSet.contains(identifier)) {
            return;
        }
        nzsxbq nzsxbq2 = fh7bgv.a(itemCooldownManager, identifier);
        int n = nzsxbq2.a() ? Math.max(0, nzsxbq2.c - nzsxbq2.a) : 0;
        float f = itemCooldownManager.getCooldownProgress(itemStack, this.a.getRenderTickCounter().getTickDelta(true));
        if (n <= 0 && f > 0.001f) {
            n = Math.max(1, Math.round(f * 20.0f));
        }
        if (n <= 0) {
            return;
        }
        hashSet.add(identifier);
        list.add(new CooldownSnapshot(itemStack.getItem(), itemStack, this.a(itemStack), CooldownsHudElement.formatCooldownTime(n), n));
    }

    public CooldownsHudElement(float f, float f2) {
        super(f, f2);
        this.S.d(0.0);
        this.u();
        this.a();
    }

    static {
        N = new ItemRenderState();
        ITEM_DISPLAY_NAMES = new HashMap<Item, String>();
        ITEM_DISPLAY_NAMES.put(Items.DRIED_KELP, "Пласт");
        ITEM_DISPLAY_NAMES.put(Items.NETHERITE_SCRAP, "Трапка");
        ITEM_DISPLAY_NAMES.put(Items.CHORUS_FRUIT, "Chorus");
        ITEM_DISPLAY_NAMES.put(Items.POPPED_CHORUS_FRUIT, "Chorus");
        ITEM_DISPLAY_NAMES.put(Items.ENDER_PEARL, "Ender Pearl");
        ITEM_DISPLAY_NAMES.put(Items.GOLDEN_APPLE, "Golden Apple");
        ITEM_DISPLAY_NAMES.put(Items.ENDER_EYE, "Глаз эндера");
    }

    private String b(int n) {
        return n <= 0 ? "0.0s" : String.format("%.1fs", Float.valueOf((float)n / 20.0f));
    }

    private List<CooldownSnapshot> x() {
        ArrayList<CooldownSnapshot> arrayList = new ArrayList<CooldownSnapshot>();
        if (this.a.player == null) {
            return arrayList;
        }
        ItemCooldownManager itemCooldownManager = this.a.player.getItemCooldownManager();
        HashSet<Identifier> hashSet = new HashSet<Identifier>();
        for (welt51 welt512 : fh7bgv.collectActiveGroups(itemCooldownManager)) {
            if (!hashSet.add(welt512.groupId)) continue;
            ItemStack itemStack = fh7bgv.stackForGroup(itemCooldownManager, welt512.groupId);
            int n = Math.max(0, welt512.remainingTicks);
            arrayList.add(new CooldownSnapshot(itemStack.getItem(), itemStack, itemStack.getName().getString(), CooldownsHudElement.formatCooldownTime(n), n));
        }
        for (int i = 0; i < this.a.player.getInventory().size(); ++i) {
            this.tryAddCooldownStack(itemCooldownManager, this.a.player.getInventory().getStack(i), hashSet, arrayList);
        }
        for (Item item : new Item[]{Items.CHORUS_FRUIT, Items.POPPED_CHORUS_FRUIT, Items.ENDER_PEARL, Items.GOLDEN_APPLE}) {
            this.tryAddCooldownStack(itemCooldownManager, item.getDefaultStack(), hashSet, arrayList);
        }
        arrayList.sort(Comparator.comparingInt(v5okuy2 -> {
            return v5okuy2.e;
        }));
        return arrayList;
    }

    private void a(MatrixStack matrixStack, DrawContext drawContext, ItemStack itemStack, float f, float f2, float f3, float f4) {
        if (this.a.player == null || itemStack.isEmpty() || f4 < 0.01f) {
            return;
        }
        MatrixStack matrixStack2 = drawContext.getMatrices();
        matrixStack2.push();
        matrixStack2.translate(f, f2, 0.0f);
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

    private String a(ItemStack itemStack) {
        String string = ITEM_DISPLAY_NAMES.get(itemStack.getItem());
        return string != null ? string : itemStack.getName().getString();
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2) {
        CooldownsHudElement.updateAccentColors();
        if (this.a.player == null) {
            return;
        }
        this.a();
        float f3 = (float)this.S.j();
        if (f3 < 0.01f) {
            return;
        }
        boolean bl = this.v();
        float f4 = this.g();
        int n = CooldownsHudElement.fontIndex(16.0f * f4);
        int n2 = CooldownsHudElement.fontIndex(16.0f * f4);
        int n3 = CooldownsHudElement.fontIndex(14.0f * f4);
        int n4 = CooldownsHudElement.fontIndex(15.0f * f4);
        FontRenderer v6hnga2 = ClientFonts.MEDIUM[n4];
        FontRenderer v6hnga3 = ClientFonts.MEDIUM[n];
        FontRenderer v6hnga4 = ClientFonts.REGULAR[n2];
        FontRenderer v6hnga5 = ClientFonts.REGULAR[n3];
        float f5 = 8.5f * f4;
        float f6 = 24.0f * f4;
        float f7 = 12.0f * f4;
        float f8 = 6.0f * f4;
        float f9 = 6.5f * f4;
        float f10 = this.b;
        float f11 = this.c;
        Color color = this.a(pryrvd.PANEL_BG_TOP, f3);
        Color color2 = this.a(pryrvd.PANEL_BG_BOT, f3);
        s7swsm2.a(f10, f11, this.d, this.e, f9, color, color, color2, color2, matrixStack);
        DrawContext drawContext = new DrawContext(this.a, this.a.getBufferBuilders().getEntityVertexConsumers());
        float f12 = 3.5f * f4;
        float f13 = f10 + f5 + 2.0f * f4;
        float f14 = v6hnga2.b("Cooldowns");
        float f15 = f11 + f6 / 2.0f - f14 / 2.0f + f12;
        v6hnga2.a("Cooldowns", f13, (double)f15, this.a(WHITE, f3), matrixStack);
        float f16 = v6hnga2.a("Cooldowns");
        float f17 = f15 + f14 / 2.0f;
        float f18 = v6hnga3.a(HEADER_SYMBOL);
        float f19 = v6hnga3.b(HEADER_SYMBOL);
        float f20 = f10 + this.d - f5 - f18 - 1.0f * f4;
        float f21 = f17 - f19 / 2.0f + 0.0f * f4;
        v6hnga3.a(HEADER_SYMBOL, f20, (double)f21, this.a(pryrvd.ACCENT_SOFT, f3), matrixStack);
        float f22 = f11 + f6;
        this.drawGlowSegment(s7swsm2, matrixStack, f13, f22, f16, f4, f3);
        float f23 = f20 - 3.0f * f4;
        float f24 = f18 + 6.0f * f4;
        this.drawGlowSegment(s7swsm2, matrixStack, f23, f22, f24, f4, f3);
        float f25 = f11 + f6 + f8;
        float f26 = f10 + f5;
        if (!bl || this.P.isEmpty()) {
            boolean bl2 = true;
            for (CooldownSnapshot v5okuy2 : this.Y) {
                float f27;
                CooldownRowState xbodcx2 = this.O.get(v5okuy2.a);
                float f28 = f27 = xbodcx2 != null ? (float)xbodcx2.f.j() : 1.0f;
                if (!bl2) {
                    this.drawDivider(s7swsm2, matrixStack, f26, f25, this.d - f5 * 2.0f, f27 * f3, f4);
                    f25 += 2.0f * f4;
                }
                this.drawRow(matrixStack, drawContext, s7swsm2, v6hnga4, v6hnga5, f26, f25, v5okuy2.b, v5okuy2.c, v5okuy2.d, f27 * f3, f4);
                f25 += 12.0f * f4;
                bl2 = false;
            }
        } else {
            boolean bl3 = true;
            for (int i = 0; i < this.P.size(); ++i) {
                CooldownPlaceholder y3mk3u2 = this.P.get(i);
                if (!bl3) {
                    this.drawDivider(s7swsm2, matrixStack, f26, f25, this.d - f5 * 2.0f, f3, f4);
                    f25 += 2.0f * f4;
                }
                this.drawRow(matrixStack, drawContext, s7swsm2, v6hnga4, v6hnga5, f26, f25, new ItemStack((ItemConvertible)y3mk3u2.a), y3mk3u2.b, y3mk3u2.c, f3, f4);
                f25 += 12.0f * f4;
                bl3 = false;
            }
        }
    }

    @Override
    protected void a() {
        int n;
        this.w();
        boolean bl = this.v();
        List<CooldownSnapshot> list = this.x();
        this.Y = list;
        float f = this.g();
        int n2 = CooldownsHudElement.fontIndex(15.0f * f);
        int n3 = CooldownsHudElement.fontIndex(16.0f * f);
        int n4 = CooldownsHudElement.fontIndex(14.0f * f);
        FontRenderer v6hnga2 = ClientFonts.MEDIUM[n2];
        FontRenderer v6hnga3 = ClientFonts.REGULAR[n3];
        FontRenderer v6hnga4 = ClientFonts.REGULAR[n4];
        if (bl) {
            this.O.clear();
        } else {
            HashSet hashSet = new HashSet();
            int n5 = 0;
            for (CooldownSnapshot object : list) {
                hashSet.add(object.a);
                CooldownRowState xbodcx2 = this.O.get(object.a);
                if (xbodcx2 == null) {
                    CooldownRowState xbodcx3 = new CooldownRowState(object.a, object.b, object.c, object.d, object.e);
                    xbodcx3.f.d(0.0);
                    xbodcx3.f.a(1.0, 0.2, Easings.h);
                    xbodcx3.g.d(n5);
                    this.O.put(object.a, xbodcx3);
                } else {
                    xbodcx2.b = object.b;
                    xbodcx2.c = object.c;
                    xbodcx2.d = object.d;
                    xbodcx2.e = object.e;
                    xbodcx2.h = false;
                    if (xbodcx2.f.i() < 1.0) {
                        xbodcx2.f.a(1.0, 0.2, Easings.h);
                    }
                    if (Math.abs(xbodcx2.g.i() - (double)n5) > 0.01) {
                        xbodcx2.g.a((double)n5, 0.2, Easings.h);
                    }
                }
                ++n5;
            }
            for (CooldownRowState xbodcx4 : new ArrayList<CooldownRowState>(this.O.values())) {
                if (hashSet.contains(xbodcx4.a) || xbodcx4.h) continue;
                xbodcx4.h = true;
                xbodcx4.f.a(0.0, 0.2, Easings.h);
            }
            this.O.entrySet().removeIf(entry -> {
                CooldownRowState xbodcx2 = (CooldownRowState)entry.getValue();
                return xbodcx2.h && xbodcx2.f.j() <= 0.01;
            });
        }
        for (CooldownRowState xbodcx4 : this.O.values()) {
            xbodcx4.f.a();
            xbodcx4.g.a();
        }
        if (bl || !list.isEmpty()) {
            if (!this.W) {
                this.S.a(1.0, 0.2, Easings.h);
                this.W = true;
            }
        } else if (this.W) {
            this.S.a(0.0, 0.2, Easings.h);
            this.W = false;
        }
        this.S.a();
        float f2 = 8.5f * f;
        float f3 = 24.0f * f;
        float f4 = 12.0f * f;
        float f5 = 6.0f * f;
        float f6 = 110.0f * f;
        if (!bl || this.P.isEmpty()) {
            for (CooldownRowState xbodcx5 : this.O.values()) {
                if (!(xbodcx5.f.j() > 0.01)) continue;
                f6 = Math.max(f6, CooldownsHudElement.rowWidth(v6hnga3, v6hnga4, xbodcx5.c, xbodcx5.d, f));
            }
        } else {
            for (CooldownPlaceholder y3mk3u2 : this.P) {
                f6 = Math.max(f6, CooldownsHudElement.rowWidth(v6hnga3, v6hnga4, y3mk3u2.b, y3mk3u2.c, f));
            }
        }
        float f7 = Math.max(f6, f2 + 10.0f * f + 4.0f * f + v6hnga2.a("Cooldowns") + f2 + 18.0f * f);
        float f8 = 0.0f;
        int n5 = n = bl ? this.P.size() : list.size();
        if (n > 1) {
            f8 = (float)(n - 1) * f4 + (float)(n - 1) * 2.0f * f;
        }
        float f9 = f7 + f2;
        float f10 = f3 + f5 + f8 + 10.0f * f + 10.0f * f;
        if (this.V) {
            if (Math.abs(this.Q.i() - (double)f9) > 0.5) {
                this.Q.a((double)f9, 0.2, Easings.h);
            }
            if (Math.abs(this.R.i() - (double)f10) > 0.5) {
                this.R.a((double)f10, 0.2, Easings.h);
            }
        } else {
            this.Q.d(f9);
            this.R.d(f10);
            this.V = true;
        }
        this.Q.a();
        this.R.a();
        this.d = (float)this.Q.j();
        this.e = (float)this.R.j();
        if (!bl || this.P.size() <= 1) {
            return;
        }
        long l = System.currentTimeMillis();
        if (l - this.T >= 1000L) {
            this.T = l;
            this.U = (this.U + 1) % this.P.size();
        }
    }

    private Color a(Color color, float f) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, (int)((float)color.getAlpha() * f))));
    }

    private boolean v() {
        return BooleanCoercion.from(this.a.currentScreen instanceof ChatScreen && this.x().isEmpty() ? 1 : 0);
    }

    private void w() {
        if (this.X) {
            return;
        }
        this.f().a(ModuleManager.COOLDOWNS_HUD);
        this.X = true;
    }

    private void u() {
        this.P.add(new CooldownPlaceholder(Items.ENDER_PEARL, "Ender Pearl", "0:23"));
        this.P.add(new CooldownPlaceholder(Items.CHORUS_FRUIT, "Chorus", "0:42"));
        this.P.add(new CooldownPlaceholder(Items.GOLDEN_APPLE, "Golden Apple", "0:05"));
    }
}

