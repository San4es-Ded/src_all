package rockstar.client.internal.render;




import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.*;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import rockstar.client.compat.RenderSystem;
import lombok.Generated;
import rockstar.client.RockstarClient;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.ui.WindowAccess;
import rockstar.client.internal.render.GlowBlurShader;
import rockstar.client.render.ManagedFramebuffer;
import rockstar.client.render.RenderPipeline;

public class GlowBloomRenderer
implements MinecraftClientAccess,
WindowAccess {
    public static final Supplier<ManagedFramebuffer> internalField0233 = Suppliers.memoize(() -> new ManagedFramebuffer(false).internalMethod06013().internalMethod03472(0.5f));
    public static final Supplier<ManagedFramebuffer> internalField0234 = Suppliers.memoize(() -> new ManagedFramebuffer(false).internalMethod06013().internalMethod03472(0.5f));
    public static final Supplier<ManagedFramebuffer> internalField1061 = Suppliers.memoize(() -> new ManagedFramebuffer(false).internalMethod06013());
    private static GlowBlurShader internalField0317;
    private static GlowBlurShader internalField0318;
    private static GlowBlurShader internalField1120;
    private final Supplier<ManagedFramebuffer> internalField1062;
    private final Supplier<ManagedFramebuffer> internalField1064;
    private final Supplier<ManagedFramebuffer> internalField1063;
    private float internalField0206 = 2.5f;
    private float internalField1048 = 7.0f;
    private int internalField0227 = 8;
    private int internalField0228 = 3;
    private float internalField1047 = 1.0f;
    private float internalField1049 = 1.0f;
    private float internalField1046 = 0.0f;
    private float internalField1456 = 0.0f;

    public GlowBloomRenderer() {
        this(internalField0233, internalField0234, internalField1061);
    }

    public GlowBloomRenderer(Supplier<ManagedFramebuffer> supplier, Supplier<ManagedFramebuffer> supplier2, Supplier<ManagedFramebuffer> supplier3) {
        this.internalField1062 = supplier;
        this.internalField1064 = supplier2;
        this.internalField1063 = supplier3;
    }

    public static GlowBloomRenderer internalMethod06776() {
        Supplier supplier = Suppliers.memoize(() -> new ManagedFramebuffer(false).internalMethod06013().internalMethod03472(0.5f));
        Supplier supplier2 = Suppliers.memoize(() -> new ManagedFramebuffer(false).internalMethod06013().internalMethod03472(0.5f));
        Supplier supplier3 = Suppliers.memoize(() -> new ManagedFramebuffer(false).internalMethod06013());
        return new GlowBloomRenderer((Supplier<ManagedFramebuffer>)supplier, (Supplier<ManagedFramebuffer>)supplier2, (Supplier<ManagedFramebuffer>)supplier3);
    }

    public void internalMethod03093() {
        if (internalField0317 == null) {
            internalField0317 = new GlowBlurShader(RockstarClient.id("glow/blur_h/data"));
            internalField0318 = new GlowBlurShader(RockstarClient.id("glow/blur_v/data"));
            internalField1120 = new GlowBlurShader(RockstarClient.id("glow/composite/data"));
        }
    }

    public void internalMethod03006(ManagedFramebuffer typedValue245) {
        this.internalMethod02623(typedValue245, -1, -1, -1, -1);
    }

    public void internalMethod02623(ManagedFramebuffer typedValue245, int n, int n2, int n3, int n4) {
        int n5;
        ManagedFramebuffer typedValue246 = (ManagedFramebuffer)this.internalField1062.get();
        ManagedFramebuffer typedValue247 = (ManagedFramebuffer)this.internalField1064.get();
        ManagedFramebuffer typedValue248 = (ManagedFramebuffer)this.internalField1063.get();
        int n6 = this.internalField0228;
        int n7 = internalField0267.getScaledWidth();
        int n8 = internalField0267.getScaledHeight();
        boolean bl = n >= 0;
        int n9 = internalField0149.getWindow().getFramebufferWidth();
        int n10 = internalField0149.getWindow().getFramebufferHeight();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        typedValue246.internalMethod03245();
        if (bl) {
            GlowBloomRenderer.internalMethod03633(typedValue246, n, n2, n3, n4, n9, n10);
        }
        internalField0317.internalMethod01220();
        internalField0317.internalMethod03262(this.internalField0206, typedValue246.textureWidth, typedValue246.textureHeight);
        RenderSystem.setShaderTexture(0, typedValue245.getColorAttachmentView());
        for (n5 = 0; n5 < n6; ++n5) {
            RenderPipeline.internalMethod01737(0.0f, 0.0f, n7, n8);
        }
        if (bl) {
            RenderSystem.disableScissor();
        }
        typedValue246.internalMethod03248();
        typedValue247.internalMethod03245();
        if (bl) {
            GlowBloomRenderer.internalMethod03633(typedValue247, n, n2, n3, n4, n9, n10);
        }
        internalField0318.internalMethod01220();
        internalField0318.internalMethod03262(this.internalField0206, typedValue246.textureWidth, typedValue246.textureHeight);
        RenderSystem.setShaderTexture(0, typedValue246.getColorAttachmentView());
        for (n5 = 0; n5 < n6; ++n5) {
            RenderPipeline.internalMethod01737(0.0f, 0.0f, n7, n8);
        }
        if (bl) {
            RenderSystem.disableScissor();
        }
        typedValue247.internalMethod03248();
        for (n5 = 1; n5 < this.internalField0227; ++n5) {
            int n11;
            typedValue246.internalMethod03245();
            if (bl) {
                GlowBloomRenderer.internalMethod03633(typedValue246, n, n2, n3, n4, n9, n10);
            }
            internalField0317.internalMethod01220();
            internalField0317.internalMethod03262(this.internalField0206, typedValue246.textureWidth, typedValue246.textureHeight);
            RenderSystem.setShaderTexture(0, typedValue247.getColorAttachmentView());
            for (n11 = 0; n11 < n6; ++n11) {
                RenderPipeline.internalMethod01737(0.0f, 0.0f, n7, n8);
            }
            if (bl) {
                RenderSystem.disableScissor();
            }
            typedValue246.internalMethod03248();
            typedValue247.internalMethod03245();
            if (bl) {
                GlowBloomRenderer.internalMethod03633(typedValue247, n, n2, n3, n4, n9, n10);
            }
            internalField0318.internalMethod01220();
            internalField0318.internalMethod03262(this.internalField0206, typedValue246.textureWidth, typedValue246.textureHeight);
            RenderSystem.setShaderTexture(0, typedValue246.getColorAttachmentView());
            for (n11 = 0; n11 < n6; ++n11) {
                RenderPipeline.internalMethod01737(0.0f, 0.0f, n7, n8);
            }
            if (bl) {
                RenderSystem.disableScissor();
            }
            typedValue247.internalMethod03248();
        }
        typedValue248.internalMethod03245();
        if (bl) {
            GlowBloomRenderer.internalMethod03633(typedValue248, n, n2, n3, n4, n9, n10);
        }
        internalField1120.internalMethod01220();
        internalField1120.internalMethod04140(this.internalField1048, typedValue245.textureWidth, typedValue245.textureHeight, this.internalField1047, this.internalField1049, this.internalField1046, this.internalField1456);
        RenderSystem.setShaderTexture(0, typedValue245.getColorAttachmentView());
        RenderSystem.setShaderTexture(1, typedValue247.getColorAttachmentView());
        RenderPipeline.internalMethod01737(0.0f, 0.0f, n7, n8);
        if (bl) {
            RenderSystem.disableScissor();
        }
        typedValue248.internalMethod03248();
        RenderSystem.setShaderTexture((int)0, (int)0);
        RenderSystem.setShaderTexture((int)1, (int)0);
        RenderSystem.disableBlend();
    }

    public void internalMethod05694(ManagedFramebuffer typedValue245) {
        this.internalMethod01499(typedValue245, -1, -1, -1, -1);
    }

    public void internalMethod01499(ManagedFramebuffer typedValue245, int n, int n2, int n3, int n4) {
        int n5;
        ManagedFramebuffer typedValue246 = (ManagedFramebuffer)this.internalField1062.get();
        ManagedFramebuffer typedValue247 = (ManagedFramebuffer)this.internalField1064.get();
        int n6 = this.internalField0228;
        int n7 = internalField0267.getScaledWidth();
        int n8 = internalField0267.getScaledHeight();
        boolean bl = n >= 0;
        int n9 = internalField0149.getWindow().getFramebufferWidth();
        int n10 = internalField0149.getWindow().getFramebufferHeight();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        typedValue246.internalMethod03245();
        if (bl) {
            GlowBloomRenderer.internalMethod03633(typedValue246, n, n2, n3, n4, n9, n10);
        }
        internalField0317.internalMethod01220();
        internalField0317.internalMethod03262(this.internalField0206, typedValue246.textureWidth, typedValue246.textureHeight);
        RenderSystem.setShaderTexture(0, typedValue245.getColorAttachmentView());
        for (n5 = 0; n5 < n6; ++n5) {
            RenderPipeline.internalMethod01737(0.0f, 0.0f, n7, n8);
        }
        if (bl) {
            RenderSystem.disableScissor();
        }
        typedValue246.internalMethod03248();
        typedValue247.internalMethod03245();
        if (bl) {
            GlowBloomRenderer.internalMethod03633(typedValue247, n, n2, n3, n4, n9, n10);
        }
        internalField0318.internalMethod01220();
        internalField0318.internalMethod03262(this.internalField0206, typedValue246.textureWidth, typedValue246.textureHeight);
        RenderSystem.setShaderTexture(0, typedValue246.getColorAttachmentView());
        for (n5 = 0; n5 < n6; ++n5) {
            RenderPipeline.internalMethod01737(0.0f, 0.0f, n7, n8);
        }
        if (bl) {
            RenderSystem.disableScissor();
        }
        typedValue247.internalMethod03248();
        for (n5 = 1; n5 < this.internalField0227; ++n5) {
            int n11;
            typedValue246.internalMethod03245();
            if (bl) {
                GlowBloomRenderer.internalMethod03633(typedValue246, n, n2, n3, n4, n9, n10);
            }
            internalField0317.internalMethod01220();
            internalField0317.internalMethod03262(this.internalField0206, typedValue246.textureWidth, typedValue246.textureHeight);
            RenderSystem.setShaderTexture(0, typedValue247.getColorAttachmentView());
            for (n11 = 0; n11 < n6; ++n11) {
                RenderPipeline.internalMethod01737(0.0f, 0.0f, n7, n8);
            }
            if (bl) {
                RenderSystem.disableScissor();
            }
            typedValue246.internalMethod03248();
            typedValue247.internalMethod03245();
            if (bl) {
                GlowBloomRenderer.internalMethod03633(typedValue247, n, n2, n3, n4, n9, n10);
            }
            internalField0318.internalMethod01220();
            internalField0318.internalMethod03262(this.internalField0206, typedValue246.textureWidth, typedValue246.textureHeight);
            RenderSystem.setShaderTexture(0, typedValue246.getColorAttachmentView());
            for (n11 = 0; n11 < n6; ++n11) {
                RenderPipeline.internalMethod01737(0.0f, 0.0f, n7, n8);
            }
            if (bl) {
                RenderSystem.disableScissor();
            }
            typedValue247.internalMethod03248();
        }
        RenderSystem.setShaderTexture((int)0, (int)0);
        RenderSystem.disableBlend();
    }

    private static void internalMethod03633(ManagedFramebuffer typedValue245, int n, int n2, int n3, int n4, int n5, int n6) {
        if (n5 <= 0 || n6 <= 0) {
            return;
        }
        float f = (float)typedValue245.textureWidth / (float)n5;
        float f2 = (float)typedValue245.textureHeight / (float)n6;
        int n7 = (int)Math.floor((float)n * f);
        int n8 = (int)Math.floor((float)n2 * f2);
        int n9 = (int)Math.ceil((float)n3 * f);
        int n10 = (int)Math.ceil((float)n4 * f2);
        if (n7 < 0) {
            n9 += n7;
            n7 = 0;
        }
        if (n8 < 0) {
            n10 += n8;
            n8 = 0;
        }
        if (n7 + n9 > typedValue245.textureWidth) {
            n9 = typedValue245.textureWidth - n7;
        }
        if (n8 + n10 > typedValue245.textureHeight) {
            n10 = typedValue245.textureHeight - n8;
        }
        if (n9 > 0 && n10 > 0) {
            RenderSystem.enableScissor((int)n7, (int)n8, (int)n9, (int)n10);
        }
    }

    public int internalMethod03092() {
        return rockstar.client.render.FramebufferCompat.glId(((ManagedFramebuffer)this.internalField1063.get()).getColorAttachment());
    }

    public int internalMethod03125() {
        return rockstar.client.render.FramebufferCompat.glId(((ManagedFramebuffer)this.internalField1064.get()).getColorAttachment());
    }

    public ManagedFramebuffer internalMethod05596() {
        return (ManagedFramebuffer)this.internalField1063.get();
    }

    public ManagedFramebuffer internalMethod06210() {
        return (ManagedFramebuffer)this.internalField1064.get();
    }

    public static int internalMethod08936() {
        return rockstar.client.render.FramebufferCompat.glId(((ManagedFramebuffer)internalField1061.get()).getColorAttachment());
    }

    @Generated
    public void internalMethod06503(float f) {
        this.internalField0206 = f;
    }

    @Generated
    public void internalMethod06579(float f) {
        this.internalField1048 = f;
    }

    @Generated
    public void internalMethod06504(int n) {
        this.internalField0227 = n;
    }

    @Generated
    public void internalMethod06580(int n) {
        this.internalField0228 = n;
    }

    @Generated
    public void internalMethod08555(float f) {
        this.internalField1047 = f;
    }

    @Generated
    public void internalMethod08567(float f) {
        this.internalField1049 = f;
    }

    @Generated
    public void internalMethod08896(float f) {
        this.internalField1046 = f;
    }

    @Generated
    public void internalMethod08914(float f) {
        this.internalField1456 = f;
    }
}
