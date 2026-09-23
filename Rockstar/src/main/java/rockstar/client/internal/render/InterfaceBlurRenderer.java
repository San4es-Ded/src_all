package rockstar.client.internal.render;





import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.*;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import rockstar.client.compat.RenderSystem;
import lombok.Generated;
import net.minecraft.client.gl.Framebuffer;
import rockstar.modules.visual.InterfaceModule;
import rockstar.client.RockstarClient;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.ui.WindowAccess;
import rockstar.client.render.ManagedFramebuffer;
import rockstar.client.render.RenderPipeline;
import rockstar.client.util.Stopwatch;
import rockstar.client.internal.render.KawasePassShader;

public class InterfaceBlurRenderer
implements MinecraftClientAccess,
WindowAccess {
    public static final Supplier<ManagedFramebuffer> internalField0233 = Suppliers.memoize(() -> new ManagedFramebuffer(false).internalMethod06013());
    public static final Supplier<ManagedFramebuffer> internalField0234 = Suppliers.memoize(() -> new ManagedFramebuffer(false).internalMethod06013());
    public static Framebuffer internalField0357;
    private final Stopwatch internalField0519 = new Stopwatch();
    private static KawasePassShader internalField0341;
    private static KawasePassShader internalField0342;
    private float internalField0205 = 1.0f;
    private float internalField0206 = 0.5f;

    public void internalMethod05154() {
        internalField0341 = new KawasePassShader(RockstarClient.id("kawase_down/data"));
        internalField0342 = new KawasePassShader(RockstarClient.id("kawase_up/data"));
    }

    public void internalMethod05186() {
        this.internalMethod02201(InterfaceModule.internalMethod09719() ? 0.1f : 4.0f);
    }

    public void internalMethod02201(float f) {
        int n;
        int n2;
        if (!this.internalField0519.internalMethod02365(25L) || RockstarClient.getInstance().internalMethod06896()) {
            return;
        }
        internalField0357 = internalField0149.getFramebuffer();
        this.internalField0205 = f;
        ManagedFramebuffer typedValue245 = (ManagedFramebuffer)internalField0233.get();
        ManagedFramebuffer typedValue246 = (ManagedFramebuffer)internalField0234.get();
        typedValue245.internalMethod03472(this.internalField0206).internalMethod06013();
        typedValue246.internalMethod03472(this.internalField0206).internalMethod06013();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        internalField0341.internalMethod01220();
        internalField0341.internalMethod02942(this.internalField0205, InterfaceBlurRenderer.internalField0357.textureWidth, InterfaceBlurRenderer.internalField0357.textureHeight);
        typedValue245.internalMethod03245();
        RenderSystem.setShaderTexture(0, internalField0357.getColorAttachmentView());
        RenderPipeline.internalMethod01737(0.0f, 0.0f, internalField0267.getScaledWidth(), internalField0267.getScaledHeight());
        typedValue245.internalMethod03248();
        ManagedFramebuffer[] iIiiIIiII_Class357Array = new ManagedFramebuffer[]{typedValue245, typedValue246};
        int n3 = this.internalField0205 > 5.0f ? 7 : (this.internalField0205 > 3.0f ? 5 : 3);
        for (n2 = 1; n2 < n3; ++n2) {
            n = n2 % 2;
            iIiiIIiII_Class357Array[n].internalMethod03245();
            RenderSystem.setShaderTexture(0, iIiiIIiII_Class357Array[(n + 1) % 2].getColorAttachmentView());
            internalField0341.internalMethod02942(this.internalField0205, iIiiIIiII_Class357Array[(n + 1) % 2].textureWidth, iIiiIIiII_Class357Array[(n + 1) % 2].textureHeight);
            RenderPipeline.internalMethod01737(0.0f, 0.0f, internalField0267.getScaledWidth(), internalField0267.getScaledHeight());
            iIiiIIiII_Class357Array[n].internalMethod03248();
        }
        internalField0342.internalMethod01220();
        for (n2 = 0; n2 < n3; ++n2) {
            n = n2 % 2;
            iIiiIIiII_Class357Array[(n + 1) % 2].internalMethod03245();
            RenderSystem.setShaderTexture(0, iIiiIIiII_Class357Array[n].getColorAttachmentView());
            internalField0342.internalMethod02942(this.internalField0205, iIiiIIiII_Class357Array[n].textureWidth, iIiiIIiII_Class357Array[n].textureHeight);
            RenderPipeline.internalMethod01737(0.0f, 0.0f, internalField0267.getScaledWidth(), internalField0267.getScaledHeight());
            iIiiIIiII_Class357Array[n].internalMethod03248();
        }
        RenderSystem.setShaderTexture((int)0, (int)0);
        RenderSystem.disableBlend();
        this.internalField0519.internalMethod00701();
    }

    public static int internalMethod05153() {
        return rockstar.client.render.FramebufferCompat.glId(((ManagedFramebuffer)internalField0234.get()).getColorAttachment());
    }

    @Generated
    public void internalMethod02255(float f) {
        this.internalField0205 = f;
    }

    @Generated
    public void internalMethod07678(float f) {
        this.internalField0206 = f;
    }
}
