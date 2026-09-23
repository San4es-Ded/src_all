package rockstar.modules.player;









import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.animation.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.*;

import lombok.Generated;
import net.minecraft.client.option.Perspective;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pyrock.events.game.RotateCameraEvent;
import pyrock.events.network.SendPacketEvent;
import pyrock.events.player.InputEvent;
import pyrock.events.render.HudRenderEvent;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.util.GameUtils;
import rockstar.client.rotation.Rotation;
import rockstar.client.module.Module;
import rockstar.client.internal.rotation.NewtonCoreManager;

@ModuleInfo(name="Free Camera", category=ModuleCategory.PLAYER, internalMethod09633="modules.descriptions.free_camera")
public class FreeCameraModule
extends Module {
    private SliderSetting internalField0383;
    private BooleanSetting internalField0650;
    private BooleanSetting internalField0651;
    private BooleanSetting internalField1261;
    private double internalField0194;
    private double internalField0193;
    private double internalField1045;
    private Vec3d internalField0283 = Vec3d.ZERO;
    private Rotation internalField0118 = Rotation.internalField0118;
    Perspective internalField0798;
    private final AnimatedValue internalField0808 = new AnimatedValue(50L, Easing.internalField1627);
    private final AnimatedValue internalField0809 = new AnimatedValue(50L, Easing.internalField1627);
    private final AnimatedValue internalField1321 = new AnimatedValue(50L, Easing.internalField1627);
    private static final Easing internalField0812 = Easing.internalMethod05127(0.17, 0.85, 0.29, 0.99);
    private static final Easing internalField0811 = Easing.internalMethod05127(0.31, 0.87, 0.43, 0.94);
    private final AnimatedValue internalField1322 = new AnimatedValue(350L, internalField0812);
    private boolean internalField0277 = false;
    private Vec3d internalField0282 = Vec3d.ZERO;
    private Rotation internalField0119 = Rotation.internalField0118;
    private final EventListener<SendPacketEvent> internalField0157 = sendPacketEvent -> {
        if (this.internalField0651.internalMethod04496() && !this.internalMethod09500() && sendPacketEvent.getPacket() instanceof PlayerMoveC2SPacket) {
            sendPacketEvent.cancel();
        }
    };
    private final EventListener<HudRenderEvent> internalField0158 = hudRenderEvent -> {
        if (this.internalField0650.internalMethod04496()) {
            Vec3d vec3d = this.internalMethod04686();
            int n = (int)vec3d.x - (int)this.internalField0283.x;
            int n2 = (int)vec3d.y - (int)this.internalField0283.y;
            int n3 = (int)vec3d.z - (int)this.internalField0283.z;
            String string = "X: " + n + " Y: " + n2 + " Z: " + n3;
            SizedFont typedValue020 = Fonts.internalField0450.internalMethod01432(8.0f);
            hudRenderEvent.getContext().drawText(typedValue020, Text.of((String)string), internalField0389.internalMethod03585() / 2.0f - typedValue020.internalMethod00965(string) / 2.0f, internalField0389.internalMethod03589() / 2.0f - 20.0f);
        }
    };
    private final EventListener<RotateCameraEvent> internalField1028 = rotateCameraEvent -> {
        this.internalField0118.internalMethod03239(this.internalField0118.internalMethod00169() + rotateCameraEvent.getDeltaYaw());
        this.internalField0118.internalMethod03289(MathHelper.clamp((float)(this.internalField0118.internalMethod00171() + rotateCameraEvent.getDeltaPitch()), (float)-90.0f, (float)90.0f));
        rotateCameraEvent.cancel();
    };
    private final EventListener<InputEvent> internalField1029 = EventListener.internalMethod05136(150, inputEvent -> {
        float f = this.internalField0383.internalMethod08576();
        if (inputEvent.getForward() != 0.0f || inputEvent.getStrafe() != 0.0f) {
            double d = GameUtils.internalMethod01047(this.internalField0118.internalMethod00169() + 90.0f, inputEvent.getForward(), inputEvent.getStrafe());
            float f2 = (float)Math.cos(d);
            float f3 = (float)Math.sin(d);
            this.internalField0194 += (double)(f2 *= f);
            this.internalField1045 += (double)(f3 *= f);
        }
        if (inputEvent.isJump()) {
            this.internalField0193 += (double)this.internalField0383.internalMethod08576();
        } else if (inputEvent.isSneak()) {
            this.internalField0193 -= (double)this.internalField0383.internalMethod08576();
        }
        inputEvent.setForward(0.0f);
        inputEvent.setStrafe(0.0f);
        inputEvent.setJump(false);
        inputEvent.setSneak(false);
    });

    public FreeCameraModule() {
        this.internalMethod09313();
    }

    private void internalMethod09313() {
        this.internalField0383 = new SliderSetting(this, "modules.settings.free_cam.speed").internalMethod08074(1.0f).internalMethod02732(15.0f).internalMethod05900(0.1f).internalMethod08673(0.1f).internalMethod08074(3.0f);
        this.internalField0650 = new BooleanSetting(this, "modules.settings.free_cam.display_coords");
        this.internalField0651 = new BooleanSetting(this, "modules.settings.free_cam.freeze").internalMethod06630();
        this.internalField1261 = new BooleanSetting(this, "modules.settings.free_cam.animation").internalMethod06630();
    }

    public Vec3d internalMethod04686() {
        return new Vec3d((double)this.internalField0808.internalMethod07059((float)this.internalField0194), (double)this.internalField0809.internalMethod07059((float)this.internalField0193), (double)this.internalField1321.internalMethod07059((float)this.internalField1045));
    }

    public boolean internalMethod09312() {
        return this.isEnabled() || this.internalField0277;
    }

    public float internalMethod00300() {
        return MathHelper.clamp((float)this.internalField1322.internalMethod02881(), (float)0.0f, (float)1.0f);
    }

    public boolean internalMethod09314() {
        return this.internalMethod09312() && this.internalField1322.internalMethod02881() < 1.0f;
    }

    private Vec3d internalMethod04712(float f) {
        if (FreeCameraModule.internalField0149.player == null) {
            return Vec3d.ZERO;
        }
        return FreeCameraModule.internalField0149.player.getCameraPosVec(f);
    }

    private static float internalMethod05821(float f, float f2, float f3) {
        float f4 = MathHelper.wrapDegrees((float)(f3 - f2));
        return f2 + f4 * f;
    }

    public void internalMethod09311() {
        if (this.internalField0277 && this.internalField1322.internalMethod02884() && this.internalField1322.internalMethod02881() <= 0.001f) {
            this.internalField0277 = false;
            if (this.internalField0798 != null) {
                FreeCameraModule.internalField0149.options.setPerspective(this.internalField0798);
                this.internalField0798 = null;
            }
        }
    }

    public Vec3d internalMethod05835(float f) {
        Vec3d vec3d = this.internalMethod04712(f);
        Vec3d vec3d2 = new Vec3d((double)this.internalField0808.internalMethod07059((float)this.internalField0194), (double)this.internalField0809.internalMethod07059((float)this.internalField0193), (double)this.internalField1321.internalMethod07059((float)this.internalField1045));
        if (this.internalField0277) {
            float f2 = this.internalField1322.internalMethod07059(0.0f);
            return this.internalField0282.lerp(vec3d, (double)(1.0f - f2));
        }
        float f3 = this.internalField1322.internalMethod07059(1.0f);
        return vec3d.lerp(vec3d2, (double)f3);
    }

    public Rotation internalMethod02039(float f) {
        if (FreeCameraModule.internalField0149.player == null) {
            return this.internalField0118;
        }
        float f2 = MathHelper.lerp((float)f, (float)FreeCameraModule.internalField0149.player.lastYaw, (float)FreeCameraModule.internalField0149.player.getYaw());
        float f3 = MathHelper.lerp((float)f, (float)FreeCameraModule.internalField0149.player.lastPitch, (float)FreeCameraModule.internalField0149.player.getPitch());
        float f4 = this.internalField1322.internalMethod02881();
        Rotation typedValue266 = this.internalField0277 ? this.internalField0119 : new Rotation(f2, f3);
        Rotation typedValue267 = this.internalField0277 ? new Rotation(f2, f3) : this.internalField0118;
        float f5 = this.internalField0277 ? 1.0f - f4 : f4;
        float f6 = FreeCameraModule.internalMethod05821(f5, typedValue266.internalMethod00169(), typedValue267.internalMethod00169());
        float f7 = MathHelper.lerp((float)f5, (float)typedValue266.internalMethod00171(), (float)typedValue267.internalMethod00171());
        return new Rotation(f6, f7);
    }

    private boolean internalMethod09500() {
        return NewtonCoreManager.internalMethod00010() && NewtonCoreManager.internalMethod00114().internalMethod06401().internalMethod03477();
    }

    @Override
    public final void internalMethod08229() {
        FreeCameraModule.internalField0149.options.setPerspective(Perspective.THIRD_PERSON_BACK);
        if (this.internalMethod09500()) {
            this.internalField0283 = FreeCameraModule.internalField0149.player.getEntityPos();
        } else if (this.internalField0651.internalMethod04496()) {
            FreeCameraModule.internalField0149.player.setVelocity(Vec3d.ZERO);
            FreeCameraModule.internalField0149.player.setPosition(this.internalField0283);
            if (FreeCameraModule.internalField0149.player.input != null) {
                rockstar.client.compat.InputCompat.setForward(FreeCameraModule.internalField0149.player.input, 0.0f);
                rockstar.client.compat.InputCompat.setSideways(FreeCameraModule.internalField0149.player.input, 0.0f);
            }
        }
        super.internalMethod08229();
    }

    @Override
    public final void onEnable() {
        this.internalField0194 = FreeCameraModule.internalField0149.player.getX();
        this.internalField0193 = FreeCameraModule.internalField0149.player.getEyeY();
        this.internalField1045 = FreeCameraModule.internalField0149.player.getZ();
        this.internalField0283 = FreeCameraModule.internalField0149.player.getEntityPos();
        this.internalField0808.internalMethod07060((float)this.internalField0194);
        this.internalField0809.internalMethod07060((float)this.internalField0193);
        this.internalField1321.internalMethod07060((float)this.internalField1045);
        Rotation typedValue266 = RockstarClient.getInstance().internalMethod02368().internalMethod07496();
        this.internalField0118 = new Rotation(typedValue266.internalMethod00169(), MathHelper.clamp((float)typedValue266.internalMethod00171(), (float)-90.0f, (float)90.0f));
        if (!this.internalField0277) {
            this.internalField0798 = FreeCameraModule.internalField0149.options.getPerspective();
            if (this.internalField0798 == null) {
                this.internalField0798 = Perspective.FIRST_PERSON;
            }
        }
        this.internalField0277 = false;
        if (this.internalField1261.internalMethod04496()) {
            this.internalField1322.internalMethod06645(internalField0812);
            this.internalField1322.internalMethod07061(350L);
            if (this.internalField1322.internalMethod02881() <= 0.0f) {
                this.internalField1322.internalMethod07060(0.0f);
            }
        } else {
            this.internalField1322.internalMethod07060(1.0f);
        }
    }

    @Override
    public final void onDisable() {
        if (!this.internalField1261.internalMethod04496()) {
            this.internalField1322.internalMethod07060(0.0f);
            this.internalField0277 = false;
            if (this.internalField0798 != null) {
                FreeCameraModule.internalField0149.options.setPerspective(this.internalField0798);
                this.internalField0798 = null;
            }
            return;
        }
        this.internalField0277 = true;
        this.internalField1322.internalMethod06645(internalField0811);
        this.internalField0282 = new Vec3d((double)this.internalField0808.internalMethod02881(), (double)this.internalField0809.internalMethod02881(), (double)this.internalField1321.internalMethod02881());
        this.internalField0119 = new Rotation(this.internalField0118.internalMethod00169(), this.internalField0118.internalMethod00171());
        double d = this.internalField0282.distanceTo(this.internalMethod04712(1.0f));
        long l = (long)MathHelper.clamp((double)(150.0 + d * 25.0), (double)180.0, (double)700.0);
        this.internalField1322.internalMethod07061(l);
    }

    @Generated
    public Vec3d internalMethod01774() {
        return this.internalField0283;
    }

    @Generated
    public Rotation internalMethod04854() {
        return this.internalField0118;
    }
}
