package haron.hud.elements;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.hud.core.HudElement;
import haron.hud.core.HudServices;
import haron.module.ModuleManager;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class TargetHudElement
extends HudElement {
    private static final float WIDTH = 105.0f;
    private static final float HEIGHT = 36.0f;
    private static final float COMPACT_HEIGHT = 26.0f;
    private static final float LEFT_SECTION = 32.0f;
    private static final float SECTION_GAP = 4.0f;
    private static final float RADIUS = 7.5f;
    private final AnimatedValue scaleAnim = new AnimatedValue();
    private final AnimatedValue healthAnim = new AnimatedValue();
    private final AnimatedValue switchAnim = new AnimatedValue();
    private LivingEntity target;
    private String targetName = "Target";
    private float displayedHealth = 0.0f;
    private float displayedAbsorption = 0.0f;
    private float animatedHeight = 36.0f;
    private long lastUpdateTime = System.currentTimeMillis();
    private boolean targetIsPlayer;
    private Identifier skinTexture;
    private Identifier lastTexture;
    private long textureChangedAtMs;
    private boolean settingsBound;

    private String trimToWidth(FontRenderer v6hnga2, String string, float f) {
        if (string == null || f <= 0.0f) {
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
            if (!(v6hnga2.a(TargetHudElement.$sf$0(string3, string2)) > f)) break;
            string3 = string3.substring(0, string3.length() - 1);
        }
        return string3.isEmpty() ? "" : TargetHudElement.$sf$0(string3, string2);
    }

    private void bindSettings() {
        if (this.settingsBound) {
            return;
        }
        this.f().a(ModuleManager.TARGET_HUD);
        this.settingsBound = true;
    }

    private void drawBackground(ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, float f5) {
        Color color = pryrvd.a(pryrvd.PANEL_BG_TOP, 255);
        Color color2 = pryrvd.a(pryrvd.PANEL_BG_BOT, 255);
        s7swsm2.a(f, f2, f3, f4, 7.5f * f5, color, color, color2, color2, new MatrixStack());
        Color color3 = pryrvd.a(new Color(45, 45, 45, 230), 255);
        MatrixStack matrixStack = new MatrixStack();
        s7swsm2.b(f, f2, f3, f4, 7.5f * f5, 0.35f * f5, color3, matrixStack);
    }

    private void drawContent(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, float f5, float f6) {
        if (this.target == null) {
            return;
        }
        float f12 = f + 32.0f * f6 + 4.0f * f6;
        float f13 = f2 + (this.hasArmorOrAbsorption() ? 14.0f : 8.0f) * f6;
        float f14 = f + f3 - 5.0f * f6;
        float f15 = this.target.getHealth();
        float f16 = this.target.getMaxHealth();
        float f17 = this.target.getAbsorptionAmount();
        this.displayedHealth = this.lerp(this.displayedHealth, f15, f5, 5.0f);
        this.displayedAbsorption = f17;
        String string = this.targetName;
        if (string == null || string.isBlank()) {
            string = "Target";
        }
        FontRenderer v6hnga2 = ClientFonts.MEDIUM[TargetHudElement.clamp(Math.round(11.0f * f6), 6, 32)];
        FontRenderer v6hnga3 = ClientFonts.MEDIUM[TargetHudElement.clamp(Math.round(10.0f * f6), 6, 32)];
        Color color = pryrvd.ACCENT;
        Color color2 = new Color(255, 255, 255, 255);
        Color color3 = new Color(255, 200, 50, 255);
        if (this.displayedAbsorption > 0.1f) {
            String healthText = String.format("%.0f", this.displayedHealth);
            float healthWidth = v6hnga3.a(healthText);
            float healthX = f14 - healthWidth;
            v6hnga3.a(healthText, healthX, (double)(f2 + 4.0f * f6), color, matrixStack);
            String string2 = String.format("%.0f", Float.valueOf(this.displayedAbsorption));
            float absorptionWidth = v6hnga3.a(string2);
            float absorptionX = f14 - absorptionWidth;
            float absorptionY = f13 + 0.5f * f6;
            MatrixStack iconStack = new MatrixStack();
            iconStack.push();
            iconStack.translate(absorptionX - 5.5f * f6, absorptionY, 0.0f);
            iconStack.scale(0.4375f * f6, 0.4375f * f6, 1.0f);
            DrawContext drawContext = new DrawContext(this.a, this.a.getBufferBuilders().getEntityVertexConsumers());
            drawContext.drawItem(new ItemStack((ItemConvertible)Items.GOLDEN_APPLE), 0, 0);
            iconStack.pop();
            v6hnga3.a(string2, absorptionX, (double)absorptionY, color3, matrixStack);
        } else {
            String healthText = String.format("%.0f", this.displayedHealth);
            float healthX = f14 - v6hnga3.a(healthText);
            v6hnga3.a(healthText, healthX, (double)f13, color, matrixStack);
        }
        float f18 = Math.max(1.0f, f14 - f12 - 4.0f * f6);
        String string3 = this.trimToWidth(v6hnga2, string, f18);
        v6hnga2.a(string3, f12, (double)f13, color2, matrixStack);
        float f11 = f12;
        float f19 = f2 + (f4 - (this.hasArmorOrAbsorption() ? 28.0f : 20.0f) * f6) / 2.0f + (this.hasArmorOrAbsorption() ? 28.0f : 20.0f) * f6;
        float f10 = f19 - 2.0f * f6;
        float f9 = f14 - f12;
        float f8 = 2.0f * f6;
        Color barBackground = pryrvd.a(new Color(30, 30, 35, 180), 255);
        s7swsm2.a(f11, f10, f9, f8, f8 / 2.0f, barBackground, matrixStack);
        float f7 = f16 > 0.0f ? Math.min(1.0f, this.displayedHealth / f16) : 0.0f;
        if (f7 > 0.01f) {
            Color color4 = pryrvd.a(color, 255);
            Color color5 = pryrvd.a(pryrvd.c(color, 25), 255);
            float f21 = Math.max(f8, f9 * f7);
            s7swsm2.a(f11, f10, f21, f8, f8 / 2.0f, color4, color5, color4, color5, matrixStack);
        }
    }

    private float getDurability(ItemStack itemStack) {
        if (itemStack.isEmpty() || !itemStack.isDamageable()) {
            return 100.0f;
        }
        int n = itemStack.getMaxDamage();
        int n2 = itemStack.getDamage();
        return n > 0 ? (float)(n - n2) * 100.0f / (float)n : 100.0f;
    }

    private void updateTarget() {
        boolean bl;
        LivingEntity livingEntity = HudServices.TARGETS.currentTarget();
        if (livingEntity == null) {
            livingEntity = this.targetFromCrosshair();
        }
        if (livingEntity == null && this.a.currentScreen instanceof ChatScreen && this.a.player != null) {
            livingEntity = this.a.player;
        }
        boolean bl2 = this.target != null;
        boolean bl3 = bl = livingEntity != null;
        if (livingEntity != null) {
            if (livingEntity != this.target) {
                this.target = livingEntity;
                this.switchAnim.d(0.0);
                this.switchAnim.a(1.0, 0.2, Easings.h);
                this.healthAnim.d(Math.max(0.0f, livingEntity.getHealth()));
                this.lastTexture = null;
            }
            this.targetIsPlayer = livingEntity instanceof PlayerEntity;
            this.displayedHealth = Math.max(0.0f, livingEntity.getHealth());
            if (this.targetIsPlayer) {
                PlayerEntity playerEntity = (PlayerEntity)livingEntity;
                this.targetName = playerEntity.getGameProfile().getName();
                this.skinTexture = this.skinFor(playerEntity);
            } else {
                this.targetName = livingEntity.getName().getString();
                this.skinTexture = null;
            }
        } else {
            this.target = null;
            this.lastTexture = null;
        }
        if (bl && !bl2) {
            this.scaleAnim.a(1.0, 0.2, Easings.h);
        } else if (!bl && bl2) {
            this.scaleAnim.a(0.0, 0.2, Easings.h);
        } else if (bl && this.scaleAnim.i() < 1.0) {
            this.scaleAnim.a(1.0, 0.2, Easings.h);
        }
        if (Math.abs(this.healthAnim.i() - (double)this.displayedHealth) > 0.01) {
            this.healthAnim.a((double)this.displayedHealth, 0.15, Easings.h);
        }
        this.lastUpdateTime = System.currentTimeMillis();
        this.scaleAnim.a();
        this.healthAnim.a();
        this.switchAnim.a();
    }

    private Color getDurabilityColor(float f) {
        if (f > 66.0f) {
            return new Color(85, 255, 85, 255);
        }
        if (f > 33.0f) {
            return new Color(255, 200, 50, 255);
        }
        return new Color(255, 85, 85, 255);
    }

    private void drawFace(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4) {
        if (this.target == null) {
            return;
        }
        float f5 = (this.hasArmorOrAbsorption() ? 28.0f : 20.0f) * f4;
        float f6 = f + 7.0f * f4;
        float f7 = f2 + (f3 - f5) / 2.0f;
        float f8 = 3.0f * f4;
        Color color = this.hurtTint();
        if (this.skinTexture != null) {
            Identifier identifier = this.skinTexture;
            if (this.lastTexture == null || !this.lastTexture.equals((Object)identifier)) {
                this.lastTexture = identifier;
                this.textureChangedAtMs = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() - this.textureChangedAtMs < 100L) {
                return;
            }
            s7swsm2.a(identifier, f6, f7, f5, f5, f8, 0.125f, 0.125f, 0.125f, 0.125f, color, matrixStack, true);
            s7swsm2.a(identifier, f6, f7, f5, f5, f8, 0.625f, 0.125f, 0.125f, 0.125f, color, matrixStack, true);
            return;
        }
        Color color2 = pryrvd.a(pryrvd.ACCENT_DARK, 255);
        s7swsm2.a(f6, f7, f5, f5, f8, color2, matrixStack);
        String string = this.targetName != null && !this.targetName.isEmpty() ? String.valueOf(this.targetName.charAt(0)).toUpperCase() : "?";
        FontRenderer v6hnga2 = ClientFonts.MEDIUM[TargetHudElement.clamp(Math.round(12.0f * f4), 6, 32)];
        v6hnga2.a(string, f6 + (f5 - v6hnga2.a(string)) / 2.0f, (double)(f7 + (f5 - v6hnga2.b(string)) / 2.0f + 1.0f * f4), color, matrixStack);
    }

    private Color hurtTint() {
        float f = 1.0f;
        if (this.target != null && this.target.hurtTime > 0) {
            float f2 = (float)this.target.hurtTime / 10.0f;
            int n = Math.min(200, (int)(f2 * 90.0f));
            return new Color(255, 255 - n, 255 - n, 255);
        }
        return new Color(255, 255, 255, 255);
    }

    private float lerp(float f, float f2, float f3, float f4) {
        float f5 = (float)(1.0 - Math.pow(0.001, f3 * f4));
        return f + (f2 - f) * f5;
    }

    private void drawArmor(ShapeRenderer s7swsm2, MatrixStack matrixStack, float f, float f2, float f3, float f4) {
        LivingEntity livingEntity = this.target;
        if (!(livingEntity instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity playerEntity = (PlayerEntity)livingEntity;
        boolean bl = ModuleManager.TARGET_HUD.isShowDurability();
        float f5 = f + 32.0f * f4 + 4.0f * f4 + 2.0f * f4;
        float f6 = f2 + 3.0f * f4;
        float f7 = 8.0f * f4;
        float f8 = 3.5f * f4;
        float f9 = 0.5f;
        EquipmentSlot[] armorSlots = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
        float f10 = f5;
        for (EquipmentSlot equipmentSlot : armorSlots) {
            ItemStack itemStack2 = playerEntity.getEquippedStack(equipmentSlot);
            if (itemStack2.isEmpty()) continue;
            MatrixStack matrixStack2 = new MatrixStack();
            matrixStack2.push();
            matrixStack2.translate(f10, f6, 0.0f);
            matrixStack2.scale(0.5f, 0.5f, 1.0f);
            DrawContext drawContext = new DrawContext(this.a, this.a.getBufferBuilders().getEntityVertexConsumers());
            drawContext.drawItem(itemStack2, 0, 0);
            matrixStack2.pop();
            if (bl && itemStack2.isDamageable() && itemStack2.getDamage() > 0) {
                float f11 = this.getDurability(itemStack2);
                Color color = this.getDurabilityColor(f11);
                String string = String.format("%.0f", Float.valueOf(f11));
                FontRenderer v6hnga2 = ClientFonts.MEDIUM[TargetHudElement.clamp(Math.round(5.0f * f4), 6, 32)];
                float f12 = v6hnga2.a(string);
                float f13 = f10 + (f7 - f12 * 0.5f) / 2.0f;
                float f14 = f6 + f7 + 0.5f * f4;
                MatrixStack matrixStack3 = new MatrixStack();
                matrixStack3.push();
                matrixStack3.translate(f13, f14, 0.0f);
                matrixStack3.scale(0.5f, 0.5f, 1.0f);
                v6hnga2.a(string, 0.0, 0.0, color, matrixStack3);
                matrixStack3.pop();
            }
            f10 += f7 + f8;
        }
        ItemStack itemStack3 = playerEntity.getOffHandStack();
        if (!itemStack3.isEmpty()) {
            MatrixStack matrixStack4 = new MatrixStack();
            matrixStack4.push();
            matrixStack4.translate(f10, f6, 0.0f);
            matrixStack4.scale(0.5f, 0.5f, 1.0f);
            DrawContext drawContext = new DrawContext(this.a, this.a.getBufferBuilders().getEntityVertexConsumers());
            drawContext.drawItem(itemStack3, 0, 0);
            matrixStack4.pop();
        }
    }

    private Identifier skinFor(PlayerEntity playerEntity) {
        AbstractClientPlayerEntity abstractClientPlayerEntity;
        Identifier identifier;
        if (playerEntity instanceof AbstractClientPlayerEntity && (identifier = (abstractClientPlayerEntity = (AbstractClientPlayerEntity)playerEntity).getSkinTextures().texture()) != null) {
            return identifier;
        }
        return DefaultSkinHelper.getTexture();
    }

    private boolean hasArmorOrAbsorption() {
        if (this.target == null) {
            return false;
        }
        if (this.target.getAbsorptionAmount() > 0.1f) {
            return true;
        }
        if (!ModuleManager.TARGET_HUD.isShowArmor()) {
            return false;
        }
        if (this.target instanceof PlayerEntity playerEntity) {
            for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                if (equipmentSlot.getType() == EquipmentSlot.Type.HAND || playerEntity.getEquippedStack(equipmentSlot).isEmpty()) continue;
                return true;
            }
        }
        return false;
    }

    private LivingEntity targetFromCrosshair() {
        if (this.a.player == null || this.a.world == null) {
            return null;
        }
        HitResult hitResult = this.a.crosshairTarget;
        if (hitResult == null || hitResult.getType() != HitResult.Type.ENTITY) {
            return null;
        }
        if (!(hitResult instanceof EntityHitResult)) {
            return null;
        }
        EntityHitResult entityHitResult = (EntityHitResult)hitResult;
        Entity entity = entityHitResult.getEntity();
        if (!(entity instanceof LivingEntity)) {
            return null;
        }
        LivingEntity livingEntity = (LivingEntity)entity;
        if (livingEntity == this.a.player || !livingEntity.isAlive() || livingEntity.isRemoved()) {
            return null;
        }
        return livingEntity;
    }

    public TargetHudElement(float f, float f2) {
        super(f, f2);
        this.scaleAnim.d(0.0);
        this.healthAnim.d(20.0);
        this.switchAnim.d(1.0);
        this.d = 105.0f;
        this.e = 36.0f;
    }

    private static int clamp(int n, int n2, int n3) {
        return Math.max(n2, Math.min(n3, n));
    }

    @Override
    protected void a() {
        this.bindSettings();
        float f = this.g();
        this.d = 105.0f * f;
        this.e = 36.0f * f;
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2) {
        float f3;
        if (this.a.player == null) {
            return;
        }
        this.a();
        this.updateTarget();
        float f4 = (float)this.scaleAnim.j();
        if (f4 < 0.01f) {
            return;
        }
        float f5 = this.g();
        float f6 = this.b;
        float f7 = this.c;
        float f8 = this.hasArmorOrAbsorption() ? 36.0f : 26.0f;
        float f9 = Math.min(0.1f, (float)(System.currentTimeMillis() - this.lastUpdateTime) / 1000.0f);
        this.animatedHeight = this.lerp(this.animatedHeight, f8, f9, 10.0f);
        float f10 = this.animatedHeight * f5;
        this.d = f3 = 105.0f * f5;
        this.e = f10;
        float f11 = f6 + f3 / 2.0f;
        float f12 = f7 + f10 / 2.0f;
        matrixStack.push();
        matrixStack.translate(f11, f12, 0.0f);
        matrixStack.scale(f4, f4, 1.0f);
        matrixStack.translate(-f11, -f12, 0.0f);
        this.drawBackground(s7swsm2, f6, f7, f3, f10, f5);
        this.drawFace(matrixStack, s7swsm2, f6, f7, f10, f5);
        this.drawContent(matrixStack, s7swsm2, f6, f7, f3, f10, f9, f5);
        matrixStack.pop();
        if (this.hasArmorOrAbsorption()) {
            matrixStack.push();
            matrixStack.translate(f11, f12, 0.0f);
            matrixStack.scale(f4, f4, 1.0f);
            matrixStack.translate(-f11, -f12, 0.0f);
            this.drawArmor(s7swsm2, matrixStack, f6, f7, f10, f5);
            matrixStack.pop();
        }
    }

    private static /* synthetic */ String $sf$0(String string, String string2) {
        return string + string2;
    }
}
