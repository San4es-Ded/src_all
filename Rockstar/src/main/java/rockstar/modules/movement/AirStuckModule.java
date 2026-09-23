package rockstar.modules.movement;






import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.*;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import pyrock.events.game.WorldChangeEvent;
import pyrock.events.network.SendPacketEvent;
import pyrock.events.player.EventMotion;
import pyrock.events.player.InputEvent;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.ModeSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.event.EventListener;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.util.ClientMessages;
import rockstar.client.util.TextUtils;
import rockstar.client.module.Module;
import rockstar.client.util.Stopwatch;

@ModuleInfo(name="Air Stuck", category=ModuleCategory.MOVEMENT)
public class AirStuckModule
extends Module {
    private ModeSetting internalField0668;
    private ModeSetting.InternalType0088 internalField0237;
    private ModeSetting.InternalType0088 internalField0238;
    private ModeSetting.InternalType0088 internalField1066;
    private BooleanSetting internalField0650;
    private SliderSetting internalField0383;
    private BooleanSetting internalField0651;
    private BooleanSetting internalField1261;
    private double internalField0194 = Double.NaN;
    private boolean internalField0277;
    private Vec3d internalField0283 = Vec3d.ZERO;
    private Vec3d internalField0282 = Vec3d.ZERO;
    private boolean internalField0276;
    private final Queue<Packet<?>> internalField0877 = new ConcurrentLinkedQueue();
    private final Stopwatch internalField0519 = new Stopwatch();
    private boolean internalField1099;
    private final EventListener<InputEvent> internalField0157 = inputEvent -> {
        if (AirStuckModule.internalField0149.player == null || !this.internalField0238.isSelected() && !this.internalField1066.isSelected()) {
            return;
        }
        inputEvent.setForward(0.0f);
        inputEvent.setStrafe(0.0f);
        inputEvent.setJump(false);
        inputEvent.setSneak(false);
        inputEvent.setSprint(false);
    };
    private final EventListener<EventMotion> internalField0158 = eventMotion -> {
        if (!this.internalField0237.isSelected() || !this.internalField0277 || AirStuckModule.internalField0149.player == null) {
            return;
        }
        eventMotion.setX(this.internalField0283.x);
        eventMotion.setY(this.internalField0283.y);
        eventMotion.setZ(this.internalField0283.z);
        eventMotion.setOnGround(false);
        AirStuckModule.internalField0149.player.fallDistance = 0.0f;
    };
    private final EventListener<SendPacketEvent> internalField1028 = sendPacketEvent -> {
        PlayerActionC2SPacket playerActionC2SPacket;
        boolean bl;
        if (AirStuckModule.internalField0149.player == null || this.internalField0276) {
            return;
        }
        if (this.internalField1066.isSelected()) {
            if (!(sendPacketEvent.getPacket() instanceof PlayerMoveC2SPacket)) {
                this.internalField0877.add(sendPacketEvent.getPacket());
            }
            sendPacketEvent.cancel();
            return;
        }
        if (this.internalField0238.isSelected()) {
            if (sendPacketEvent.getPacket() instanceof PlayerMoveC2SPacket) {
                sendPacketEvent.cancel();
            }
            return;
        }
        if (!this.internalField0277) {
            return;
        }
        if (sendPacketEvent.getPacket() instanceof PlayerMoveC2SPacket) {
            sendPacketEvent.cancel();
            return;
        }
        Packet<?> packet = sendPacketEvent.getPacket();
        boolean bl2 = bl = packet instanceof PlayerActionC2SPacket && (playerActionC2SPacket = (PlayerActionC2SPacket)packet).getAction() == PlayerActionC2SPacket.Action.RELEASE_USE_ITEM;
        if (sendPacketEvent.getPacket() instanceof PlayerInteractItemC2SPacket || bl) {
            this.internalField0276 = true;
            try {
                AirStuckModule.internalField0149.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.LookAndOnGround(AirStuckModule.internalField0149.player.getYaw(), AirStuckModule.internalField0149.player.getPitch(), false, AirStuckModule.internalField0149.player.horizontalCollision));
            }
            finally {
                this.internalField0276 = false;
            }
        }
    };
    private final EventListener<WorldChangeEvent> internalField1029 = worldChangeEvent -> {
        this.internalField0877.clear();
        this.internalField1099 = false;
        this.disable();
    };

    public AirStuckModule() {
        this.internalMethod09751();
    }

    private void internalMethod09751() {
        this.internalField0668 = new ModeSetting(this, "modules.settings.air_stuck.mode");
        this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.air_stuck.mode.normal");
        this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.air_stuck.mode.reallyworld");
        this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.air_stuck.mode.funtime");
        this.internalField0650 = new BooleanSetting(this, "modules.settings.air_stuck.change_aura_distance");
        this.internalField0383 = new SliderSetting((SettingOwner)this, "modules.settings.air_stuck.aura_distance", () -> !this.internalField0650.internalMethod04496()).internalMethod05900(2.0f).internalMethod02732(6.0f).internalMethod08673(0.1f).internalMethod08074(3.0f).internalMethod05660(f -> " %s".formatted(LanguageManager.internalMethod07214("block")) + TextUtils.internalMethod05759(f));
        this.internalField0651 = new BooleanSetting((SettingOwner)this, "modules.settings.air_stuck.catch_moment", () -> !this.internalField0237.isSelected()).internalMethod06630();
        this.internalField1261 = new BooleanSetting((SettingOwner)this, "modules.settings.air_stuck.fall_check", () -> !this.internalField1066.isSelected()).internalMethod06630();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.internalField0277 = false;
        this.internalField0283 = Vec3d.ZERO;
        this.internalField0194 = Double.NaN;
        this.internalField0282 = Vec3d.ZERO;
        this.internalField0877.clear();
        this.internalField0519.internalMethod00701();
        this.internalField1099 = false;
        if (AirStuckModule.internalField0149.player == null) {
            return;
        }
        if (this.internalField1066.isSelected()) {
            if (this.internalField1261.internalMethod04496() && AirStuckModule.internalField0149.player.isOnGround()) {
                ClientMessages.internalMethod09025((Text)Text.literal((String)LanguageManager.internalMethod07214("modules.messages.air_stuck.air_required")));
                this.disable();
                return;
            }
            this.internalField0282 = AirStuckModule.internalField0149.player.getVelocity();
            AirStuckModule.internalField0149.player.setNoGravity(true);
            this.internalField1099 = true;
            return;
        }
        if (this.internalField0238.isSelected()) {
            this.internalField0282 = AirStuckModule.internalField0149.player.getVelocity();
            AirStuckModule.internalField0149.player.setNoGravity(true);
            return;
        }
        if (this.internalField0651.internalMethod04496()) {
            this.internalField0194 = AirStuckModule.internalField0149.player.isOnGround() ? Double.NaN : AirStuckModule.internalField0149.player.getY();
            return;
        }
        this.internalField0194 = AirStuckModule.internalField0149.player.getY();
        this.internalMethod09752();
    }

    @Override
    public void onDisable() {
        if (AirStuckModule.internalField0149.player != null) {
            Packet<?> packet;
            if (this.internalField1099) {
                AirStuckModule.internalField0149.player.setVelocity(this.internalField0282);
            } else if (this.internalField0238.isSelected() && !AirStuckModule.internalField0149.player.isOnGround()) {
                AirStuckModule.internalField0149.player.setVelocity(this.internalField0282);
            }
            if (this.internalField1099 || this.internalField0238.isSelected()) {
                AirStuckModule.internalField0149.player.setNoGravity(false);
            }
            while ((packet = this.internalField0877.poll()) != null) {
                if (internalField0149.getNetworkHandler() == null) continue;
                internalField0149.getNetworkHandler().sendPacket(packet);
            }
        } else {
            this.internalField0877.clear();
        }
        this.internalField0277 = false;
        this.internalField0194 = Double.NaN;
        this.internalField0283 = Vec3d.ZERO;
        this.internalField0282 = Vec3d.ZERO;
        this.internalField1099 = false;
        super.onDisable();
    }

    public float internalMethod02232() {
        return this.isEnabled() && this.internalField0650.internalMethod04496() ? this.internalField0383.internalMethod08576() : 0.0f;
    }

    @Override
    public void internalMethod08229() {
        if (AirStuckModule.internalField0149.player == null) {
            this.internalField0277 = false;
            this.internalField0194 = Double.NaN;
            super.internalMethod08229();
            return;
        }
        if (AirStuckModule.internalField0149.player.isDead()) {
            this.disable();
            return;
        }
        if (this.internalField1066.isSelected()) {
            if (this.internalField0519.internalMethod02365(28000L)) {
                ClientMessages.internalMethod09025((Text)Text.literal((String)LanguageManager.internalMethod07214("modules.messages.air_stuck.timeout")));
                this.disable();
                return;
            }
            AirStuckModule.internalField0149.player.setVelocity(Vec3d.ZERO);
            AirStuckModule.internalField0149.player.setNoGravity(true);
            AirStuckModule.internalField0149.player.fallDistance = 0.0f;
            super.internalMethod08229();
            return;
        }
        if (this.internalField0238.isSelected()) {
            AirStuckModule.internalField0149.player.setVelocity(Vec3d.ZERO);
            AirStuckModule.internalField0149.player.setNoGravity(true);
            AirStuckModule.internalField0149.player.fallDistance = 0.0f;
            super.internalMethod08229();
            return;
        }
        if (this.internalField0651.internalMethod04496()) {
            double d = AirStuckModule.internalField0149.player.getY();
            if (AirStuckModule.internalField0149.player.isOnGround()) {
                this.internalField0194 = Double.NaN;
                this.internalField0277 = false;
            } else if (!this.internalField0277) {
                if (Double.isNaN(this.internalField0194)) {
                    this.internalField0194 = d;
                } else if (d > this.internalField0194) {
                    this.internalField0194 = d;
                } else if (d < this.internalField0194) {
                    this.internalMethod09752();
                }
            }
        }
        if (this.internalField0277) {
            AirStuckModule.internalField0149.player.setVelocity(Vec3d.ZERO);
            AirStuckModule.internalField0149.player.setPosition(this.internalField0283);
            AirStuckModule.internalField0149.player.fallDistance = 0.0f;
            if (AirStuckModule.internalField0149.player.input != null) {
                rockstar.client.compat.InputCompat.setForward(AirStuckModule.internalField0149.player.input, 0.0f);
                rockstar.client.compat.InputCompat.setSideways(AirStuckModule.internalField0149.player.input, 0.0f);
            }
        }
        super.internalMethod08229();
    }

    private void internalMethod09752() {
        if (AirStuckModule.internalField0149.player == null) {
            return;
        }
        this.internalField0277 = true;
        this.internalField0283 = AirStuckModule.internalField0149.player.getEntityPos();
        this.internalField0194 = AirStuckModule.internalField0149.player.getY();
    }
}
