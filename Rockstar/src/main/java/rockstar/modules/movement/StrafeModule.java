package rockstar.modules.movement;






import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;

import net.minecraft.util.math.MathHelper;
import pyrock.events.player.ClientPlayerTickEvent;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.util.GameUtils;
import rockstar.client.internal.game.JumpVelocityCalculator;
import rockstar.client.module.Module;

@ModuleInfo(name="Strafe", category=ModuleCategory.MOVEMENT, internalMethod09633="modules.descriptions.strafe")
public class StrafeModule
extends Module {
    private BooleanSetting internalField0650;
    private BooleanSetting internalField0651;
    private SliderSetting internalField0383;
    private final JumpVelocityCalculator internalField0400 = new JumpVelocityCalculator();
    private final EventListener<ClientPlayerTickEvent> internalField0157 = clientPlayerTickEvent -> {
        double d;
        if (StrafeModule.internalField0149.player == null || StrafeModule.internalField0149.world == null) {
            return;
        }
        if (!GameUtils.internalMethod00469() || StrafeModule.internalField0149.player.isGliding() || StrafeModule.internalField0149.player.isClimbing() || StrafeModule.internalField0149.player.isTouchingWater()) {
            this.internalField0400.internalMethod00917(0.0);
            return;
        }
        if (this.internalField0650.internalMethod04496() && StrafeModule.internalField0149.player.isOnGround() && !StrafeModule.internalField0149.options.jumpKey.isPressed()) {
            StrafeModule.internalField0149.player.jump();
        }
        if ((d = this.internalField0400.internalMethod01329(this.internalField0651.internalMethod04496(), StrafeModule.internalField0149.player.hurtTime > 0, this.internalField0650.internalMethod04496(), this.internalField0383.internalMethod08576())) <= 0.0) {
            return;
        }
        this.internalMethod04759(d);
        double d2 = Math.hypot(StrafeModule.internalField0149.player.getVelocity().x, StrafeModule.internalField0149.player.getVelocity().z);
        this.internalField0400.internalMethod00917(d2);
    };

    public StrafeModule() {
        this.internalMethod09660();
    }

    private void internalMethod09660() {
        this.internalField0650 = new BooleanSetting(this, "modules.settings.strafe.auto_jump");
        this.internalField0651 = new BooleanSetting(this, "modules.settings.strafe.damage_boost");
        this.internalField0383 = new SliderSetting((SettingOwner)this, "modules.settings.strafe.damage_speed", () -> !this.internalField0651.internalMethod04496()).internalMethod05900(0.05f).internalMethod02732(0.8f).internalMethod08673(0.01f).internalMethod08074(0.4f);
    }

    @Override
    public final void onDisable() {
        this.internalField0400.internalMethod04901();
        super.onDisable();
    }

    private void internalMethod04759(double d) {
        float f = rockstar.client.compat.InputCompat.forward(StrafeModule.internalField0149.player.input);
        float f2 = rockstar.client.compat.InputCompat.sideways(StrafeModule.internalField0149.player.input);
        float f3 = StrafeModule.internalField0149.player.getYaw();
        if (f == 0.0f && f2 == 0.0f) {
            return;
        }
        if (f != 0.0f) {
            if (f2 > 0.0f) {
                f3 += f > 0.0f ? -45.0f : 45.0f;
            } else if (f2 < 0.0f) {
                f3 += f > 0.0f ? 45.0f : -45.0f;
            }
            f2 = 0.0f;
            f = MathHelper.clamp((float)(f > 0.0f ? 1.0f : -1.0f), (float)-1.0f, (float)1.0f);
        }
        f2 = MathHelper.clamp((float)f2, (float)-1.0f, (float)1.0f);
        double d2 = Math.toRadians(f3 + 90.0f);
        double d3 = Math.sin(d2);
        double d4 = Math.cos(d2);
        double d5 = (double)f * d * d4 + (double)f2 * d * d3;
        double d6 = (double)f * d * d3 - (double)f2 * d * d4;
        StrafeModule.internalField0149.player.setVelocity(d5, StrafeModule.internalField0149.player.getVelocity().y, d6);
    }
}
