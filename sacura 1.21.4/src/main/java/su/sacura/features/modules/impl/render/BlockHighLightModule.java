package su.sacura.features.modules.impl.render;

import com.google.common.eventbus.Subscribe;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import su.sacura.events.render.WorldRenderEvent;
import su.sacura.features.modules.api.core.Module;
import su.sacura.features.modules.api.core.ModuleAnnotations;
import su.sacura.features.modules.impl.Category;
import su.sacura.features.modules.settings.impl.BooleanSetting;
import su.sacura.features.modules.settings.impl.SliderSetting;
import su.sacura.util.impl.render.RenderWorld;
import su.sacura.util.impl.render.providers.ColorProvider;

@ModuleAnnotations(name="Block High Light", category=Category.RENDER)
public class BlockHighLightModule
        extends Module {
    BooleanSetting slideAnimation = new BooleanSetting("Анимация", true).setDescription("Устанавливает режим анимации глайда");
    SliderSetting slideSpeed = new SliderSetting("Скорость анимации", 0.2f, 0.01f, 1.0f, 0.01f).setDescription("Устанавливает скорость анимации глайда");
    BooleanSetting depth = new BooleanSetting("Глубина", true).setDescription("Устанавливает глубину у блока");
    BooleanSetting fill = new BooleanSetting("Заливка", true).setDescription("Устанавливает заливку блока");
    BooleanSetting line = new BooleanSetting("Линии", true).setDescription("Устанавливает линии вокруг блока");
    BlockPos lastPos = null;
    Vec3d animatedPos = null;
    float lastBreakingProgress = 0.0f;
    float breakingProgress = 0.0f;

    public BlockHighLightModule() {
        this.addSettings(this.slideAnimation, this.slideSpeed, this.depth, this.fill, this.line);
    }

    @Subscribe
    public void render(WorldRenderEvent e) {
        BlockHitResult result;
        HitResult hitResult = BlockHighLightModule.mc.crosshairTarget;
        // Исправлено: method_17783() -> getType(), field_1332 -> BLOCK
        if (!(hitResult instanceof BlockHitResult) || (result = (BlockHitResult)hitResult).getType() != HitResult.Type.BLOCK) {
            this.lastPos = null;
            this.animatedPos = null;
            return;
        }
        BlockPos currentPos = result.getBlockPos();
        // Исправлено: method_22347 -> isAir
        if (BlockHighLightModule.mc.world != null && BlockHighLightModule.mc.world.isAir(currentPos)) {
            this.lastPos = null;
            this.animatedPos = null;
            return;
        }
        if (this.animatedPos == null || !currentPos.equals((Object)this.lastPos)) {
            if (this.animatedPos == null || this.lastPos == null) {
                // Исправлено: method_10263 -> getX, method_10264 -> getY, method_10260 -> getZ
                this.animatedPos = new Vec3d((double)currentPos.getX(), (double)currentPos.getY(), (double)currentPos.getZ());
            }
            this.lastPos = currentPos;
        }
        // Исправлено: method_10263 -> getX, method_10264 -> getY, method_10260 -> getZ
        this.animatedPos = (Boolean)this.slideAnimation.get() != false ? this.animatedPos.lerp(new Vec3d((double)currentPos.getX(), (double)currentPos.getY(), (double)currentPos.getZ()), (double)(e.getPartialTicks() * ((Float)this.slideSpeed.get()).floatValue())) : new Vec3d((double)currentPos.getX(), (double)currentPos.getY(), (double)currentPos.getZ());
        Box boundingBox = null;
        if (BlockHighLightModule.mc.world != null) {
            // Исправлено: method_8320 -> getBlockState, method_26218 -> getOutlineShape
            boundingBox = BlockHighLightModule.mc.world.getBlockState(currentPos).getOutlineShape((BlockView)BlockHighLightModule.mc.world, currentPos).getBoundingBox().offset(this.animatedPos.x, this.animatedPos.y, this.animatedPos.z);
        }
        RenderWorld.drawBox(boundingBox, ColorProvider.getColorStyle(360.0f), 2.0f, (Boolean)this.line.get(), (Boolean)this.fill.get(), (Boolean)this.depth.get());
        ClientPlayerInteractionManager interactionManager = BlockHighLightModule.mc.interactionManager;
        float currentBreakingProgress = 0.0f;
        this.breakingProgress = currentBreakingProgress > this.lastBreakingProgress ? (this.breakingProgress += (currentBreakingProgress - this.lastBreakingProgress) * e.getPartialTicks()) : 0.0f;
        this.lastBreakingProgress = currentBreakingProgress;
        this.breakingProgress = Math.min(this.breakingProgress, 1.0f);
        if (this.breakingProgress > 0.0f && ((Boolean)this.fill.get()).booleanValue()) {
            Box fillBox = new Box(this.animatedPos.x + 0.5 - (double)this.breakingProgress * 0.5, this.animatedPos.y + 0.5 - (double)this.breakingProgress * 0.5, this.animatedPos.z + 0.5 - (double)this.breakingProgress * 0.5, this.animatedPos.x + 0.5 + (double)this.breakingProgress * 0.5, this.animatedPos.y + 0.5 + (double)this.breakingProgress * 0.5, this.animatedPos.z + 0.5 + (double)this.breakingProgress * 0.5);
            RenderWorld.drawBox(fillBox, ColorProvider.setAlpha(ColorProvider.getColorStyle(360.0f), (int)(this.breakingProgress * 150.0f)), 0.0f, (Boolean)this.line.get(), (Boolean)this.fill.get(), (Boolean)this.depth.get());
        }
    }
}