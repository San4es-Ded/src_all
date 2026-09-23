package rockstar.modules.movement;







import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.rotation.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.*;

import moscow.rockstar.mixin.accessors.FireworkRocketEntityAccessor;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.events.network.SendPacketEvent;
import pyrock.events.player.EventOnTravelPost;
import pyrock.events.player.EventUpdatePostTick;
import pyrock.events.render.HudRenderEvent;
import rockstar.modules.player.FreeCameraModule;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.ModeSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.util.GameUtils;
import rockstar.client.util.ClientMessages;
import rockstar.client.server.KnownServer;
import rockstar.client.server.ServerUtils;
import rockstar.client.util.MathUtils;
import rockstar.client.rotation.RotationBehavior;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationPriority;
import rockstar.client.module.Module;

@ModuleInfo(name="Grim Glide", category=ModuleCategory.MOVEMENT, internalMethod09633="modules.descriptions.grim_glide")
public class GrimGlideModule
extends Module {
    private final ModeSetting internalField0668 = new ModeSetting(this, "modules.settings.grim_glide.mode");
    private final ModeSetting.InternalType0088 internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "RWFlag");
    private final ModeSetting.InternalType0088 internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "ReallyWorld");
    private final ModeSetting.InternalType0088 internalField1066 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.grim_glide.mode.normal");
    private final BooleanSetting internalField0650 = new BooleanSetting(this, "modules.settings.grim_glide.climb").internalMethod06630();
    private final ModeSetting internalField0669 = new ModeSetting((SettingOwner)this, "modules.settings.grim_glide.climb_trigger", () -> !this.internalField0650.internalMethod04496());
    private final ModeSetting.InternalType0088 internalField1067 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.grim_glide.climb_trigger.on_jump");
    private boolean internalField0277;
    private boolean internalField0276;
    private int internalField0227;
    private boolean internalField1099;
    private int internalField0228;
    private boolean internalField1100;
    private final EventListener<ReceivePacketEvent> internalField0157 = receivePacketEvent -> {
        if (receivePacketEvent.getPacket() instanceof PlayerPositionLookS2CPacket) {
            this.internalField0227 = 2;
            this.internalField1099 = true;
        }
    };
    private final EventListener<SendPacketEvent> internalField0158 = sendPacketEvent -> {
        if (!this.internalField1066.isSelected() || this.internalField1100 || !(sendPacketEvent.getPacket() instanceof PlayerMoveC2SPacket)) {
            return;
        }
        if (GrimGlideModule.internalField0149.player != null && GrimGlideModule.internalField0149.player.isGliding() && this.internalField0227 == 0 && !this.internalField1099) {
            this.internalField1100 = true;
            try {
                GrimGlideModule.internalField0149.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.OnGroundOnly(true, true));
            }
            finally {
                this.internalField1100 = false;
            }
            sendPacketEvent.cancel();
        }
        this.internalField1099 = false;
    };
    private final EventListener<EventOnTravelPost> internalField1028 = eventOnTravelPost -> {
        double d;
        if (!this.internalField1066.isSelected() || GrimGlideModule.internalField0149.player == null || !GrimGlideModule.internalField0149.player.isGliding()) {
            return;
        }
        Vec3d vec3d = GrimGlideModule.internalField0149.player.getVelocity();
        Vec3d vec3d2 = GrimGlideModule.internalField0149.player.getRotationVector();
        float f = GrimGlideModule.internalField0149.player.getPitch() * ((float)Math.PI / 180);
        double d2 = Math.sqrt(vec3d2.x * vec3d2.x + vec3d2.z * vec3d2.z);
        double d3 = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
        boolean bl = vec3d.y <= 0.0;
        double d4 = bl && GrimGlideModule.internalField0149.player.hasStatusEffect(StatusEffects.SLOW_FALLING) ? 0.01 : 0.08;
        double d5 = MathHelper.cos((float)f);
        d5 *= d5;
        vec3d = vec3d.add(0.0, d4 * (-1.0 + d5 * 0.75), 0.0);
        if (vec3d.y < 0.0 && d2 > 0.0) {
            d = vec3d.y * -0.1 * d5;
            vec3d = vec3d.add(vec3d2.x * d / d2, d, vec3d2.z * d / d2);
        }
        if (f < 0.0f && d2 > 0.0) {
            d = d3 * (double)(-MathHelper.sin((float)f)) * 0.04;
            vec3d = vec3d.add(-vec3d2.x * d / d2, d * 3.2, -vec3d2.z * d / d2);
        }
        if (d2 > 0.0) {
            vec3d = vec3d.add((vec3d2.x / d2 * d3 - vec3d.x) * 0.1, 0.0, (vec3d2.z / d2 * d3 - vec3d.z) * 0.1);
        }
        d = Math.toRadians(GrimGlideModule.internalField0149.player.getYaw());
        double d6 = -Math.sin(d);
        double d7 = Math.cos(d);
        if (this.internalField0227 >= 1) {
            eventOnTravelPost.setOldVelocity(vec3d.multiply(0.99, (double)0.98f, 0.99).add(d6 * 0.09, 0.03, d7 * 0.09));
        } else {
            eventOnTravelPost.setOldVelocity(vec3d.multiply(0.3, 0.3, 0.3));
        }
    };
    private final EventListener<HudRenderEvent> internalField1029 = hudRenderEvent -> {
        if (!this.internalField0238.isSelected() || GrimGlideModule.internalField0149.player == null || !GrimGlideModule.internalField0149.player.isGliding()) {
            return;
        }
        if (!this.internalField1099) {
            GameUtils.internalMethod03366(GrimGlideModule.internalField0149.player.age % 2 == 0 ? 1.6f : 0.35f);
        } else {
            GameUtils.internalMethod00468();
        }
    };
    private final EventListener<EventUpdatePostTick> internalField1030 = eventUpdatePostTick -> {
        if (!this.internalField0237.isSelected() || GrimGlideModule.internalField0149.player == null || GrimGlideModule.internalField0149.world == null || !GrimGlideModule.internalField0149.player.isGliding()) {
            return;
        }
        if (this.internalMethod09612() || this.internalMethod09613()) {
            return;
        }
        ++this.internalField0228;
        if (this.internalField0228 % 4 != 0) {
            return;
        }
        if (this.internalMethod02371() > 49.0) {
            if (!this.internalField0277) {
                ClientMessages.internalMethod01809(Text.of((String)"\u0421\u0431\u0440\u043e\u0441\u044c \u0441\u043a\u043e\u0440\u043e\u0441\u0442\u044c, \u043c\u0430\u043b\u044b\u0448\u043a\u0430"));
                this.internalField0277 = true;
            }
            return;
        }
        this.internalField0277 = false;
        float f = GrimGlideModule.internalField0149.player.getYaw();
        double d = 0.01;
        double d2 = this.internalMethod02371();
        float f2 = ServerUtils.internalMethod01786(KnownServer.internalField1220) ? 50.0f : 64.0f;
        if (d2 >= (double)f2) {
            d = 0.0;
        }
        double d3 = -Math.sin(Math.toRadians(f)) * d;
        double d4 = Math.cos(Math.toRadians(f)) * d;
        GrimGlideModule.internalField0149.player.setVelocity(d3 * (double)MathUtils.internalMethod05368(0.5, 1.15f), GrimGlideModule.internalField0149.player.getVelocity().y - (double)0.01f, d4 * (double)MathUtils.internalMethod05368(0.5, 0.9f));
    };

    @Override
    public void internalMethod08229() {
        if (GrimGlideModule.internalField0149.player == null || GrimGlideModule.internalField0149.world == null) {
            return;
        }
        if (this.internalField0227 > 0) {
            --this.internalField0227;
        }
        if (!this.internalField0237.isSelected()) {
            return;
        }
        if (!GrimGlideModule.internalField0149.player.isGliding()) {
            this.internalField0276 = false;
            return;
        }
        if (!this.internalField0650.internalMethod04496()) {
            return;
        }
        if (this.internalField1067.isSelected() && !GrimGlideModule.internalField0149.options.jumpKey.isPressed()) {
            this.internalField0276 = false;
            return;
        }
        double d = this.internalMethod02371();
        if (this.internalField0276) {
            if (d >= (double)18.9f) {
                this.internalField0276 = false;
            }
        } else if (d < 14.0) {
            this.internalField0276 = true;
        }
        float f = !this.internalField0276 && GrimGlideModule.internalField0149.player.getY() >= 320.0 ? 0.0f : (this.internalField0276 ? 60.0f : -60.0f);
        FreeCameraModule typedValue262 = RockstarClient.getInstance().getModuleManager().getModule(FreeCameraModule.class);
        float f2 = typedValue262 != null && typedValue262.isEnabled() ? typedValue262.internalMethod04854().internalMethod00169() : GrimGlideModule.internalField0149.player.getYaw();
        RockstarClient.getInstance().internalMethod02368().internalMethod00418(new Rotation(f2, f), RotationBehavior.internalField0114, 25.0f, 1.0f, 2.0f, RotationPriority.internalField0121);
    }

    @Override
    public void onEnable() {
        this.internalField0277 = false;
        this.internalField0228 = 0;
        this.internalField0276 = false;
        this.internalField0227 = 0;
        this.internalField1099 = false;
        this.internalField1100 = false;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        GameUtils.internalMethod00468();
        this.internalField0277 = false;
        this.internalField0228 = 0;
        this.internalField0276 = false;
        this.internalField0227 = 0;
        this.internalField1099 = false;
        this.internalField1100 = false;
        super.onDisable();
    }

    private double internalMethod02371() {
        double d = GrimGlideModule.internalField0149.player.getX() - GrimGlideModule.internalField0149.player.lastX;
        double d2 = GrimGlideModule.internalField0149.player.getZ() - GrimGlideModule.internalField0149.player.lastZ;
        return (double)Math.round(Math.sqrt(d * d + d2 * d2) * 2000.0) / 100.0;
    }

    private boolean internalMethod09612() {
        return GrimGlideModule.internalField0149.player.isUsingItem() && GrimGlideModule.internalField0149.player.getActiveItem().isOf(Items.FIREWORK_ROCKET);
    }

    private boolean internalMethod09613() {
        return !GrimGlideModule.internalField0149.world.getEntitiesByClass(FireworkRocketEntity.class, GrimGlideModule.internalField0149.player.getBoundingBox().expand(2.0), fireworkRocketEntity -> ((FireworkRocketEntityAccessor)(Object)fireworkRocketEntity).getShooter() == GrimGlideModule.internalField0149.player).isEmpty();
    }
}
