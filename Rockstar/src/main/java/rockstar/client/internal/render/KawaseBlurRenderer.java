package rockstar.client.internal.render;





import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.*;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gl.Framebuffer;
import org.lwjgl.opengl.GL11;
import rockstar.client.RockstarClient;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.ui.WindowAccess;
import rockstar.client.render.ManagedFramebuffer;
import rockstar.client.render.RenderPipeline;
import rockstar.client.render.UiBatchRenderer;
import rockstar.client.util.Stopwatch;
import rockstar.client.internal.render.KawasePassShader;

public class KawaseBlurRenderer
implements MinecraftClientAccess,
WindowAccess {
    public static final int internalField0227 = 0;
    public static final int internalField0228 = 1;
    private final List<InternalType0117> internalField0416 = new ArrayList<InternalType0117>();
    private KawasePassShader internalField0341;
    private KawasePassShader internalField0342;
    private float internalField0205 = 0.5f;

    public void internalMethod02986() {
        this.internalField0341 = new KawasePassShader(RockstarClient.id("kawase_down/data"));
        this.internalField0342 = new KawasePassShader(RockstarClient.id("kawase_up/data"));
    }

    private InternalType0117 internalMethod00148(int n) {
        while (this.internalField0416.size() <= n) {
            this.internalField0416.add(new InternalType0117());
        }
        return this.internalField0416.get(n);
    }

    public void internalMethod02988() {
        for (InternalType0117 nestedValue0051 : this.internalField0416) {
            nestedValue0051.internalField0277 = true;
        }
    }

    public void internalMethod02311(int n) {
        this.internalMethod00148((int)n).internalField0277 = true;
    }

    public boolean internalMethod02987() {
        return this.internalMethod02312(0);
    }

    public boolean internalMethod02312(int n) {
        return n >= 0 && n < this.internalField0416.size() && this.internalField0416.get((int)n).internalField0276;
    }

    public void internalMethod02309(float f) {
        this.internalMethod03955(0, f);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void internalMethod03955(int n, float f) {
        if (this.internalField0341 == null || RockstarClient.getInstance().internalMethod06896()) {
            return;
        }
        if (UiBatchRenderer.internalField0277) {
            return;
        }
        InternalType0117 nestedValue0051 = this.internalMethod00148(n);
        if (!nestedValue0051.internalField0277 && !nestedValue0051.internalField0519.internalMethod02365(25L)) {
            return;
        }
        nestedValue0051.internalField0277 = false;
        nestedValue0051.internalField0519.internalMethod00701();
        Framebuffer framebuffer = internalField0149.getFramebuffer();
        ManagedFramebuffer typedValue245 = (ManagedFramebuffer)nestedValue0051.internalField0233.get();
        ManagedFramebuffer typedValue246 = (ManagedFramebuffer)nestedValue0051.internalField0234.get();
        typedValue245.internalMethod03472(this.internalField0205).internalMethod06013();
        typedValue246.internalMethod03472(this.internalField0205).internalMethod06013();
        boolean bl = GL11.glIsEnabled((int)3089);
        if (bl) {
            GL11.glDisable((int)3089);
        }
        try {
            int n2;
            int n3;
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            this.internalField0341.internalMethod01220();
            this.internalField0341.internalMethod02942(f, framebuffer.textureWidth, framebuffer.textureHeight);
            typedValue245.internalMethod03245();
            RenderSystem.setShaderTexture(0, framebuffer.getColorAttachmentView());
            RenderPipeline.internalMethod01737(0.0f, 0.0f, internalField0267.getScaledWidth(), internalField0267.getScaledHeight());
            typedValue245.internalMethod03248();
            ManagedFramebuffer[] iIiiIIiII_Class357Array = new ManagedFramebuffer[]{typedValue245, typedValue246};
            int n4 = f > 5.0f ? 7 : (f > 3.0f ? 5 : 3);
            for (n3 = 1; n3 < n4; ++n3) {
                n2 = n3 % 2;
                iIiiIIiII_Class357Array[n2].internalMethod03245();
                RenderSystem.setShaderTexture(0, iIiiIIiII_Class357Array[(n2 + 1) % 2].getColorAttachmentView());
                this.internalField0341.internalMethod02942(f, iIiiIIiII_Class357Array[(n2 + 1) % 2].textureWidth, iIiiIIiII_Class357Array[(n2 + 1) % 2].textureHeight);
                RenderPipeline.internalMethod01737(0.0f, 0.0f, internalField0267.getScaledWidth(), internalField0267.getScaledHeight());
                iIiiIIiII_Class357Array[n2].internalMethod03248();
            }
            this.internalField0342.internalMethod01220();
            for (n3 = 0; n3 < n4; ++n3) {
                n2 = n3 % 2;
                iIiiIIiII_Class357Array[(n2 + 1) % 2].internalMethod03245();
                RenderSystem.setShaderTexture(0, iIiiIIiII_Class357Array[n2].getColorAttachmentView());
                this.internalField0342.internalMethod02942(f, iIiiIIiII_Class357Array[n2].textureWidth, iIiiIIiII_Class357Array[n2].textureHeight);
                RenderPipeline.internalMethod01737(0.0f, 0.0f, internalField0267.getScaledWidth(), internalField0267.getScaledHeight());
                iIiiIIiII_Class357Array[n2].internalMethod03248();
            }
            RenderSystem.setShaderTexture((int)0, (int)0);
            RenderSystem.disableBlend();
            nestedValue0051.internalField0276 = true;
        }
        finally {
            if (bl) {
                GL11.glEnable((int)3089);
            }
        }
    }

    public int internalMethod02985() {
        return this.internalMethod02310(0);
    }

    public int internalMethod02310(int n) {
        return rockstar.client.render.FramebufferCompat.glId(((ManagedFramebuffer)this.internalMethod00148((int)n).internalField0234.get()).getColorAttachment());
    }

    static final class InternalType0117 {
        final Supplier<ManagedFramebuffer> internalField0233 = Suppliers.memoize(() -> new ManagedFramebuffer(false).internalMethod06013());
        final Supplier<ManagedFramebuffer> internalField0234 = Suppliers.memoize(() -> new ManagedFramebuffer(false).internalMethod06013());
        final Stopwatch internalField0519 = new Stopwatch();
        boolean internalField0277 = true;
        boolean internalField0276 = false;

        InternalType0117() {
        }
    }
}

