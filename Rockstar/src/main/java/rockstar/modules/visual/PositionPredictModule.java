package rockstar.modules.visual;








import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.*;

import rockstar.client.compat.RenderSystem;
import java.util.List;
import rockstar.client.compat.ShaderProgramKey;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import pyrock.events.render.PreHudRenderEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.ui.ThemeColors;
import rockstar.client.internal.rotation.TargetTrajectoryPredictor;
import rockstar.client.render.Render3DUtils;
import rockstar.client.render.HudRenderUtils;
import rockstar.client.internal.rotation.ScreenProjectionUtils;
import rockstar.client.module.Module;

@ModuleInfo(name="PositionPredict", category=ModuleCategory.VISUALS)
public class PositionPredictModule
extends Module {
    private SliderSetting internalField0383;
    private SliderSetting internalField0382;
    private SliderSetting internalField1142;
    private SliderSetting internalField1140;
    private BooleanSetting internalField0650;
    private BooleanSetting internalField0651;
    private BooleanSetting internalField1261;
    private BooleanSetting internalField1263;
    private BooleanSetting internalField1262;
    private final TargetTrajectoryPredictor.InternalType0128 internalField0511 = new TargetTrajectoryPredictor.InternalType0128();
    private TargetTrajectoryPredictor.InternalType0130 internalField0512;
    private final EventListener<Render3DEvent> internalField0157 = render3DEvent -> {
        float f;
        TargetTrajectoryPredictor.InternalType0130 nestedValue2017 = this.internalField0512;
        if (nestedValue2017 == null) {
            return;
        }
        MatrixStack matrixStack = render3DEvent.getMatrices();
        matrixStack.push();
        HudRenderUtils.internalMethod02691(true);
        HudRenderUtils.internalMethod01900(matrixStack);
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        BufferBuilder bufferBuilder = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        double d = 1.0E-6;
        for (TargetTrajectoryPredictor.InternalType0131 nestedValue2018 : nestedValue2017.internalMethod03491()) {
            d = Math.max(d, nestedValue2018.internalMethod05262());
        }
        if (this.internalField1263.internalMethod04496()) {
            for (TargetTrajectoryPredictor.InternalType0131 nestedValue2018 : nestedValue2017.internalMethod03491()) {
                f = (float)(25.0 + 165.0 * (nestedValue2018.internalMethod05262() / d));
                ColorRGBA colorRGBA = ThemeColors.internalMethod02531().withAlpha(f);
                List<Vec3d> list = nestedValue2018.internalMethod00026();
                for (int i = 0; i < list.size() - 1; ++i) {
                    Render3DUtils.internalMethod06311(matrixStack, bufferBuilder, list.get(i), list.get(i + 1), colorRGBA);
                }
            }
        }
        if (this.internalField1262.internalMethod04496()) {
            for (TargetTrajectoryPredictor.InternalType0131 nestedValue2018 : nestedValue2017.internalMethod03491()) {
                if (nestedValue2018.internalMethod05263()) {
                    this.internalMethod00782(matrixStack, bufferBuilder, nestedValue2018.internalMethod04240(), ThemeColors.internalField1312.withAlpha(200.0f));
                    continue;
                }
                f = (float)(20.0 + 120.0 * (nestedValue2018.internalMethod05262() / d));
                this.internalMethod00782(matrixStack, bufferBuilder, nestedValue2018.internalMethod04240(), ThemeColors.internalMethod02531().withAlpha(f));
            }
        }
        Render3DUtils.internalMethod06311(matrixStack, bufferBuilder, nestedValue2017.internalMethod08835(), nestedValue2017.internalMethod07844(), ThemeColors.internalField0776.withAlpha(190.0f));
        this.internalMethod01021(matrixStack, bufferBuilder, nestedValue2017.internalMethod06541(), ThemeColors.internalField1312.withAlpha(220.0f));
        this.internalMethod01021(matrixStack, bufferBuilder, nestedValue2017.internalMethod07844(), ThemeColors.internalField0776.withAlpha(255.0f));
        HudRenderUtils.internalMethod05816(bufferBuilder);
        HudRenderUtils.internalMethod04670();
        matrixStack.pop();
    };
    private final EventListener<PreHudRenderEvent> internalField0158 = preHudRenderEvent -> {
        TargetTrajectoryPredictor.InternalType0130 nestedValue2017 = this.internalField0512;
        if (nestedValue2017 == null) {
            return;
        }
        Vec2f vec2f = ScreenProjectionUtils.internalMethod00612(nestedValue2017.internalMethod07844());
        if (vec2f == null) {
            return;
        }
        CustomDrawContext customDrawContext = preHudRenderEvent.getContext();
        SizedFont typedValue020 = Fonts.internalField0449.internalMethod01432(11.0f);
        String string = String.format("%.0f%% \u00b7 %dt \u00b7 %d", nestedValue2017.internalMethod03416(), nestedValue2017.internalMethod03421(), nestedValue2017.internalMethod03417());
        customDrawContext.drawCenteredText(typedValue020, string, vec2f.x, vec2f.y - typedValue020.internalMethod04890() - 2.0f, ThemeColors.internalField0776);
    };

    public PositionPredictModule() {
        this.internalField0383 = new SliderSetting(this, "ticks").internalMethod05900(1.0f).internalMethod02732(40.0f).internalMethod08673(1.0f).internalMethod08074(10.0f).internalMethod06240(" t");
        this.internalField0382 = new SliderSetting(this, "directions").internalMethod05900(4.0f).internalMethod02732(16.0f).internalMethod08673(1.0f).internalMethod08074(8.0f);
        this.internalField1142 = new SliderSetting(this, "branch_interval").internalMethod05900(1.0f).internalMethod02732(20.0f).internalMethod08673(1.0f).internalMethod08074(4.0f).internalMethod06240(" t");
        this.internalField1140 = new SliderSetting(this, "hitchance").internalMethod05900(0.0f).internalMethod02732(100.0f).internalMethod08673(1.0f).internalMethod08074(100.0f).internalMethod06240("%");
        this.internalField0650 = new BooleanSetting(this, "jump_branches").internalMethod06630();
        this.internalField0651 = new BooleanSetting(this, "predict_self").internalMethod06630();
        this.internalField1261 = new BooleanSetting(this, "nearest_fallback").internalMethod06630();
        this.internalField1263 = new BooleanSetting(this, "render_cloud").internalMethod06630();
        this.internalField1262 = new BooleanSetting(this, "render_boxes").internalMethod06630();
    }

    @Override
    public void onDisable() {
        this.internalField0512 = null;
        this.internalField0511.internalMethod06620();
    }

    @Override
    public void internalMethod08229() {
        Vec3d vec3d;
        this.internalField0512 = null;
        if (PositionPredictModule.internalField0149.player == null || PositionPredictModule.internalField0149.world == null) {
            return;
        }
        LivingEntity livingEntity = this.internalMethod01788();
        if (livingEntity == null) {
            this.internalField0511.internalMethod06620();
            return;
        }
        this.internalField0511.internalMethod00730((Entity)livingEntity);
        int n = (int)this.internalField0383.internalMethod08576();
        Vec3d vec3d2 = vec3d = this.internalField0651.internalMethod04496() ? TargetTrajectoryPredictor.internalMethod01475(n) : PositionPredictModule.internalField0149.player.getEyePos();
        if (vec3d == null) {
            vec3d = PositionPredictModule.internalField0149.player.getEyePos();
        }
        TargetTrajectoryPredictor.InternalType0029 nestedValue2006 = new TargetTrajectoryPredictor.InternalType0029(n, (int)this.internalField0382.internalMethod08576(), (int)this.internalField1142.internalMethod08576(), this.internalField0650.internalMethod04496(), 64.0, 96);
        this.internalField0512 = TargetTrajectoryPredictor.internalMethod02301((Entity)livingEntity, this.internalField0511.internalMethod05393(), this.internalField0511.internalMethod06619(), vec3d, (double)this.internalField1140.internalMethod08576() / 100.0, nestedValue2006);
    }

    private LivingEntity internalMethod01788() {
        LivingEntity livingEntity = RockstarClient.getInstance().internalMethod04463().internalMethod01783();
        if (livingEntity != null && livingEntity.isAlive() && livingEntity != PositionPredictModule.internalField0149.player) {
            return livingEntity;
        }
        if (!this.internalField1261.internalMethod04496()) {
            return null;
        }
        PlayerEntity playerEntity = null;
        double d = Double.MAX_VALUE;
        for (PlayerEntity playerEntity2 : PositionPredictModule.internalField0149.world.getPlayers()) {
            double d2;
            if (playerEntity2 == PositionPredictModule.internalField0149.player || !playerEntity2.isAlive() || !((d2 = playerEntity2.squaredDistanceTo((Entity)PositionPredictModule.internalField0149.player)) < d)) continue;
            d = d2;
            playerEntity = playerEntity2;
        }
        return playerEntity;
    }

    private void internalMethod00782(MatrixStack matrixStack, BufferBuilder bufferBuilder, Box box, ColorRGBA colorRGBA) {
        double d = box.minX;
        double d2 = box.minY;
        double d3 = box.minZ;
        double d4 = box.maxX;
        double d5 = box.maxY;
        double d6 = box.maxZ;
        this.internalMethod02254(matrixStack, bufferBuilder, d, d2, d3, d4, d2, d3, colorRGBA);
        this.internalMethod02254(matrixStack, bufferBuilder, d4, d2, d3, d4, d2, d6, colorRGBA);
        this.internalMethod02254(matrixStack, bufferBuilder, d4, d2, d6, d, d2, d6, colorRGBA);
        this.internalMethod02254(matrixStack, bufferBuilder, d, d2, d6, d, d2, d3, colorRGBA);
        this.internalMethod02254(matrixStack, bufferBuilder, d, d5, d3, d4, d5, d3, colorRGBA);
        this.internalMethod02254(matrixStack, bufferBuilder, d4, d5, d3, d4, d5, d6, colorRGBA);
        this.internalMethod02254(matrixStack, bufferBuilder, d4, d5, d6, d, d5, d6, colorRGBA);
        this.internalMethod02254(matrixStack, bufferBuilder, d, d5, d6, d, d5, d3, colorRGBA);
        this.internalMethod02254(matrixStack, bufferBuilder, d, d2, d3, d, d5, d3, colorRGBA);
        this.internalMethod02254(matrixStack, bufferBuilder, d4, d2, d3, d4, d5, d3, colorRGBA);
        this.internalMethod02254(matrixStack, bufferBuilder, d4, d2, d6, d4, d5, d6, colorRGBA);
        this.internalMethod02254(matrixStack, bufferBuilder, d, d2, d6, d, d5, d6, colorRGBA);
    }

    private void internalMethod01021(MatrixStack matrixStack, BufferBuilder bufferBuilder, Vec3d vec3d, ColorRGBA colorRGBA) {
        double d = 0.18;
        this.internalMethod02254(matrixStack, bufferBuilder, vec3d.x - d, vec3d.y, vec3d.z, vec3d.x + d, vec3d.y, vec3d.z, colorRGBA);
        this.internalMethod02254(matrixStack, bufferBuilder, vec3d.x, vec3d.y - d, vec3d.z, vec3d.x, vec3d.y + d, vec3d.z, colorRGBA);
        this.internalMethod02254(matrixStack, bufferBuilder, vec3d.x, vec3d.y, vec3d.z - d, vec3d.x, vec3d.y, vec3d.z + d, colorRGBA);
    }

    private void internalMethod02254(MatrixStack matrixStack, BufferBuilder bufferBuilder, double d, double d2, double d3, double d4, double d5, double d6, ColorRGBA colorRGBA) {
        Render3DUtils.internalMethod06311(matrixStack, bufferBuilder, new Vec3d(d, d2, d3), new Vec3d(d4, d5, d6), colorRGBA);
    }
}
