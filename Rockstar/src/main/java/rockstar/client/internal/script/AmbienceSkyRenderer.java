package rockstar.client.internal.script;




import rockstar.client.internal.render.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.internal.render.VolumetricFogRenderer;
import rockstar.client.internal.render.VolumetricFogShader;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.internal.game.FogNoiseTexture;

public class AmbienceSkyRenderer
implements MinecraftClientAccess {
    private static final float internalField0205 = 0.07f;
    private static final float internalField0206 = 0.0224f;
    private static final float internalField1048 = 0.0152f;
    private static final float internalField1047 = -0.0091f;
    private static final float internalField1049 = 0.0134f;
    private static final float internalField1046 = 160.0f;
    private static final float internalField1456 = 5.0f;
    private static final float internalField1457 = 3.5f;
    private final FogNoiseTexture internalField0845 = new FogNoiseTexture();
    private final VolumetricFogRenderer internalField0442 = new VolumetricFogRenderer();
    private final VolumetricFogShader.InternalType0351 internalField0419 = new VolumetricFogShader.InternalType0351();
    private final Matrix4f internalField0788 = new Matrix4f();

    public AmbienceSkyRenderer() {
        this.internalField0442.internalMethod03568();
    }

    public void internalMethod04690(Camera camera, Matrix4f matrix4f, Matrix4f matrix4f2, ColorRGBA colorRGBA, float f, float f2, float f3, float f4, boolean bl) {
        this.internalField0419.internalField0227 = this.internalField0845.internalMethod06321();
        if (this.internalField0419.internalField0227 == 0) {
            return;
        }
        this.internalField0419.internalField0277 = bl;
        this.internalField0419.internalField1727 = bl ? 3.5f : 5.0f;
        Vec3d vec3d = camera.getCameraPos();
        this.internalField0419.internalField0205 = AmbienceSkyRenderer.internalMethod01142(vec3d.x, 1152.0f);
        this.internalField0419.internalField0206 = (float)vec3d.y;
        this.internalField0419.internalField1048 = AmbienceSkyRenderer.internalMethod01142(vec3d.z, 1152.0f);
        this.internalField0419.internalField1047 = colorRGBA.getRed() / 255.0f;
        this.internalField0419.internalField1049 = colorRGBA.getGreen() / 255.0f;
        this.internalField0419.internalField1046 = colorRGBA.getBlue() / 255.0f;
        this.internalField0419.internalField1460 = f * 0.07f;
        this.internalField0419.internalField1723 = 0.75f - f2 * 0.55f;
        this.internalField0419.internalField1461 = f3;
        this.internalField0419.internalField1462 = f4;
        this.internalField0419.internalField1731 = 0.0034722222f;
        this.internalField0419.internalField1455 = Math.min(160.0f, (float)((Integer)AmbienceSkyRenderer.internalField0149.options.getViewDistance().getValue()).intValue() * 16.0f);
        double d = (double)(System.currentTimeMillis() % 3600000L) / 1000.0;
        this.internalField0419.internalField1456 = AmbienceSkyRenderer.internalMethod01142((double)0.0224f * d, 288.0f);
        this.internalField0419.internalField1457 = AmbienceSkyRenderer.internalMethod01142((double)0.0152f * d, 288.0f);
        this.internalField0419.internalField1458 = AmbienceSkyRenderer.internalMethod01142((double)-0.0091f * d, 1152.0f);
        this.internalField0419.internalField1459 = AmbienceSkyRenderer.internalMethod01142((double)0.0134f * d, 1152.0f);
        this.internalField0788.set((Matrix4fc)matrix4f2).mul((Matrix4fc)matrix4f).invert();
        this.internalField0442.internalMethod03258(this.internalField0788, this.internalField0419);
    }

    private static float internalMethod01142(double d, float f) {
        return (float)(d - Math.floor(d / (double)f) * (double)f);
    }
}

