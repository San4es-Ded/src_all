package rockstar.client.internal.render;


import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import rockstar.client.render.compat.ImmediateRenderer;
import com.mojang.blaze3d.pipeline.BlendFunction;
import java.util.Random;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import rockstar.client.RockstarClient;
import rockstar.client.internal.render.AmbientParticleShader;

public class AmbientParticleRenderer {
    private final AmbientParticleShader internalField0106 = new AmbientParticleShader(RockstarClient.id("ambient_particles/data"));
    private final AmbientParticleShader.InternalType0476 internalField0738 = new AmbientParticleShader.InternalType0476();
    private int internalField0227;
    private float internalField0205;
    private float internalField0206;
    private float internalField1048;

    public AmbientParticleShader.InternalType0476 internalMethod07369() {
        return this.internalField0738;
    }

    public void internalMethod03973(Matrix4f matrix4f, int n, boolean bl, Identifier identifier) {
        if (n <= 0) {
            return;
        }
        if (!this.internalMethod00464(n)) {
            return;
        }
        this.internalField0106.internalMethod05560(matrix4f, this.internalField0738);
        this.internalField0106.internalMethod01220();
        if (identifier != null) {
            RenderSystem.setShaderTexture(0, identifier);
        }
        ImmediateRenderer.setBlend(bl ? BlendFunction.ADDITIVE : BlendFunction.TRANSLUCENT);
        ImmediateRenderer.drawWorldImmediate(this.internalMethod02123());
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public void internalMethod05645() {
        this.internalField0227 = 0;
    }

    private boolean internalMethod00464(int n) {
        boolean bl;
        boolean bl2 = bl = this.internalField0205 == this.internalField0738.internalField1462 && this.internalField0206 == this.internalField0738.internalField1455 && this.internalField1048 == this.internalField0738.internalField1723;
        if (this.internalField0227 == n && bl) {
            return true;
        }
        this.internalMethod05645();
        this.internalField0227 = n;
        this.internalField0205 = this.internalField0738.internalField1462;
        this.internalField0206 = this.internalField0738.internalField1455;
        this.internalField1048 = this.internalField0738.internalField1723;
        return true;
    }

    private static void internalMethod05947(VertexConsumer vertexConsumer, int n, float f, float f2, float f3) {
        Random random = new Random(1592635409L);
        for (int i = 0; i < n; ++i) {
            float f4 = random.nextFloat() * f;
            float f5 = random.nextFloat() * f2;
            float f6 = random.nextFloat() * f3;
            int n2 = (int)(random.nextFloat() * 255.0f);
            int n3 = (int)(random.nextFloat() * 255.0f);
            int n4 = (int)(random.nextFloat() * 255.0f);
            int n5 = 150 + (int)(random.nextFloat() * 105.0f);
            vertexConsumer.vertex(f4, f5, f6).texture(0.0f, 0.0f).color(n2, n3, n4, n5);
            vertexConsumer.vertex(f4, f5, f6).texture(0.0f, 1.0f).color(n2, n3, n4, n5);
            vertexConsumer.vertex(f4, f5, f6).texture(1.0f, 1.0f).color(n2, n3, n4, n5);
            vertexConsumer.vertex(f4, f5, f6).texture(1.0f, 0.0f).color(n2, n3, n4, n5);
        }
    }

    private BuiltBuffer internalMethod02123() {
        BufferBuilder builder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        internalMethod05947(builder, this.internalField0227, this.internalField0205, this.internalField0206, this.internalField1048);
        return builder.end();
    }
}
