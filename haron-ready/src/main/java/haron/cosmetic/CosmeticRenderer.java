package haron.cosmetic;

import haron.cosmetic.RabbitCosmeticModel;
import haron.cosmetic.WheelchairCosmeticModel;
import haron.modules.visuals.Cosmetics;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public final class CosmeticRenderer {
    private static final float SIT_DROP = 0.0625f;
    private static RabbitCosmeticModel rabbit;
    private static WheelchairCosmeticModel wheelchair;
    private static Cosmetics module;
    private static boolean worldPass;

    private static boolean active() {
        Cosmetics xjw3p82 = CosmeticRenderer.module();
        return xjw3p82 != null && xjw3p82.k();
    }

    public static void render(PlayerEntityRenderState playerEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int n) {
        RabbitCosmeticModel ekqa5y2 = CosmeticRenderer.rabbitModel();
        float f = CosmeticRenderer.module() == null ? 1.0f : CosmeticRenderer.module().scale();
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f - playerEntityRenderState.bodyYaw));
        matrixStack.scale(-f, -f, f);
        matrixStack.translate(0.0f, -1.501f / f, 0.0f);
        ekqa5y2.setAngles(playerEntityRenderState);
        boolean bl = CosmeticRenderer.trimSelf() && CosmeticRenderer.isSelf(playerEntityRenderState);
        ekqa5y2.firstPersonTrim(bl);
        ekqa5y2.render(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull((Identifier)RabbitCosmeticModel.TEXTURE)), n, OverlayTexture.DEFAULT_UV);
        ekqa5y2.firstPersonTrim(false);
        if (!bl) {
            CosmeticRenderer.renderHeld(ekqa5y2, playerEntityRenderState.leftHandItemState, Arm.LEFT, matrixStack, vertexConsumerProvider, n);
            CosmeticRenderer.renderHeld(ekqa5y2, playerEntityRenderState.rightHandItemState, Arm.RIGHT, matrixStack, vertexConsumerProvider, n);
        }
        matrixStack.pop();
    }

    public static WheelchairCosmeticModel wheelchairModel() {
        int n = 314;
        return wheelchair;
    }

    public static boolean firstPerson() {
        return MinecraftClient.getInstance().options.getPerspective().isFirstPerson();
    }

    public static boolean firstPersonBody() {
        return CosmeticRenderer.firstPerson() && CosmeticRenderer.active() && CosmeticRenderer.module().isFirstPersonBody();
    }

    public static RabbitCosmeticModel rabbitModel() {
        int n = 902;
        return rabbit;
    }

    public static boolean isSelf(PlayerEntityRenderState playerEntityRenderState) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        return minecraftClient.player != null && playerEntityRenderState.id == minecraftClient.player.getId();
    }

    public static void renderArm(Arm arm, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int n) {
        RabbitCosmeticModel ekqa5y2 = CosmeticRenderer.rabbitModel();
        ekqa5y2.resetArm(arm);
        ekqa5y2.arm((Arm)arm).roll = arm == Arm.LEFT ? -0.1f : 0.1f;
        ekqa5y2.arm(arm).render(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull((Identifier)RabbitCosmeticModel.TEXTURE)), n, OverlayTexture.DEFAULT_UV);
    }

    public static void worldPass(boolean bl) {
        worldPass = bl;
    }

    public static boolean trimSelf() {
        return worldPass && CosmeticRenderer.firstPerson();
    }

    private static void renderHeld(RabbitCosmeticModel ekqa5y2, ItemRenderState itemRenderState, Arm arm, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int n) {
        if (!itemRenderState.isEmpty()) {
            matrixStack.push();
            ekqa5y2.applyArmTransform(arm, matrixStack);
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0f));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f));
            matrixStack.translate(arm == Arm.LEFT ? -1.0f : 0.0625f, 0.125f, -0.625f);
            itemRenderState.render(matrixStack, vertexConsumerProvider, n, OverlayTexture.DEFAULT_UV);
            matrixStack.pop();
        }
    }

    public static boolean shouldSit(PlayerEntityRenderState playerEntityRenderState) {
        return CosmeticRenderer.active() && CosmeticRenderer.module().wheelchair() && !playerEntityRenderState.invisible && CosmeticRenderer.isSelf(playerEntityRenderState);
    }

    public static float sitDrop() {
        return 0.0625f;
    }

    public static boolean shouldReplaceArm() {
        return CosmeticRenderer.active() && CosmeticRenderer.module().rabbit();
    }

    public static void renderChair(PlayerEntityRenderState playerEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int n) {
        WheelchairCosmeticModel h1f1ug2 = CosmeticRenderer.wheelchairModel();
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f - playerEntityRenderState.bodyYaw));
        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        matrixStack.translate(0.0f, -1.501f, 0.0f);
        h1f1ug2.setAngles(playerEntityRenderState.limbFrequency);
        h1f1ug2.render(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull((Identifier)WheelchairCosmeticModel.TEXTURE)), n, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
    }

    private CosmeticRenderer() {
    }

    private static Cosmetics module() {
        return module;
    }

    public static boolean shouldReplace(PlayerEntityRenderState playerEntityRenderState) {
        return CosmeticRenderer.active() && CosmeticRenderer.module().rabbit() && !playerEntityRenderState.invisible && CosmeticRenderer.isSelf(playerEntityRenderState);
    }
}

