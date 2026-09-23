package rockstar.client.render;



import rockstar.client.ui.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import com.mojang.blaze3d.opengl.GlStateManager;
import rockstar.client.compat.RenderSystem;
import java.util.function.Predicate;
import lombok.Generated;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;
import rockstar.client.render.CornerRadii;
import rockstar.client.internal.script.HudManager;
import rockstar.client.internal.script.HotbarHud;
import rockstar.client.RockstarClient;
import rockstar.client.ui.AlternatingColorGradient;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.ui.WindowAccess;

public final class HudRenderUtils
implements MinecraftClientAccess,
WindowAccess {
    public static void internalMethod02076(CustomDrawContext customDrawContext, int n, ColorRGBA colorRGBA) {
        float f;
        float f2;
        if (n < 0 || n > 8 || HudRenderUtils.internalField0149.player == null) {
            return;
        }
        RenderSystem.enableBlend();
        int n2 = internalField0149.getWindow().getScaledWidth();
        int n3 = internalField0149.getWindow().getScaledHeight();
        float f3 = HotbarHud.internalMethod06380();
        if (HudRenderUtils.internalMethod04671()) {
            float f4 = (float)n2 / 2.0f - 101.0f;
            float f5 = (float)n3 - 30.0f - f3;
            f2 = f4 + 6.0f + (float)n * 21.5f;
            f = f5 + 5.5f;
        } else {
            int n4 = n2 / 2 - 91;
            float f6 = (float)(n3 - 22) - f3;
            f2 = n4 + n * 20 + 3;
            f = f6 + 3.0f;
        }
        customDrawContext.drawRoundedRect(f2, f, 18.0f, 18.0f, CornerRadii.internalMethod03908(4.0f), new AlternatingColorGradient(colorRGBA.mulAlpha(0.0f), colorRGBA));
    }

    private static boolean internalMethod04671() {
        HudManager typedValue196 = RockstarClient.getInstance().internalMethod01271();
        if (typedValue196 == null) {
            return false;
        }
        HotbarHud typedValue200 = typedValue196.internalMethod01440();
        return typedValue200 != null && typedValue200.isShowing() && typedValue200.show();
    }

    public static boolean internalMethod04554(CustomDrawContext customDrawContext, Item item, ColorRGBA colorRGBA) {
        if (HudRenderUtils.internalField0149.player == null) {
            return false;
        }
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = HudRenderUtils.internalField0149.player.getInventory().getStack(i);
            if (itemStack.getItem() != item) continue;
            HudRenderUtils.internalMethod02076(customDrawContext, i, colorRGBA);
            return true;
        }
        return false;
    }

    public static boolean internalMethod02251(CustomDrawContext customDrawContext, Predicate<ItemStack> predicate, ColorRGBA colorRGBA) {
        if (HudRenderUtils.internalField0149.player == null) {
            return false;
        }
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = HudRenderUtils.internalField0149.player.getInventory().getStack(i);
            if (itemStack.isEmpty() || !predicate.test(itemStack)) continue;
            HudRenderUtils.internalMethod02076(customDrawContext, i, colorRGBA);
            return true;
        }
        return false;
    }

    public static int internalMethod04553(CustomDrawContext customDrawContext, Item item, ColorRGBA colorRGBA) {
        if (HudRenderUtils.internalField0149.player == null) {
            return 0;
        }
        int n = 0;
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = HudRenderUtils.internalField0149.player.getInventory().getStack(i);
            if (itemStack.getItem() != item) continue;
            HudRenderUtils.internalMethod02076(customDrawContext, i, colorRGBA);
            ++n;
        }
        return n;
    }

    public static int internalMethod02250(CustomDrawContext customDrawContext, Predicate<ItemStack> predicate, ColorRGBA colorRGBA) {
        if (HudRenderUtils.internalField0149.player == null) {
            return 0;
        }
        int n = 0;
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = HudRenderUtils.internalField0149.player.getInventory().getStack(i);
            if (itemStack.isEmpty() || !predicate.test(itemStack)) continue;
            HudRenderUtils.internalMethod02076(customDrawContext, i, colorRGBA);
            ++n;
        }
        return n;
    }

    public static void internalMethod02865(MatrixStack matrixStack, float f, float f2, float f3) {
        matrixStack.push();
        matrixStack.translate(f, f2, 0.0f);
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f3));
        matrixStack.translate(-f, -f2, 0.0f);
    }

    public static void internalMethod06491(MatrixStack matrixStack, float f, float f2, float f3) {
        matrixStack.push();
        matrixStack.translate(f, f2, 150.0f);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f3));
        matrixStack.translate(-f, -f2, -150.0f);
    }

    public static void internalMethod08976(MatrixStack matrixStack, float f, float f2, float f3) {
        matrixStack.push();
        matrixStack.translate(f, f2, 0.0f);
        matrixStack.scale(f3, f3, 1.0f);
        matrixStack.translate(-f, -f2, 0.0f);
    }

    public static void internalMethod00012(MatrixStack matrixStack) {
        matrixStack.pop();
    }

    public static void internalMethod01900(MatrixStack matrixStack) {
        Camera camera = HudRenderUtils.internalField0149.gameRenderer.getCamera();
        Vec3d vec3d = camera.getCameraPos();
        Vec3d vec3d2 = Vec3d.ZERO.subtract(vec3d);
        matrixStack.translate(vec3d2.getX(), vec3d2.getY(), vec3d2.getZ());
    }

    public static void internalMethod03474(MatrixStack matrixStack, Vec3d vec3d) {
        Camera camera = HudRenderUtils.internalField0149.gameRenderer.getCamera();
        Vec3d vec3d2 = camera.getCameraPos();
        Vec3d vec3d3 = vec3d.subtract(vec3d2);
        matrixStack.translate(vec3d3.getX(), vec3d3.getY(), vec3d3.getZ());
    }

    public static void internalMethod02865(org.joml.Matrix3x2fStack matrixStack, float x, float y, float degrees) {
        matrixStack.pushMatrix();
        matrixStack.translate(x, y);
        matrixStack.rotate((float)Math.toRadians(degrees));
        matrixStack.translate(-x, -y);
    }

    public static void internalMethod06491(org.joml.Matrix3x2fStack matrixStack, float x, float y, float degrees) {
        matrixStack.pushMatrix();
        matrixStack.translate(x, y);
        matrixStack.scale((float)Math.cos(Math.toRadians(degrees)), 1.0F);
        matrixStack.translate(-x, -y);
    }

    public static void internalMethod08976(org.joml.Matrix3x2fStack matrixStack, float x, float y, float scale) {
        matrixStack.pushMatrix();
        matrixStack.translate(x, y);
        matrixStack.scale(scale, scale);
        matrixStack.translate(-x, -y);
    }

    public static void internalMethod00012(org.joml.Matrix3x2fStack matrixStack) {
        matrixStack.popMatrix();
    }

    public static void internalMethod02691(boolean bl) {
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask((boolean)false);
        if (bl) {
            RenderSystem.blendFunc((com.mojang.blaze3d.platform.SourceFactor)com.mojang.blaze3d.platform.SourceFactor.SRC_ALPHA, (com.mojang.blaze3d.platform.DestFactor)com.mojang.blaze3d.platform.DestFactor.ONE);
        } else {
            RenderSystem.defaultBlendFunc();
        }
    }

    public static void internalMethod04670() {
        RenderSystem.depthMask((boolean)true);
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
    }

    public static void internalMethod05816(BufferBuilder bufferBuilder) {
        BuiltBuffer builtBuffer = bufferBuilder.endNullable();
        if (builtBuffer != null) {
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)builtBuffer);
        }
    }

    @Generated
    private HudRenderUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
