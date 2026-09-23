package rockstar.client.internal.script;







import rockstar.client.ui.*;
import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.event.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.*;
import com.mojang.blaze3d.opengl.GlStateManager;
import rockstar.client.compat.RenderSystem;
import lombok.Generated;
import rockstar.client.compat.ShaderProgramKey;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import pyrock.events.game.InternalAttackEvent;
import pyrock.events.network.SendPacketEvent;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.render.Render3DEvent;
import rockstar.modules.player.BlinkModule;
import rockstar.client.event.EventListener;
import rockstar.client.RockstarClient;
import rockstar.client.ui.ThemeColors;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.render.Render3DUtils;
import rockstar.client.render.HudRenderUtils;
import rockstar.client.internal.rotation.EntityMotionSnapshot;

public class PositionLagTrail {
    private int internalField0227;
    private EntityMotionSnapshot internalField0307;
    private final EventListener<Render3DEvent> internalField0157 = render3DEvent -> {
        if (MinecraftClientAccess.internalField0149.world == null || MinecraftClientAccess.internalField0149.player == null || this.internalField0307 == null) {
            return;
        }
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.blendFunc((com.mojang.blaze3d.platform.SourceFactor)com.mojang.blaze3d.platform.SourceFactor.SRC_ALPHA, (com.mojang.blaze3d.platform.DestFactor)com.mojang.blaze3d.platform.DestFactor.ONE);
        RenderSystem.lineWidth((float)10.0f);
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        render3DEvent.getMatrices().push();
        HudRenderUtils.internalMethod01900(render3DEvent.getMatrices());
        BufferBuilder bufferBuilder = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        Render3DUtils.internalMethod08795(render3DEvent.getMatrices(), bufferBuilder, this.internalField0307.internalMethod07420(), ThemeColors.internalMethod02531());
        BuiltBuffer builtBuffer = bufferBuilder.endNullable();
        if (builtBuffer != null) {
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)builtBuffer);
        }
        render3DEvent.getMatrices().pop();
        RenderSystem.lineWidth(1.0f);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    };
    private final EventListener<ClientPlayerTickEvent> internalField0158 = clientPlayerTickEvent -> {
        LivingEntity livingEntity;
        LivingEntity livingEntity2;
        Entity entity = RockstarClient.getInstance().internalMethod04463().internalMethod04526();
        LivingEntity livingEntity3 = livingEntity2 = entity instanceof LivingEntity ? (livingEntity = (LivingEntity)entity) : null;
        if (livingEntity2 != null && MinecraftClientAccess.internalField0149.player.fallDistance > 0.2f && MinecraftClientAccess.internalField0149.player.distanceTo((Entity)livingEntity2) < 3.2f && this.internalField0307 == null) {
            this.internalField0307 = new EntityMotionSnapshot(MinecraftClientAccess.internalField0149.player.getEntityPos(), MinecraftClientAccess.internalField0149.player.getVelocity(), RockstarClient.getInstance().internalMethod02368().internalMethod00024(), MinecraftClientAccess.internalField0149.player.isOnGround(), MinecraftClientAccess.internalField0149.player.getBoundingBox());
        }
        if (this.internalField0307 != null && this.internalField0307.internalMethod00677().distanceTo(livingEntity2.getEntityPos()) > 6.0 && MinecraftClientAccess.internalField0149.player.isOnGround()) {
            this.internalField0307 = null;
            MinecraftClientAccess.internalField0149.player.setVelocity(this.internalField0307.internalMethod05854());
            MinecraftClientAccess.internalField0149.player.setPosition(this.internalField0307.internalMethod00677());
            MinecraftClientAccess.internalField0149.player.setOnGround(this.internalField0307.internalMethod01146());
            this.internalField0227 = 0;
        }
        if (MinecraftClientAccess.internalField0149.player.isOnGround()) {
            // empty if block
        }
    };
    private final EventListener<InternalAttackEvent> internalField1028 = internalAttackEvent -> {
        if (internalAttackEvent.isCancelled()) {
            return;
        }
        ++this.internalField0227;
    };
    private final EventListener<SendPacketEvent> internalField1029 = sendPacketEvent -> {
        Packet<?> packet = sendPacketEvent.getPacket();
        if (this.internalField0307 != null && (packet instanceof PlayerMoveC2SPacket || packet instanceof PlayerMoveC2SPacket.Full || packet instanceof PlayerMoveC2SPacket.PositionAndOnGround || packet instanceof PlayerMoveC2SPacket.LookAndOnGround || packet instanceof PlayerMoveC2SPacket.OnGroundOnly)) {
            sendPacketEvent.cancel();
        }
    };

    public void internalMethod05985() {
        RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
    }

    public void internalMethod05988() {
        if (this.internalField0307 != null) {
            MinecraftClientAccess.internalField0149.player.setVelocity(this.internalField0307.internalMethod05854());
            MinecraftClientAccess.internalField0149.player.setPosition(this.internalField0307.internalMethod00677());
            MinecraftClientAccess.internalField0149.player.setOnGround(this.internalField0307.internalMethod01146());
        }
        RockstarClient.getInstance().internalMethod03317().internalMethod07237(this);
        this.internalField0307 = null;
        this.internalField0227 = 0;
    }

    @Generated
    public int internalMethod05984() {
        return this.internalField0227;
    }

    @Generated
    public EntityMotionSnapshot internalMethod03184() {
        return this.internalField0307;
    }
}
