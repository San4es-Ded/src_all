package rockstar.client.esp;







import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.event.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.compat.RenderSystem;
import lombok.Generated;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.ModeSetting;
import rockstar.client.event.EventListener;
import rockstar.client.RockstarClient;
import rockstar.client.esp.EspFeature;
import rockstar.client.esp.PlayerTargetType;
import rockstar.client.esp.EntityTargetType;
import rockstar.client.internal.script.DiamondMarkerRenderer;
import rockstar.client.render.HudRenderUtils;
import rockstar.client.internal.rotation.ScreenProjectionUtils;

public class FriendMarkerEspFeature
extends EspFeature {
    private static final ColorRGBA internalField0777 = new ColorRGBA(52.0f, 199.0f, 89.0f);
    private static boolean internalField0277 = false;
    private static boolean internalField0276 = false;
    private final BooleanSetting internalField0650 = this.internalMethod02236("esp.friend_markers");
    private final ModeSetting internalField0668 = new ModeSetting((SettingOwner)this, "esp.friend_markers.type", () -> !this.internalMethod06170(PlayerTargetType.internalField0961));
    private final ModeSetting.InternalType0088 internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "esp.friend_markers.heads");
    private final ModeSetting.InternalType0088 internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "esp.friend_markers.sims").select();
    private final EventListener<Render3DEvent> internalField0157 = render3DEvent -> {
        if (!this.internalMethod06170(PlayerTargetType.internalField0961)) {
            return;
        }
        if (!this.internalField0238.isSelected()) {
            return;
        }
        HudRenderUtils.internalMethod02691(true);
        MatrixStack matrixStack = render3DEvent.getMatrices();
        BufferBuilder bufferBuilder = DiamondMarkerRenderer.internalMethod05818();
        for (AbstractClientPlayerEntity abstractClientPlayerEntity : FriendMarkerEspFeature.internalField0149.world.getPlayers()) {
            if (!RockstarClient.getInstance().internalMethod03375().internalMethod00380(abstractClientPlayerEntity.getName().getString()) || abstractClientPlayerEntity == FriendMarkerEspFeature.internalField0149.player) continue;
            matrixStack.push();
            HudRenderUtils.internalMethod03474(matrixStack, ScreenProjectionUtils.internalMethod02822((Entity)abstractClientPlayerEntity, render3DEvent.getTickDelta()));
            float f = 0.1f;
            DiamondMarkerRenderer.internalMethod04026(matrixStack, bufferBuilder, 0.0f, abstractClientPlayerEntity.getHeight() + 0.4f, 0.0f, f, internalField0777.withAlpha(255.0f));
            matrixStack.pop();
        }
        BuiltBuffer builtBuffer = bufferBuilder.endNullable();
        if (builtBuffer != null) {
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)builtBuffer);
        }
        HudRenderUtils.internalMethod04670();
    };

    public FriendMarkerEspFeature() {
        super("friend_markers", EntityTargetType.internalField0027);
        this.internalMethod02197(PlayerTargetType.internalField0961);
    }

    @Override
    public boolean internalMethod05541(PlayerTargetType typedValue092) {
        return typedValue092 == PlayerTargetType.internalField0961;
    }

    @Override
    public void internalMethod06439(UiRenderContext iII, Entity entity, float f, float f2, EntityTargetType typedValue093, PlayerTargetType typedValue092) {
        if (typedValue092 != PlayerTargetType.internalField0961) {
            return;
        }
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity)entity;
        if (!this.internalField0238.isSelected()) {
            return;
        }
        MatrixStack matrixStack = rockstar.client.render.GuiMatrixCompat.toLegacyStack(iII.getMatrices());
        matrixStack.push();
        matrixStack.translate(f, f2 - livingEntity.getHeight() * 15.0f, 50.0f);
        matrixStack.scale(48.0f, 48.0f, -100.0f);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        BufferBuilder bufferBuilder = DiamondMarkerRenderer.internalMethod05818();
        DiamondMarkerRenderer.internalMethod04026(matrixStack, bufferBuilder, 0.0f, 0.0f, 0.0f, 0.1f, internalField0777.withAlpha(255.0f));
        BuiltBuffer builtBuffer = bufferBuilder.endNullable();
        if (builtBuffer != null) {
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)builtBuffer);
        }
        RenderSystem.enableDepthTest();
        matrixStack.pop();
    }

    public boolean internalMethod01517() {
        return this.internalMethod06170(PlayerTargetType.internalField0961) && this.internalField0237.isSelected();
    }

    @Generated
    public BooleanSetting internalMethod04300() {
        return this.internalField0650;
    }

    @Generated
    public ModeSetting internalMethod04356() {
        return this.internalField0668;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod05869() {
        return this.internalField0237;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod06068() {
        return this.internalField0238;
    }

    @Generated
    public EventListener<Render3DEvent> internalMethod04354() {
        return this.internalField0157;
    }

    @Generated
    public static boolean internalMethod08144() {
        return internalField0277;
    }

    @Generated
    public static void internalMethod02332(boolean bl) {
        internalField0277 = bl;
    }

    @Generated
    public static boolean internalMethod08145() {
        return internalField0276;
    }

    @Generated
    public static void internalMethod02388(boolean bl) {
        internalField0276 = bl;
    }
}

