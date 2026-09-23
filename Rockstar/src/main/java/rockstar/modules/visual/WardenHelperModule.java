package rockstar.modules.visual;







import rockstar.client.ui.*;
import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import pyrock.events.render.PreHudRenderEvent;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.render.CornerRadii;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.ui.ThemeColors;
import rockstar.client.internal.rotation.ScreenProjectionUtils;
import rockstar.client.module.Module;

@ModuleInfo(name="Warden Helper", category=ModuleCategory.VISUALS, internalMethod08049=true)
public class WardenHelperModule
extends Module {
    private static final Pattern internalField0293 = Pattern.compile("(\\d{1,2}):(\\d{2})(?::(\\d{2}))?");
    private static final Pattern internalField0294 = Pattern.compile("(\\d+)\\s*(\u0441|s|\u0441\u0435\u043a|sec)");
    private static final Pattern internalField1112 = Pattern.compile("(\\d+)\\s*(\u043c|m|\u043c\u0438\u043d|min(?:\\.|ute)?)\\s*(?:(\\d+)\\s*(\u0441|s|\u0441\u0435\u043a|sec(?:\\.|ond)?))?");
    private final EventListener<PreHudRenderEvent> internalField0157 = preHudRenderEvent -> {
        if (WardenHelperModule.internalField0149.world == null || WardenHelperModule.internalField0149.player == null) {
            return;
        }
        org.joml.Matrix3x2fStack matrixStack = preHudRenderEvent.getContext().getMatrices();
        ColorRGBA colorRGBA = ThemeColors.internalMethod02531();
        for (Entity entity : WardenHelperModule.internalField0149.world.getEntities()) {
            Vec3d vec3d;
            Vec2f vec2f;
            String string;
            ArmorStandEntity armorStandEntity;
            if (!(entity instanceof ArmorStandEntity) || !this.internalMethod07466(armorStandEntity = (ArmorStandEntity)entity) || (string = this.internalMethod07449(armorStandEntity.getName().getString())) == null || (vec2f = ScreenProjectionUtils.internalMethod00612(vec3d = ScreenProjectionUtils.internalMethod02822((Entity)armorStandEntity, internalField0149.getRenderTickCounter().getTickProgress(true)).add(0.0, 1.2, 0.0))) == null) continue;
            float f = (float)WardenHelperModule.internalField0149.player.getEntityPos().distanceTo(vec3d);
            float f2 = MathHelper.clamp((float)(1.0f - f / 20.0f), (float)0.5f, (float)1.0f);
            matrixStack.pushMatrix();
            matrixStack.translate(vec2f.x, vec2f.y);
            matrixStack.scale(f2, f2);
            WardenHelperModule.internalMethod05911(preHudRenderEvent.getContext(), string, colorRGBA, 1.0f);
            matrixStack.popMatrix();
        }
    };

    public boolean internalMethod07466(ArmorStandEntity armorStandEntity) {
        if (!this.isEnabled()) {
            return false;
        }
        if (WardenHelperModule.internalField0149.world == null) {
            return false;
        }
        String string = armorStandEntity.getName().getString();
        if (string == null || string.isBlank()) {
            return false;
        }
        if (this.internalMethod07449(string) == null) {
            return false;
        }
        return this.internalMethod06873(armorStandEntity.getBlockPos()) != null;
    }

    private String internalMethod07449(String string) {
        Matcher matcher = internalField0293.matcher(string);
        if (matcher.find()) {
            return matcher.group();
        }
        matcher = internalField1112.matcher(string);
        if (matcher.find()) {
            return matcher.group();
        }
        matcher = internalField0294.matcher(string);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private BlockPos internalMethod06873(BlockPos blockPos) {
        if (WardenHelperModule.internalField0149.world == null) {
            return null;
        }
        for (int i = 1; i <= 3; ++i) {
            BlockPos blockPos2 = blockPos.down(i);
            if (!(WardenHelperModule.internalField0149.world.getBlockState(blockPos2).getBlock() instanceof ChestBlock)) continue;
            return blockPos2;
        }
        return null;
    }

    private static void internalMethod05911(CustomDrawContext customDrawContext, String string, ColorRGBA colorRGBA, float f) {
        float f2 = MathHelper.clamp((float)f, (float)0.0f, (float)1.0f);
        if (f2 <= 0.01f) {
            return;
        }
        float f3 = f2 * InterfaceModule.internalMethod07584();
        float f4 = f2 * InterfaceModule.internalMethod07585();
        if (f3 > 0.01f) {
            WardenHelperModule.internalMethod05672(customDrawContext, string, colorRGBA, f3);
        }
        if (f4 > 0.01f) {
            WardenHelperModule.internalMethod08389(customDrawContext, string, colorRGBA, f4);
        }
    }

    private static void internalMethod05672(CustomDrawContext customDrawContext, String string, ColorRGBA colorRGBA, float f) {
        SizedFont typedValue020 = Fonts.internalField1157.internalMethod01432(10.0f);
        float f2 = 6.0f;
        float f3 = 21.0f;
        float f4 = 8.0f;
        float f5 = 6.0f;
        float f6 = typedValue020.internalMethod00965(string);
        float f7 = Math.max(f4 * 2.0f + f2 + f5 + f6, 52.0f) - 1.0f;
        float f8 = -f7 / 2.0f;
        float f9 = 4.0f;
        CornerRadii typedParameter1014 = CornerRadii.internalMethod03908(f3 / 2.0f);
        ColorRGBA colorRGBA2 = ThemeColors.internalMethod08573().withAlpha(60.0f * f);
        ColorRGBA colorRGBA3 = ThemeColors.internalMethod08459().withAlpha(255.0f * f);
        customDrawContext.drawLiquidGlass(f8, f9, f7, f3, 2.0f, 0.08f, typedParameter1014, ColorRGBA.WHITE.withAlpha(191.0f));
        customDrawContext.drawRoundedRect(f8, f9, f7, f3, typedParameter1014, colorRGBA2);
        float f10 = f8 + f4;
        float f11 = f9 + (f3 - f2) / 2.0f;
        customDrawContext.drawRoundedRect(f10, f11, f2, f2, CornerRadii.internalMethod03908(f2 / 2.0f), colorRGBA.withAlpha(245.0f * f));
        customDrawContext.drawText(typedValue020, string, f10 + f2 + f5, f9 + (f3 - typedValue020.internalMethod04890()) / 2.0f, colorRGBA3);
    }

    private static void internalMethod08389(CustomDrawContext customDrawContext, String string, ColorRGBA colorRGBA, float f) {
        SizedFont typedValue020 = Fonts.internalField1157.internalMethod01432(9.0f);
        float f2 = 21.0f;
        float f3 = 6.0f;
        float f4 = 8.0f;
        float f5 = 6.0f;
        float f6 = WardenHelperModule.internalMethod05561(typedValue020.internalMethod00965(string));
        float f7 = f3 + f5 + f6;
        float f8 = WardenHelperModule.internalMethod05561(f7 + f4 * 2.0f);
        float f9 = WardenHelperModule.internalMethod05561(-f8 / 2.0f);
        float f10 = 5.5f;
        CornerRadii typedParameter1014 = CornerRadii.internalMethod03908(f2 / 2.0f);
        ColorRGBA colorRGBA2 = new ColorRGBA(13.0f, 18.0f, 20.0f, 238.0f * f);
        customDrawContext.drawRoundedRect(f9, f10, f8, f2, typedParameter1014, colorRGBA2);
        float f11 = f9 + f4;
        float f12 = WardenHelperModule.internalMethod05090(f10, f2, f3);
        customDrawContext.drawRoundedRect(f11, f12, f3, f3, CornerRadii.internalMethod03908(f3 / 2.0f), colorRGBA.withAlpha(245.0f * f));
        float f13 = f11 + f3 + f5;
        float f14 = WardenHelperModule.internalMethod05561(f10 + (f2 - typedValue020.internalMethod04890()) / 2.0f - 0.5f);
        customDrawContext.drawText(typedValue020, string, f13, f14, ColorRGBA.WHITE.withAlpha(250.0f * f));
    }

    private static float internalMethod05090(float f, float f2, float f3) {
        return WardenHelperModule.internalMethod05561(f + (f2 - f3) / 2.0f);
    }

    private static float internalMethod05561(float f) {
        return (float)Math.round(f * 2.0f) / 2.0f;
    }
}
