package rockstar.modules.visual;












import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.asset.*;
import rockstar.client.animation.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.render.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;

import java.util.HashSet;
import java.util.Set;
import lombok.Generated;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.LightType;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.biome.Biome;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.internal.script.AccentTimeShader;
import rockstar.client.internal.render.WetWorldRenderer;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.ColorSetting;
import rockstar.client.setting.SectionSetting;
import rockstar.client.setting.ModeSetting;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.ui.ThemeColors;
import rockstar.client.util.GameUtils;
import rockstar.client.render.ShaderPair;
import rockstar.client.render.RenderPipeline;
import rockstar.client.internal.game.DynamicLightEngine;
import rockstar.client.internal.game.RainShaderRenderer;
import rockstar.client.internal.script.AmbienceSkyRenderer;
import rockstar.client.module.Module;
import rockstar.client.asset.AssetTextureManager;

@ModuleInfo(name="Ambience", category=ModuleCategory.VISUALS, internalMethod08049=true, internalMethod09633="modules.descriptions.ambience")
public class AmbienceModule
extends Module {
    private MultiSelectSetting internalField0675;
    private MultiSelectSetting.InternalType0091 internalField0245;
    private MultiSelectSetting.InternalType0091 internalField0244;
    private MultiSelectSetting.InternalType0091 internalField1075;
    private BooleanSetting internalField1261;
    private ColorSetting internalField0665;
    private ColorSetting internalField0664;
    private ColorSetting internalField1268;
    private ModeSetting internalField0668;
    private ModeSetting.InternalType0088 internalField0237;
    private ModeSetting.InternalType0088 internalField0238;
    private ModeSetting.InternalType0088 internalField1066;
    private ModeSetting.InternalType0088 internalField1067;
    private ModeSetting.InternalType0088 internalField1068;
    private ModeSetting.InternalType0088 internalField1065;
    private BooleanSetting internalField1263;
    private ModeSetting internalField0669;
    private ModeSetting.InternalType0088 internalField1480;
    private ModeSetting.InternalType0088 internalField1481;
    private ModeSetting.InternalType0088 internalField1483;
    private ModeSetting.InternalType0088 internalField1485;
    private SliderSetting internalField0383;
    private BooleanSetting internalField1262;
    private SliderSetting internalField0382;
    private SliderSetting internalField1142;
    private SliderSetting internalField1140;
    private SliderSetting internalField1141;
    private BooleanSetting internalField1264;
    private ModeSetting internalField1272;
    private ModeSetting.InternalType0088 internalField1484;
    private ModeSetting.InternalType0088 internalField1482;
    private ModeSetting.InternalType0088 internalField1479;
    private ColorSetting internalField1267;
    private SliderSetting internalField1143;
    private SliderSetting internalField1529;
    private SliderSetting internalField1534;
    private SliderSetting internalField1535;
    private ModeSetting internalField1269;
    private ModeSetting.InternalType0088 internalField1478;
    private ModeSetting.InternalType0088 internalField1749;
    private BooleanSetting internalField1587;
    private SliderSetting internalField1536;
    private SliderSetting internalField1533;
    private ModeSetting internalField1270;
    private ModeSetting.InternalType0088 internalField1761;
    private ModeSetting.InternalType0088 internalField1751;
    private ModeSetting.InternalType0088 internalField1752;
    private BooleanSetting internalField1590;
    private ColorSetting internalField1266;
    private ModeSetting internalField1271;
    private ModeSetting.InternalType0088 internalField1753;
    private ModeSetting.InternalType0088 internalField1754;
    private ModeSetting.InternalType0088 internalField1755;
    private ModeSetting.InternalType0088 internalField1750;
    private SliderSetting internalField1532;
    private SliderSetting internalField1531;
    private BooleanSetting internalField1594;
    private SliderSetting internalField1530;
    public BooleanSetting internalField0650;
    public BooleanSetting internalField0651;
    private ModeSetting internalField1596;
    private ModeSetting.InternalType0088 internalField1756;
    private ModeSetting.InternalType0088 internalField1757;
    private ModeSetting.InternalType0088 internalField1758;
    private SliderSetting internalField1797;
    private SliderSetting internalField1798;
    private BooleanSetting internalField1588;
    private BooleanSetting internalField1589;
    private ColorSetting internalField1265;
    private SliderSetting internalField1796;
    private static final float internalField0205 = 0.35f;
    private static final float internalField0206 = 0.45f;
    private static final float internalField1048 = 24.0f;
    private final WetWorldRenderer.InternalType0469 internalField0827 = new WetWorldRenderer.InternalType0469();
    private final RainShaderRenderer internalField0184 = new RainShaderRenderer();
    private final AmbienceSkyRenderer internalField0196 = new AmbienceSkyRenderer();
    private long internalField0229;
    private boolean internalField0277;
    private BlockPos internalField0352;
    private float internalField1047 = -1.0f;
    private int internalField0227 = -1;
    private boolean internalField0276;
    private final AnimatedValue internalField0808 = new AnimatedValue(450L, Easing.internalField1818);
    private final EventListener<ReceivePacketEvent> internalField0157 = receivePacketEvent -> {
        if (receivePacketEvent.getPacket() instanceof WorldTimeUpdateS2CPacket && this.internalField1594.internalMethod04496()) {
            receivePacketEvent.cancel();
        }
    };
    private final EventListener<Render3DEvent> internalField0158 = render3DEvent -> {
        if (AmbienceModule.internalField0149.world == null || AmbienceModule.internalField0149.player == null) {
            this.internalField0277 = false;
            this.internalMethod09399();
            return;
        }
        this.internalMethod08181(this.internalMethod10071());
        Camera camera = render3DEvent.getCamera();
        if (camera == null || !camera.isReady()) {
            return;
        }
        if (camera.getSubmersionType() != CameraSubmersionType.NONE) {
            return;
        }
        if (this.internalField1262.internalMethod04496()) {
            this.internalField0184.internalMethod05726(camera, render3DEvent.getPositionMatrix(), render3DEvent.getProjectionMatrix(), this.internalField0382.internalMethod08576() / 100.0f, this.internalField1142.internalMethod08576() / 100.0f, this.internalField1140.internalMethod08576() / 100.0f, this.internalField1141.internalMethod08576() / 100.0f);
        }
        if (this.internalField1264.internalMethod04496()) {
            this.internalField0196.internalMethod04690(camera, render3DEvent.getPositionMatrix(), render3DEvent.getProjectionMatrix(), this.internalMethod06032(camera.getCameraPos()), this.internalField1143.internalMethod08576() / 100.0f, this.internalField1529.internalMethod08576() / 100.0f, this.internalField1534.internalMethod08576(), this.internalField1535.internalMethod08576(), this.internalField1749.isSelected());
        }
    };

    public AmbienceModule() {
        this.internalMethod09242();
    }

    private void internalMethod09242() {
        this.internalField1594 = new BooleanSetting(this, "modules.settings.ambience.custom_time").internalMethod06630();
        this.internalField1530 = new SliderSetting((SettingOwner)this, "modules.settings.ambience.time", () -> !this.internalField1594.internalMethod04496()).internalMethod08673(500.0f).internalMethod05900(0.0f).internalMethod02732(24000.0f).internalMethod08074(17000.0f);
        this.internalField0651 = new BooleanSetting((SettingOwner)this, "modules.settings.ambience.bright", () -> this.internalField0650.internalMethod04496()).internalMethod06630();
        this.internalField1596 = new ModeSetting((SettingOwner)this, "modules.settings.ambience.mode", () -> !this.internalMethod09243());
        this.internalField1756 = new ModeSetting.InternalType0088(this.internalField1596, "modules.settings.ambience.mode.gamma");
        this.internalField1757 = new ModeSetting.InternalType0088(this.internalField1596, "modules.settings.ambience.mode.effect");
        this.internalField1758 = new ModeSetting.InternalType0088(this.internalField1596, "modules.settings.ambience.mode.dynamic").select();
        this.internalField1797 = new SliderSetting((SettingOwner)this, "modules.settings.ambience.dynamic.radius", () -> !this.internalMethod09243() || !this.internalField1758.isSelected()).internalMethod05900(4.0f).internalMethod02732(12.0f).internalMethod08673(0.5f).internalMethod08074(12.0f);
        this.internalField1798 = new SliderSetting((SettingOwner)this, "modules.settings.ambience.dynamic.light", () -> !this.internalMethod09243() || !this.internalField1758.isSelected()).internalMethod05900(8.0f).internalMethod02732(15.0f).internalMethod08673(1.0f).internalMethod08074(15.0f);
        this.internalField1588 = new BooleanSetting((SettingOwner)this, "modules.settings.ambience.dynamic.only_in_cave", () -> !this.internalMethod09243() || !this.internalField1758.isSelected()).internalMethod06630();
        new SectionSetting(this, "modules.settings.ambience.section.color").internalMethod00983().internalMethod04286(2);
        this.internalField0675 = new MultiSelectSetting(this, "modules.settings.ambience.change_color");
        this.internalField0245 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.ambience.change_color.sky").select();
        this.internalField0244 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.ambience.change_color.clouds").select();
        this.internalField1075 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.ambience.change_color.stars").select();
        this.internalField1261 = new BooleanSetting(this, "theme.sync").internalMethod06630();
        this.internalField0665 = new ColorSetting(this, "modules.settings.ambience.sky_color", () -> this.internalField1261.internalMethod04496() || !this.internalField0245.isSelected()).internalMethod04886(ThemeColors.internalMethod02531()).internalMethod05166(false);
        this.internalField0664 = new ColorSetting(this, "modules.settings.ambience.cloud_color", () -> this.internalField1261.internalMethod04496() || !this.internalField0244.isSelected()).internalMethod04886(ThemeColors.internalMethod02531()).internalMethod05166(false);
        this.internalField1268 = new ColorSetting(this, "modules.settings.ambience.stars_color", () -> this.internalField1261.internalMethod04496() || !this.internalField1075.isSelected()).internalMethod04886(ThemeColors.internalMethod02531());
        this.internalField0650 = new BooleanSetting(this, "modules.settings.ambience.night_mode");
        this.internalField1589 = new BooleanSetting((SettingOwner)this, "modules.settings.ambience.night_mode.sync", () -> !this.internalField0650.internalMethod04496()).internalMethod06630();
        this.internalField1265 = new ColorSetting(this, "modules.settings.ambience.night_mode.color", () -> !this.internalField0650.internalMethod04496() || this.internalField1589.internalMethod04496()).internalMethod04886(new ColorRGBA(80.0f, 120.0f, 220.0f, 255.0f)).internalMethod05166(false);
        this.internalField1796 = new SliderSetting((SettingOwner)this, "modules.settings.ambience.night_mode.strength", () -> !this.internalField0650.internalMethod04496()).internalMethod05900(0.0f).internalMethod02732(100.0f).internalMethod08673(1.0f).internalMethod08074(70.0f);
        this.internalField1590 = new BooleanSetting(this, "modules.settings.ambience.color_isolation");
        this.internalField1266 = new ColorSetting(this, "modules.settings.ambience.color_isolation.color", () -> !this.internalField1590.internalMethod04496()).internalMethod04886(new ColorRGBA(0.0f, 122.0f, 255.0f, 255.0f)).internalMethod05166(false);
        this.internalField1271 = new ModeSetting((SettingOwner)this, "modules.settings.ambience.color_isolation.mode", () -> !this.internalField1590.internalMethod04496());
        this.internalField1753 = new ModeSetting.InternalType0088(this.internalField1271, "modules.settings.ambience.color_isolation.mode.strict");
        this.internalField1754 = new ModeSetting.InternalType0088(this.internalField1271, "modules.settings.ambience.color_isolation.mode.balanced").select();
        this.internalField1755 = new ModeSetting.InternalType0088(this.internalField1271, "modules.settings.ambience.color_isolation.mode.loose");
        this.internalField1750 = new ModeSetting.InternalType0088(this.internalField1271, "modules.settings.ambience.color_isolation.mode.custom");
        this.internalField1532 = new SliderSetting((SettingOwner)this, "modules.settings.ambience.color_isolation.sensitivity", () -> !this.internalField1590.internalMethod04496() || !this.internalField1750.isSelected()).internalMethod05900(0.0f).internalMethod02732(100.0f).internalMethod08673(1.0f).internalMethod08074(50.0f);
        this.internalField1531 = new SliderSetting((SettingOwner)this, "modules.settings.ambience.color_isolation.background", () -> !this.internalField1590.internalMethod04496()).internalMethod05900(0.0f).internalMethod02732(100.0f).internalMethod08673(1.0f).internalMethod08074(0.0f);
        this.internalField0668 = new ModeSetting(this, "modules.settings.ambience.skybox");
        this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.ambience.skybox.default");
        this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.ambience.skybox.bright_clouds").select();
        this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.ambience.skybox.lake");
        this.internalField1067 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.ambience.skybox.cloud_space");
        this.internalField1068 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.ambience.skybox.clear_evening");
        this.internalField1065 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.ambience.skybox.underwater");
        this.internalField1263 = new BooleanSetting(this, "modules.settings.ambience.shader");
        this.internalField0669 = new ModeSetting((SettingOwner)this, "modules.settings.ambience.shader_type", () -> !this.internalField1263.internalMethod04496());
        this.internalField1480 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.ambience.shader_type.nebula").select();
        this.internalField1481 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.ambience.shader_type.caustic");
        this.internalField1483 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.ambience.shader_type.galaxy");
        this.internalField1485 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.ambience.shader_type.space");
        this.internalField0383 = new SliderSetting((SettingOwner)this, "modules.settings.ambience.shader_opacity", () -> !this.internalField1263.internalMethod04496()).internalMethod05900(0.0f).internalMethod02732(100.0f).internalMethod08673(5.0f).internalMethod08074(70.0f);
        new SectionSetting(this, "modules.settings.ambience.section.fog").internalMethod00983().internalMethod04286(2);
        this.internalField1264 = new BooleanSetting(this, "modules.settings.ambience.fog");
        this.internalField1272 = new ModeSetting((SettingOwner)this, "modules.settings.ambience.fog.color_mode", () -> !this.internalField1264.internalMethod04496());
        this.internalField1484 = new ModeSetting.InternalType0088(this.internalField1272, "modules.settings.ambience.fog.color_mode.biome").select();
        this.internalField1482 = new ModeSetting.InternalType0088(this.internalField1272, "modules.settings.ambience.fog.color_mode.theme");
        this.internalField1479 = new ModeSetting.InternalType0088(this.internalField1272, "modules.settings.ambience.fog.color_mode.custom");
        this.internalField1267 = new ColorSetting(this, "modules.settings.ambience.fog.color", () -> !this.internalField1264.internalMethod04496() || !this.internalField1479.isSelected()).internalMethod04886(new ColorRGBA(205.0f, 210.0f, 220.0f, 255.0f)).internalMethod05166(false);
        this.internalField1143 = new SliderSetting((SettingOwner)this, "modules.settings.ambience.fog.density", () -> !this.internalField1264.internalMethod04496()).internalMethod05900(10.0f).internalMethod02732(250.0f).internalMethod08673(5.0f).internalMethod08074(90.0f).internalMethod06240("%");
        this.internalField1529 = new SliderSetting((SettingOwner)this, "modules.settings.ambience.fog.coverage", () -> !this.internalField1264.internalMethod04496()).internalMethod05900(0.0f).internalMethod02732(100.0f).internalMethod08673(1.0f).internalMethod08074(55.0f).internalMethod06240("%");
        this.internalField1534 = new SliderSetting((SettingOwner)this, "modules.settings.ambience.fog.level", () -> !this.internalField1264.internalMethod04496()).internalMethod05900(-64.0f).internalMethod02732(320.0f).internalMethod08673(1.0f).internalMethod08074(66.0f);
        this.internalField1535 = new SliderSetting((SettingOwner)this, "modules.settings.ambience.fog.thickness", () -> !this.internalField1264.internalMethod04496()).internalMethod05900(2.0f).internalMethod02732(48.0f).internalMethod08673(1.0f).internalMethod08074(6.0f);
        this.internalField1269 = new ModeSetting((SettingOwner)this, "modules.settings.ambience.fog.quality", () -> !this.internalField1264.internalMethod04496());
        this.internalField1478 = new ModeSetting.InternalType0088(this.internalField1269, "modules.settings.ambience.fog.quality.low");
        this.internalField1749 = new ModeSetting.InternalType0088(this.internalField1269, "modules.settings.ambience.fog.quality.high").select();
        new SectionSetting(this, "modules.settings.ambience.section.wet_world").internalMethod00983().internalMethod04286(2);
        this.internalField1587 = new BooleanSetting(this, "modules.settings.ambience.wet_world");
        this.internalField1536 = new SliderSetting((SettingOwner)this, "modules.settings.ambience.wet_world.reflection", () -> !this.internalField1587.internalMethod04496()).internalMethod05900(0.0f).internalMethod02732(100.0f).internalMethod08673(5.0f).internalMethod08074(70.0f).internalMethod06240("%");
        this.internalField1533 = new SliderSetting((SettingOwner)this, "modules.settings.ambience.wet_world.amount", () -> !this.internalField1587.internalMethod04496()).internalMethod05900(0.0f).internalMethod02732(100.0f).internalMethod08673(5.0f).internalMethod08074(60.0f).internalMethod06240("%");
        this.internalField1270 = new ModeSetting((SettingOwner)this, "modules.settings.ambience.wet_world.quality", () -> !this.internalField1587.internalMethod04496());
        this.internalField1761 = new ModeSetting.InternalType0088(this.internalField1270, "modules.settings.ambience.wet_world.quality.low");
        this.internalField1751 = new ModeSetting.InternalType0088(this.internalField1270, "modules.settings.ambience.wet_world.quality.medium").select();
        this.internalField1752 = new ModeSetting.InternalType0088(this.internalField1270, "modules.settings.ambience.wet_world.quality.high");
        new SectionSetting(this, "modules.settings.ambience.section.rain").internalMethod00983().internalMethod04286(2);
        this.internalField1262 = new BooleanSetting(this, "modules.settings.ambience.rain");
        this.internalField0382 = new SliderSetting((SettingOwner)this, "modules.settings.ambience.rain.density", () -> !this.internalField1262.internalMethod04496()).internalMethod05900(10.0f).internalMethod02732(200.0f).internalMethod08673(5.0f).internalMethod08074(100.0f).internalMethod06240("%");
        this.internalField1142 = new SliderSetting((SettingOwner)this, "modules.settings.ambience.rain.opacity", () -> !this.internalField1262.internalMethod04496()).internalMethod05900(5.0f).internalMethod02732(100.0f).internalMethod08673(5.0f).internalMethod08074(45.0f).internalMethod06240("%");
        this.internalField1140 = new SliderSetting((SettingOwner)this, "modules.settings.ambience.rain.drops", () -> !this.internalField1262.internalMethod04496()).internalMethod05900(0.0f).internalMethod02732(100.0f).internalMethod08673(5.0f).internalMethod08074(60.0f).internalMethod06240("%");
        this.internalField1141 = new SliderSetting((SettingOwner)this, "modules.settings.ambience.rain.splashes", () -> !this.internalField1262.internalMethod04496()).internalMethod05900(0.0f).internalMethod02732(100.0f).internalMethod08673(5.0f).internalMethod08074(60.0f).internalMethod06240("%");
    }

    public boolean internalMethod09243() {
        return this.internalField0651.internalMethod04496() && !this.internalField0650.internalMethod04496();
    }

    public boolean internalMethod09245() {
        return this.isEnabled() && this.internalField0650.internalMethod04496();
    }

    public Vector3f internalMethod07168() {
        ColorRGBA colorRGBA = this.internalField1589.internalMethod04496() ? ThemeColors.internalMethod02531() : this.internalField1265.internalMethod05620();
        return new Vector3f(colorRGBA.getRed() / 255.0f, colorRGBA.getGreen() / 255.0f, colorRGBA.getBlue() / 255.0f);
    }

    public float internalMethod06258() {
        return this.internalField1796.internalMethod08576() / 100.0f;
    }

    private ColorRGBA internalMethod06032(Vec3d vec3d) {
        if (this.internalField1482.isSelected()) {
            return ThemeColors.internalMethod02531();
        }
        if (this.internalField1479.isSelected()) {
            return this.internalField1267.internalMethod05620();
        }
        int n = AmbienceModule.internalField0149.world.getEnvironmentAttributes().getAttributeValue(EnvironmentAttributes.FOG_COLOR_VISUAL, vec3d);
        return new ColorRGBA(this.internalMethod05650(n >> 16 & 0xFF), this.internalMethod05650(n >> 8 & 0xFF), this.internalMethod05650(n & 0xFF), 255.0f);
    }

    private int internalMethod05650(int n) {
        return MathHelper.clamp((int)Math.round((float)n + (float)(255 - n) * 0.55f), (int)0, (int)255);
    }

    @Override
    public void internalMethod08229() {
        if (AmbienceModule.internalField0149.world == null) {
            this.internalField0277 = false;
            this.internalMethod09399();
            this.internalMethod09414();
            return;
        }
        if (this.internalField1594.internalMethod04496()) {
            AmbienceModule.internalField0149.world.getLevelProperties().setTimeOfDay((long)this.internalField1530.internalMethod08576());
        }
        if (this.internalField1757.isSelected() && this.internalMethod09243()) {
            this.internalMethod09412();
        } else {
            this.internalMethod09414();
        }
        super.internalMethod08229();
    }

    @Override
    public void onEnable() {
        if (!GameUtils.internalMethod00471() || AmbienceModule.internalField0149.world == null) {
            return;
        }
        this.internalField0229 = AmbienceModule.internalField0149.world.getTime();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.internalField0184.internalMethod01427();
        this.internalMethod09414();
        this.internalMethod09399();
        this.internalField0277 = false;
        if (!GameUtils.internalMethod00471() || AmbienceModule.internalField0149.world == null) {
            return;
        }
        AmbienceModule.internalField0149.world.getLevelProperties().setTimeOfDay(this.internalField0229);
        super.onDisable();
    }

    private boolean internalMethod10071() {
        return this.isEnabled() && this.internalMethod09243() && this.internalField1758.isSelected() && (!this.internalField1588.internalMethod04496() || this.internalMethod10127());
    }

    private void internalMethod08181(boolean bl) {
        float f = this.internalField0808.internalMethod07059(bl ? 1.0f : 0.0f);
        int n = MathHelper.clamp((int)Math.round(this.internalField1798.internalMethod08576() * f), (int)0, (int)15);
        if (n <= 0) {
            this.internalMethod09244();
            return;
        }
        BlockPos blockPos = BlockPos.ofFloored((Position)AmbienceModule.internalField0149.gameRenderer.getCamera().getCameraPos());
        float f2 = this.internalField1797.internalMethod08576();
        if (this.internalField0276 && blockPos.equals((Object)this.internalField0352) && Float.compare(f2, this.internalField1047) == 0 && n == this.internalField0227) {
            return;
        }
        BlockPos blockPos2 = this.internalField0352;
        int n2 = MathHelper.ceil((float)this.internalField1047);
        DynamicLightEngine.internalMethod03622((BlockRenderView)AmbienceModule.internalField0149.world, blockPos, f2, n);
        this.internalMethod06729(blockPos2, n2, blockPos, MathHelper.ceil((float)f2));
        this.internalField0352 = blockPos;
        this.internalField1047 = f2;
        this.internalField0227 = n;
        this.internalField0276 = true;
    }

    private boolean internalMethod10127() {
        if (AmbienceModule.internalField0149.player == null || AmbienceModule.internalField0149.world == null) {
            this.internalField0277 = false;
            return false;
        }
        BlockPos blockPos = BlockPos.ofFloored((Position)AmbienceModule.internalField0149.player.getEyePos());
        int n = AmbienceModule.internalField0149.world.getLightLevel(LightType.SKY, blockPos);
        if (AmbienceModule.internalField0149.world.isSkyVisible(blockPos) || n >= 8) {
            this.internalField0277 = false;
        } else if (n <= 4) {
            this.internalField0277 = true;
        }
        return this.internalField0277;
    }

    private void internalMethod09244() {
        if (!this.internalField0276) {
            return;
        }
        DynamicLightEngine.internalMethod00949();
        this.internalMethod06729(this.internalField0352, MathHelper.ceil((float)this.internalField1047), null, 0);
        this.internalMethod09401();
    }

    private void internalMethod09399() {
        this.internalField0808.internalMethod02883();
        if (!this.internalField0276) {
            DynamicLightEngine.internalMethod00949();
            return;
        }
        DynamicLightEngine.internalMethod00949();
        this.internalMethod06729(this.internalField0352, MathHelper.ceil((float)this.internalField1047), null, 0);
        this.internalMethod09401();
    }

    private void internalMethod09401() {
        this.internalField0352 = null;
        this.internalField1047 = -1.0f;
        this.internalField0227 = -1;
        this.internalField0276 = false;
    }

    private void internalMethod06729(BlockPos blockPos, int n, BlockPos blockPos2, int n2) {
        if (AmbienceModule.internalField0149.worldRenderer == null) {
            return;
        }
        HashSet<BlockPos> hashSet = new HashSet<BlockPos>();
        this.internalMethod03442(hashSet, blockPos, n);
        this.internalMethod03442(hashSet, blockPos2, n2);
        for (BlockPos blockPos3 : hashSet) {
            AmbienceModule.internalField0149.worldRenderer.scheduleChunkRender(blockPos3.getX(), blockPos3.getY(), blockPos3.getZ());
        }
    }

    private void internalMethod03442(Set<BlockPos> set, BlockPos blockPos, int n) {
        if (blockPos == null || n <= 0) {
            return;
        }
        int n2 = n + 1;
        int n3 = ChunkSectionPos.getSectionCoord((int)(blockPos.getX() - n2));
        int n4 = ChunkSectionPos.getSectionCoord((int)(blockPos.getY() - n2));
        int n5 = ChunkSectionPos.getSectionCoord((int)(blockPos.getZ() - n2));
        int n6 = ChunkSectionPos.getSectionCoord((int)(blockPos.getX() + n2));
        int n7 = ChunkSectionPos.getSectionCoord((int)(blockPos.getY() + n2));
        int n8 = ChunkSectionPos.getSectionCoord((int)(blockPos.getZ() + n2));
        for (int i = n3; i <= n6; ++i) {
            for (int j = n4; j <= n7; ++j) {
                for (int k = n5; k <= n8; ++k) {
                    int n9;
                    int n10;
                    int n11 = MathHelper.clamp((int)blockPos.getX(), (int)(i << 4), (int)((i << 4) + 15));
                    if (!(blockPos.getSquaredDistance((double)n11, (double)(n10 = MathHelper.clamp((int)blockPos.getY(), (int)(j << 4), (int)((j << 4) + 15))), (double)(n9 = MathHelper.clamp((int)blockPos.getZ(), (int)(k << 4), (int)((k << 4) + 15)))) <= (double)(n2 * n2))) continue;
                    set.add(new BlockPos(i, j, k));
                }
            }
        }
    }

    private void internalMethod09412() {
        if (AmbienceModule.internalField0149.player == null) {
            return;
        }
        StatusEffectInstance statusEffectInstance = AmbienceModule.internalField0149.player.getStatusEffect(StatusEffects.NIGHT_VISION);
        if (statusEffectInstance == null) {
            AmbienceModule.internalField0149.player.addStatusEffect(this.internalMethod06966());
            return;
        }
        if (this.internalMethod02680(statusEffectInstance) && statusEffectInstance.getDuration() <= 220) {
            AmbienceModule.internalField0149.player.addStatusEffect(this.internalMethod06966());
        }
    }

    private void internalMethod09414() {
        if (AmbienceModule.internalField0149.player == null) {
            return;
        }
        StatusEffectInstance statusEffectInstance = AmbienceModule.internalField0149.player.getStatusEffect(StatusEffects.NIGHT_VISION);
        if (this.internalMethod02680(statusEffectInstance)) {
            AmbienceModule.internalField0149.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
        }
    }

    private StatusEffectInstance internalMethod06966() {
        return GameUtils.internalMethod04749((RegistryEntry<StatusEffect>)StatusEffects.NIGHT_VISION, 400, 0);
    }

    private boolean internalMethod02680(StatusEffectInstance statusEffectInstance) {
        return statusEffectInstance != null && statusEffectInstance.getEffectType() == StatusEffects.NIGHT_VISION && statusEffectInstance.getAmplifier() == 0 && GameUtils.internalMethod06278(statusEffectInstance);
    }

    public boolean internalMethod09400() {
        return !this.internalField0668.internalMethod06103(this.internalField0237) && AssetTextureManager.internalMethod02970(this.internalMethod08421());
    }

    public boolean internalMethod09402() {
        return this.internalField1263.internalMethod04496();
    }

    public boolean internalMethod09413() {
        return this.isEnabled() && (this.internalMethod09400() || this.internalMethod09402());
    }

    public AccentTimeShader internalMethod01702() {
        if (this.internalField0669.internalMethod06103(this.internalField1481)) {
            return RenderPipeline.internalField1152;
        }
        return RenderPipeline.internalField0439;
    }

    public ShaderPair internalMethod03395() {
        if (this.internalField0669.internalMethod06103(this.internalField1483)) {
            return RenderPipeline.internalField0315;
        }
        if (this.internalField0669.internalMethod06103(this.internalField1485)) {
            return RenderPipeline.internalField0316;
        }
        return null;
    }

    public boolean internalMethod09415() {
        return this.isEnabled() && this.internalMethod10069();
    }

    public boolean internalMethod10068() {
        return this.isEnabled() && this.internalField1587.internalMethod04496();
    }

    public WetWorldRenderer.InternalType0469 internalMethod03372(Matrix4f matrix4f, Matrix4f matrix4f2, Camera camera, float f) {
        this.internalField0827.internalField0788.set((Matrix4fc)matrix4f2).mul((Matrix4fc)matrix4f);
        this.internalField0827.internalField0787.set((Matrix4fc)this.internalField0827.internalField0788).invert();
        Vec3d vec3d = camera.getCameraPos();
        this.internalField0827.internalField0205 = (float)vec3d.x;
        this.internalField0827.internalField0206 = (float)vec3d.y;
        this.internalField0827.internalField1048 = (float)vec3d.z;
        int n = camera.getEnvironmentAttributeInterpolator().get(EnvironmentAttributes.SKY_COLOR_VISUAL, f);
        this.internalField0827.internalField1047 = (float)(n >> 16 & 0xFF) / 255.0f;
        this.internalField0827.internalField1049 = (float)(n >> 8 & 0xFF) / 255.0f;
        this.internalField0827.internalField1046 = (float)(n & 0xFF) / 255.0f;
        boolean bl = AmbienceModule.internalField0149.world.getDimension().hasSkyLight();
        float f2 = camera.getEnvironmentAttributeInterpolator().get(EnvironmentAttributes.SUN_ANGLE_VISUAL, f) * ((float)Math.PI / 180.0F);
        this.internalField0827.internalField1456 = -MathHelper.sin((float)f2);
        this.internalField0827.internalField1457 = MathHelper.cos((float)f2);
        this.internalField0827.internalField1458 = 0.0f;
        this.internalField0827.internalField1459 = this.internalField1536.internalMethod08576() / 100.0f;
        this.internalField0827.internalField1460 = this.internalField1533.internalMethod08576() / 100.0f;
        this.internalField0827.internalField1461 = 0.35f;
        this.internalField0827.internalField1462 = bl ? 0.45f : 0.0f;
        this.internalField0827.internalField1731 = 24.0f;
        this.internalField0827.internalField1723 = 0.98f;
        this.internalField0827.internalField1727 = 1.0f;
        this.internalField0827.internalField1728 = (float)(System.currentTimeMillis() % 3600000L) / 1000.0f;
        this.internalField0827.internalField1455 = this.internalField1761.isSelected() ? 14.0f : (this.internalField1752.isSelected() ? 40.0f : 24.0f);
        return this.internalField0827;
    }

    public boolean internalMethod10069() {
        return this.internalField1590.internalMethod04496();
    }

    public float internalMethod06261() {
        return this.internalField1590.internalMethod04496() ? 1.0f : 0.0f;
    }

    public Vector3f internalMethod05730() {
        ColorRGBA colorRGBA = this.internalField1266.internalMethod05620();
        return new Vector3f(colorRGBA.getRed() / 255.0f, colorRGBA.getGreen() / 255.0f, colorRGBA.getBlue() / 255.0f);
    }

    private float internalMethod09241() {
        if (this.internalField1750.isSelected()) {
            return this.internalField1532.internalMethod08576() / 100.0f;
        }
        if (this.internalField1753.isSelected()) {
            return 0.15f;
        }
        if (this.internalField1755.isSelected()) {
            return 0.85f;
        }
        return 0.5f;
    }

    public float internalMethod08781() {
        return (8.0f + this.internalMethod09241() * 47.0f) / 360.0f;
    }

    public float internalMethod08783() {
        return 0.55f - this.internalMethod09241() * 0.43f;
    }

    public float internalMethod08789() {
        return 0.45f - this.internalMethod09241() * 0.37f;
    }

    public float internalMethod08790() {
        return this.internalField1531.internalMethod08576() / 100.0f;
    }

    public Identifier internalMethod04216() {
        return AssetTextureManager.internalMethod02604(this.internalMethod08421());
    }

    private String internalMethod08421() {
        return "sky/" + Math.max(this.internalField0668.internalMethod06723().indexOf(this.internalField0668.internalMethod07418()), 1);
    }

    public float internalMethod09226() {
        return this.internalField0383.internalMethod08576() / 100.0f;
    }

    @Generated
    public MultiSelectSetting internalMethod00853() {
        return this.internalField0675;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod06409() {
        return this.internalField0245;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod06628() {
        return this.internalField0244;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod07969() {
        return this.internalField1075;
    }

    @Generated
    public BooleanSetting internalMethod00796() {
        return this.internalField1261;
    }

    @Generated
    public ColorSetting internalMethod00850() {
        return this.internalField0665;
    }

    @Generated
    public ColorSetting internalMethod01575() {
        return this.internalField0664;
    }

    @Generated
    public ColorSetting internalMethod08238() {
        return this.internalField1268;
    }

    @Generated
    public ModeSetting internalMethod00852() {
        return this.internalField0668;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod06348() {
        return this.internalField0237;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod06558() {
        return this.internalField0238;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod07956() {
        return this.internalField1066;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod07993() {
        return this.internalField1067;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod09090() {
        return this.internalField1068;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod09116() {
        return this.internalField1065;
    }

    @Generated
    public BooleanSetting internalMethod01511() {
        return this.internalField1263;
    }

    @Generated
    public ModeSetting internalMethod01578() {
        return this.internalField0669;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod09176() {
        return this.internalField1480;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod09185() {
        return this.internalField1481;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod09763() {
        return this.internalField1483;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod09781() {
        return this.internalField1485;
    }

    @Generated
    public SliderSetting internalMethod06665() {
        return this.internalField0383;
    }

    @Generated
    public BooleanSetting internalMethod08227() {
        return this.internalField1262;
    }

    @Generated
    public SliderSetting internalMethod07332() {
        return this.internalField0382;
    }

    @Generated
    public SliderSetting internalMethod07795() {
        return this.internalField1142;
    }

    @Generated
    public SliderSetting internalMethod07931() {
        return this.internalField1140;
    }

    @Generated
    public SliderSetting internalMethod08766() {
        return this.internalField1141;
    }

    @Generated
    public BooleanSetting internalMethod08364() {
        return this.internalField1264;
    }

    @Generated
    public ModeSetting internalMethod08239() {
        return this.internalField1272;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod09683() {
        return this.internalField1484;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod09712() {
        return this.internalField1482;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod09492() {
        return this.internalField1479;
    }

    @Generated
    public ColorSetting internalMethod08372() {
        return this.internalField1267;
    }

    @Generated
    public SliderSetting internalMethod08876() {
        return this.internalField1143;
    }

    @Generated
    public SliderSetting internalMethod09734() {
        return this.internalField1529;
    }

    @Generated
    public SliderSetting internalMethod09796() {
        return this.internalField1534;
    }

    @Generated
    public SliderSetting internalMethod09440() {
        return this.internalField1535;
    }

    @Generated
    public ModeSetting internalMethod08373() {
        return this.internalField1269;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod09502() {
        return this.internalField1478;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod10081() {
        return this.internalField1749;
    }

    @Generated
    public BooleanSetting internalMethod07930() {
        return this.internalField1587;
    }

    @Generated
    public SliderSetting internalMethod09498() {
        return this.internalField1536;
    }

    @Generated
    public SliderSetting internalMethod09750() {
        return this.internalField1533;
    }

    @Generated
    public ModeSetting internalMethod08093() {
        return this.internalField1270;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod09933() {
        return this.internalField1761;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod10080() {
        return this.internalField1751;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod10091() {
        return this.internalField1752;
    }

    @Generated
    public BooleanSetting internalMethod07735() {
        return this.internalField1590;
    }

    @Generated
    public ColorSetting internalMethod08090() {
        return this.internalField1266;
    }

    @Generated
    public ModeSetting internalMethod07754() {
        return this.internalField1271;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod10058() {
        return this.internalField1753;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod10063() {
        return this.internalField1754;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod09985() {
        return this.internalField1755;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod09989() {
        return this.internalField1750;
    }

    @Generated
    public BooleanSetting internalMethod09846() {
        return this.internalField1594;
    }

    @Generated
    public SliderSetting internalMethod09818() {
        return this.internalField1530;
    }

    @Generated
    public BooleanSetting internalMethod09214() {
        return this.internalField0650;
    }

    @Generated
    public BooleanSetting internalMethod09640() {
        return this.internalField0651;
    }

    @Generated
    public ModeSetting internalMethod09155() {
        return this.internalField1596;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod10118() {
        return this.internalField1756;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod10126() {
        return this.internalField1757;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod10044() {
        return this.internalField1758;
    }

    @Generated
    public SliderSetting internalMethod09455() {
        return this.internalField1797;
    }

    @Generated
    public SliderSetting internalMethod09515() {
        return this.internalField1798;
    }

    @Generated
    public BooleanSetting internalMethod09711() {
        return this.internalField1588;
    }

    @Generated
    public BooleanSetting internalMethod09163() {
        return this.internalField1589;
    }

    @Generated
    public ColorSetting internalMethod07753() {
        return this.internalField1265;
    }

    @Generated
    public WetWorldRenderer.InternalType0469 internalMethod00128() {
        return this.internalField0827;
    }

    @Generated
    public RainShaderRenderer internalMethod05188() {
        return this.internalField0184;
    }

    @Generated
    public AmbienceSkyRenderer internalMethod05265() {
        return this.internalField0196;
    }

    @Generated
    public long internalMethod06259() {
        return this.internalField0229;
    }

    @Generated
    public BlockPos internalMethod03139() {
        return this.internalField0352;
    }

    @Generated
    public float internalMethod09227() {
        return this.internalField1047;
    }

    @Generated
    public int internalMethod08782() {
        return this.internalField0227;
    }

    @Generated
    public boolean internalMethod10070() {
        return this.internalField0276;
    }

    @Generated
    public AnimatedValue internalMethod04118() {
        return this.internalField0808;
    }

    @Generated
    public EventListener<ReceivePacketEvent> internalMethod03134() {
        return this.internalField0157;
    }

    @Generated
    public EventListener<Render3DEvent> internalMethod04543() {
        return this.internalField0158;
    }
}
