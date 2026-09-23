package rockstar.client.internal.render;



import rockstar.client.internal.game.*;
import rockstar.client.*;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import rockstar.client.internal.game.PlayerMotionTracker;

@Environment(value=EnvType.CLIENT)
public class TaksaModel
extends Model {
    private final ModelPart internalField0569;
    private final ModelPart internalField0568;
    private final ModelPart internalField1209;
    private final ModelPart internalField1211;
    private final ModelPart internalField1212;
    private final ModelPart internalField1210;
    private final ModelPart internalField1564;
    private final ModelPart internalField1565;

    public TaksaModel(ModelPart modelPart) {
        this(modelPart, RenderLayers::entityTranslucent);
    }

    public TaksaModel(ModelPart modelPart, Function<Identifier, RenderLayer> function) {
        super(modelPart, function);
        this.internalField0569 = modelPart.getChild("head");
        this.internalField0568 = modelPart.getChild("neck");
        this.internalField1209 = modelPart.getChild("body");
        this.internalField1211 = modelPart.getChild("front_left_leg");
        this.internalField1212 = modelPart.getChild("front_right_leg");
        this.internalField1210 = modelPart.getChild("left_back_leg");
        this.internalField1564 = modelPart.getChild("right_back_leg");
        this.internalField1565 = modelPart.getChild("tail");
    }

    public static TexturedModelData internalMethod02526() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData modelPartData2 = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0f, -3.0f, -4.0f, 6.0f, 6.0f, 4.0f, Dilation.NONE).uv(21, 0).cuboid(-1.5f, 0.0f, -7.0f, 3.0f, 3.0f, 3.0f, Dilation.NONE), ModelTransform.origin((float)0.0f, (float)10.5f, (float)-6.8f));
        modelPartData2.addChild("left_ear", ModelPartBuilder.create().uv(32, 4).cuboid(0.0f, -5.0f, -1.5f, 1.0f, 3.0f, 3.0f, Dilation.NONE).uv(34, 1).cuboid(0.0f, -5.5f, -0.75f, 1.0f, 1.0f, 2.0f, Dilation.NONE), ModelTransform.origin((float)3.0f, (float)3.0f, (float)-2.0f));
        modelPartData2.addChild("right_ear", ModelPartBuilder.create().uv(32, 4).mirrored().cuboid(-1.0f, -5.0f, -1.5f, 1.0f, 3.0f, 3.0f, Dilation.NONE).uv(34, 1).mirrored().cuboid(-1.0f, -5.5f, -0.75f, 1.0f, 1.0f, 2.0f, Dilation.NONE), ModelTransform.origin((float)-3.0f, (float)3.0f, (float)-2.0f));
        modelPartData.addChild("neck", ModelPartBuilder.create().uv(15, 7).cuboid(-2.95f, -1.0f, -4.0f, 5.9f, 5.0f, 6.0f, Dilation.NONE), ModelTransform.of((float)0.0f, (float)10.5f, (float)-5.0f, (float)((float)Math.toRadians(-25.0)), (float)0.0f, (float)0.0f));
        ModelPartData modelPartData3 = modelPartData.addChild("body", ModelPartBuilder.create(), ModelTransform.origin((float)0.0f, (float)13.5f, (float)-5.0f));
        modelPartData3.addChild("chest", ModelPartBuilder.create().uv(32, 13).cuboid(-4.0f, -3.5f, -3.0f, 8.0f, 7.0f, 6.0f, Dilation.NONE), ModelTransform.origin((float)0.0f, (float)0.0f, (float)3.0f));
        modelPartData3.addChild("back", ModelPartBuilder.create().uv(3, 19).cuboid(-3.0f, -3.0f, -0.5f, 6.0f, 6.0f, 11.0f, Dilation.NONE), ModelTransform.origin((float)0.0f, (float)-0.5f, (float)5.5f));
        modelPartData.addChild("front_left_leg", ModelPartBuilder.create().uv(42, 0).cuboid(-1.0f, 0.0f, -1.0f, 2.0f, 5.0f, 2.0f, Dilation.NONE), ModelTransform.origin((float)1.5f, (float)16.0f, (float)-3.0f));
        modelPartData.addChild("front_right_leg", ModelPartBuilder.create().uv(42, 0).mirrored().cuboid(-1.0f, 0.0f, -1.0f, 2.0f, 5.0f, 2.0f, Dilation.NONE), ModelTransform.origin((float)-1.5f, (float)16.0f, (float)-3.0f));
        modelPartData.addChild("left_back_leg", ModelPartBuilder.create().uv(52, 0).cuboid(-1.0f, 0.0f, -1.0f, 2.0f, 5.0f, 2.0f, Dilation.NONE), ModelTransform.origin((float)1.5f, (float)16.0f, (float)9.0f));
        modelPartData.addChild("right_back_leg", ModelPartBuilder.create().uv(52, 0).mirrored().cuboid(-1.0f, 0.0f, -1.0f, 2.0f, 5.0f, 2.0f, Dilation.NONE), ModelTransform.origin((float)-1.5f, (float)16.0f, (float)9.0f));
        modelPartData.addChild("tail", ModelPartBuilder.create().uv(2, 12).cuboid(-1.0f, 2.0f, -1.0f, 2.0f, 8.0f, 2.0f, Dilation.NONE), ModelTransform.of((float)0.0f, (float)9.0f, (float)10.0f, (float)((float)Math.toRadians(22.5)), (float)0.0f, (float)0.0f));
        return TexturedModelData.of((ModelData)modelData, (int)60, (int)36);
    }

    public void internalMethod05907(float f, PlayerMotionTracker typedValue263) {
        super.resetTransforms();
        this.internalField0569.yaw = typedValue263.internalMethod07608() * ((float)Math.PI / 180);
        this.internalField0569.pitch = typedValue263.internalMethod09042() * ((float)Math.PI / 180);
        this.internalField1211.pitch = MathHelper.cos((float)(typedValue263.internalField1048 * 0.6662f)) * 1.4f * typedValue263.internalField0206;
        this.internalField1212.pitch = MathHelper.cos((float)(typedValue263.internalField1048 * 0.6662f + (float)Math.PI)) * 1.4f * typedValue263.internalField0206;
        this.internalField1210.pitch = MathHelper.cos((float)(typedValue263.internalField1048 * 0.6662f + (float)Math.PI)) * 1.4f * typedValue263.internalField0206;
        this.internalField1564.pitch = MathHelper.cos((float)(typedValue263.internalField1048 * 0.6662f)) * 1.4f * typedValue263.internalField0206;
        if (typedValue263.internalMethod07575()) {
            this.internalField1211.pitch = (float)Math.toRadians(-90.0);
            this.internalField1212.pitch = (float)Math.toRadians(-90.0);
            this.internalField1210.pitch = (float)Math.toRadians(90.0);
            this.internalField1564.pitch = (float)Math.toRadians(90.0);
            this.internalField1211.yaw = (float)Math.toRadians(-22.0);
            this.internalField1212.yaw = (float)Math.toRadians(22.0);
            this.internalField1210.yaw = (float)Math.toRadians(22.0);
            this.internalField1564.yaw = (float)Math.toRadians(-22.0);
        } else {
            this.internalField1211.yaw = 0.0f;
            this.internalField1212.yaw = 0.0f;
            this.internalField1210.yaw = 0.0f;
            this.internalField1564.yaw = 0.0f;
        }
        this.internalField1565.pitch = (float)Math.toRadians(typedValue263.internalMethod07575() ? 45.0 : 22.0);
        float f2 = typedValue263.internalMethod09043() ? 0.5f : (typedValue263.internalMethod07610() ? 0.3f : 0.15f);
        float f3 = typedValue263.internalMethod09043() ? 0.5f : 0.3f;
        this.internalField1565.roll = (float)(Math.toRadians(-22.5) + Math.toRadians(22.5) + (double)(MathHelper.cos((float)(f * f2)) * f3));
    }

    public void internalMethod01267(MatrixStack matrixStack, VertexConsumer vertexConsumer, int n, int n2, PlayerMotionTracker typedValue263, float f) {
        this.internalMethod05907(f, typedValue263);
        matrixStack.push();
        float f2 = 1.0f;
        matrixStack.scale(f2, f2, f2);
        matrixStack.translate(0.0, 1.3 - (typedValue263.internalMethod07575() ? 0.3 : 0.0), 0.0);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0f));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(typedValue263.internalMethod07573()));
        this.root.render(matrixStack, vertexConsumer, n, n2);
        matrixStack.pop();
    }
}
