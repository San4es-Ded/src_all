package haron.cosmetic;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public final class WheelchairCosmeticModel {
    public static final Identifier TEXTURE = Identifier.of((String)"haron", (String)"textures/cosmetics/cosmetic_wheelchair.png");
    private static final float GROUND = 24.0f;
    public static final float SEAT_H = 11.0f;
    private final ModelPart root = WheelchairCosmeticModel.createModelData().createModel();
    private final ModelPart leftWheel = this.root.getChild("left_wheel");
    private final ModelPart rightWheel = this.root.getChild("right_wheel");

    public void render(MatrixStack matrixStack, VertexConsumer vertexConsumer, int n, int n2) {
        this.root.render(matrixStack, vertexConsumer, n, n2);
    }

    private static void seat(ModelPartData modelPartData) {
        modelPartData.addChild("seat", ModelPartBuilder.create().uv(64, 64).cuboid(-5.5f, WheelchairCosmeticModel.y(11.0f), -6.0f, 11.0f, 1.5f, 10.0f).uv(64, 64).cuboid(-5.5f, WheelchairCosmeticModel.y(21.0f), 2.5f, 11.0f, 10.0f, 1.5f).uv(0, 0).cuboid(-7.5f, WheelchairCosmeticModel.y(6.0f) - 0.6f, 0.9f, 15.0f, 1.2f, 1.2f).uv(64, 0).cuboid(-5.0f, WheelchairCosmeticModel.y(9.5f), -13.5f, 10.0f, 1.2f, 4.5f), ModelTransform.NONE);
    }

    private static void wheel(ModelPartData modelPartData, String string, float f, float f2, float f3) {
        ModelPartData modelPartData2 = modelPartData.addChild(string, ModelPartBuilder.create().uv(64, 0).cuboid(-1.6f, -1.6f, -1.6f, 3.2f, 3.2f, 3.2f), ModelTransform.pivot((float)f, (float)f2, (float)f3));
        float f4 = 12.0f * MathHelper.sin((float)0.3926991f);
        float f5 = 6.0f * MathHelper.cos((float)0.3926991f);
        for (int i = 0; i < 4; ++i) {
            modelPartData2.addChild(WheelchairCosmeticModel.$sf$2(i), ModelPartBuilder.create().uv(64, 0).cuboid(-0.6f, -6.0f, -0.6f, 1.2f, 12.0f, 1.2f).uv(0, 64).cuboid(-1.1f, -f5 - 0.65f, -f4 / 2.0f, 2.2f, 1.3f, f4).uv(0, 64).cuboid(-1.1f, f5 - 0.65f, -f4 / 2.0f, 2.2f, 1.3f, f4), ModelTransform.rotation((float)((float)i * (float)Math.PI / 4.0f), (float)0.0f, (float)0.0f));
        }
    }

    private static void sideFrame(ModelPartData modelPartData, String string, float f) {
        float f2 = 6.2f;
        modelPartData.addChild(WheelchairCosmeticModel.$sf$1(string), ModelPartBuilder.create().uv(0, 0).cuboid(f * 6.2f - 0.75f, WheelchairCosmeticModel.y(16.0f), -5.0f, 1.5f, 1.5f, 9.0f).uv(0, 0).cuboid(f * 6.2f - 0.6f, WheelchairCosmeticModel.y(16.0f), 2.5f, 1.2f, 5.0f, 1.2f).uv(0, 0).cuboid(f * 4.6f - 0.6f, WheelchairCosmeticModel.y(9.5f), -5.0f, 1.2f, 5.5f, 7.0f).uv(0, 0).cuboid(f * 4.6f - 0.6f, WheelchairCosmeticModel.y(9.5f), -13.0f, 1.2f, 1.2f, 8.0f), ModelTransform.NONE);
    }

    public static TexturedModelData createModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        WheelchairCosmeticModel.seat(modelPartData);
        for (int i = 0; i < 2; ++i) {
            boolean bl = i == 0;
            String string = bl ? "left" : "right";
            float f = bl ? 1.0f : -1.0f;
            WheelchairCosmeticModel.sideFrame(modelPartData, string, f);
            WheelchairCosmeticModel.wheel(modelPartData, WheelchairCosmeticModel.$sf$0(string), f * 7.5f, WheelchairCosmeticModel.y(6.0f), 1.5f);
        }
        return TexturedModelData.of((ModelData)modelData, (int)128, (int)128);
    }

    public void setAngles(float f) {
        float f2;
        this.leftWheel.pitch = f2 = -f * 0.75f;
        this.rightWheel.pitch = f2;
    }

    private static float y(float f) {
        return 24.0f - f;
    }

    private static /* synthetic */ String $sf$0(String string) {
        return string + "_wheel";
    }

    private static /* synthetic */ String $sf$1(String string) {
        return string + "_frame";
    }

    private static /* synthetic */ String $sf$2(int n) {
        return "spoke" + n;
    }
}

