package rockstar.modules.player;






import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.*;

import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import rockstar.client.compat.ShaderProgramKey;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.math.Vec3d;
import pyrock.events.game.AfterAttackEvent;
import pyrock.events.game.WorldChangeEvent;
import pyrock.events.network.SendPacketEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.util.GameUtils;
import rockstar.client.render.Render3DUtils;
import rockstar.client.module.Module;
import rockstar.client.util.Stopwatch;
import rockstar.modules.combat.AutoTotemModule;

@ModuleInfo(name="Blink", category=ModuleCategory.PLAYER)
public class BlinkModule
extends Module {
    private final List<Packet<?>> internalField0416 = new ArrayList();
    private final Stopwatch internalField0519 = new Stopwatch();
    private BooleanSetting internalField0650;
    private SliderSetting internalField0383;
    private BooleanSetting internalField0651;
    private BooleanSetting internalField1261;
    private Vec3d internalField0283;
    private boolean internalField0277;
    private final EventListener<SendPacketEvent> internalField0157 = this::onEvent;
    private final EventListener<AfterAttackEvent> internalField0158 = afterAttackEvent -> {
        this.onDisable();
        this.onEnable();
        this.internalField0519.internalMethod00701();
    };
    private final EventListener<Render3DEvent> internalField1028 = render3DEvent -> {
        if (this.internalField0651.internalMethod04496() && this.internalField0283 != null && (BlinkModule.internalField0149.options.getPerspective() != Perspective.FIRST_PERSON || !this.internalField1261.internalMethod04496())) {
            MatrixStack matrixStack = render3DEvent.getMatrices();
            BufferBuilder bufferBuilder = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
            Vec3d vec3d = BlinkModule.internalField0149.gameRenderer.getCamera().getCameraPos();
            matrixStack.push();
            RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            Render3DUtils.internalMethod08795(matrixStack, bufferBuilder, BlinkModule.internalField0149.player.getBoundingBox().offset(this.internalField0283.subtract(BlinkModule.internalField0149.player.getEntityPos())).offset(-vec3d.x, -vec3d.y, -vec3d.z), ColorRGBA.WHITE.withAlpha(180.0f));
            BuiltBuffer builtBuffer = bufferBuilder.endNullable();
            if (builtBuffer != null) {
                BufferRenderer.drawWithGlobalProgram((BuiltBuffer)builtBuffer);
            }
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            matrixStack.pop();
        }
    };
    private final EventListener<WorldChangeEvent> internalField1029 = worldChangeEvent -> this.disable();

    public BlinkModule() {
        this.internalMethod09706();
    }

    private void internalMethod09706() {
        this.internalField0650 = new BooleanSetting(this, "modules.settings.blink.pulse");
        this.internalField0383 = new SliderSetting((SettingOwner)this, "modules.settings.blink.time", () -> !this.internalField0650.internalMethod04496()).internalMethod05900(1.0f).internalMethod02732(40.0f).internalMethod08673(1.0f).internalMethod08074(12.0f);
        this.internalField0651 = new BooleanSetting(this, "modules.settings.blink.display");
        this.internalField1261 = new BooleanSetting((SettingOwner)this, "modules.settings.blink.hide_first_person", () -> !this.internalField0651.internalMethod04496());
    }

    public void onEvent(SendPacketEvent sendPacketEvent) {
        if (this.internalField0277 || !GameUtils.internalMethod00471() || RockstarClient.getInstance().getModuleManager().getModule(AutoTotemModule.class).internalMethod09477()) {
            return;
        }
        this.internalField0416.add(sendPacketEvent.getPacket());
        sendPacketEvent.cancel();
        if (this.internalField0650.internalMethod04496() && this.internalField0519.internalMethod02365((long)(this.internalField0383.internalMethod08576() * 50.0f))) {
            this.onDisable();
            this.onEnable();
            this.internalField0519.internalMethod00701();
        }
    }

    @Override
    public void onEnable() {
        if (BlinkModule.internalField0149.player == null) {
            return;
        }
        this.internalField0416.clear();
        this.internalField0283 = BlinkModule.internalField0149.player.getEntityPos();
        this.internalField0519.internalMethod00701();
        this.internalField0277 = false;
    }

    @Override
    public void onDisable() {
        if (BlinkModule.internalField0149.player == null) {
            return;
        }
        this.internalField0277 = true;
        for (Packet<?> packet : this.internalField0416) {
            BlinkModule.internalField0149.player.networkHandler.sendPacket(packet);
        }
        this.internalField0277 = false;
        this.internalField0416.clear();
        this.internalField0283 = null;
    }

    @Generated
    public Stopwatch internalMethod02640() {
        return this.internalField0519;
    }

    @Generated
    public BooleanSetting internalMethod03674() {
        return this.internalField0650;
    }

    @Generated
    public SliderSetting internalMethod01477() {
        return this.internalField0383;
    }
}
