package rockstar.client.render;



import rockstar.client.ui.*;
import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import net.minecraft.client.gl.Framebuffer;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.ui.WindowAccess;

public class ManagedFramebuffer
extends Framebuffer
implements MinecraftClientAccess,
WindowAccess {
    private boolean internalField0277;
    private float internalField0205 = 1.0f;
    private int clearColor;

    public ManagedFramebuffer(boolean bl) {
        super("Rockstar managed framebuffer", bl);
    }

    public ManagedFramebuffer(int n, int n2, boolean bl) {
        super("Rockstar managed framebuffer", bl);
        this.resize(n, n2);
    }

    public ManagedFramebuffer internalMethod06013() {
        this.internalField0277 = true;
        return this;
    }

    public void initFbo(int width, int height) {
        super.initFbo(width, height);
    }

    public ManagedFramebuffer internalMethod03472(float f) {
        this.internalField0205 = Math.max(0.1f, Math.min(1.0f, f));
        return this;
    }

    public void setTexFilter(int texFilter) {
        // Filtering is selected by the sampler bound to each explicit render pass.
    }

    public void setClearColor(float red, float green, float blue, float alpha) {
        this.clearColor = FramebufferCompat.color(red, green, blue, alpha);
    }

    public void clear() {
        FramebufferCompat.clear(this, this.clearColor);
    }

    public void beginWrite(boolean setViewport) {
        FramebufferCompat.beginWrite(this, setViewport);
    }

    public void endWrite() {
        FramebufferCompat.endWrite(this);
    }

    public void internalMethod02227(boolean bl) {
        this.internalMethod08117();
        if (bl) {
            this.clear();
            this.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        }
        this.beginWrite(false);
    }

    public void internalMethod03245() {
        this.internalMethod02227(true);
    }

    public void internalMethod03248() {
        this.endWrite();
        FramebufferCompat.beginWrite(internalField0149.getFramebuffer(), false);
    }

    private void internalMethod08117() {
        if (this.internalMethod03246()) {
            int n = Math.max((int)Math.floor((float)internalField0149.getWindow().getFramebufferWidth() * this.internalField0205), 1);
            int n2 = Math.max((int)Math.floor((float)internalField0149.getWindow().getFramebufferHeight() * this.internalField0205), 1);
            if (this.colorAttachment != null) {
                this.delete();
            }
            this.initFbo(n, n2);
        }
    }

    private boolean internalMethod03246() {
        int n = Math.max((int)Math.floor((float)internalField0149.getWindow().getFramebufferWidth() * this.internalField0205), 1);
        int n2 = Math.max((int)Math.floor((float)internalField0149.getWindow().getFramebufferHeight() * this.internalField0205), 1);
        return this.textureWidth != n || this.textureHeight != n2;
    }
}
