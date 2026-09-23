package rockstar.client.internal.game;




import rockstar.client.animation.*;
import rockstar.client.internal.render.*;
import rockstar.client.*;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import rockstar.client.internal.render.RainGlassShader;
import rockstar.client.RockstarClient;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.internal.render.AmbientParticleShader;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.internal.render.AmbientParticleRenderer;
import rockstar.client.internal.game.RainHeightmapTexture;

public class RainShaderRenderer
implements MinecraftClientAccess {
    private static final float internalField0205 = 24.0f;
    private static final float internalField0206 = 0.034f;
    private static final float internalField1048 = 17.0f;
    private static final float internalField1047 = 42.0f;
    private static final float internalField1049 = 30.0f;
    private static final float internalField1046 = 0.0f;
    private static final int internalField0227 = 4000;
    private final AmbientParticleRenderer internalField0559 = new AmbientParticleRenderer();
    private final RainHeightmapTexture internalField0183 = new RainHeightmapTexture();
    private final RainGlassShader internalField0436 = new RainGlassShader(RockstarClient.id("rain_screen/data"));
    private final RainGlassShader.InternalType0344 internalField0168 = new RainGlassShader.InternalType0344();
    private final Matrix4f internalField0788 = new Matrix4f();
    private final Matrix4f internalField0787 = new Matrix4f();
    private final Vector3f internalField0310 = new Vector3f();
    private final Vector3f internalField0311 = new Vector3f();
    private final AnimatedValue internalField0808 = new AnimatedValue(350L, 1.0f, Easing.internalField1627);

    public void internalMethod05726(Camera camera, Matrix4f matrix4f, Matrix4f matrix4f2, float f, float f2, float f3, float f4) {
        if (RainShaderRenderer.internalField0149.world == null || RainShaderRenderer.internalField0149.player == null) {
            return;
        }
        Vec3d vec3d = camera.getCameraPos();
        this.internalField0183.internalMethod03532(vec3d.x, vec3d.y, vec3d.z);
        this.internalMethod01618(camera, f2);
        this.internalField0788.set((Matrix4fc)matrix4f2).mul((Matrix4fc)matrix4f);
        this.internalField0559.internalMethod03973(this.internalField0788, this.internalMethod01995(f), false, this.internalField0183.internalMethod01292() ? this.internalField0183.internalMethod05377() : null);
        float f5 = this.internalField0808.internalMethod07059(RainShaderRenderer.internalField0149.world.isSkyVisible(BlockPos.ofFloored((Position)RainShaderRenderer.internalField0149.player.getEyePos())) ? 1.0f : 0.0f);
        float f6 = f3 * f5;
        if (f6 <= 0.01f && f4 <= 0.01f) {
            return;
        }
        AmbientParticleShader.InternalType0476 nestedValue0168 = this.internalField0559.internalMethod07369();
        this.internalField0168.internalField0205 = nestedValue0168.internalField0205;
        this.internalField0168.internalField0206 = nestedValue0168.internalField0206;
        this.internalField0168.internalField1048 = nestedValue0168.internalField1048;
        this.internalField0168.internalField1047 = 0.69803923f;
        this.internalField0168.internalField1049 = 0.76862746f;
        this.internalField0168.internalField1046 = 0.8392157f;
        this.internalField0168.internalField1456 = nestedValue0168.internalField1718;
        this.internalField0168.internalField1457 = f6;
        this.internalField0168.internalField1458 = f4;
        this.internalField0168.internalField1459 = this.internalField0183.internalMethod01289();
        this.internalField0168.internalField1460 = this.internalField0183.internalMethod01330();
        this.internalField0168.internalField1461 = this.internalField0183.internalMethod08240();
        this.internalField0168.internalField1455 = 192.0f;
        this.internalField0168.internalField1462 = this.internalField0183.internalMethod01292() ? 1.0f : 0.0f;
        this.internalField0168.internalField0227 = this.internalField0183.internalMethod01290();
        this.internalField0787.set((Matrix4fc)this.internalField0788).invert();
        this.internalField0436.internalMethod01779(this.internalField0787, this.internalField0168);
    }

    public void internalMethod01427() {
        this.internalField0559.internalMethod05645();
        this.internalField0183.internalMethod01291();
    }

    private int internalMethod01995(float f) {
        return Math.max(1, Math.round(4000.0f * f));
    }

    private void internalMethod01618(Camera camera, float f) {
        AmbientParticleShader.InternalType0476 nestedValue0168 = this.internalField0559.internalMethod07369();
        Vec3d vec3d = camera.getCameraPos();
        nestedValue0168.internalField0205 = (float)vec3d.x;
        nestedValue0168.internalField0206 = (float)vec3d.y;
        nestedValue0168.internalField1048 = (float)vec3d.z;
        Quaternionf quaternionf = camera.getRotation();
        quaternionf.transform(this.internalField0310.set(1.0f, 0.0f, 0.0f));
        quaternionf.transform(this.internalField0311.set(0.0f, 1.0f, 0.0f));
        nestedValue0168.internalField1047 = this.internalField0310.x;
        nestedValue0168.internalField1049 = this.internalField0310.y;
        nestedValue0168.internalField1046 = this.internalField0310.z;
        nestedValue0168.internalField1456 = this.internalField0311.x;
        nestedValue0168.internalField1457 = this.internalField0311.y;
        nestedValue0168.internalField1458 = this.internalField0311.z;
        nestedValue0168.internalField1459 = 0.0f;
        nestedValue0168.internalField1460 = -24.0f;
        nestedValue0168.internalField1461 = 0.0f;
        nestedValue0168.internalField1462 = 42.0f;
        nestedValue0168.internalField1455 = 30.0f;
        nestedValue0168.internalField1723 = 42.0f;
        nestedValue0168.internalField1719 = 0.034f;
        nestedValue0168.internalField1720 = 17.0f;
        nestedValue0168.internalField1721 = 0.05f;
        nestedValue0168.internalField1722 = 0.0f;
        nestedValue0168.internalField1731 = 0.69803923f;
        nestedValue0168.internalField1727 = 0.76862746f;
        nestedValue0168.internalField1728 = 0.8392157f;
        nestedValue0168.internalField1717 = f;
        nestedValue0168.internalField1730 = this.internalField0183.internalMethod01289();
        nestedValue0168.internalField1729 = this.internalField0183.internalMethod01330();
        nestedValue0168.internalField1725 = this.internalField0183.internalMethod08240();
        nestedValue0168.internalField1724 = 192.0f;
        nestedValue0168.internalField1726 = this.internalField0183.internalMethod01292() ? 1.0f : 0.0f;
        nestedValue0168.internalField1718 = (float)(System.currentTimeMillis() % 3600000L) / 1000.0f;
    }
}

