package rockstar.modules.movement;





import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.*;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.world.Difficulty;
import pyrock.events.game.WorldChangeEvent;
import pyrock.events.network.SendPacketEvent;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.ModeSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.util.GameUtils;
import rockstar.client.module.Module;

@ModuleInfo(name="Timer", category=ModuleCategory.MOVEMENT, internalMethod09633="modules.descriptions.timer")
public class TimerModule
extends Module {
    private ModeSetting internalField0668;
    private ModeSetting.InternalType0088 internalField0237;
    private ModeSetting.InternalType0088 internalField0238;
    private SliderSetting internalField0383;
    private BooleanSetting internalField0650;
    private float internalField0205 = 100.0f;
    private long internalField0229;
    private final EventListener<SendPacketEvent> internalField0157 = sendPacketEvent -> {
        if (this.internalField0650.internalMethod04496() && this.internalField0237.isSelected() && sendPacketEvent.getPacket() instanceof PlayerMoveC2SPacket) {
            if (System.currentTimeMillis() - this.internalField0229 < 1000L) {
                float f = (float)(0.05 - (double)((float)(System.currentTimeMillis() - this.internalField0229) / 1000.0f)) * 400.0f;
                this.internalField0205 -= Math.max(0.0f, f);
            }
            if (GameUtils.internalMethod00469()) {
                this.internalField0205 += 0.5f;
            }
            this.internalField0205 = Math.max(0.0f, Math.min(100.0f, this.internalField0205));
            this.internalField0229 = System.currentTimeMillis();
        }
    };
    private final EventListener<WorldChangeEvent> internalField0158 = worldChangeEvent -> {
        if (this.internalField0650.internalMethod04496() && this.internalField0238.isSelected()) {
            this.internalField0205 += 7.0f;
        }
    };

    public TimerModule() {
        this.internalMethod09301();
    }

    private void internalMethod09301() {
        this.internalField0668 = new ModeSetting(this, "\u0420\u0435\u0436\u0438\u043c");
        this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "\u041e\u0431\u044b\u0447\u043d\u044b\u0439");
        this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "VonTam");
        this.internalField0383 = new SliderSetting((SettingOwner)this, "modules.settings.timer.speed", this.internalField0238::isSelected).internalMethod08673(0.1f).internalMethod05900(0.1f).internalMethod02732(15.0f).internalMethod08074(1.0f);
        this.internalField0650 = new BooleanSetting(this, "\u0423\u043c\u043d\u044b\u0439");
    }

    @Override
    public void internalMethod08229() {
        if (this.internalField0237.isSelected()) {
            if (this.internalField0650.internalMethod04496()) {
                GameUtils.internalMethod03366(this.internalField0205 > 10.0f ? this.internalField0383.internalMethod08576() : 1.0f);
            } else {
                GameUtils.internalMethod03366(this.internalField0383.internalMethod08576());
            }
        } else {
            if (!this.internalField0650.internalMethod04496() || this.internalField0205 > 10.0f) {
                TimerModule.internalField0149.player.setVelocity(TimerModule.internalField0149.player.getVelocity().x * 1.05, TimerModule.internalField0149.player.getVelocity().y * (TimerModule.internalField0149.player.fallDistance > 0.0f ? 1.05 : 1.0), TimerModule.internalField0149.player.getVelocity().z * (double)1.05f);
            }
            if (TimerModule.internalField0149.world.getDifficulty() == Difficulty.EASY) {
                this.internalField0205 = 100.0f;
            }
            this.internalField0205 += 0.006f;
            this.internalField0205 -= 2.5f;
            this.internalField0205 = Math.clamp(this.internalField0205, 0.0f, 100.0f);
        }
        super.internalMethod08229();
    }

    @Override
    public void onDisable() {
        GameUtils.internalMethod00468();
        super.onDisable();
    }
}
