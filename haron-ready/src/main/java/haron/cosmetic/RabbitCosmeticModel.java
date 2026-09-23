package haron.cosmetic;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public final class RabbitCosmeticModel {
    public static final Identifier TEXTURE = Identifier.of((String)"haron", (String)"textures/cosmetics/cosmetic_rabbit.png");
    private final ModelPart root = RabbitCosmeticModel.createModelData().createModel();
    private final ModelPart body = this.root.getChild("body");
    private final ModelPart head = this.root.getChild("head");
    private final ModelPart leftEar = this.head.getChild("left_ear");
    private final ModelPart rightEar = this.head.getChild("right_ear");
    private final ModelPart leftArm = this.root.getChild("left_arm");
    private final ModelPart rightArm = this.root.getChild("right_arm");
    private final ModelPart leftLeg = this.root.getChild("left_leg");
    private final ModelPart rightLeg = this.root.getChild("right_leg");
    private final ModelPart tail = this.body.getChild("tail");

    public void render(MatrixStack matrixStack, VertexConsumer vertexConsumer, int n, int n2) {
        this.root.render(matrixStack, vertexConsumer, n, n2);
    }

    private void holdPose(ModelPart modelPart, boolean bl, float f) {
        if (bl) {
            modelPart.pitch = modelPart.pitch * 0.5f - 0.31f;
            modelPart.yaw = 0.0f;
            modelPart.roll = f * 0.08f;
        }
    }

    private void swingArm(PlayerEntityRenderState playerEntityRenderState) {
        float f = playerEntityRenderState.handSwingProgress;
        if (f <= 0.0f) {
            return;
        }
        ModelPart modelPart = playerEntityRenderState.preferredArm == Arm.LEFT ? this.leftArm : this.rightArm;
        this.body.yaw = MathHelper.sin((float)(MathHelper.sqrt((float)f) * ((float)Math.PI * 2))) * 0.2f;
        float f2 = 1.0f - f;
        f2 *= f2;
        f2 *= f2;
        f2 = 1.0f - f2;
        float f3 = MathHelper.sin((float)(f2 * (float)Math.PI));
        float f4 = MathHelper.sin((float)(f * (float)Math.PI)) * -(this.head.pitch - 0.7f) * 0.75f;
        modelPart.pitch -= f3 * 1.2f + f4;
        modelPart.yaw += this.body.yaw * 2.0f;
        modelPart.roll += MathHelper.sin((float)(f * (float)Math.PI)) * -0.4f;
        ModelPart modelPart2 = modelPart == this.leftArm ? this.rightArm : this.leftArm;
        modelPart2.yaw += this.body.yaw * 2.0f;
    }

    public static TexturedModelData createModelData() {
        ModelPartData modelPartData;
        ModelData modelData = new ModelData();
        ModelPartData modelPartData2 = modelData.getRoot();
        ModelPartData modelPartData3 = modelPartData2.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0f, 0.0f, -2.5f, 8.0f, 11.0f, 5.0f), ModelTransform.pivot((float)0.0f, (float)0.0f, (float)0.0f));
        modelPartData3.addChild("tail", ModelPartBuilder.create().uv(32, 0).cuboid(-2.5f, 6.0f, 2.5f, 5.0f, 5.0f, 3.0f), ModelTransform.NONE);
        modelPartData3.addChild("belly", ModelPartBuilder.create().uv(32, 0).cuboid(-3.0f, 2.0f, -3.0f, 6.0f, 8.0f, 0.6f), ModelTransform.NONE);
        ModelPartData modelPartData4 = modelPartData2.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0f, -7.0f, -4.0f, 8.0f, 7.0f, 8.0f), ModelTransform.pivot((float)0.0f, (float)0.0f, (float)0.0f));
        modelPartData4.addChild("muzzle", ModelPartBuilder.create().uv(32, 0).cuboid(-3.0f, -3.5f, -8.0f, 6.0f, 3.5f, 4.0f), ModelTransform.NONE);
        modelPartData4.addChild("nose", ModelPartBuilder.create().uv(32, 32).cuboid(-1.2f, -3.1f, -8.6f, 2.4f, 1.8f, 0.6f), ModelTransform.NONE);
        modelPartData4.addChild("right_eye", ModelPartBuilder.create().uv(0, 32).cuboid(-3.4f, -6.0f, -4.5f, 1.8f, 1.8f, 0.6f), ModelTransform.NONE);
        modelPartData4.addChild("left_eye", ModelPartBuilder.create().uv(0, 32).cuboid(1.6f, -6.0f, -4.5f, 1.8f, 1.8f, 0.6f), ModelTransform.NONE);
        for (String string : new String[]{"left", "right"}) {
            float f = string.equals("left") ? 2.2f : -2.2f;
            modelPartData = modelPartData4.addChild(RabbitCosmeticModel.$sf$0(string), ModelPartBuilder.create().uv(0, 0).cuboid(-1.5f, -9.0f, -1.0f, 3.0f, 9.0f, 2.0f), ModelTransform.pivot((float)f, (float)-6.0f, (float)0.0f));
            modelPartData.addChild("inner", ModelPartBuilder.create().uv(32, 32).cuboid(-1.0f, -8.5f, -1.3f, 2.0f, 7.5f, 0.5f), ModelTransform.NONE);
        }
        for (String string : new String[]{"left", "right"}) {
            boolean bl = string.equals("left");
            modelPartData = modelPartData2.addChild(RabbitCosmeticModel.$sf$1(string), ModelPartBuilder.create().uv(0, 0).mirrored(bl).cuboid(-2.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f), ModelTransform.pivot((float)(bl ? 5.0f : -5.0f), (float)2.0f, (float)0.0f));
            modelPartData.addChild("paw", ModelPartBuilder.create().uv(32, 0).mirrored(bl).cuboid(-2.25f, 6.5f, -2.25f, 4.5f, 4.0f, 4.5f), ModelTransform.NONE);
        }
        for (String string : new String[]{"left", "right"}) {
            boolean bl = string.equals("left");
            modelPartData = modelPartData2.addChild(RabbitCosmeticModel.$sf$2(string), ModelPartBuilder.create().uv(0, 0).mirrored(bl).cuboid(-2.0f, 0.0f, -2.5f, 4.0f, 13.0f, 5.0f), ModelTransform.pivot((float)(bl ? 2.0f : -2.0f), (float)11.0f, (float)0.0f));
            modelPartData.addChild("foot", ModelPartBuilder.create().uv(32, 0).mirrored(bl).cuboid(-2.0f, 11.0f, -6.0f, 4.0f, 2.0f, 8.0f), ModelTransform.NONE);
        }
        return TexturedModelData.of((ModelData)modelData, (int)64, (int)64);
    }

    public void setAngles(PlayerEntityRenderState playerEntityRenderState) {
        float f = playerEntityRenderState.limbFrequency;
        float f2 = Math.min(playerEntityRenderState.limbAmplitudeMultiplier, 1.0f);
        float f3 = playerEntityRenderState.age;
        this.root.setPivot(0.0f, 0.0f, 0.0f);
        this.root.setAngles(0.0f, 0.0f, 0.0f);
        this.body.setPivot(0.0f, 0.0f, 0.0f);
        this.body.setAngles(0.0f, 0.0f, 0.0f);
        this.head.setPivot(0.0f, 0.0f, 0.0f);
        this.leftArm.setPivot(5.0f, 2.0f, 0.0f);
        this.rightArm.setPivot(-5.0f, 2.0f, 0.0f);
        this.head.yaw = playerEntityRenderState.yawDegrees * ((float)Math.PI / 180);
        this.head.pitch = playerEntityRenderState.pitch * ((float)Math.PI / 180);
        float f4 = f * 0.6662f;
        this.rightLeg.pitch = MathHelper.cos((float)f4) * 1.15f * f2;
        this.leftLeg.pitch = MathHelper.cos((float)(f4 + (float)Math.PI)) * 1.15f * f2;
        this.rightArm.pitch = MathHelper.cos((float)(f4 + (float)Math.PI)) * 0.85f * f2;
        this.leftArm.pitch = MathHelper.cos((float)f4) * 0.85f * f2;
        this.rightArm.roll = 0.08f;
        this.leftArm.roll = -0.08f;
        float f5 = Math.abs(MathHelper.sin((float)f4)) * f2;
        this.root.pivotY = -f5 * 1.6f;
        float f6 = MathHelper.sin((float)(f3 * 0.07f)) * 0.06f;
        float f7 = MathHelper.cos((float)(f4 - 0.9f)) * 0.4f * f2;
        this.rightEar.pitch = -0.22f + f6 + f7;
        this.leftEar.pitch = -0.22f - f6 + f7;
        this.rightEar.roll = -0.2f - f6;
        this.leftEar.roll = 0.2f + f6;
        this.tail.pitch = MathHelper.sin((float)(f3 * 0.2f)) * 0.08f;
        this.holdPose(this.rightArm, !playerEntityRenderState.rightHandItemState.isEmpty(), 1.0f);
        this.holdPose(this.leftArm, !playerEntityRenderState.leftHandItemState.isEmpty(), -1.0f);
        this.swingArm(playerEntityRenderState);
        if (playerEntityRenderState.isInSneakingPose) {
            this.body.pitch = 0.42f;
            this.root.pivotY += 2.5f;
            this.head.pivotY = 3.4f;
            this.head.pivotZ = -1.6f;
            this.rightArm.pitch += 0.4f;
            this.leftArm.pitch += 0.4f;
            this.rightLeg.pivotZ = 3.0f;
            this.leftLeg.pivotZ = 3.0f;
            this.rightLeg.pivotY = 8.5f;
            this.leftLeg.pivotY = 8.5f;
        } else {
            this.rightLeg.setPivot(-2.0f, 11.0f, 0.0f);
            this.leftLeg.setPivot(2.0f, 11.0f, 0.0f);
        }
    }

    public ModelPart arm(Arm arm) {
        return arm == Arm.LEFT ? this.leftArm : this.rightArm;
    }

    public void resetArm(Arm arm) {
        int n = 840;
        ModelPart modelPart = this.arm(arm);
        modelPart.resetTransform();
        modelPart.visible = true;
    }

    public void applyArmTransform(Arm arm, MatrixStack matrixStack) {
        this.root.rotate(matrixStack);
        this.arm(arm).rotate(matrixStack);
    }

    public void firstPersonTrim(boolean bl) {
        this.root.visible = !bl;
    }

    private static /* synthetic */ String $sf$0(String string) {
        return string + "_ear";
    }

    private static /* synthetic */ String $sf$1(String string) {
        return string + "_arm";
    }

    private static /* synthetic */ String $sf$2(String string) {
        return string + "_leg";
    }
}

