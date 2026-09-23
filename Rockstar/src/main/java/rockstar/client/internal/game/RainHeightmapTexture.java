package rockstar.client.internal.game;


import rockstar.client.*;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap;
import rockstar.client.RockstarClient;
import rockstar.client.MinecraftClientAccess;

public class RainHeightmapTexture
implements MinecraftClientAccess {
    public static final int internalField0227 = 96;
    public static final float internalField0205 = 2.0f;
    public static final float internalField0206 = 192.0f;
    private final Identifier internalField0354 = RockstarClient.id("dynamic/rain_heightmap");
    private NativeImageBackedTexture internalField0789;
    private boolean internalField0277;
    private float internalField1047;
    private float internalField1049;
    private float internalField1046;
    private long internalField0230;
    private boolean internalField0276;

    public Identifier internalMethod05377() {
        return this.internalField0354;
    }

    public boolean internalMethod01292() {
        return this.internalField0276;
    }

    public int internalMethod01290() {
        return this.internalField0789 == null ? 0 : rockstar.client.render.FramebufferCompat.glId(this.internalField0789.getGlTexture());
    }

    public float internalMethod01289() {
        return this.internalField1047;
    }

    public float internalMethod01330() {
        return this.internalField1049;
    }

    public float internalMethod08240() {
        return this.internalField1046;
    }

    public void internalMethod03532(double d, double d2, double d3) {
        boolean bl;
        ClientWorld clientWorld = RainHeightmapTexture.internalField0149.world;
        if (clientWorld == null) {
            return;
        }
        long l = System.currentTimeMillis();
        float f = (float)d - 96.0f;
        float f2 = (float)d3 - 96.0f;
        boolean bl2 = bl = Math.abs(f - this.internalField1047) > 8.0f || Math.abs(f2 - this.internalField1049) > 8.0f;
        if (this.internalField0276 && !bl && l - this.internalField0230 < 500L) {
            return;
        }
        this.internalField0230 = l;
        this.internalField1047 = f;
        this.internalField1049 = f2;
        this.internalField1046 = (float)d2;
        this.internalMethod01332();
        NativeImage nativeImage = this.internalField0789.getImage();
        if (nativeImage == null) {
            return;
        }
        for (int i = 0; i < 96; ++i) {
            int n = (int)Math.floor(this.internalField1049 + (float)i * 2.0f);
            for (int j = 0; j < 96; ++j) {
                int n2 = (int)Math.floor(this.internalField1047 + (float)j * 2.0f);
                int n3 = clientWorld.getTopY(Heightmap.Type.MOTION_BLOCKING, n2, n);
                int n4 = MathHelper.clamp((int)(Math.round((float)n3 - this.internalField1046) + 128), (int)0, (int)255);
                nativeImage.setColor(j, i, 0xFF000000 | n4 << 16 | n4 << 8 | n4);
            }
        }
        this.internalField0789.upload();
        this.internalField0276 = true;
    }

    private void internalMethod01332() {
        if (this.internalField0789 == null) {
            this.internalField0789 = new NativeImageBackedTexture("Rockstar generated image", 96, 96, false);
        }
        if (!this.internalField0277) {
            internalField0149.getTextureManager().registerTexture(this.internalField0354, (AbstractTexture)this.internalField0789);
            this.internalField0277 = true;
        }
    }

    public void internalMethod01291() {
        if (this.internalField0789 != null) {
            this.internalField0789.close();
            this.internalField0789 = null;
        }
        if (this.internalField0277) {
            internalField0149.getTextureManager().destroyTexture(this.internalField0354);
            this.internalField0277 = false;
        }
        this.internalField0276 = false;
    }

    public static int internalMethod01331() {
        return 128;
    }
}
